package com.redhat.iot.trackman.datagen.domain;

public final class Hole {

    private String courseId;
    private String id;
    private int number;

    public Hole() {
        // nothing to do
    }

    public String getCourseId() {
        return this.courseId;
    }

    public String getId() {
        return this.id;
    }

    public int getNumber() {
        return this.number;
    }

    public void setCourseId( final String courseId ) {
        this.courseId = courseId;
    }

    public void setId( final String id ) {
        this.id = id;
    }

    public void setNumber( final int number ) {
        this.number = number;
    }

}