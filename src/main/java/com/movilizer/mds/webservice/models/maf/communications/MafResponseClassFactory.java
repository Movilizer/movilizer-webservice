package com.movilizer.mds.webservice.models.maf.communications;


import com.movilizer.mds.webservice.exceptions.MovilizerWebServiceException;
import com.movilizer.mds.webservice.models.maf.MafEventScript;
import com.movilizer.mds.webservice.models.maf.MafGenericScript;
import com.movilizer.mds.webservice.models.maf.MafLibraryScript;
import com.movilizer.mds.webservice.models.maf.MafSource;

import java.lang.reflect.Type;

public class MafResponseClassFactory {

    private MafResponseClassFactory() {
    }

    public static MafResponseClassFactory getInstance() {
        return new MafResponseClassFactory();
    }

    public Type getResponseClass(MafSource source) {
        Type responseClass;
        if (source instanceof MafEventScript) {
            responseClass = MafEventResponse.class;
        } else if (source instanceof MafLibraryScript) {
            responseClass = MafLibraryResponse.class;
        } else if (source instanceof MafGenericScript) {
            responseClass = MafGenericResponse.class;
        } else {
            throw new MovilizerWebServiceException("Unknown MafSource type.");
        }
        return responseClass;
    }
}
