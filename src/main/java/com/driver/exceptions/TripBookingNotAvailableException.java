package com.driver.exceptions;

public class TripBookingNotAvailableException extends RuntimeException{

    public TripBookingNotAvailableException(String message){
        super(message);
    }
}
