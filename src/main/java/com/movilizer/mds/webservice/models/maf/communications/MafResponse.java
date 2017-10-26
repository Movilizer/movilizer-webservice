package com.movilizer.mds.webservice.models.maf.communications;


public abstract class MafResponse {
    private boolean successful;
    private String errorMessage;

    public MafResponse(boolean successful, String errorMessage) {
        this.successful = successful;
        this.errorMessage = errorMessage;
    }

    public boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
