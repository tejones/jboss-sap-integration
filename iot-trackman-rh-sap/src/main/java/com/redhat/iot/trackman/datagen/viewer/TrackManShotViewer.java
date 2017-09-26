package com.redhat.iot.trackman.datagen.viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public final class TrackManShotViewer {

    public static final String CSV_OUTPUT_FILE = "src/main/resources/generated//ballPositions.csv";

    public static void main( final String[] args ) {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                try {
                    final ViewerModel model = ShotProcessor.createModel( CSV_OUTPUT_FILE );

                    final HeightShotViewer heightShotViewer = new HeightShotViewer( model );
                    heightShotViewer.setPreferredSize( new Dimension( 1000, 400 ) );

                    final SideShotViewer sideShotViewer = new SideShotViewer( model );
                    sideShotViewer.setPreferredSize( new Dimension( 1000, 400 ) );
                    
                    final ResultsPanel resultsPanel = new ResultsPanel();
                    resultsPanel.setPreferredSize( new Dimension( 200, 800 ) );

                    final JFrame frame = new JFrame( "Historical Shot Data" );
                    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

                    final JPanel c = new JPanel();
                    frame.setContentPane( c );
                    c.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );
                    final BorderLayout layout = new BorderLayout( 10, 10 );
                    c.setLayout( layout );
                    
                    final JPanel left = new JPanel();
                    left.setLayout( new BorderLayout( 10, 10 ) );
                    left.add( heightShotViewer, BorderLayout.NORTH );
                    left.add( sideShotViewer, BorderLayout.SOUTH );
                    c.add( left, BorderLayout.WEST );
                    
                    c.add( resultsPanel, BorderLayout.EAST );

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