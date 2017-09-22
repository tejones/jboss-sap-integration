package com.redhat.iot.trackman.datagen.viewer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public final class TrackManShotViewer {

    public static final String CSV_OUTPUT_FILE = "src/main/resources/generated//ballPositions.csv";

    public static void main( final String[] args ) {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                try {
                    final ViewerModel model = ShotProcessor.createModel( CSV_OUTPUT_FILE );

                    final HeightShotViewer heightShotViewer = new HeightShotViewer( model );
                    heightShotViewer.setPreferredSize( new Dimension( 1200, 400 ) );

                    final SideShotViewer sideShotViewer = new SideShotViewer( model );
                    sideShotViewer.setPreferredSize( new Dimension( 1200, 400 ) );

                    final JFrame frame = new JFrame( "Historical Shot Data" );
                    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

                    final Container c = frame.getContentPane();
                    c.setLayout( new BorderLayout() );
                    c.add( heightShotViewer, BorderLayout.NORTH );
                    c.add( sideShotViewer, BorderLayout.SOUTH );

                    frame.pack();
                    frame.setLocationRelativeTo( null );
                    frame.setVisible( true );
                } catch ( final Exception e ) {
                    e.printStackTrace();
                }
            }
        } );
    }

    private TrackManShotViewer() throws Exception {
        // nothing to do
    }

}