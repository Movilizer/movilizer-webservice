package com.movilizer.mds.webservice;

import com.movilizer.mds.webservice.exceptions.MovilizerWebServiceException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public enum EndPoint {
    DEMO("https://demo.movilizer.com/MovilizerDistributionService/WebService/", "https://demo.movilizer.com/mds/document"),
    PROD("https://movilizer.com/MovilizerDistributionService/WebService/", "https://movilizer.com/mds/document");

    private final URL mdsUrl;
    private final URL uploadUrl;

    private EndPoint(final String mdsUrl, final String uploadUrl) throws MovilizerWebServiceException {
        try {
            this.mdsUrl = URI.create(mdsUrl).toURL();
            this.uploadUrl = URI.create(uploadUrl).toURL();
        } catch (MalformedURLException e) {
            throw new MovilizerWebServiceException(e);
        }

    }

    @Override
    public String toString() {
        return "EndPoint{" +
                "mdsUrl='" + mdsUrl + '\'' +
                ", uploadUrl='" + uploadUrl + '\'' +
                '}';
    }

    public URL getMdsUrl() {
        return mdsUrl;
    }

    public URL getUploadUrl() {
        return uploadUrl;
    }
}
