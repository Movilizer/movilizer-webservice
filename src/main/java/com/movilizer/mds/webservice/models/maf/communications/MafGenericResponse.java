package com.movilizer.mds.webservice.models.maf.communications;


import com.movilizer.mds.webservice.models.maf.MafGenericScript;

public class MafGenericResponse extends MafResponse { ;
    private MafGenericScript genericScript;

    public MafGenericResponse(boolean successful, String errorMessage,
                              MafGenericScript genericScript) {
        super(successful, errorMessage);
        this.genericScript = genericScript;
    }

    public MafGenericScript getGenericScript() {
        return genericScript;
    }

    public void setGenericScript(MafGenericScript genericScript) {
        this.genericScript = genericScript;
    }
}
