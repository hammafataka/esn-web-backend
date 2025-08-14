package dev.mfataka.esnzlin.exceptions;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String exception){
        super(exception);
    }
}
