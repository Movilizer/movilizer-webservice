package com.movilizer.mds.webservice.services;

import com.movilitas.movilizer.v12.MovilizerRequest;
import com.movilitas.movilizer.v12.MovilizerResponse;
import com.movilizer.mds.webservice.exceptions.MovilizerXMLException;
import com.movilizer.mds.webservice.messages.EN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

class MovilizerXMLParserService {
    private static final Logger logger = LoggerFactory.getLogger(MovilizerWebService.class);

    private Unmarshaller movilizerRequestUnmarshaller;
    private Marshaller movilizerRequestMarshaller;
    private Marshaller movilizerResponseMarshaller;
    private Charset outputEncoding;

    protected MovilizerXMLParserService(Charset outputEncoding) {
        this.outputEncoding = outputEncoding;
        try {
            JAXBContext movilizerRequestContext = JAXBContext.newInstance(MovilizerRequest.class);
            movilizerRequestMarshaller = movilizerRequestContext.createMarshaller();
            movilizerRequestMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            movilizerRequestMarshaller.setProperty(Marshaller.JAXB_ENCODING, outputEncoding.name());
            movilizerRequestUnmarshaller = movilizerRequestContext.createUnmarshaller();
//        ValidationEventHandler validationEventHandler = new javax.xml.bind.helpers.DefaultValidationEventHandler();
            movilizerRequestUnmarshaller.setEventHandler(new ValidationEventHandler() {
                @Override
                public boolean handleEvent(ValidationEvent event) {
                    logger.error(EN.UNMARSHALL_XML_ERROR + event.getMessage());
                    return true;
                }
            });

            JAXBContext movilizerResponseContext = JAXBContext.newInstance(MovilizerResponse.class);
            movilizerResponseMarshaller = movilizerResponseContext.createMarshaller();
            movilizerResponseMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        } catch (JAXBException e) {
            throw new MovilizerXMLException(e);
        }
    }

    protected MovilizerRequest getRequestFromFile(Path filePath) {
        if (!Files.exists(filePath)) {
            logger.error(EN.REQUEST_FILE_NOT_FOUND + filePath.toAbsolutePath().toString());
        }
        JAXBElement<MovilizerRequest> root;
        try {
            root = movilizerRequestUnmarshaller.unmarshal(new StreamSource(filePath.toFile()), MovilizerRequest.class);
        } catch (JAXBException e) {
            throw new MovilizerXMLException(e);
        }
        return root.getValue();
    }

    protected String printRequest(MovilizerRequest request) {
        StringWriter writer = new StringWriter();
        try {
            movilizerRequestMarshaller.marshal(request, writer);
        } catch (JAXBException e) {
            throw new MovilizerXMLException(e);
        }
        return writer.toString();
    }

    protected String printResponse(MovilizerResponse response) {
        StringWriter writer = new StringWriter();
        try {
            movilizerResponseMarshaller.marshal(response, writer);
        } catch (JAXBException e) {
            throw new MovilizerXMLException(e);
        }
        return writer.toString();
    }

    protected void saveRequestToFile(MovilizerRequest request, Path filePath) {
        filePath.toFile().getParentFile().mkdirs();
        BufferedWriter fileWriter;
        try {
            fileWriter = Files.newBufferedWriter(filePath, outputEncoding);
        } catch (IOException e) {
            throw new MovilizerXMLException(e);
        }
        try {
            movilizerRequestMarshaller.marshal(request, fileWriter);
        } catch (JAXBException e) {
            throw new MovilizerXMLException(e);
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                logger.error(EN.CANNOT_CLOSE_FILE + filePath.toAbsolutePath());
            }
        }
    }
}
