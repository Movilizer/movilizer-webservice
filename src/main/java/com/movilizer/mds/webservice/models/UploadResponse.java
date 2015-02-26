package com.movilizer.mds.webservice.models;

public class UploadResponse {
    private static final int SC_OK = 200;
    private static final int SC_MULTIPLE_CHOICES = 300;

    private int statusCode;
    private String message;

    public UploadResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public boolean wasSucceful() {
        return SC_OK <= statusCode && statusCode < SC_MULTIPLE_CHOICES;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
