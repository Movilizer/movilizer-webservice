package com.movilizer.mds.webservice.models.maf.communications;


import com.movilizer.mds.webservice.models.maf.MafLibraryScript;

public class MafLibraryResponse extends MafResponse { ;
    private MafLibraryScript libraryScript;

    public MafLibraryResponse(boolean successful, String errorMessage,
                              MafLibraryScript libraryScript) {
        super(successful, errorMessage);
        this.libraryScript = libraryScript;
    }

    public MafLibraryScript getLibraryScript() {
        return libraryScript;
    }

    public void setLibraryScript(MafLibraryScript libraryScript) {
        this.libraryScript = libraryScript;
    }
}
