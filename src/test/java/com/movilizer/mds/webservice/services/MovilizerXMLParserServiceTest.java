package com.movilizer.mds.webservice.services;

import com.movilitas.movilizer.v12.MovilizerRequest;
import com.movilizer.mds.webservice.defaults.DefaultValues;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(JUnit4.class)
public class MovilizerXMLParserServiceTest {

    private static final String testRequestDir = "/requests";
    private static final String testEmptyRequestFilename = "ping-request.mxml";
    private static final String testMoveletRequestFilename = "create-movelet-request.mxml";
    private static final String testMoveletKey = "com.movilizer.mds.webservice.createMoveletTest";

    private MovilizerXMLParserService xmlParserService;

    @Before
    public void setUp() throws Exception {
        xmlParserService = new MovilizerXMLParserService(DefaultValues.OUTPUT_ENCODING);
    }

    @Test
    public void testLoadEmptyRequest() throws Exception {
        Path folderPath = Paths.get(getClass().getResource(testRequestDir).toURI());
        Path filePath = folderPath.resolve(testEmptyRequestFilename);
        MovilizerRequest request = xmlParserService.getRequestFromFile(filePath);
        assertThat(request, is(notNullValue()));
    }

    @Test
    public void testLoadMoveletRequest() throws Exception {
        Path folderPath = Paths.get(getClass().getResource(testRequestDir).toURI());
        Path filePath = folderPath.resolve(testMoveletRequestFilename);
        MovilizerRequest request = xmlParserService.getRequestFromFile(filePath);
        assertThat(request, is(notNullValue()));
        assertThat(request.getMoveletSet().isEmpty(), is(false));
        assertThat(request.getMoveletSet().size(), is(1));
        assertThat(request.getMoveletSet().get(0).getMovelet(), is(notNullValue()));
        assertThat(request.getMoveletSet().get(0).getMovelet().size(), is(1));
        assertThat(request.getMoveletSet().get(0).getMovelet().get(0).getMoveletKey(), is(testMoveletKey));
    }
}
