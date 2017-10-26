package com.movilizer.mds.webservice.services;

import com.movilizer.mds.webservice.defaults.DefaultValues;
import com.movilizer.mds.webservice.models.FutureCallback;
import com.movilizer.mds.webservice.models.MovilizerUploadForm;
import com.movilizer.mds.webservice.models.UploadResponse;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(JUnit4.class)
public class UploadServiceTest {
  private static final String documentsDir = "/documents";
  private static final String pdfFilename = "movilizer_days.pdf";
  private static long SYSTEM_ID = Long.parseLong(System.getenv("MOV_SYSTEM_ID")); //Put your own here
  private static String PASSWORD = System.getenv("MOV_PASSWORD"); //Put your own here
  private static String DOCUMENT_POOL = "testMovilizerWebservicePdfPool";
  private static String DOCUMENT_KEY = "testMovilizerWebservicePdfKey";
  private static String LANG = "";
  private static String ACK = "";

  private UploadFileService uploadService;

  @Before
  public void setUp() throws Exception {
    uploadService = new UploadFileService(DefaultValues.MOVILIZER_ENDPOINT.getUploadUrl(), new MovilizerUploadForm(),
        DefaultValues.CONNECTION_TIMEOUT_IN_MILLIS);
  }

  @Test
  public void testSavePdf() throws Exception {
    Path folderPath = Paths.get(getClass().getResource(documentsDir).toURI());
    Path filePath = folderPath.resolve(pdfFilename);
    UploadResponse response = uploadService.uploadDocumentSync(filePath, SYSTEM_ID, PASSWORD,
              DOCUMENT_POOL, DOCUMENT_KEY, LANG, ACK);

    assertThat(response.wasSuccessful(), is(true));
    assertThat(response.getStatusCode(), is(200));
  }

  @Test
  public void testSavePdfAsync() throws Exception {
    Path folderPath = Paths.get(getClass().getResource(documentsDir).toURI());
    Path filePath = folderPath.resolve(pdfFilename);
    uploadService.uploadDocument(filePath, SYSTEM_ID, PASSWORD, DOCUMENT_POOL, DOCUMENT_KEY, LANG, ACK,
        new FutureCallback<UploadResponse>() {
          @Override
          public void onSuccess(UploadResponse response) {
          }

          @Override
          public void onComplete(UploadResponse response, Exception e) {
            assertThat(response, is(notNullValue()));
            assertThat(response.wasSuccessful(), is(true));
          }

          @Override
          public void onFailure(Exception e) {
          }
        });
  }
}
