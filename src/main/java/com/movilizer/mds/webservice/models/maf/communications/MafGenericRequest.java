package com.movilizer.mds.webservice.models.maf.communications;

import com.movilizer.mds.webservice.models.maf.MafGenericScript;


public class MafGenericRequest extends MafRequest {
    private static final String RESOURCE_RELATIVE_URI = "/api/v2/gen/store";

    private MafGenericScript genericScript;

    public MafGenericRequest(Long systemId, String systemPassword, String authToken,
                             MafGenericScript genericScript) {
        super(systemId, systemPassword, authToken);
        this.genericScript = genericScript;
    }

    @Override
    public String getResourceRelativeUri() {
        return RESOURCE_RELATIVE_URI;
    }

    public MafGenericScript getGenericScript() {
        return genericScript;
    }

    public void setGenericScript(MafGenericScript genericScript) {
        this.genericScript = genericScript;
    }
}
