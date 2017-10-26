package com.movilizer.mds.webservice.services;

import com.movilitas.movilizer.v15.MovilizerMovelet;
import com.movilitas.movilizer.v15.MovilizerRequest;
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

public class MovilizerXMLParserServiceTestFixture {

    private static final String testRequestDir = "/requests";
    private static final String testEmptyRequestFilename = "ping-request.mxml";
    private static final String testMoveletRequestFilename = "create-movelet-request.mxml";
    private static final String testMoveletKey = "com.movilizer.mds.webservice.createMoveletTest";
    private static final String testMoveletQuestionTitle = "Create simple movelet test request";

    protected MovilizerXMLParserService xmlParserService;

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
            "    xmlns=\"http://movilitas.com/movilizer/v15\" >\n" +
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
    public void testLoadRequestFromString() throws Exception {
        String moveletString = "<MovilizerRequest requestTrackingKey=\"\" systemId=\"1\" systemPassword=\"1\" \n" +
                "  numResponses=\"1000\" synchronousResponse=\"true\" \n" +
                "  useAutoAcknowledge=\"true\" xmlns=\"http://movilitas.com/movilizer/v15\">\n" +
                "  <moveletSet>\n" +
                "    <movelet moveletKey=\"com.movilizer.copec.poc.readMasterdata\" moveletVersion=\"1\" validTillDate=\"2049-12-31T23:59:00.000Z\" visible=\"false\" moveletType=\"MULTI\" initialQuestionKey=\"EPSILON_SCREEN\">\n" +
                "   \n" +
                "      <question type=\"41\" title=\"Epsilon Screen\" key=\"EPSILON_SCREEN\">\n" +
                "        <answer dummyAnswer=\"false\" nextQuestionKey=\"END\" position=\"1\" key=\"EPSILON_SCREEN_0\">\n" +
                "          <text>Mandatory Answer</text>\n" +
                "        </answer>\n" +
                "        <text/>\n" +
                "      </question>\n" +
                "      <name>Read Masterdata</name>\n" +
                "    </movelet>\n" +
                "  </moveletSet>\n" +
                "</MovilizerRequest>";
        MovilizerRequest movelet = xmlParserService.getRequestFromString(moveletString);
        assertThat(movelet, is(notNullValue()));
        assertThat(movelet.getMoveletSet().size(), is(1));
        assertThat(movelet.getMoveletSet().get(0).getMovelet().size(), is(1));
    }

    @Test
    public void testPrintMoveletToString() throws Exception {
        String moveletString = "" +
            "<movelet moveletKey=\"com.movilizer.mds.webservice.createMoveletTest\" moveletVersion=\"1\"" +
            "    moveletKeyExtension=\"DEV\" moveletType=\"MULTI\" initialQuestionKey=\"#1\" " +
            "    xmlns=\"http://movilitas.com/movilizer/v15\" >\n" +
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
