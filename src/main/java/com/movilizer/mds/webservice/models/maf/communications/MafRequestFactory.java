package com.movilizer.mds.webservice.models.maf.communications;


import com.movilizer.mds.webservice.exceptions.MovilizerWebServiceException;
import com.movilizer.mds.webservice.models.maf.MafEventScript;
import com.movilizer.mds.webservice.models.maf.MafGenericScript;
import com.movilizer.mds.webservice.models.maf.MafLibraryScript;
import com.movilizer.mds.webservice.models.maf.MafSource;

public class MafRequestFactory {

    private MafRequestFactory() {
    }

    public static MafRequestFactory getInstance() {
        return new MafRequestFactory();
    }

    public MafRequest getRequest(Long systemId, String systemPassword, String authToken, MafSource
            source) {
        MafRequest request;
        if (source instanceof MafEventScript) {
            request = new MafEventRequest(systemId, systemPassword, authToken,
                    (MafEventScript) source);
        } else if (source instanceof MafLibraryScript) {
            request = new MafLibraryRequest(systemId, systemPassword, authToken,
                    (MafLibraryScript) source);
        } else if (source instanceof MafGenericScript) {
            request = new MafGenericRequest(systemId, systemPassword, authToken,
                    (MafGenericScript) source);
        } else {
            throw new MovilizerWebServiceException("Unknown MafSource type.");
        }
        return request;
    }
}
