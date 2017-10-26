package com.movilizer.mds.webservice.models.maf.communications;

import java.net.URI;


public abstract class MafRequest {
    private Long systemId;
    private String systemPassword;
    private String authToken;

    public MafRequest(Long systemId, String systemPassword, String authToken) {
        this.systemId = systemId;
        this.systemPassword = systemPassword;
        this.authToken = authToken;
    }

    public String getResourceURI(URI cloudBaseEndpoint) {
        return cloudBaseEndpoint + getResourceRelativeUri();
    }

    abstract String getResourceRelativeUri();

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getSystemPassword() {
        return systemPassword;
    }

    public void setSystemPassword(String systemPassword) {
        this.systemPassword = systemPassword;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
