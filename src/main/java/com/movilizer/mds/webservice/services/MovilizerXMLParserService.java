/*
 * Copyright 2017 Movilizer GmbH
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

package com.movilizer.mds.webservice.services;

import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilitas.movilizer.v15.MovilizerResponse;

import java.nio.file.Path;

interface MovilizerXMLParserService {

  MovilizerRequest getRequestFromFile(final Path filePath);

  MovilizerRequest getRequestFromString(final String requestString);

  <T> T getMovilizerElementFromString(final String elementString, final Class<T> movilizerElementClass);

  String printRequest(final MovilizerRequest request);

  String printResponse(final MovilizerResponse response);

  <T> String printMovilizerElementToString(final T movilizerElement, final Class<T> movilizerElementClass);

  void saveRequestToFile(final MovilizerRequest request, final Path filePath);
}
