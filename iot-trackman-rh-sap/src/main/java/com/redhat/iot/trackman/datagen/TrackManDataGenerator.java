package com.redhat.iot.trackman.datagen;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.redhat.iot.trackman.datagen.domain.BallPosition;
import com.redhat.iot.trackman.datagen.domain.BallTrajectory;
import com.redhat.iot.trackman.datagen.domain.Course;
import com.redhat.iot.trackman.datagen.domain.Hole;
import com.redhat.iot.trackman.datagen.domain.Player;
import com.redhat.iot.trackman.datagen.domain.Shot;
import com.redhat.iot.trackman.datagen.domain.TrajectoryCoefficient;
import com.redhat.iot.trackman.datagen.util.JsonUtil;
import com.redhat.iot.trackman.datagen.viewer.TrackManShotViewer;

abstract class TrackManDataGenerator {

    protected interface JsonId {

        String BALL_SPEED = "BallSpeed";
        String BAll_TRAJECTORY = "BallTrajectory";
        String CARRY = "Carry";
        String CARRY_SIDE = "CarrySide";
        String CLUB_SPEED = "ClubSpeed";
        String COURSE_ID = "CourseId";
        String COURSE_NAME = "CourseName";
        String COURSE_NUMBER = "CourseNumber";
        String DATA = "Data";
        String HOLE_ID = "HoleId";
        String HOLE_NUMBER = "HoleNumber";
        String ID = "Id";
        String KIND = "Kind";
        String LANDING_ANGLE = "LandingAngle";
        String LAUNCH_DIRECTION = "LaunchDirection";
        String MAX_HEIGHT = "MaxHeight";
        String MEASURED_TIME_INTERVAL = "MeasuredTimeInterval";
        String PAYLOAD = "Payload";
        String POSITION = "Position";
        String POSITIONS = "Positions";
        String SHOT_NUMBER = "ShotNumber";
        String SPIN_RATE = "SpinRate";
        String SUBTYPE = "SubType";
        String TIME = "Time";
        String TIME_INTERVAL = "TimeInterval";
        String TYPE = "Type";
        String VALID_TIME_INTERVAL = "ValidTimeInterval";
        String X_FIT = "XFit";
        String Y_FIT = "YFit";
        String Z_FIT = "ZFit";

    }

    protected interface JsonValue {

        String BROADCAST = "Broadcast";
        String FLIGHT = "Flight";
        String FLIGHT_DATA = "FlightData";
        String LAUNCH_DATA = "LaunchData";
        String LIVE_APEX = "LiveApex";
        String LIVE_TRAJECTORY = "LiveTrajectory";
        String MEASUREMENT = "Measurement";
        String ROLL = "Roll";

    }

    private static final String CSV_PATTERN = "%s,%s,%s,%s"; // shotId, x, y, z
    private static final boolean DEBUG = false;
    private static final String INPUT_FILE = "src/main/resources/trackman/shots.json";
    private static final String HANA_OUTPUT_FILE = "src/main/resources/generated/HanaSchema.ddl";
    private static final String SA_OUTPUT_FILE = "src/main/resources/generated/SqlAnywhereSchema.ddl";

    public static void main( final String[] args ) {
        { // generate SQL Anywhere schema and inserts
            System.out.println( "Generating SQL Anywhere ..." );
            final long start = System.currentTimeMillis();

            final SqlAnywhereDdlGenerator generator = new SqlAnywhereDdlGenerator();

            try {
                generator.processFile( INPUT_FILE );
                generator.writeOutputFile( SA_OUTPUT_FILE, true, true, false );
            } catch ( final Exception e ) {
                e.printStackTrace();
            } finally {
                System.out.println( "done (" + ( System.currentTimeMillis() - start ) + " ms)" );
                System.out.println( "----------" );
            }
        }

        { // generate HANA schema and inserts
            System.out.println( "Generating HANA ..." );
            final long start = System.currentTimeMillis();

            final HanaDdlGenerator generator = new HanaDdlGenerator();

            try {
                generator.processFile( INPUT_FILE );
                generator.writeOutputFile( HANA_OUTPUT_FILE, true, true, true );
            } catch ( final Exception e ) {
                e.printStackTrace();
            } finally {
                System.out.println( "done (" + ( System.currentTimeMillis() - start ) + " ms)" );
                System.out.println( "----------" );
            }
        }

    }

    protected final Map< String, List< BallPosition > > ballPositions = new HashMap<>();
    protected final List< BallTrajectory > ballTrajectories = new ArrayList<>();
    protected int ballTrajectoryId = 0;
    protected final Map< String, Course > courses = new HashMap<>();
    protected final Map< String, Hole > holes = new HashMap<>();
    protected final List< String > messages = new ArrayList<>();
    protected Player player;
    protected final List< Player > players = new ArrayList<>();
    protected final Map< String, Shot > shots = new HashMap<>();
    protected final List< TrajectoryCoefficient > trajectoryCoefficients = new ArrayList<>();

    protected TrackManDataGenerator() {
        createPlayers();
        this.player = this.players.get( 0 ); // pick first player
    }

    private void createPlayers() {
        this.players.add( new Player( 1, "Rickie", "Fowler" ) );
        this.players.add( new Player( 2, "Dustin", "Johnson" ) );
        this.players.add( new Player( 3, "Jordan", "Spieth" ) );
        this.players.add( new Player( 4, "Sergio", "Garcia" ) );
        this.players.add( new Player( 5, "Jason", "Dufner" ) );
        this.players.add( new Player( 6, "Phil", "Mickelson" ) );
    }

    protected abstract void generateCreateTableStatements( final StringBuilder builder );

    protected void generateCsvFile( final StringBuilder builder ) {
        for ( final List< BallPosition > positions : this.ballPositions.values() ) {
            for ( final BallPosition ballPosition : positions ) {
                builder.append( String.format( CSV_PATTERN,
                                               ballPosition.getShotId(),
                                               ballPosition.getX(),
                                               ballPosition.getY(),
                                               ballPosition.getZ() ) )
                        .append( '\n' );
            }
        }
    }

    protected abstract void generateDropStatements( final StringBuilder builder );

    protected abstract void generateInsertStatements( final StringBuilder builder );

    private boolean isBroadcastRecord( final JSONObject jobj ) {
        return jobj.get( JsonId.TYPE ).equals( JsonValue.BROADCAST );
    }

    private boolean isFlightDataRecord( final JSONObject jobj ) {
        if ( jobj.get( JsonId.SUBTYPE ).equals( JsonValue.MEASUREMENT ) ) {
            // Payload.Data.FlightData == FlightData
            if ( jobj.containsKey( JsonId.PAYLOAD ) ) {
                final JSONObject payload = ( JSONObject )jobj.get( JsonId.PAYLOAD );

                if ( payload.containsKey( JsonId.DATA ) ) {
                    final JSONObject data = ( JSONObject )payload.get( JsonId.DATA );
                    return data.containsKey( JsonId.KIND ) && data.get( JsonId.KIND ).equals( JsonValue.FLIGHT_DATA );
                }
            }
        }

        return false;
    }

    private boolean isLaunchDataRecord( final JSONObject jobj ) {
        if ( jobj.get( JsonId.SUBTYPE ).equals( JsonValue.MEASUREMENT ) ) {
            // Payload.Data.Kind == LaunchData
            if ( jobj.containsKey( JsonId.PAYLOAD ) ) {
                final JSONObject payload = ( JSONObject )jobj.get( JsonId.PAYLOAD );

                if ( payload.containsKey( JsonId.DATA ) ) {
                    final JSONObject data = ( JSONObject )payload.get( JsonId.DATA );
                    return data.containsKey( JsonId.KIND ) && data.get( JsonId.KIND ).equals( JsonValue.LAUNCH_DATA );
                }
            }
        }

        return false;
    }

    private boolean isLiveApexRecord( final JSONObject jobj ) {
        if ( jobj.get( JsonId.SUBTYPE ).equals( JsonValue.MEASUREMENT ) ) {
            // Payload.Data.Kind == LiveApex
            if ( jobj.containsKey( JsonId.PAYLOAD ) ) {
                final JSONObject payload = ( JSONObject )jobj.get( JsonId.PAYLOAD );

                if ( payload.containsKey( JsonId.DATA ) ) {
                    final JSONObject data = ( JSONObject )payload.get( JsonId.DATA );
                    return data.containsKey( JsonId.KIND ) && data.get( JsonId.KIND ).equals( JsonValue.LIVE_APEX );
                }
            }
        }

        return false;
    }

    private boolean isLiveTrajectoryRecord( final JSONObject jobj ) {
        return jobj.get( JsonId.SUBTYPE ).equals( JsonValue.LIVE_TRAJECTORY );
    }

    private boolean isMeasurementRecord( final JSONObject jobj ) {
        if ( jobj.get( JsonId.SUBTYPE ).equals( JsonValue.MEASUREMENT ) ) {
            // Payload.Data.Kind == Measurement
            if ( jobj.containsKey( JsonId.PAYLOAD ) ) {
                final JSONObject payload = ( JSONObject )jobj.get( JsonId.PAYLOAD );

                if ( payload.containsKey( JsonId.DATA ) ) {
                    final JSONObject data = ( JSONObject )payload.get( JsonId.DATA );
                    return data.containsKey( JsonId.KIND ) && data.get( JsonId.KIND ).equals( JsonValue.MEASUREMENT );
                }
            }
        }

        return false;
    }

    private List< String > loadData( final File file ) throws Exception {
        final String inputFileName = file.getPath();
        final List< String > records = new ArrayList<>();
        final Path input = Paths.get( inputFileName );
        final String content = new String( Files.readAllBytes( input ) );

        for ( final String line : content.split( System.lineSeparator() ) ) {
            final String message = line.trim();

            if ( !message.startsWith( "//" ) && !message.isEmpty() ) {
                records.add( message );
                this.messages.add( message );
            }
        }

        return Collections.unmodifiableList( records );
    }

    public void processFile( final String inputFilePath ) throws Exception {
        if ( inputFilePath == null ) {
            throw new RuntimeException( "Input file path is null" );
        }

        final Path input = Paths.get( inputFilePath );

        // load input file
        System.out.println( "\tLoading input file '" + inputFilePath + "' ... " );
        final File inputFile = input.toFile();
        final List< String > records = loadData( inputFile );

        // process records creating domain objects
        System.out.println( "\tProcessing input file ... " );
        for ( final String record : records ) {
            processMessage( record );
        }
    }

    private void processFlightDataRecord( final JSONObject jobj ) {
        if ( DEBUG ) System.out.println( "\tFlightData" );

        final String shotId = jobj.get( JsonId.ID ).toString();
        final JSONObject payload = ( JSONObject )jobj.get( JsonId.PAYLOAD );
        final JSONObject data = ( JSONObject )payload.get( JsonId.DATA );
        final Shot shot = this.shots.get( shotId );

        if ( shot == null ) {
            throw new RuntimeException( "Shot with ID '" + shotId + "' not found to process FlightData message" );
        }

        shot.setCarry( JsonUtil.toDouble( data, JsonId.CARRY ) );
        shot.setCarrySide( JsonUtil.toDouble( data, JsonId.CARRY_SIDE ) );
        shot.setLandingAngle( JsonUtil.toDouble( data, JsonId.LANDING_ANGLE ) );

        // ball trajectories
        final JSONArray trajectories = ( JSONArray )data.get( JsonId.BAll_TRAJECTORY );

        for ( int i = 0, size = trajectories.size(); i < size; ++i ) {
            final JSONObject trajectory = ( JSONObject )trajectories.get( i );

            final BallTrajectory ballTrajectory = new BallTrajectory();
            this.ballTrajectories.add( ballTrajectory );
            ballTrajectory.setId( this.ballTrajectoryId++ );
            ballTrajectory.setDataPointIndex( i );
            ballTrajectory.setShotId( shot.getId() );
            ballTrajectory.setKind( trajectory.get( JsonId.KIND ).toString() );

            { // time interval
                final JSONArray interval = ( JSONArray )trajectory.get( JsonId.TIME_INTERVAL );
                ballTrajectory.setTimeIntervalBegin( JsonUtil.toDoubleValue( interval.get( 0 ) ) );
                ballTrajectory.setTimeIntervalEnd( JsonUtil.toDoubleValue( interval.get( 1 ) ) );
            }

            { // valid time interval
                final JSONArray interval = ( JSONArray )trajectory.get( JsonId.VALID_TIME_INTERVAL );
                ballTrajectory.setValidTimeIntervalBegin( JsonUtil.toDoubleValue( interval.get( 0 ) ) );
                ballTrajectory.setValidTimeIntervalEnd( JsonUtil.toDoubleValue( interval.get( 1 ) ) );
            }

            // measured time interval
            if ( JsonValue.FLIGHT.equals( ballTrajectory.getKind() ) ) {
                final JSONArray interval = ( JSONArray )trajectory.get( JsonId.MEASURED_TIME_INTERVAL );
                ballTrajectory.setMeasuredTimeIntervalBegin( JsonUtil.toDoubleValue( interval.get( 0 ) ) );
                ballTrajectory.setMeasuredTimeIntervalEnd( JsonUtil.toDoubleValue( interval.get( 1 ) ) );
            } else {
                ballTrajectory.setMeasuredTimeIntervalBegin( 0.0 );
                ballTrajectory.setMeasuredTimeIntervalEnd( 0.0 );
            }

            { // trajectory coefficients
                final JSONArray xfits = ( JSONArray )trajectory.get( JsonId.X_FIT );
                final JSONArray yfits = ( JSONArray )trajectory.get( JsonId.Y_FIT );
                final JSONArray zfits = ( JSONArray )trajectory.get( JsonId.Z_FIT );

                for ( int j = 0, fitsSize = xfits.size(); j < fitsSize; ++j ) {
                    final TrajectoryCoefficient coefficient = new TrajectoryCoefficient();
                    this.trajectoryCoefficients.add( coefficient );
                    coefficient.setBallTrajectoryId( ballTrajectory.getId() );
                    coefficient.setDataPointIndex( j );

                    // distance coefficient
                    coefficient.setXAxis( JsonUtil.toDoubleValue( xfits.get( j ) ) );

                    // roll will not have any or maybe one YFit (height) and
                    // will always be 0.0
                    if ( JsonValue.ROLL.equals( ballTrajectory.getKind() ) ) {
                        coefficient.setYAxis( 0.0 );
                    } else {
                        coefficient.setYAxis( JsonUtil.toDoubleValue( yfits.get( j ) ) );
                    }

                    // side to side coefficient
                    coefficient.setZAxis( JsonUtil.toDoubleValue( zfits.get( j ) ) );
                }
            }
        }
    }

    private void processLaunchDataRecord( final JSONObject jobj ) {
        if ( DEBUG ) System.out.println( "\tLaunchData" );

        final String shotId = jobj.get( JsonId.ID ).toString();
        System.out.println( "\t\tprocessing shot '" + shotId + '\'' );

        final JSONObject payload = ( JSONObject )jobj.get( JsonId.PAYLOAD );
        final JSONObject data = ( JSONObject )payload.get( JsonId.DATA );

        // create course (if necessary)
        final Course course = new Course();
        course.setId( payload.get( JsonId.COURSE_ID ).toString() );

        if ( !this.courses.containsKey( course.getId() ) ) {
            course.setName( payload.get( JsonId.COURSE_NAME ).toString() );
            course.setNumber( JsonUtil.toInt( payload, JsonId.COURSE_NUMBER ) );
            this.courses.put( course.getId(), course );
        }

        // create hole (if necessary)
        final Hole hole = new Hole();
        hole.setId( payload.get( JsonId.HOLE_ID ).toString() );

        if ( !this.holes.containsKey( hole.getId() ) ) {
            hole.setCourseId( course.getId() );
            hole.setNumber( JsonUtil.toInt( payload, JsonId.HOLE_NUMBER ) );
            this.holes.put( hole.getId(), hole );
        }

        // create shot
        final Shot shot = new Shot();
        shot.setId( shotId );
        shot.setPlayerId( this.player.getId() );
        shot.setHoleId( hole.getId() );
        shot.setShotTime( data.get( JsonId.TIME ).toString() );
        shot.setLaunchDirection( JsonUtil.toDouble( data, JsonId.LAUNCH_DIRECTION ) );
        shot.setBallSpeed( JsonUtil.toDouble( data, JsonId.BALL_SPEED ) );
        shot.setClupSpeed( JsonUtil.toDouble( data, JsonId.CLUB_SPEED ) );
        shot.setShotNumber( JsonUtil.toInt( payload, JsonId.SHOT_NUMBER ) );
        this.shots.put( shot.getId(), shot );
    }

    private void processLiveApexRecord( final JSONObject jobj ) {
        if ( DEBUG ) System.out.println( "\tLiveApex" );

        // add max height to shot
        final String shotId = jobj.get( JsonId.ID ).toString();
        final JSONObject payload = ( JSONObject )jobj.get( JsonId.PAYLOAD );
        final JSONObject data = ( JSONObject )payload.get( JsonId.DATA );
        final Shot shot = this.shots.get( shotId );

        if ( shot == null ) {
            throw new RuntimeException( "Shot with ID '" + shotId + "' not found to process LiveApex message" );
        }

        shot.setMaxHeight( JsonUtil.toDouble( data, JsonId.MAX_HEIGHT ) );
    }

    private void processLiveTrajectoryRecord( final JSONObject jobj ) {
        if ( DEBUG ) System.out.println( "\tLiveTrajectory" );

        final String shotId = jobj.get( JsonId.ID ).toString();
        final JSONObject payload = ( JSONObject )jobj.get( JsonId.PAYLOAD );

        if ( this.shots.get( shotId ) == null ) {
            throw new RuntimeException( "Shot with ID '" + shotId + "' not found to process LiveTrajectory message" );
        }

        final JSONObject data = ( JSONObject )payload.get( JsonId.DATA );
        final JSONArray positions = ( JSONArray )data.get( JsonId.POSITIONS );

        for ( int i = 0, size = positions.size(); i < size; ++i ) {
            final JSONObject position = ( JSONObject )positions.get( i );

            final BallPosition ballPositions = new BallPosition();
            ballPositions.setRelativeTime( JsonUtil.toDouble( position, JsonId.TIME ) );

            // only add the ball position if not previously added
            boolean doAdd = false;
            List< BallPosition > dataPoints = this.ballPositions.get( shotId );

            if ( ( dataPoints == null ) || dataPoints.isEmpty() ) {
                dataPoints = new ArrayList< BallPosition >( size );
                this.ballPositions.put( shotId, dataPoints );
                doAdd = true;
            }

            if ( !dataPoints.isEmpty() ) {
                boolean found = false;

                for ( final BallPosition dataPoint : dataPoints ) {
                    if ( dataPoint.getRelativeTime() == ballPositions.getRelativeTime() ) {
                        found = true;
                        break;
                    }
                }

                if ( !found ) {
                    doAdd = true;
                }
            }

            if ( doAdd ) {
                final JSONArray coordinates = ( JSONArray )position.get( JsonId.POSITION );
                ballPositions.setX( JsonUtil.toDoubleValue( coordinates.get( 0 ) ) );
                ballPositions.setY( JsonUtil.toDoubleValue( coordinates.get( 1 ) ) );
                ballPositions.setZ( JsonUtil.toDoubleValue( coordinates.get( 2 ) ) );
                ballPositions.setShotId( shotId );
                dataPoints.add( ballPositions );
            }
        }
    }

    private void processMeasurementRecord( final JSONObject jobj ) {
        if ( DEBUG ) System.out.println( "\tMeasurement" );

        // add spin rate to shot
        final String shotId = jobj.get( JsonId.ID ).toString();
        final JSONObject payload = ( JSONObject )jobj.get( JsonId.PAYLOAD );
        final JSONObject data = ( JSONObject )payload.get( JsonId.DATA );
        final Shot shot = this.shots.get( shotId );

        if ( shot == null ) {
            throw new RuntimeException( "Shot with ID '" + shotId + "' not found to process Measurement message" );
        }

        shot.setSpinRate( JsonUtil.toDouble( data, JsonId.SPIN_RATE ) );
    }

    public String processMessage( final String json ) throws Exception {
        // parse json and determine how to process
        final JSONParser parser = new JSONParser();
        JSONObject jobj = null;

        try {
            jobj = ( JSONObject )parser.parse( json );

            if ( !isBroadcastRecord( jobj ) ) {
                throw new Exception( "Record type is not " + JsonValue.BROADCAST );
            }

            if ( isLiveTrajectoryRecord( jobj ) ) {
                processLiveTrajectoryRecord( jobj );
            } else if ( isLaunchDataRecord( jobj ) ) {
                processLaunchDataRecord( jobj );
            } else if ( isFlightDataRecord( jobj ) ) {
                processFlightDataRecord( jobj );
            } else if ( isLiveApexRecord( jobj ) ) {
                processLiveApexRecord( jobj );
            } else if ( isMeasurementRecord( jobj ) ) {
                processMeasurementRecord( jobj );
            } else {
                throw new Exception( "Unknown data record type" );
            }
        } catch ( final Exception e ) {
            System.err.println( "Error processing record:\n\n" + json );
            throw e;
        }

        return json;
    }

    public void writeCsvFile() throws Exception {
        System.out.println( "\tWriting CSV file '" + TrackManShotViewer.CSV_OUTPUT_FILE + '\'' );
        final Path output = Paths.get( TrackManShotViewer.CSV_OUTPUT_FILE );
        final StringBuilder builder = new StringBuilder();
        generateCsvFile( builder );
        Files.write( output, builder.toString().getBytes() );
    }

    public void writeOutputFile( final String outputFileName,
                                 final boolean generateDropStatements,
                                 final boolean generateCreateTableStatements,
                                 final boolean writeCsvFile )
            throws Exception {
        if ( outputFileName == null ) {
            throw new RuntimeException( "Output file name is null" );
        }

        System.out.println( "\tWriting DDL file '" + outputFileName + '\'' );

        final Path output = Paths.get( outputFileName );
        final StringBuilder builder = new StringBuilder();

        // drop statements
        if ( generateDropStatements ) {
            generateDropStatements( builder );
            builder.append( '\n' );
        }

        // create table statements
        if ( generateCreateTableStatements ) {
            generateCreateTableStatements( builder );
            builder.append( '\n' );
        }

        // create insert statements
        generateInsertStatements( builder );

        // write file
        Files.write( output, builder.toString().getBytes() );

        if ( writeCsvFile ) {
            writeCsvFile();
        }
    }

}
