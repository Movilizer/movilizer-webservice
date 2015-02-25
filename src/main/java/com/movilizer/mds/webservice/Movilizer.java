package com.movilizer.mds.webservice;

import com.movilizer.mds.webservice.services.MovilizerDistributionService;

public abstract class Movilizer {

    public static MovilizerDistributionService getService() {
        return new MovilizerConfBuilder().getService();
    }

    public static MovilizerConfBuilder buildConf() {
        return new MovilizerConfBuilder();
    }

}
