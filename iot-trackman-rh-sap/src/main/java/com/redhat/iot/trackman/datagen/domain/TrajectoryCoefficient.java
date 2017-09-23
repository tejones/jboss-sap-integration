package com.redhat.iot.trackman.datagen.domain;

public final class TrajectoryCoefficient {

    private int ballTrajectoryId;
    private int dataPointIndex;
    private double xAxis;
    private double yAxis;
    private double zAxis;

    public TrajectoryCoefficient() {
        // nothing to do
    }

    public int getBallTrajectoryId() {
        return this.ballTrajectoryId;
    }

    public int getDataPointIndex() {
        return this.dataPointIndex;
    }

    public double getXAxis() {
        return this.xAxis;
    }

    public double getYAxis() {
        return this.yAxis;
    }

    public double getZAxis() {
        return this.zAxis;
    }

    public void setBallTrajectoryId( final int ballTrajectoryId ) {
        this.ballTrajectoryId = ballTrajectoryId;
    }

    public void setDataPointIndex( final int dataPointIndex ) {
        this.dataPointIndex = dataPointIndex;
    }

    public void setXAxis( final double xAxis ) {
        this.xAxis = xAxis;
    }

    public void setYAxis( final double yAxis ) {
        this.yAxis = yAxis;
    }

    public void setZAxis( final double zAxis ) {
        this.zAxis = zAxis;
    }

}