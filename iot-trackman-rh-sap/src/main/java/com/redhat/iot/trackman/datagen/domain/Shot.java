package com.redhat.iot.trackman.datagen.domain;

public final class Shot {

    private double ballSpeed;
    private double carry;
    private double carrySide;
    private double clupSpeed;
    private String holeId;
    private String id;
    private double landingAngle;
    private double launchAngle;
    private double launchDirection;
    private double maxHeight;
    private int playerId;
    private int shotNumber;
    private String shotTime;
    private double spinRate;

    public Shot() {
        // nothing to do
    }

    public double getBallSpeed() {
        return this.ballSpeed;
    }

    public double getCarry() {
        return this.carry;
    }

    public double getCarrySide() {
        return this.carrySide;
    }

    public double getClupSpeed() {
        return this.clupSpeed;
    }

    public String getHoleId() {
        return this.holeId;
    }

    public String getId() {
        return this.id;
    }

    public double getLandingAngle() {
        return this.landingAngle;
    }

    public double getLaunchAngle() {
        return this.launchAngle;
    }

    public double getLaunchDirection() {
        return this.launchDirection;
    }

    public double getMaxHeight() {
        return this.maxHeight;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public int getShotNumber() {
        return this.shotNumber;
    }

    public String getShotTime() {
        return this.shotTime;
    }

    public double getSpinRate() {
        return this.spinRate;
    }

    public void setBallSpeed( final double ballSpeed ) {
        this.ballSpeed = ballSpeed;
    }

    public void setCarry( final double carry ) {
        this.carry = carry;
    }

    public void setCarrySide( final double carrySide ) {
        this.carrySide = carrySide;
    }

    public void setClupSpeed( final double clupSpeed ) {
        this.clupSpeed = clupSpeed;
    }

    public void setHoleId( final String holeId ) {
        this.holeId = holeId;
    }

    public void setId( final String id ) {
        this.id = id;
    }

    public void setLandingAngle( final double landingAngle ) {
        this.landingAngle = landingAngle;
    }

    public void setLaunchAngle( final double launchAngle ) {
        this.launchAngle = launchAngle;
    }

    public void setLaunchDirection( final double launchDirection ) {
        this.launchDirection = launchDirection;
    }

    public void setMaxHeight( final double maxHeight ) {
        this.maxHeight = maxHeight;
    }

    public void setPlayerId( final int playerId ) {
        this.playerId = playerId;
    }

    public void setShotNumber( final int shotNumber ) {
        this.shotNumber = shotNumber;
    }

    public void setShotTime( final String shotTime ) {
        this.shotTime = shotTime;
    }

    public void setSpinRate( final double spinRate ) {
        this.spinRate = spinRate;
    }

}