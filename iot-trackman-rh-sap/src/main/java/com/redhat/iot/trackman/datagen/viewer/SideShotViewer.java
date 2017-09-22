package com.redhat.iot.trackman.datagen.viewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import com.redhat.iot.trackman.datagen.domain.BallPosition;

final class SideShotViewer extends JPanel {

    private static final long serialVersionUID = 1L;

    private final ViewerModel model;

    public SideShotViewer( final ViewerModel model ) throws Exception {
        this.model = model;
    }

    @Override
    protected void paintComponent( final Graphics g ) {
        super.paintComponent( g );

        final Graphics2D g2 = ( Graphics2D )g;
        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

        final double xScale = ( ( double )getWidth() - ( 2 * this.model.getPadding() ) - this.model.getLabelPadding() )
                / this.model.getDistanceSize();
        final double ySideScale = ( ( double )getHeight() - ( 2 * this.model.getPadding() )
                - this.model.getLabelPadding() ) / this.model.getSideSize();

        final Map< String, List< Point > > sideGraphPoints = new HashMap<>();
        final Map< String, List< BallPosition > > ballPositions = this.model.getBallPositions();

        for ( final String shotId : ballPositions.keySet() ) {
            final List< BallPosition > positions = ballPositions.get( shotId );

            final List< Point > sidePoints = new ArrayList<>();
            sideGraphPoints.put( shotId, sidePoints );

            for ( final BallPosition ballPosition : positions ) {
                final int x = ( int )( ( ballPosition.getX() * xScale ) + this.model.getPadding()
                        + this.model.getLabelPadding() );
                final int z = ( int )( ( ( this.model.getMaxSide() - ballPosition.getZ() ) * ySideScale )
                        + this.model.getPadding() );
                sidePoints.add( new Point( x, z ) );
            }
        }

        // draw background
        g2.setColor( this.model.getBackgroundColor() );
        g2.fillRect( this.model.getPadding() + this.model.getLabelPadding(),
                     this.model.getPadding(),
                     getWidth() - ( 2 * this.model.getPadding() ) - this.model.getLabelPadding(),
                     getHeight() - ( 2 * this.model.getPadding() ) - this.model.getLabelPadding() );
        g2.setColor( this.model.getForegroundColor() );

        // draw hatch marks and grid lines for y axis
        for ( int i = 0; i < this.model.getSideSize(); i++ ) {
            final int x0 = this.model.getPadding() + this.model.getLabelPadding();
            final int x1 = this.model.getPointWidth() + this.model.getPadding() + this.model.getLabelPadding();
            final int y0 = getHeight()
                    - ( ( ( i * ( getHeight() - ( this.model.getPadding() * 2 ) - this.model.getLabelPadding() ) )
                            / this.model.getYDivisions() ) + this.model.getPadding() + this.model.getLabelPadding() );
            final int y1 = y0;

            if ( sideGraphPoints.size() > 0 ) {
                g2.setColor( this.model.getGridColor() );
                g2.drawLine( this.model.getPadding() + this.model.getLabelPadding() + 1 + this.model.getPointWidth(),
                             y0,
                             getWidth() - this.model.getPadding(),
                             y1 );
                g2.setColor( this.model.getYLabelColor() );
                final String yLabel = ( ( ( int )( ( this.model.getMinSide()
                        + ( ( this.model.getMaxSide() - this.model.getMinSide() )
                                * ( ( i * 1.0 ) / this.model.getYDivisions() ) ) )
                        * 100 ) ) / 100.0 ) + "";
                final FontMetrics metrics = g2.getFontMetrics();
                final int labelWidth = metrics.stringWidth( yLabel );
                g2.drawString( yLabel, x0 - labelWidth - 5, ( y0 + ( metrics.getHeight() / 2 ) ) - 3 );
                g2.setColor( this.model.getForegroundColor() ); // set color
                                                                // back
            }
            g2.drawLine( x0, y0, x1, y1 );
        }

        { // draw y axis title
          // save off some state
            final AffineTransform saveTransform = g2.getTransform();
            final Color saveColor = g2.getColor();

            // draw title
            final String title = "Side-To-Side (meters)";
            final FontMetrics metrics = g2.getFontMetrics();
            final int labelWidth = metrics.stringWidth( title );
            final AffineTransform at = new AffineTransform();
            final int anchorX = this.model.getPadding();
            final int anchorY = ( getParent().getHeight() / 2 ) + ( getHeight() / 2 ) + ( labelWidth / 2 )
                    - this.model.getPadding();
            at.setToRotation( Math.toRadians( -90 ), anchorX, anchorY );
            g2.setTransform( at );
            g2.setColor( this.model.getYLabelColor() );
            g2.drawString( title, anchorX, anchorY );

            // restore previous settings
            g2.setTransform( saveTransform );
            g2.setColor( saveColor );
        }

        // draw hatch marks and grid lines for x axis
        int x1 = 0;
        for ( int i = 0; i < this.model.getDistanceSize(); i++ ) {
            final int x0 = ( ( i * ( getWidth() - ( this.model.getPadding() * 2 ) - this.model.getLabelPadding() ) )
                    / ( this.model.getDistanceSize() - 1 ) ) + this.model.getPadding() + this.model.getLabelPadding();
            x1 = x0;
            final int y0 = getHeight() - this.model.getPadding() - this.model.getLabelPadding();
            final int y1 = y0 - this.model.getPointWidth();
            if ( ( i % ( ( int )( ( this.model.getDistanceSize() / 20.0 ) ) + 1 ) ) == 0 ) {
                g2.setColor( this.model.getGridColor() );
                g2.drawLine( x0,
                             getHeight() - this.model.getPadding() - this.model.getLabelPadding() - 1
                                     - this.model.getPointWidth(),
                             x1,
                             this.model.getPadding() );
                g2.setColor( this.model.getXLabelColor() );
                final String xLabel = i + "";
                final FontMetrics metrics = g2.getFontMetrics();
                final int labelWidth = metrics.stringWidth( xLabel );
                g2.drawString( xLabel, x0 - ( labelWidth / 2 ), y0 + metrics.getHeight() + 3 );
                g2.setColor( this.model.getForegroundColor() );
            }
            g2.drawLine( x0, y0, x1, y1 );
        }

        { // draw x axis title
            g2.setColor( this.model.getXLabelColor() );
            final String title = "Distance (meters)";
            final int x = ( getWidth() - ( this.model.getPadding() * 2 ) - this.model.getLabelPadding() ) / 2;
            final int y = getHeight() - this.model.getPadding();
            g2.drawString( title, x, y );
            g2.setColor( this.model.getForegroundColor() );
        }

        // draw x and y axes
        g2.drawLine( this.model.getPadding() + this.model.getLabelPadding(),
                     getHeight() - this.model.getPadding() - this.model.getLabelPadding(),
                     this.model.getPadding() + this.model.getLabelPadding(),
                     this.model.getPadding() );
        g2.drawLine( this.model.getPadding() + this.model.getLabelPadding(),
                     getHeight() - this.model.getPadding() - this.model.getLabelPadding(),
                     getWidth() - this.model.getPadding(),
                     getHeight() - this.model.getPadding() - this.model.getLabelPadding() );

        // draw lines
        int colorIndex = 0;

        { // draw center line
            final Stroke saveStroke = g2.getStroke();
            g2.setColor( Color.GRAY );
            final float thickness = 2.0f;
            final float length = 5.0f;
            final float spacing = 5.0f;
            final float[] array = { thickness * ( length - 1.0f ), thickness * ( spacing + 1.0f ) };
            g2.setStroke( new BasicStroke( thickness, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, thickness * 2.0f,
                    array, 0.0f ) );
            final int x = this.model.getPadding() + this.model.getLabelPadding();
            final int y = ( int )( ( ( this.model.getMaxSide() - 0.0 ) * ySideScale ) + this.model.getPadding() );
            g2.drawLine( x, y, x1, y );
            g2.setStroke( saveStroke );
        }

        // plot shots
        for ( final String shotId : sideGraphPoints.keySet() ) {
            final List< Point > points = sideGraphPoints.get( shotId );

            final Stroke oldStroke = g2.getStroke();
            g2.setColor( ViewerModel.LINE_COLORS[ colorIndex ] );
            g2.setStroke( ViewerModel.GRAPH_STROKE );

            for ( int i = 0, size = points.size() - 1; i < size; ++i ) {
                final int xOne = points.get( i ).x;
                final int yOne = points.get( i ).y;
                final int xTwo = points.get( i + 1 ).x;
                final int yTwo = points.get( i + 1 ).y;
                g2.drawLine( xOne, yOne, xTwo, yTwo );
            }

            g2.setStroke( oldStroke );
            g2.setColor( ViewerModel.POINT_COLORS[ colorIndex ] );

            for ( final Point point : points ) {
                final int x = point.x - ( this.model.getPointWidth() / 2 );
                final int y = point.y - ( this.model.getPointWidth() / 2 );
                final int ovalW = this.model.getPointWidth();
                final int ovalH = this.model.getPointWidth();
                g2.fillOval( x, y, ovalW, ovalH );
            }

            colorIndex++;

            if ( colorIndex > ( ViewerModel.LINE_COLORS.length - 1 ) ) {
                colorIndex = 0;
            }
        }
    }

}