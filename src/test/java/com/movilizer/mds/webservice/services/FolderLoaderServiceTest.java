package com.movilizer.mds.webservice.services;

import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilizer.mds.webservice.defaults.DefaultValues;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@RunWith(JUnit4.class)
public class FolderLoaderServiceTest {
  private static final String requestsDir = "/requests";

  private FolderLoaderService loaderService;
  private MovilizerXMLParserService xmlParserService;

  @Before
  public void setUp() throws Exception {
      xmlParserService = new MovilizerXMLParserServiceImpl(DefaultValues.OUTPUT_ENCODING);
      loaderService = new FolderLoaderService(xmlParserService);
  }


  @Test
  public void testLoad2RequestFromFolder() throws Exception {
    Path folderPath = Paths.get(getClass().getResource(requestsDir).toURI());
    List<MovilizerRequest> loadedRequests = loaderService.loadRequestsFromFolder(folderPath);

    assertThat(loadedRequests.size(), is(2));
  }

}
