package com.movilizer.mds.webservice.services;

import com.movilizer.mds.webservice.defaults.DefaultValues;
import com.movilizer.mds.webservice.models.maf.*;
import com.movilizer.mds.webservice.models.maf.communications.MafGenericResponse;
import com.movilizer.mds.webservice.models.maf.communications.MafLibraryResponse;
import com.movilizer.mds.webservice.models.maf.communications.MafResponse;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(JUnit4.class)
public class MafManagementServiceTest {
    private static long SYSTEM_ID = Long.parseLong(System.getenv("MOV_SYSTEM_ID")); //Put your own here
    private static String PASSWORD = System.getenv("MOV_PASSWORD"); //Put your own here
    private static String TOKEN = System.getenv("MOV_TOKEN"); //Put your own here

    private MafManagementService mafService;

    @Before
    public void setUp() throws Exception {
        mafService = new MafManagementService(DefaultValues.MOVILIZER_ENDPOINT.getMafUrl(),
                DefaultValues.CONNECTION_TIMEOUT_IN_MILLIS);
    }

    @Test
    @Ignore
    public void testDeployLibraryScriptSource() throws Exception {
        MafSource library = new MafLibraryScript(SYSTEM_ID, "", "Test library from J",
                "com.movilizer.JTest", false, null);
        MafResponse response = mafService.deploySourceSync(SYSTEM_ID, PASSWORD, TOKEN,
                library);

        assertThat(response.getSuccessful(), is(true));
        assertThat(response.getErrorMessage(), nullValue());
        assertThat(response, instanceOf(MafLibraryResponse.class));
        MafLibraryResponse libraryResponse = (MafLibraryResponse) response;
        assertThat(libraryResponse.getLibraryScript(), equalTo(library));
    }

    @Test
    @Ignore
    public void testLoadLibraryMeta() throws Exception {
        MafSource library = new MafLibraryScript(0L, "",
                "Test library with valid syntax",
                "com.movilizer.ps.test.ValidLibraryScript", false, null);
        File libFile = new File(getClass().getResource("/maf/ValidLibraryScript.meta.json")
                .toURI());

        MafCliMetaFile loadedLibraryMeta = mafService.readMetaFile(libFile);
        MafSource loadedSource = loadedLibraryMeta.getSource();

        assertThat(loadedSource, instanceOf(MafLibraryScript.class));
        MafLibraryScript loadedLibrary = (MafLibraryScript) loadedSource;
        assertThat(loadedLibrary, equalTo(library));
    }

    @Test
    @Ignore
    public void testLoadEventScript() throws Exception {
        String scriptSrc = "import com.movilizer.ps.test.ValidLibraryScript\n\n"
                + "ValidLibraryScript lib = new ValidLibraryScript(mafContext)";
        MafSource event = new MafEventScript(0L, "", "Test script for PS", "",
                "ONLINECONTAINER_RECEIVED", "", "com.movilizer.ps.test", "", "", "", "", "");

        File eventFile = new File(getClass().getResource("/maf/ValidEventScript.groovy")
                .toURI());

        MafSource source = mafService.readSource(eventFile);

        assertThat(source, instanceOf(MafEventScript.class));
        MafEventScript loadedEvent = (MafEventScript) source;
        assertThat(loadedEvent, equalTo(event));
        assertThat(loadedEvent.getScriptSrc(), equalTo(scriptSrc));
    }

    @Test
    @Ignore
    public void testDeployGenericScriptFile() throws Exception {
        MafSource generic = new MafGenericScript(SYSTEM_ID, "", "Test generic script for PS",
                "", "");

        File genericFile = new File(getClass().getResource("/maf/ValidGenericScript.groovy")
                .toURI());

        MafResponse response = mafService.deploySourceSync(SYSTEM_ID, PASSWORD, TOKEN,
                genericFile);

        assertThat(response.getSuccessful(), is(true));
        assertThat(response.getErrorMessage(), nullValue());
        assertThat(response, instanceOf(MafGenericResponse.class));
        MafGenericResponse genericResponse = (MafGenericResponse) response;
        assertThat(genericResponse.getGenericScript(), equalTo(generic));
    }
}
