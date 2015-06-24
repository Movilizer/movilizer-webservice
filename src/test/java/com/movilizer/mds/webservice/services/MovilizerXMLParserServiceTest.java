package com.movilizer.mds.webservice.services;

import com.movilitas.movilizer.v12.MovilizerMovelet;
import com.movilitas.movilizer.v12.MovilizerRequest;
import com.movilizer.mds.webservice.defaults.DefaultValues;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(JUnit4.class)
public class MovilizerXMLParserServiceTest {

    private static final String testRequestDir = "/requests";
    private static final String testEmptyRequestFilename = "ping-request.mxml";
    private static final String testMoveletRequestFilename = "create-movelet-request.mxml";
    private static final String testMoveletKey = "com.movilizer.mds.webservice.createMoveletTest";
    private static final String testMoveletQuestionTitle = "Create simple movelet test request";

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
        assertThat(request.getMoveletSet().get(0).getMovelet().get(0).getQuestion().isEmpty(), is(false));
        assertThat(request.getMoveletSet().get(0).getMovelet().get(0).getQuestion().size(), is(1));
        assertThat(request.getMoveletSet().get(0).getMovelet().get(0).getQuestion().get(0).getTitle(),
            is(testMoveletQuestionTitle));
    }

    @Test
    public void testLoadMoveletFromString() throws Exception {
        String moveletString = "" +
            "<movelet moveletKey=\"com.movilizer.mds.webservice.createMoveletTest\" moveletVersion=\"1\"" +
            "    moveletKeyExtension=\"DEV\" moveletType=\"MULTI\" initialQuestionKey=\"#1\" " +
            "    xmlns=\"http://movilitas.com/movilizer/v12\" >\n" +
            "            <question type=\"0\" key=\"#1\" title=\"Create simple movelet test request\">\n" +
            "                <answer key=\"#1_1\" nextQuestionKey=\"END\" position=\"0\" action=\"NONE\">\n" +
            "                    <text>This is a paragraph for the test movelet.</text>\n" +
            "                </answer>\n" +
            "                <text>Hi, this is a test movelet!</text>\n" +
            "            </question>\n" +
            "            <name>Create simple movelet test request</name>\n" +
            "        </movelet>";
        MovilizerMovelet movelet = xmlParserService.getMovilizerElementFromString(moveletString, MovilizerMovelet.class);
        assertThat(movelet, is(notNullValue()));
        assertThat(movelet.getMoveletKey(), is(notNullValue()));
        assertThat(movelet.getMoveletKey(), is(testMoveletKey));
        assertThat(movelet.getQuestion().isEmpty(), is(false));
        assertThat(movelet.getQuestion().size(), is(1));
        assertThat(movelet.getQuestion().get(0).getTitle(), is(testMoveletQuestionTitle));
    }

    @Test
    public void testPrintMoveletToString() throws Exception {
        String moveletString = "" +
            "<movelet moveletKey=\"com.movilizer.mds.webservice.createMoveletTest\" moveletVersion=\"1\"" +
            "    moveletKeyExtension=\"DEV\" moveletType=\"MULTI\" initialQuestionKey=\"#1\" " +
            "    xmlns=\"http://movilitas.com/movilizer/v12\" >\n" +
            "            <question type=\"0\" key=\"#1\" title=\"Create simple movelet test request\">\n" +
            "                <answer key=\"#1_1\" nextQuestionKey=\"END\" position=\"0\" action=\"NONE\">\n" +
            "                    <text>This is a paragraph for the test movelet.</text>\n" +
            "                </answer>\n" +
            "                <text>Hi, this is a test movelet!</text>\n" +
            "            </question>\n" +
            "            <name>Create simple movelet test request</name>\n" +
            "        </movelet>";
        MovilizerMovelet movelet = xmlParserService.getMovilizerElementFromString(moveletString, MovilizerMovelet.class);
        String printedMovelet = xmlParserService.printMovilizerElementToString(movelet, MovilizerMovelet.class);
        assertThat(printedMovelet, is(notNullValue()));
        assertThat(printedMovelet, containsString(testMoveletKey));
        assertThat(printedMovelet, containsString(testMoveletQuestionTitle));

        //Double check!!
        MovilizerMovelet moveletFromOutput = xmlParserService.getMovilizerElementFromString(printedMovelet,
            MovilizerMovelet.class);
        assertThat(moveletFromOutput, is(notNullValue()));
        assertThat(moveletFromOutput.getMoveletKey(), is(notNullValue()));
        assertThat(moveletFromOutput.getMoveletKey(), is(testMoveletKey));
        assertThat(moveletFromOutput.getQuestion().isEmpty(), is(false));
        assertThat(moveletFromOutput.getQuestion().size(), is(1));
        assertThat(moveletFromOutput.getQuestion().get(0).getTitle(), is(testMoveletQuestionTitle));
    }
}
