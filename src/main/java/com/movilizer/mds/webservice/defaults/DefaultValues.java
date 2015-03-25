/*
 * Copyright 2015 Movilizer GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.movilizer.mds.webservice.defaults;

import com.movilizer.mds.webservice.EndPoint;

import java.nio.charset.Charset;

public class DefaultValues {
    public static final EndPoint MOVILIZER_ENDPOINT = EndPoint.DEMO;
    //    public static final String MOVILIZER_FOLDER = "movilizer/";
//    public static final String REQUEST_FOLDER = MOVILIZER_FOLDER + "requests/";
//    public static final String MOVILIZER_XML_EXTENSION = ".mxml";
//    public static final String REQUEST_FILE_NAME = "default" + MOVILIZER_XML_EXTENSION;
//    public static final String CUSTOMIZING_FOLDER = MOVILIZER_FOLDER + "customizing/";
//    public static final String DOCUMENTS_FOLDER = MOVILIZER_FOLDER + "documents/";
    public static final Charset OUTPUT_ENCODING = Charset.forName("UTF-8");
    public static final String USER_AGENT = "MovilizerJavaConnector/0.1";
}