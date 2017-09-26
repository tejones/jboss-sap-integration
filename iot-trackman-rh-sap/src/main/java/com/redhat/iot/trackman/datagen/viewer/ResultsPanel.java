package com.redhat.iot.trackman.datagen.viewer;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public final class ResultsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public ResultsPanel() {
        setBackground( Color.WHITE );
        setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        setLayout( new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextArea txaNotes = new JTextArea(20, 1);
        txaNotes.setLineWrap( true );
        txaNotes.setWrapStyleWord( true );
        final StringBuilder notes = new StringBuilder();
        notes.append( "We look at many attributes of your shot to determine the appropriate training plan. Here are some of what we look at:\n\n" );
        notes.append(  "- launch direction,\n" );
        notes.append(  "- ball speed,\n" );
        notes.append(  "- club speed,\n" );
        notes.append(  "- max height,\n" );
        notes.append(  "- carry,\n" );
        notes.append(  "- carry side,\n" );
        notes.append(  "- landing angle, and\n" );
        notes.append(  "- spin rate.\n\n" );
        notes.append( "Results & Recommendations\n\n" );
        notes.append(  "- The bottom chart on the left indicates a tendency to slice both iron and driver shots. Here are a few things you can try:\n " );
        notes.append(  " > close your stance a little,\n" );
        notes.append(  "  > keep you club face square,\n" );
        notes.append(  "  > tilt your shoulders away from the target,\n" );
        notes.append(  "  > turn your hands to the right on your grip, and\n" );
        notes.append(  "  > make sure you are aiming straight at your target,\n" );
        notes.append(  "- The top chart on the left indicates your max height is the same for both clubs which is good but the overall height is a bit high. Try moving the ball back in your stance. " );
        txaNotes.setText( notes.toString() );
        add( txaNotes, gbc );
    }

}
