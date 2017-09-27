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

package com.movilizer.mds.webservice.defaults;

import com.movilizer.mds.webservice.EndPoint;

import java.nio.charset.Charset;

public class DefaultValues {
  public static final EndPoint MOVILIZER_ENDPOINT = EndPoint.DEMO;
  public static final Charset OUTPUT_ENCODING = Charset.forName("UTF-8");
  public static final Integer CONNECTION_TIMEOUT_IN_MILLIS = 30000;
  public static final Integer RECEIVE_TIMEOUT_IN_MILLIS = 60000;
  public static final String AGENT_ID = "movilizer-webservice";
  public static final String AGENT_VERSION = "15.11.1.5";
  public static final String USER_AGENT = USER_AGENT_FORMAT_STRING(AGENT_ID, AGENT_VERSION);

  private DefaultValues() {}

  public static String USER_AGENT_FORMAT_STRING(String agentId, String agentVersion) {
    String userAgentString = "";
    if (agentId != null) {
      userAgentString = userAgentString + agentId;
    }
    if (agentVersion != null && !agentVersion.isEmpty()) {
      userAgentString = userAgentString + "/" + agentVersion;
    }
    return userAgentString;
  }
}
