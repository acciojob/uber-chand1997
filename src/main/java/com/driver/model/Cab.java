package com.driver.model;

import javax.persistence.*;

@Entity
public class Cab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int cabId;

    int perKmRate;

    boolean available;

    @OneToOne
    @JoinColumn
    Driver driver;

    public Cab() {
    }

    public Cab(int perKmRate, boolean available) {

        this.perKmRate = perKmRate;
        this.available = available;

    }

    public int getId() {
        return cabId;
    }

    public void setId(int cabId) {
        this.cabId = cabId;
    }

    public int getPerKmRate() {
        return perKmRate;
    }

    public void setPerKmRate(int perKmRate) {
        this.perKmRate = perKmRate;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}