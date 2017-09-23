package com.redhat.iot.trackman.datagen.domain;

public final class BallTrajectory {

    private int dataPointIndex;
    private int id;
    private String kind;
    private double measuredTimeIntervalBegin;
    private double measuredTimeIntervalEnd;
    private String shotId;
    private double timeIntervalBegin;
    private double timeIntervalEnd;
    private double validTimeIntervalBegin;
    private double validTimeIntervalEnd;
    
    public BallTrajectory() {
        // nothing to do
    }

    public int getDataPointIndex() {
        return this.dataPointIndex;
    }

    public int getId() {
        return this.id;
    }

    public String getKind() {
        return this.kind;
    }

    public double getMeasuredTimeIntervalBegin() {
        return this.measuredTimeIntervalBegin;
    }

    public double getMeasuredTimeIntervalEnd() {
        return this.measuredTimeIntervalEnd;
    }

    public String getShotId() {
        return this.shotId;
    }

    public double getTimeIntervalBegin() {
        return this.timeIntervalBegin;
    }

    public double getTimeIntervalEnd() {
        return this.timeIntervalEnd;
    }

    public double getValidTimeIntervalBegin() {
        return this.validTimeIntervalBegin;
    }

    public double getValidTimeIntervalEnd() {
        return this.validTimeIntervalEnd;
    }

    public void setDataPointIndex( final int dataPointIndex ) {
        this.dataPointIndex = dataPointIndex;
    }

    public void setId( final int id ) {
        this.id = id;
    }

    public void setKind( final String kind ) {
        this.kind = kind;
    }

    public void setMeasuredTimeIntervalBegin( final double measuredTimeIntervalBegin ) {
        this.measuredTimeIntervalBegin = measuredTimeIntervalBegin;
    }

    public void setMeasuredTimeIntervalEnd( final double measuredTimeIntervalEnd ) {
        this.measuredTimeIntervalEnd = measuredTimeIntervalEnd;
    }

    public void setShotId( final String shotId ) {
        this.shotId = shotId;
    }

    public void setTimeIntervalBegin( final double timeIntervalBegin ) {
        this.timeIntervalBegin = timeIntervalBegin;
    }

    public void setTimeIntervalEnd( final double timeIntervalEnd ) {
        this.timeIntervalEnd = timeIntervalEnd;
    }

    public void setValidTimeIntervalBegin( final double validTimeIntervalBegin ) {
        this.validTimeIntervalBegin = validTimeIntervalBegin;
    }

    public void setValidTimeIntervalEnd( final double validTimeIntervalEnd ) {
        this.validTimeIntervalEnd = validTimeIntervalEnd;
    }

}