package com.redhat.iot.trackman.datagen.viewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.iot.trackman.datagen.domain.BallPosition;

final class ViewerModel {

    public static final Stroke GRAPH_STROKE = new BasicStroke( 2f );

    public static final Color[] LINE_COLORS = new Color[] { new Color( 95, 76, 255 ),
                                                            new Color( 255, 120, 76 ),
                                                            new Color( 233, 231, 73 ),
                                                            new Color( 229, 0, 99 ),
                                                            new Color( 0, 229, 112 ),
                                                            new Color( 89, 79, 148 ),
                                                            new Color( 127, 127, 127 ), };

    public static final Color[] POINT_COLORS = new Color[] { new Color( 25, 0, 229 ),
                                                             new Color( 153, 37, 0 ),
                                                             new Color( 204, 201, 0 ),
                                                             new Color( 0, 178, 87 ),
                                                             new Color( 153, 0, 76 ),
                                                             new Color( 16, 11, 58 ),
                                                             new Color( 19, 19, 22 ), };

    private Color backgroundColor = Color.WHITE;
    private final Map< String, List< BallPosition > > ballPositions = new HashMap<>();
    private int distanceSize;
    private Color foregroundColor = Color.BLACK;
    private Color gridColor = new Color( 200, 200, 200, 200 );
    private int heightSize;
    private int labelPadding = 40;
    private double maxDistance;
    private double maxHeight;
    private double maxSide;
    private double minDistance;
    private double minHeight;
    private double minSide;
    private int yDivisions = 10;
    private int padding = 25;
    private int pointWidth = 4;
    private int sideSize;
    private Color xLabelColor = Color.black;
    private Color yLabelColor = Color.black;

    public ViewerModel() {
        // nothing to do
    }

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    public Map< String, List< BallPosition > > getBallPositions() {
        return this.ballPositions;
    }

    public int getDistanceSize() {
        return this.distanceSize;
    }

    public Color getForegroundColor() {
        return this.foregroundColor;
    }

    public Color getGridColor() {
        return this.gridColor;
    }

    public int getHeightSize() {
        return this.heightSize;
    }

    public int getLabelPadding() {
        return this.labelPadding;
    }

    public double getMaxDistance() {
        return this.maxDistance;
    }

    public double getMaxHeight() {
        return this.maxHeight;
    }

    public double getMaxSide() {
        return this.maxSide;
    }

    public double getMinDistance() {
        return this.minDistance;
    }

    public double getMinHeight() {
        return this.minHeight;
    }

    public double getMinSide() {
        return this.minSide;
    }

    public int getPadding() {
        return this.padding;
    }

    public int getPointWidth() {
        return this.pointWidth;
    }

    public int getSideSize() {
        return this.sideSize;
    }

    public Color getXLabelColor() {
        return this.xLabelColor;
    }

    public int getYDivisions() {
        return this.yDivisions;
    }

    public Color getYLabelColor() {
        return this.yLabelColor;
    }

    public void setBackgroundColor( final Color backgroundColor ) {
        this.backgroundColor = backgroundColor;
    }

    public void setDistanceSize( final int distanceSize ) {
        this.distanceSize = distanceSize;
    }

    public void setForegroundColor( final Color foregroundColor ) {
        this.foregroundColor = foregroundColor;
    }

    public void setGridColor( final Color gridColor ) {
        this.gridColor = gridColor;
    }

    public void setHeightSize( final int heightSize ) {
        this.heightSize = heightSize;
    }

    public void setLabelPadding( final int labelPadding ) {
        this.labelPadding = labelPadding;
    }

    public void setMaxDistance( final double maxDistance ) {
        this.maxDistance = maxDistance;
    }

    public void setMaxHeight( final double maxHeight ) {
        this.maxHeight = maxHeight;
    }

    public void setMaxSide( final double maxSide ) {
        this.maxSide = maxSide;
    }

    public void setMinDistance( final double minDistance ) {
        this.minHeight = minDistance;
    }

    public void setMinHeight( final double minHeight ) {
        this.minHeight = minHeight;
    }

    public void setMinSide( final double minSide ) {
        this.minSide = minSide;
    }

    public void setPadding( final int padding ) {
        this.padding = padding;
    }

    public void setPointWidth( final int pointWidth ) {
        this.pointWidth = pointWidth;
    }

    public void setSideSize( final int sideSize ) {
        this.sideSize = sideSize;
    }

    public void setXLabelColor( final Color xLabelColor ) {
        this.xLabelColor = xLabelColor;
    }

    public void setYDivisions( final int yDivisions ) {
        this.yDivisions = yDivisions;
    }

    public void setYLabelColor( final Color yLabelColor ) {
        this.yLabelColor = yLabelColor;
    }

}
