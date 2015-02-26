package com.movilizer.mds.webservice.defaults;

import com.movilizer.mds.webservice.EndPoint;

import java.nio.charset.Charset;

public class DefaultValues {
    public static final EndPoint MOVILIZER_ENDPOINT = EndPoint.DEMO;
    public static final String MOVILIZER_FOLDER = "movilizer/";
    public static final String REQUEST_FOLDER = MOVILIZER_FOLDER + "requests/";
    public static final String MOVILIZER_XML_EXTENSION = ".mxml";
    public static final String REQUEST_FILE_NAME = "default" + MOVILIZER_XML_EXTENSION;
    public static final String CUSTOMIZING_FOLDER = MOVILIZER_FOLDER + "customizing/";
    public static final String DOCUMENTS_FOLDER = MOVILIZER_FOLDER + "documents/";
    public static final Charset OUTPUT_ENCODING = Charset.forName("UTF-8");
}
