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

package com.movilizer.mds.webservice.models;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.File;
import java.io.InputStream;

public class MovilizerUploadForm {

    public HttpEntity getForm(InputStream file, String filename, long systemId, String password, String pool, String key, String lang, String suffix, String ack) {
        MultipartEntityBuilder form = getForm(systemId, password, pool, key, lang, suffix, ack);
        form.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, filename);
        return form.build();
    }

    public HttpEntity getForm(File file, long systemId, String password, String pool, String key, String lang, String suffix, String ack) {
        MultipartEntityBuilder form = getForm(systemId, password, pool, key, lang, suffix, ack);
        form.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
        return form.build();
    }

    private MultipartEntityBuilder getForm(long systemId, String password, String pool, String key, String lang, String suffix, String ack) {
        MultipartEntityBuilder form = MultipartEntityBuilder.create()
                .addTextBody("systemId", String.valueOf(systemId), ContentType.TEXT_PLAIN)
                .addTextBody("password", password, ContentType.TEXT_PLAIN)
                .addTextBody("pool", pool, ContentType.TEXT_PLAIN)
                .addTextBody("key", key, ContentType.TEXT_PLAIN)
                .addTextBody("suffix", suffix, ContentType.TEXT_PLAIN);
        if (lang != null) form.addTextBody("lang", lang, ContentType.TEXT_PLAIN);
        if (ack != null) form.addTextBody("ack", suffix, ContentType.TEXT_PLAIN);
        return form;
    }
}
