package com.driver.exceptions;

public class NoCabAvailableException extends  RuntimeException{

    public NoCabAvailableException(String message){
        super(message);
    }
}
