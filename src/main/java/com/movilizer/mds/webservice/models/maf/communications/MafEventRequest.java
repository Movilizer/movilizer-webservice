package com.movilizer.mds.webservice.models.maf.communications;

import com.movilizer.mds.webservice.models.maf.MafEventScript;


public class MafEventRequest extends MafRequest {
    private static final String RESOURCE_RELATIVE_URI = "/api/v2/event/store";

    private MafEventScript eventScript;

    public MafEventRequest(Long systemId, String systemPassword, String authToken,
                           MafEventScript eventScript) {
        super(systemId, systemPassword, authToken);
        this.eventScript = eventScript;
    }

    @Override
    public String getResourceRelativeUri() {
        return RESOURCE_RELATIVE_URI;
    }

    public MafEventScript getEventScript() {
        return eventScript;
    }

    public void setEventScript(MafEventScript eventScript) {
        this.eventScript = eventScript;
    }
}
