package com.redhat.iot.trackman.datagen.viewer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.redhat.iot.trackman.datagen.domain.BallPosition;

final class ShotProcessor {

    public static ViewerModel createModel( final String csvDataFilePath ) throws Exception {
        final ShotProcessor processor = new ShotProcessor();
        return processor.process( csvDataFilePath );
    }

    private ShotProcessor() {
        // nothing to do
    }

    private ViewerModel process( final String csvDataFilePath ) throws Exception {
        final ViewerModel model = new ViewerModel();
        final Map< String, List< BallPosition > > ballPositions = model.getBallPositions();
        final Path input = Paths.get( csvDataFilePath );
        final String content = new String( Files.readAllBytes( input ) );

        // process file
        READING_FILE: for ( final String line : content.split( System.lineSeparator() ) ) {
            final StringTokenizer tokenizer = new StringTokenizer( line, ",\n\r\f" );
            final BallPosition ballPosition = new BallPosition();

            while ( tokenizer.hasMoreTokens() ) {
                ballPosition.setShotId( tokenizer.nextToken() );
                ballPosition.setX( Double.parseDouble( tokenizer.nextToken() ) );
                ballPosition.setY( Double.parseDouble( tokenizer.nextToken() ) );

                // some y points were negative (not sure why) so remove them
                if ( ballPosition.getY() < 0 ) {
                    continue READING_FILE;
                }

                ballPosition.setZ( Double.parseDouble( tokenizer.nextToken() ) );
            }

            List< BallPosition > dataPoints = ballPositions.get( ballPosition.getShotId() );

            if ( ( dataPoints == null ) || dataPoints.isEmpty() ) {
                dataPoints = new ArrayList< BallPosition >();
                ballPositions.put( ballPosition.getShotId(), dataPoints );
            }

            dataPoints.add( ballPosition );
        }

        // find min/max values
        double minDistance = Double.MIN_VALUE;
        double maxDistance = Double.MIN_VALUE;
        double minHeight = Double.MIN_VALUE;
        double maxHeight = Double.MIN_VALUE;
        double minSide = Double.MIN_VALUE;
        double maxSide = Double.MIN_VALUE;

        for ( final List< BallPosition > positions : ballPositions.values() ) {
            for ( final BallPosition ballPosition : positions ) {
                minDistance = Math.min( minDistance, ballPosition.getX() );
                maxDistance = Math.max( maxDistance, ballPosition.getX() );
                minHeight = Math.min( minHeight, ballPosition.getY() );
                maxHeight = Math.max( maxHeight, ballPosition.getY() );
                minSide = Math.min( minSide, ballPosition.getZ() );
                maxSide = Math.max( maxSide, ballPosition.getZ() );
            }
        }

        model.setMinDistance( minDistance );
        model.setMaxDistance( maxDistance );
        model.setDistanceSize( ( int )( model.getMaxDistance() + Math.abs( model.getMinDistance() ) ) );

        model.setMinHeight( minHeight );
        model.setMaxHeight( maxHeight );
        model.setHeightSize( ( int )( model.getMaxHeight() + Math.abs( model.getMinHeight() ) ) );

        model.setMinSide( minSide );
        model.setMaxSide( maxSide );
        model.setSideSize( ( int )( model.getMaxSide() + Math.abs( model.getMinSide() ) ) );

        return model;
    }

}
