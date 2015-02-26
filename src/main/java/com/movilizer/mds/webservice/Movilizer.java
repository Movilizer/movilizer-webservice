package com.movilizer.mds.webservice;

import com.movilizer.mds.webservice.services.MovilizerConfBuilder;
import com.movilizer.mds.webservice.services.MovilizerDistributionService;

public class Movilizer {

    public static MovilizerDistributionService getService() {
        return new MovilizerConfBuilder().getService();
    }

    public static MovilizerConfBuilder buildConf() {
        return new MovilizerConfBuilder();
    }

    private Movilizer() {}
}
