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

package com.movilizer.mds.webservice.services;

import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilitas.movilizer.v15.MovilizerResponse;
import com.movilizer.mds.webservice.exceptions.MovilizerXMLException;
import com.movilizer.mds.webservice.messages.MESSAGES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

class MovilizerXMLParserServiceImpl implements MovilizerXMLParserService {
    private static final Logger logger = LoggerFactory.getLogger(MovilizerWebService.class);
    private final Charset outputEncoding;
    private final Unmarshaller movilizerXMLUnmarshaller;
    private final Marshaller movilizerXMLMarshaller;

    protected MovilizerXMLParserServiceImpl(final Charset outputEncoding) {
        this.outputEncoding = outputEncoding;
        try {

            // see: http://stackoverflow.com/questions/14162159/supplying-a-different-version-of-jaxb-for-jax-ws-in-java-1-6
            System.setProperty("javax.xml.bind.JAXBContext",
                    "com.sun.xml.internal.bind.v2.ContextFactory");

            final JAXBContext movilizerXMLContext = JAXBContext.newInstance(MovilizerRequest.class,
                    MovilizerResponse.class);
            movilizerXMLMarshaller = movilizerXMLContext.createMarshaller();
            movilizerXMLMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            movilizerXMLMarshaller.setProperty(Marshaller.JAXB_ENCODING, outputEncoding.name());
            movilizerXMLUnmarshaller = movilizerXMLContext.createUnmarshaller();
            movilizerXMLUnmarshaller.setEventHandler(new ValidationEventHandler() {
                @Override
                public boolean handleEvent(final ValidationEvent event) {
                    if (logger.isErrorEnabled()) {
                        logger.error(MESSAGES.UNMARSHALLING_XML_ERROR + event.getMessage());
                    }
                    return true;
                }
            });
        } catch (JAXBException e) {
            throw new MovilizerXMLException(e);
        }
    }

    public MovilizerRequest getRequestFromFile(final Path filePath) {
        return getMovilizerElementFromPath(filePath, MovilizerRequest.class);
    }

    public MovilizerRequest getRequestFromString(final String requestString) {
        return getMovilizerElementFromString(requestString, MovilizerRequest.class);
    }

    public <T> T getMovilizerElementFromPath(final Path elementPath,
                                             final Class<T> movilizerElementClass) {
        if (!Files.exists(elementPath)) {
            if (logger.isErrorEnabled()) {
                logger.error(MESSAGES.REQUEST_FILE_NOT_FOUND
                        + elementPath.toAbsolutePath().toString());
            }
            throw new MovilizerXMLException(MESSAGES.REQUEST_FILE_NOT_FOUND
                    + elementPath.toAbsolutePath().toString());
        }
        JAXBElement<T> root;
        try {
            root = movilizerXMLUnmarshaller.unmarshal(new StreamSource(elementPath.toFile()),
                    movilizerElementClass);
        } catch (JAXBException e) {
            throw new MovilizerXMLException(e);
        }
        return root.getValue();
    }

    public <T> T getMovilizerElementFromString(final String elementString,
                                               final Class<T> movilizerElementClass) {
        if (elementString == null) {
            if (logger.isErrorEnabled()) {
                logger.error(MESSAGES.REQUEST_STRING_MUST_NOT_BE_NULL);
            }
            throw new MovilizerXMLException(MESSAGES.REQUEST_STRING_MUST_NOT_BE_NULL);
        }
        JAXBElement<T> root;
        try {
            root = movilizerXMLUnmarshaller.unmarshal(new StreamSource(new BufferedReader(
                    new StringReader(elementString))), movilizerElementClass);
        } catch (JAXBException e) {
            throw new MovilizerXMLException(e);
        }
        return root.getValue();
    }

    public String printRequest(final MovilizerRequest request) {
        final StringWriter writer = new StringWriter();
        try {
            movilizerXMLMarshaller.marshal(request, writer);
        } catch (JAXBException e) {
            throw new MovilizerXMLException(e);
        }
        return writer.toString();
    }

    public String printResponse(final MovilizerResponse response) {
        final StringWriter writer = new StringWriter();
        try {
            movilizerXMLMarshaller.marshal(response, writer);
        } catch (JAXBException e) {
            throw new MovilizerXMLException(e);
        }
        return writer.toString();
    }

    public <T> String printMovilizerElementToString(final T movilizerElement,
                                                    final Class<T> movilizerElementClass) {
        final StringWriter writer = new StringWriter();
        try {
            JAXBElement<T> element = new JAXBElement<>(
                    QName.valueOf(movilizerElementClass.getSimpleName()), movilizerElementClass,
                    movilizerElement);
            movilizerXMLMarshaller.marshal(element, writer);
        } catch (JAXBException e) {
            throw new MovilizerXMLException(e);
        }
        return writer.toString();
    }

    public void saveRequestToFile(final MovilizerRequest request, final Path filePath) {
        final boolean foldersHaveBeenCreated = filePath.toFile().getParentFile().mkdirs();
        if (foldersHaveBeenCreated && logger.isTraceEnabled()) {
            logger.trace(String.format(MESSAGES.FOLDERS_CREATED, filePath));
        }
        BufferedWriter fileWriter;
        try {
            fileWriter = Files.newBufferedWriter(filePath, outputEncoding);
        } catch (IOException e) {
            throw new MovilizerXMLException(e);
        }
        try {
            movilizerXMLMarshaller.marshal(request, fileWriter);
            if (logger.isInfoEnabled()) {
                logger.info(String.format(MESSAGES.SUCCESSFUL_REQUEST_TO_FILE,
                        filePath.toAbsolutePath().toString()));
            }
        } catch (JAXBException e) {
            throw new MovilizerXMLException(e);
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(MESSAGES.CANNOT_CLOSE_FILE + filePath.toAbsolutePath());
                }
            }
        }
    }
}
