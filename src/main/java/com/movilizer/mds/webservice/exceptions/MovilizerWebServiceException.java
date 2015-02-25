package com.movilizer.mds.webservice.exceptions;


public class MovilizerWebServiceException extends RuntimeException {
    public MovilizerWebServiceException(String message) {
        super(message);
    }
    public MovilizerWebServiceException(Throwable throwable) {
        super(throwable);
    }
}
