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

package com.movilizer.mds.webservice.models;

/**
 * Indicates the type of hash that will be used to manage the <tt>Participant</tt> secret
 * (com.movilitas.movilizer.v12.MovilizerParticipantConfiguration#passwordHashValue)
 *
 * @author Jesús de Mula Cano
 * @see com.movilitas.movilizer.v14.MovilizerParticipantConfiguration#passwordHashType
 * @see com.movilitas.movilizer.v14.MovilizerParticipantConfiguration#passwordHashValue
 * @since 12.11.1.3
 */
public enum PasswordHashTypes {
  DONT_CHANGE_PASSWORD(-1),
  DISABLE_PASSWORD(0),
  PLAIN_TEXT(1),
  MD5(2),
  SHA_256(3),
  SHA_512(4);

  private final Integer passwordType;

  PasswordHashTypes(Integer passwordType) {
    this.passwordType = passwordType;
  }

  /**
   * Hash type identifier for the webservice.
   * @return Integer for the given hash type identifier
   */
  public Integer getValue() {
    return passwordType;
  }
}

