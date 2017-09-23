package com.redhat.iot.trackman.datagen.domain;

public final class BallPosition {

    private double relativeTime;
    private String shotId;
    private double x;
    private double y;
    private double z;

    public BallPosition() {
        // nothing to do
    }

    public double getRelativeTime() {
        return this.relativeTime;
    }

    public String getShotId() {
        return this.shotId;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public void setRelativeTime( final double relativeTime ) {
        this.relativeTime = relativeTime;
    }

    public void setShotId( final String shotId ) {
        this.shotId = shotId;
    }

    public void setX( final double x ) {
        this.x = x;
    }

    public void setY( final double y ) {
        this.y = y;
    }

    public void setZ( final double z ) {
        this.z = z;
    }

}