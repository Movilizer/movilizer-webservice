package com.movilizer.mds.webservice.services;

import com.movilitas.movilizer.v16.MovilizerRequest;
import com.movilitas.movilizer.v16.MovilizerResponse;
import com.movilitas.movilizer.v16.MovilizerWebServiceV16;
import com.movilitas.movilizer.v16.MovilizerWebServiceV16Service;
import com.movilizer.mds.webservice.defaults.DefaultValues;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.CompletableFuture;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(JUnit4.class)
public class MovilizerWebServiceTest {
    private static long SYSTEM_ID = 0L;
    private static String PASSWORD = "";

    private MovilizerWebService webService;

    @Before
    public void setUp() throws Exception {
        MovilizerWebServiceV16 movilizerCloud = new MovilizerWebServiceV16Service().getMovilizerWebServiceV16Soap11();
        webService = new MovilizerWebService(movilizerCloud, DefaultValues.CONNECTION_TIMEOUT_IN_MILLIS,
                DefaultValues.RECEIVE_TIMEOUT_IN_MILLIS, DefaultValues.AGENT_ID, DefaultValues.AGENT_VERSION);
        webService.setEndpoint(DefaultValues.MOVILIZER_ENDPOINT.getMdsUrl());
    }

    @Test
    public void testPerformEmptyRequest() throws Exception {
        MovilizerRequest request = new MovilizerRequest();
        request = webService.prepareUploadRequest(SYSTEM_ID, PASSWORD, request);
        MovilizerResponse response = webService.getReplyFromCloudSync(request);
        assertThat(response, is(notNullValue()));
        assertThat(webService.responseHasErrors(response), is(false));
    }

    @Test
    public void testPerformEmptyRequestAsync() throws Exception {
        MovilizerRequest request = new MovilizerRequest();
        request = webService.prepareUploadRequest(SYSTEM_ID, PASSWORD, request);
        CompletableFuture<MovilizerResponse> future = webService.getReplyFromCloud(request);
        MovilizerResponse response = future.get();
        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusMessage().get(0).getMessage(), startsWith("Testcall successful: CUSTOMER_SYSTEM_TYPE_WEBSERVICE"));

        assertThat(webService.responseHasErrors(response), is(false));
    }
}
