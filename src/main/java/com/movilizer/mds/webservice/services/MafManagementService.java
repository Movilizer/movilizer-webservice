/*
 * Copyright 2017 Movilizer GmbH
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

package com.movilizer.mds.webservice.services;

import com.google.gson.Gson;
import com.movilizer.mds.webservice.defaults.DefaultValues;
import com.movilizer.mds.webservice.exceptions.MovilizerMAFManagementException;
import com.movilizer.mds.webservice.exceptions.MovilizerWebServiceException;
import com.movilizer.mds.webservice.messages.MESSAGES;
import com.movilizer.mds.webservice.models.maf.MafCliMetaFile;
import com.movilizer.mds.webservice.models.maf.MafSource;
import com.movilizer.mds.webservice.models.maf.communications.MafRequest;
import com.movilizer.mds.webservice.models.maf.communications.MafRequestFactory;
import com.movilizer.mds.webservice.models.maf.communications.MafResponse;
import com.movilizer.mds.webservice.models.maf.communications.MafResponseClassFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;


class MafManagementService {
    private static final Logger logger = LoggerFactory.getLogger(MovilizerWebService.class);
    private static final Gson gson = new Gson();
    private static final String USER_AGENT_HEADER_KEY = "User-Agent";
    private static final String GROOVY_EXTENSION = ".groovy";
    private static final String META_FILE_EXTENSION = ".meta.json";

    private URL mafBaseAddress;
    private Integer defaultConnectionTimeoutInMillis;

    protected MafManagementService(URL mafBaseAddress, Integer defaultConnectionTimeoutInMillis) {
        this.mafBaseAddress = mafBaseAddress;
        this.defaultConnectionTimeoutInMillis = defaultConnectionTimeoutInMillis;
    }

    public void setMafBaseAddress(URL mafBaseAddress) {
        this.mafBaseAddress = mafBaseAddress;
    }

    public MafSource readSource(File sourceFile) {
        try {
            MafCliMetaFile meta = readMetaFile(sourceFile);
            String scriptSrc = new String(Files.readAllBytes(sourceFile.toPath()));
            meta.getSource().setScriptSrc(scriptSrc);
            return meta.getSource();
        } catch (IOException e) {
            throw new MovilizerMAFManagementException(e);
        }
    }

    protected MafCliMetaFile readMetaFile(File sourceFile) throws FileNotFoundException {
        String metaFileName = sourceFile.getName().replace(GROOVY_EXTENSION, META_FILE_EXTENSION);
        Path metaFilePath = sourceFile.toPath().resolveSibling(metaFileName);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(MESSAGES.LOADING_MAF_METADATA, metaFilePath.toString()));
        }
        return gson.fromJson(new FileReader(metaFilePath.toFile()), MafCliMetaFile.class);
    }

    public MafResponse deploySourceSync(long systemId, String password, String token,
                                        File sourceFile) {
        MafSource source = readSource(sourceFile);
        return deploySourceSync(systemId, password, token, source);
    }

    public MafResponse deploySourceSync(long systemId, String password, String token,
                                        MafSource source) {
        // Override system id in meta file
        source.setScriptSystemId(systemId);

        MafRequest request = MafRequestFactory.getInstance().getRequest(systemId, password, token,
                source);

        HttpEntity response = performRequest(request);

        Reader responseReader;
        try {
            responseReader = new InputStreamReader(response.getContent());
        } catch (IOException e) {
            throw new MovilizerWebServiceException(e);
        }

        MafResponse result = gson.fromJson(responseReader,
                MafResponseClassFactory.getInstance().getResponseClass(source));

        if (!result.getSuccessful()) {
            throw new MovilizerWebServiceException(String.format(MESSAGES.MAF_UPLOAD_FAILED,
                    result.getErrorMessage()));
        }

        return result;
    }

    private HttpEntity performRequest(MafRequest request) {
        try {
            HttpResponse response = Request.Post(request.getResourceURI(mafBaseAddress.toURI()))
                    .addHeader(USER_AGENT_HEADER_KEY, DefaultValues.USER_AGENT)
                    .connectTimeout(defaultConnectionTimeoutInMillis)
                    .body(new StringEntity(gson.toJson(request), ContentType.APPLICATION_JSON))
                    .execute().returnResponse();
            int statusCode = response.getStatusLine().getStatusCode();
            if (!(HttpStatus.SC_OK <= statusCode && statusCode < HttpStatus.SC_MULTIPLE_CHOICES)) {
                String errorMessage = response.getStatusLine().getReasonPhrase();
                throw new MovilizerWebServiceException(String.format(
                        MESSAGES.MAF_UPLOAD_FAILED_WITH_CODE, statusCode, errorMessage));
            }
            return response.getEntity();
        } catch (IOException | URISyntaxException e) {
            throw new MovilizerWebServiceException(e);
        }
    }
}
