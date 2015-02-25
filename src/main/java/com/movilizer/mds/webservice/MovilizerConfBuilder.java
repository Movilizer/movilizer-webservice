package com.movilizer.mds.webservice;

import com.movilizer.mds.webservice.services.MovilizerDistributionService;

class MovilizerConfBuilder {

    protected MovilizerConfBuilder() {
    }

    public MovilizerDistributionService getService() {
        return null;
    }

    public MovilizerConfBuilder setEndpoint(String endpointAddress) {
        return this;
    }

}
