package com.redhat.iot.trackman.datagen.domain;

public final class Course {

    private String id;
    private String name;
    private int number;

    public Course() {
        // nothing to do
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getNumber() {
        return this.number;
    }

    public void setId( final String id ) {
        this.id = id;
    }

    public void setName( final String name ) {
        this.name = name;
    }

    public void setNumber( final int number ) {
        this.number = number;
    }

}