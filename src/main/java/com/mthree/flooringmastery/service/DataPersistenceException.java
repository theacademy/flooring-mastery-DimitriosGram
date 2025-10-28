package com.mthree.flooringmastery.service;

public class DataPersistenceException extends Exception {
    public DataPersistenceException(String message) {
        super(message);
    }

    public DataPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
