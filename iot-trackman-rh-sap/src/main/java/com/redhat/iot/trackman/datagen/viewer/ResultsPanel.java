package com.redhat.iot.trackman.datagen.viewer;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

public final class ResultsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public ResultsPanel() {
        setBackground( Color.WHITE );
        setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        setLayout( new GridBagLayout() );
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        final JEditorPane results = new JEditorPane( "text/html", getResults() );
        results.setText( getResults() );
        add( results, gbc );
    }

    private String getResults() {
        final StringBuilder notes = new StringBuilder();
        notes.append( "<b>Summary</b>" );
        notes.append( "<p>" );
        notes.append( "We look at many attributes of your shot to determine the appropriate training plan. Here are some of what we look at:" );
        notes.append( "<ul>" );
        notes.append( "  <li>launch direction,</li>" );
        notes.append( "  <li>ball speed,</li>" );
        notes.append( "  <li>club speed,</li>" );
        notes.append( "  <li>max height,</li>" );
        notes.append( "  <li>carry distance,</li>" );
        notes.append( "  <li>carry side distance,</li>" );
        notes.append( "  <li>landing angle, and</li>" );
        notes.append( "  <li>spin rate.</li>" );
        notes.append( "</ul>" );
        notes.append( "<p>" );
        notes.append( "<b>Findings & Recommendations</b>" );
        notes.append( "<p>" );
        notes.append( "The bottom chart on the left indicates your accuracy could be improved since 3 of the 5 shots were pushed to the right. Here are a few things you can try:" );
        notes.append( "<ul>" );
        notes.append( "  <li>slow down your swing,</li>" );
        notes.append( "  <li>keep shoulders square with the ball,</li>" );
        notes.append( "  <li>pick specific targets in the fairway,</li>" );
        notes.append( "  <li>keep body aligned to target, and</li>" );
        notes.append( "  <li>make sure to keep head down until back shoulder brings it forward.</li>" );
        notes.append( "</ul>" );
        notes.append( "<p>" );
        notes.append( "The top chart on the left indicates your max height is not as consistent as it could be. Try the following:" );
        notes.append( "<ul>" );
        notes.append( "  <li>position the ball for longer yardage clubs toward the front foot of your stance, and</li>" );
        notes.append( "  <li>position the ball for shorter yardage clubs toward the center of your stance.</li>" );
        notes.append( "</ul>" );
        return notes.toString();
    }

}
