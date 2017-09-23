package com.redhat.iot.trackman.datagen;

import java.util.List;

import com.redhat.iot.trackman.datagen.domain.BallPosition;
import com.redhat.iot.trackman.datagen.domain.BallTrajectory;
import com.redhat.iot.trackman.datagen.domain.Course;
import com.redhat.iot.trackman.datagen.domain.Hole;
import com.redhat.iot.trackman.datagen.domain.Player;
import com.redhat.iot.trackman.datagen.domain.Shot;
import com.redhat.iot.trackman.datagen.domain.TrajectoryCoefficient;

final class SqlAnywhereDdlGenerator extends TrackManDataGenerator {

    private static final String BALL_POSITION_PATTERN = "INSERT INTO ballPosition VALUES ( '%s', %s, %s, %s, %s );";
    private static final String BALL_TRAJECTORY_PATTERN = "INSERT INTO ballTrajectory VALUES ( %s, '%s', %s, '%s', %s, %s, %s, %s, %s, %s );";
    private static final String COURSE_PATTERN = "INSERT INTO course VALUES ( '%s', '%s', %s );";
    private static final String HOLE_PATTERN = "INSERT INTO hole VALUES ( '%s', '%s', %s );";
    private static final String MESSAGE_PATTERN = "INSERT INTO trackmanMessage ( msg ) VALUES ( '%s' );";
    private static final String PLAYER_PATTERN = "INSERT INTO player VALUES ( %s, '%s', '%s' );";
    private static final String SHOT_PATTERN = "INSERT INTO shot VALUES ( '%s', %s, '%s', %s, '%s', %s, %s, %s, %s, %s, %s, %s, %s, %s );";
    private static final String TRAJECTORY_COEFFICIENT_PATTERN = "INSERT INTO trajectoryCoefficient VALUES ( %s, %s, %s, %s, %s );";

    SqlAnywhereDdlGenerator() {
        // nothing to do
    }

    @Override
    protected void generateCreateTableStatements( final StringBuilder builder ) {
        // player
        builder.append( "--\n" );
        builder.append( "-- Player\n" );
        builder.append( "--\n" );
        builder.append( "-- Not included in Trackman data.\n" );
        builder.append( "--\n" );
        builder.append( "CREATE TABLE player (" ).append( '\n' );
        builder.append( "    id INTEGER NOT NULL," ).append( '\n' );
        builder.append( "    firstName VARCHAR( 25 ) NOT NULL," ).append( '\n' );
        builder.append( "    lastName VARCHAR( 25 ) NOT NULL," ).append( '\n' );
        builder.append( "    PRIMARY KEY ( id )" ).append( '\n' );
        builder.append( ");" ).append( '\n' ).append( '\n' );

        // Trackman messages
        builder.append( "--\n" );
        builder.append( "-- Trackman JSON responses\n" );
        builder.append( "--\n" );
        builder.append( "CREATE TABLE trackmanMessage (" ).append( '\n' );
        builder.append( "    id INTEGER DEFAULT AUTOINCREMENT," ).append( '\n' );
        builder.append( "    msg VARCHAR( 32767 )," ).append( '\n' );
        builder.append( "    PRIMARY KEY ( id )" ).append( '\n' );
        builder.append( ");" ).append( '\n' ).append( '\n' );

        // Course
        builder.append( "--\n" );
        builder.append( "-- Course\n" );
        builder.append( "--\n" );
        builder.append( "-- Create record from the Broadcast/Measurement/LaunchData response record.\n" );
        builder.append( "--\n" );
        builder.append( "CREATE TABLE course (" ).append( '\n' );
        builder.append( "    id VARCHAR( 36 ) NOT NULL," ).append( '\n' );
        builder.append( "    name VARCHAR( 25 ) NOT NULL," ).append( '\n' );
        builder.append( "    number INT NOT NULL," ).append( '\n' );
        builder.append( "    PRIMARY KEY ( id )" ).append( '\n' );
        builder.append( ");" ).append( '\n' ).append( '\n' );

        // Hole
        builder.append( "--\n" );
        builder.append( "-- Hole\n" );
        builder.append( "--\n" );
        builder.append( "-- Create record from the Broadcast/Measurement/LaunchData response record.\n" );
        builder.append( "--\n" );
        builder.append( "CREATE TABLE hole (" ).append( '\n' );
        builder.append( "    id VARCHAR( 36 ) NOT NULL," ).append( '\n' );
        builder.append( "    courseId VARCHAR( 36 ) NOT NULL," ).append( '\n' );
        builder.append( "    number INT NOT NULL," ).append( '\n' );
        builder.append( "    PRIMARY KEY ( id )," ).append( '\n' );
        builder.append( "    FOREIGN KEY ( courseId ) REFERENCES course ( id )" ).append( '\n' );
        builder.append( ");" ).append( '\n' ).append( '\n' );

        // Shot
        builder.append( "--\n" );
        builder.append( "-- Shot\n" );
        builder.append( "--\n" );
        builder.append( "-- Create record from the Broadcast/Measurement/LaunchData response.\n" );
        builder.append( "-- Need to pick a player as that is not in the Trackman response record.\n" );
        builder.append( "--\n" );
        builder.append( "CREATE TABLE shot (" ).append( '\n' );
        builder.append( "    id VARCHAR( 36 ) NOT NULL," ).append( '\n' );
        builder.append( "    playerId INT NOT NULL," ).append( '\n' );
        builder.append( "    holeId VARCHAR( 36 ) NOT NULL," ).append( '\n' );
        builder.append( "    shotNumber INT NOT NULL," ).append( '\n' );
        builder.append( "    shotTime TIMESTAMP NOT NULL, // the time the shot was taken" ).append( '\n' );
        builder.append( "    launchDirection DECIMAL( 5,2 )," ).append( '\n' );
        builder.append( "    ballSpeed DECIMAL( 7,4 )," ).append( '\n' );
        builder.append( "    clupSpeed DECIMAL( 18,15 )," ).append( '\n' );
        builder.append( "    maxHeight DECIMAL( 5, 3 )," ).append( '\n' );
        builder.append( "    carry DECIMAL( 6, 3 )," ).append( '\n' );
        builder.append( "    carrySide DECIMAL( 6, 3 )," ).append( '\n' );
        builder.append( "    landingAngle DECIMAL( 7, 4 )," ).append( '\n' );
        builder.append( "    launchAngle DECIMAL( 5, 3 )," ).append( '\n' );
        builder.append( "    spinRate DECIMAL( 5, 1 )," ).append( '\n' );
        builder.append( "    PRIMARY KEY ( id )," ).append( '\n' );
        builder.append( "    FOREIGN KEY ( playerId ) REFERENCES player ( id )," ).append( '\n' );
        builder.append( "    FOREIGN KEY ( holeId ) REFERENCES hole ( id )" ).append( '\n' );
        builder.append( ");" ).append( '\n' ).append( '\n' );

        // Ball Positions
        builder.append( "--\n" );
        builder.append( "-- Ball positions\n" );
        builder.append( "--\n" );
        builder.append( "-- Create record from Broadcast/LiveTrajectory Trackman response record.\n" );
        builder.append( "--\n" );
        builder.append( "CREATE TABLE ballPosition (" ).append( '\n' );
        builder.append( "    shotId VARCHAR( 36 ) NOT NULL," ).append( '\n' );
        builder.append( "    relativeTime DECIMAL( 18,16 ), // seconds since shot was taken" ).append( '\n' );
        builder.append( "    x DECIMAL( 17,14 ), // distance traveled since shot was taken" ).append( '\n' );
        builder.append( "    y DECIMAL( 17,14 ), // height since shot was taken" ).append( '\n' );
        builder.append( "    z DECIMAL( 17,14 ), // side-to-side distance since shot was taken" ).append( '\n' );
        builder.append( "    PRIMARY KEY ( shotId, relativeTime )," ).append( '\n' );
        builder.append( "    FOREIGN KEY ( shotId ) REFERENCES shot ( id )" ).append( '\n' );
        builder.append( ");" ).append( '\n' ).append( '\n' );

        // Ball Trajectories
        builder.append( "--\n" );
        builder.append( "-- Ball trajectories\n" );
        builder.append( "--\n" );
        builder.append( "-- Create record from Broadcast/FlightData Trackman response record.\n" );
        builder.append( "--\n" );
        builder.append( "CREATE TABLE ballTrajectory (" ).append( '\n' );
        builder.append( "    id INTEGER NOT NULL," ).append( '\n' );
        builder.append( "    shotId VARCHAR( 36 ) NOT NULL," ).append( '\n' );
        builder.append( "    dataPointIndex INTEGER NOT NULL," ).append( '\n' );
        builder.append( "    kind VARCHAR( 10 ) NOT NULL," ).append( '\n' );
        builder.append( "    timeIntervalBegin DECIMAL( 12, 10 ) NOT NULL," ).append( '\n' );
        builder.append( "    timeIntervalEnd DECIMAL( 12, 10 ) NOT NULL," ).append( '\n' );
        builder.append( "    validTimeIntervalBegin DECIMAL( 12, 10 ) NOT NULL," ).append( '\n' );
        builder.append( "    validTimeIntervalEnd DECIMAL( 12, 10 ) NOT NULL," ).append( '\n' );
        builder.append( "    measuredTimeIntervalBegin DECIMAL( 12, 10 ) NOT NULL," ).append( '\n' );
        builder.append( "    measuredTimeIntervalEnd DECIMAL( 12, 10 ) NOT NULL," ).append( '\n' );
        builder.append( "    PRIMARY KEY ( id )," ).append( '\n' );
        builder.append( "    UNIQUE ( shotId, dataPointIndex ASC )," ).append( '\n' );
        builder.append( "    FOREIGN KEY ( shotId ) REFERENCES shot ( id )" ).append( '\n' );
        builder.append( ");" ).append( '\n' ).append( '\n' );

        // Ball Trajectory Coefficients
        builder.append( "--\n" );
        builder.append( "-- Ball trajectory coefficients\n" );
        builder.append( "--\n" );
        builder.append( "-- Create record from Broadcast/FlightData Trackman response record.\n" );
        builder.append( "--\n" );
        builder.append( "CREATE TABLE trajectoryCoefficient (" ).append( '\n' );
        builder.append( "    ballTrajectoryId INTEGER NOT NULL," ).append( '\n' );
        builder.append( "    dataPointIndex INTEGER NOT NULL," ).append( '\n' );
        builder.append( "    xAxis DECIMAL( 12, 9 ) NOT NULL, // distance" ).append( '\n' );
        builder.append( "    yAxis DECIMAL( 12, 9 ) NOT NULL, // height" ).append( '\n' );
        builder.append( "    zAxis DECIMAL( 12, 9 ) NOT NULL, // side" ).append( '\n' );
        builder.append( "    PRIMARY KEY ( ballTrajectoryId, dataPointIndex )," ).append( '\n' );
        builder.append( "    FOREIGN KEY ( ballTrajectoryId ) REFERENCES ballTrajectory ( id )" ).append( '\n' );
        builder.append( ");" ).append( '\n' ).append( '\n' );
    }

    @Override
    protected void generateDropStatements( final StringBuilder builder ) {
        builder.append( "DROP TABLE IF EXISTS trajectoryCoefficient;" ).append( '\n' );
        builder.append( "DROP TABLE IF EXISTS ballTrajectory;" ).append( '\n' );
        builder.append( "DROP TABLE IF EXISTS ballPosition;" ).append( '\n' );
        builder.append( "DROP TABLE IF EXISTS shot;" ).append( '\n' );
        builder.append( "DROP TABLE IF EXISTS hole;" ).append( '\n' );
        builder.append( "DROP TABLE IF EXISTS course;" ).append( '\n' );
        builder.append( "DROP TABLE IF EXISTS trackmanMessage;" ).append( '\n' );
        builder.append( "DROP TABLE IF EXISTS player;" ).append( '\n' );
    }

    @Override
    protected void generateInsertStatements( final StringBuilder builder ) {
        { // message inserts
            builder.append( "--\n" );
            builder.append( "-- Trackman messages\n" );
            builder.append( "--\n" );

            for ( final String message : this.messages ) {
                builder.append( String.format( MESSAGE_PATTERN, message ) ).append( '\n' );
            }
        }

        { // player inserts
            builder.append( "\n--\n" );
            builder.append( "-- Players\n" );
            builder.append( "--\n" );

            for ( final Player player : this.players ) {
                builder.append( String.format( PLAYER_PATTERN,
                                               player.getId(),
                                               player.getFirstName(),
                                               player.getLastName() ) )
                        .append( '\n' );
            }
        }

        { // course inserts
            builder.append( "\n--\n" );
            builder.append( "-- Courses\n" );
            builder.append( "--\n" );

            for ( final Course course : this.courses.values() ) {
                builder.append( String.format( COURSE_PATTERN, course.getId(), course.getName(), course.getNumber() ) )
                        .append( '\n' );
            }
        }

        { // hole inserts
            builder.append( "\n--\n" );
            builder.append( "-- Holes\n" );
            builder.append( "--\n" );

            for ( final Hole hole : this.holes.values() ) {
                builder.append( String.format( HOLE_PATTERN, hole.getId(), hole.getCourseId(), hole.getNumber() ) )
                        .append( '\n' );
            }
        }

        { // shot inserts
            builder.append( "\n--\n" );
            builder.append( "-- Shots\n" );
            builder.append( "--\n" );

            for ( final Shot shot : this.shots.values() ) {
                builder.append( String.format( SHOT_PATTERN,
                                               shot.getId(),
                                               shot.getPlayerId(),
                                               shot.getHoleId(),
                                               shot.getShotNumber(),
                                               shot.getShotTime(),
                                               shot.getLaunchDirection(),
                                               shot.getBallSpeed(),
                                               shot.getClupSpeed(),
                                               shot.getMaxHeight(),
                                               shot.getCarry(),
                                               shot.getCarrySide(),
                                               shot.getLandingAngle(),
                                               shot.getLaunchAngle(),
                                               shot.getSpinRate() ) )
                        .append( '\n' );
            }
        }

        { // ball position inserts
            builder.append( "\n--\n" );
            builder.append( "-- Ball Positions\n" );
            builder.append( "--\n" );

            for ( final List< BallPosition > positions : this.ballPositions.values() ) {
                for ( final BallPosition ballPosition : positions ) {
                    builder.append( String.format( BALL_POSITION_PATTERN,
                                                   ballPosition.getShotId(),
                                                   ballPosition.getRelativeTime(),
                                                   ballPosition.getX(),
                                                   ballPosition.getY(),
                                                   ballPosition.getZ() ) )
                            .append( '\n' );
                }
            }
        }

        { // ball trajectory inserts
            builder.append( "\n--\n" );
            builder.append( "-- Ball Trajectories\n" );
            builder.append( "--\n" );

            for ( final BallTrajectory trajectory : this.ballTrajectories ) {
                builder.append( String.format( BALL_TRAJECTORY_PATTERN,
                                               trajectory.getId(),
                                               trajectory.getShotId(),
                                               trajectory.getDataPointIndex(),
                                               trajectory.getKind(),
                                               trajectory.getTimeIntervalBegin(),
                                               trajectory.getTimeIntervalEnd(),
                                               trajectory.getValidTimeIntervalBegin(),
                                               trajectory.getValidTimeIntervalEnd(),
                                               trajectory.getMeasuredTimeIntervalBegin(),
                                               trajectory.getMeasuredTimeIntervalEnd() ) )
                        .append( '\n' );
            }
        }

        { // trajectory coefficient inserts
            builder.append( "\n--\n" );
            builder.append( "-- Trajectory Coefficients\n" );
            builder.append( "--\n" );

            for ( final TrajectoryCoefficient coefficient : this.trajectoryCoefficients ) {
                builder.append( String.format( TRAJECTORY_COEFFICIENT_PATTERN,
                                               coefficient.getBallTrajectoryId(),
                                               coefficient.getDataPointIndex(),
                                               coefficient.getXAxis(),
                                               coefficient.getYAxis(),
                                               coefficient.getZAxis() ) )
                        .append( '\n' );
            }
        }

        builder.append( "\ncommit;\n" );
    }

}
