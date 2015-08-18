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

package com.movilizer.mds.webservice;

import com.movilitas.movilizer.v14.MovilizerRequest;
import com.movilitas.movilizer.v14.MovilizerResponse;
import com.movilizer.mds.webservice.defaults.DefaultValues;
import com.movilizer.mds.webservice.models.FutureCallback;
import com.movilizer.mds.webservice.services.MovilizerDistributionService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@RunWith(JUnit4.class)
public class MovilizerTest {
  private static long SYSTEM_ID = 1L; //Put your own here
  private static String PASSWORD = "pass"; //Put your own here

  private MovilizerDistributionService mds;

  @Before
  public void setUp() throws Exception {
    mds = Movilizer.buildConf()
        .setEndpoint(DefaultValues.MOVILIZER_ENDPOINT)
        .setDefaultConnectionTimeout(DefaultValues.CONNECTION_TIMEOUT_IN_MILLIS)
        .setDefaultReceiveTimeout(DefaultValues.RECEIVE_TIMEOUT_IN_MILLIS)
        .setUserAgent(DefaultValues.AGENT_ID, DefaultValues.AGENT_VERSION)
        .getService();
  }

  @Ignore
  @Test
  public void testPerformEmptyRequest() throws Exception {
    URL requestFileResource = this.getClass().getResource("/requests/ping-request.mxml");
    Path requestFilePath = Paths.get(requestFileResource.toURI());
    MovilizerRequest request = mds.getRequestFromFile(requestFilePath);
    request = mds.prepareUploadRequest(SYSTEM_ID, PASSWORD, request);
    MovilizerResponse response = mds.getReplyFromCloudSync(request);
    assertThat(response, is(notNullValue()));
    assertThat(mds.responseHasErrors(response), is(false));
  }

  @Ignore
  @Test
  public void testPerformEmptyRequestAsync() throws Exception {
    MovilizerRequest request = new MovilizerRequest();
    request = mds.prepareUploadRequest(SYSTEM_ID, PASSWORD, request);
    mds.getReplyFromCloud(request, new FutureCallback<MovilizerResponse>() {
      @Override
      public void onSuccess(MovilizerResponse movilizerResponse) {
        assertThat(movilizerResponse, is(notNullValue()));
        assertThat(mds.responseHasErrors(movilizerResponse), is(false));
      }

      @Override
      public void onComplete(MovilizerResponse movilizerResponse, Exception e) {
        assertThat(movilizerResponse, is(notNullValue()));
        assertThat(e, is(nullValue()));
        assertThat(mds.responseHasErrors(movilizerResponse), is(false));
      }

      @Override
      public void onFailure(Exception e) {
      }
    });
  }
}