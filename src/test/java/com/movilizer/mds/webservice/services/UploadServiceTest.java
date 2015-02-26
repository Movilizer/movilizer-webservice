package com.movilizer.mds.webservice.services;

import com.movilizer.mds.webservice.defaults.DefaultValues;
import com.movilizer.mds.webservice.models.MovilizerUploadForm;
import com.movilizer.mds.webservice.models.UploadResponse;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Future;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(JUnit4.class)
public class UploadServiceTest {
    private static final String documentsDir = "/documents";
    private static final String pdfFilename = "movilizer_days.pdf";
    private static long SYSTEM_ID = 1L; //Put your own here
    private static String PASSWORD = "pass"; //Put your own here
    private static String DOCUMENT_POOL = "testMovilizerWebservicePdfPool";
    private static String DOCUMENT_KEY = "testMovilizerWebservicePdfKey";
    private static String LANG = "";
    private static String ACK = "";

    private UploadFileService uploadService;

    @Before
    public void setUp() throws Exception {
        uploadService = new UploadFileService(DefaultValues.MOVILIZER_ENDPOINT.getUploadUrl(), new MovilizerUploadForm());
    }

    @Ignore
    @Test
    public void testSavePdf() throws Exception {
        Path folderPath = Paths.get(getClass().getResource(documentsDir).toURI());
        Path filePath = folderPath.resolve(pdfFilename);
        uploadService.uploadDocument(filePath, SYSTEM_ID, PASSWORD,DOCUMENT_POOL, DOCUMENT_KEY, LANG, ACK);
    }

    @Ignore
    @Test
    public void testSavePdfAsync() throws Exception {
        Path folderPath = Paths.get(getClass().getResource(documentsDir).toURI());
        Path filePath = folderPath.resolve(pdfFilename);
        Future<UploadResponse> responseFuture = uploadService.uploadDocumentAsync(filePath, SYSTEM_ID, PASSWORD, DOCUMENT_POOL, DOCUMENT_KEY, LANG, ACK);
        while(!responseFuture.isDone()){
            //let's block!
        }
        UploadResponse response = responseFuture.get();
        assertThat(response, is(notNullValue()));
        assertThat(response.wasSucceful(), is(true));
    }
}
