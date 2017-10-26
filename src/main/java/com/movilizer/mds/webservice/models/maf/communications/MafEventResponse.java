package com.movilizer.mds.webservice.models.maf.communications;


import com.movilizer.mds.webservice.models.maf.MafEventScript;

public class MafEventResponse extends MafResponse { ;
    private MafEventScript eventScript;

    public MafEventResponse(boolean successful, String errorMessage, MafEventScript eventScript) {
        super(successful, errorMessage);
        this.eventScript = eventScript;
    }

    public MafEventScript getEventScript() {
        return eventScript;
    }

    public void setEventScript(MafEventScript eventScript) {
        this.eventScript = eventScript;
    }
}
