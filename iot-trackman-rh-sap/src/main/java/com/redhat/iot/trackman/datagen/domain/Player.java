package com.redhat.iot.trackman.datagen.domain;

public final class Player {

    private final String firstName;
    private final int id;
    private final String lastName;

    public Player( final int id,
                   final String firstName,
                   final String lastName ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public int getId() {
        return this.id;
    }

    public String getLastName() {
        return this.lastName;
    }

}