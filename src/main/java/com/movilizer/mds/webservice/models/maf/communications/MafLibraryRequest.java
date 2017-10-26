package com.movilizer.mds.webservice.models.maf.communications;

import com.movilizer.mds.webservice.models.maf.MafLibraryScript;


public class MafLibraryRequest extends MafRequest {
    private static final String RESOURCE_RELATIVE_URI = "/api/v2/lib/store";

    private MafLibraryScript libraryScript;

    public MafLibraryRequest(Long systemId, String systemPassword, String authToken,
                             MafLibraryScript libraryScript) {
        super(systemId, systemPassword, authToken);
        this.libraryScript = libraryScript;
    }

    @Override
    public String getResourceRelativeUri() {
        return RESOURCE_RELATIVE_URI;
    }

    public MafLibraryScript getLibraryScript() {
        return libraryScript;
    }

    public void setLibraryScript(MafLibraryScript libraryScript) {
        this.libraryScript = libraryScript;
    }
}
