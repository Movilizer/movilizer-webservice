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

package com.movilizer.mds.webservice.messages;

/*
 @see: https://devtools.movilizer.com/confluence/display/DOC21/Common+Errors+Messages
 */
public enum  MovilizerCloudMessages {
    MOVELET_PORTAL_CREATED(100, "MOVELET_PORTAL_CREATED", false),
    MOVELET_PORTAL_SENT(101, "MOVELET_PORTAL_SENT", false),
    MOVELET_PORTAL_STOPPED(107, "MOVELET_PORTAL_STOPPED", false),
    MOVELET_PORTAL_DEFINED(112, "MOVELET_PORTAL_DEFINED", false),
    MOVELET_PORTAL_CHANGED(113, "MOVELET_PORTAL_CHANGED", false),
    MOVELET_PORTAL_DELETED(114, "MOVELET_PORTAL_DELETED", false),
    MOVELET_PORTAL_RESET(118, "MOVELET_PORTAL_RESET", false),
    MOVELET_CREATED(115, "MOVELET_CREATED", false),
    MOVELET_DELETED(116, "MOVELET_DELETED", false),
    MOVELET_MANUALLY_DELETED(120, "MOVELET_MANUALLY_DELETED", false),
    MOVELET_RESET(119, "MOVELET_RESET", false),
    PARTICIPANT_PORTAL_CREATED(102, "PARTICIPANT_PORTAL_CREATED", false),
    PARTICIPANT_PORTAL_ASSIGNED(103, "PARTICIPANT_PORTAL_ASSIGNED", false),
    PARTICIPANT_PORTAL_UNASSIGNED(104, "PARTICIPANT_PORTAL_UNASSIGNED", false),
    PARTICIPANT_PORTAL_UPDATED(108, "PARTICIPANT_PORTAL_UPDATED", false),
    PARTICIPANT_CREATED(105, "PARTICIPANT_CREATED", false),
    PARTICIPANT_MULTIPLE_INSTALLATION(109, "PARTICIPANT_MULTIPLE_INSTALLATION", false),
    PARTICIPANT_DELETED(110, "PARTICIPANT_DELETED", false),
    MASTERDATA_UPDATED(310, "MASTERDATA_UPDATED", false),
    MASTERDATA_DELETED(311, "MASTERDATA_DELETED", false),
    MASTERDATA_UPDATE_FAILED(317, "MASTERDATA_UPDATE_FAILED", false),
    MASTERDATA_DELETE_FAILED(318, "MASTERDATA_DELETE_FAILED", false),
    PARTICIPANT_MASTERDATA_ASSIGNED(312, "PARTICIPANT_MASTERDATA_ASSIGNED", false),
    PARTICIPANT_MASTERDATA_UNASSIGNED(313, "PARTICIPANT_MASTERDATA_UNASSIGNED", false),
    ILLEGAL_MOVELET(150, "ILLEGAL_MOVELET", false),
    ILLEGAL_PARTICIPANT(151, "ILLEGAL_PARTICIPANT", false),
    ILLEGAL_REQUEST(152, "ILLEGAL_REQUEST", false),
    ILLEGAL_REPLY(153, "ILLEGAL_REPLY", false),
    ILLEGAL_PARTICIPANT_RESET(154, "ILLEGAL_PARTICIPANT_RESET", false),
    ILLEGAL_MOVELET_RESET(155, "ILLEGAL_MOVELET_RESET", false),
    ILLEGAL_MOVELET_DELETE(156, "ILLEGAL_MOVELET_DELETE", false),
    ILLEGAL_MOVELET_ACK(157, "ILLEGAL_MOVELET_ACK", false),
    DUPLICATE_MOVELET(160, "DUPLICATE_MOVELET", false),
    ILLEGAL_MOVELET_IN_MOVELETSET(161, "ILLEGAL_MOVELET_IN_MOVELETSET", false),
    ILLEGAL_MOVELET_DEPENDENCY(162, "ILLEGAL_MOVELET_DEPENDENCY", false),
    ILLEGAL_PARTICIPANT_CONFIGURATION(163, "ILLEGAL_PARTICIPANT_CONFIGURATION", false),
    ILLEGAL_USAGE_SCENARIO(164, "ILLEGAL_USAGE_SCENARIO", false),
    SMS_SENT_DEPLOYMENT(200, "SMS_SENT_DEPLOYMENT", false),
    SMS_ARRIVAL_DEPLOYMENT(202, "SMS_ARRIVAL_DEPLOYMENT", false),
    EMAIL_SENT_DEPLOYMENT(203, "EMAIL_SENT_DEPLOYMENT", false),
    CLIENT_DEPLOYMENT_REQUEST(204, "CLIENT_DEPLOYMENT_REQUEST", false),
    CLIENT_DEPLOYMENT_ERROR(205, "CLIENT_DEPLOYMENT_ERROR", false),
    CLIENT_DEPLOYMENT_PORTAL_RESET(207, "CLIENT_DEPLOYMENT_PORTAL_RESET", false),
    CLIENT_DEPLOYMENT_RESET(208, "CLIENT_DEPLOYMENT_RESET", false),
    CLIENT_RUNTIME_ERROR(209, "CLIENT_RUNTIME_ERROR", false),
    CLIENT_PARTICIPANT_CONFIG(210, "CLIENT_PARTICIPANT_CONFIG", false),
    CLIENT_TIMESET(211, "CLIENT_TIMESET", false),
    SMS_SEND_UNKNOWNCOUNTRYCODE(212, "SMS_SEND_UNKNOWNCOUNTRYCODE", false),
    SYNC_REQUEST(300, "SYNC_REQUEST", false),
    SYNCERROR_ILLEGAL_CREDENTIALS(301, "SYNCERROR_ILLEGAL_CREDENTIALS", false),
    SYNCERROR_DUPLICATE_SYNC(315, "SYNCERROR_DUPLICATE_SYNC", false),
    SYNCERROR_SLOW_SYNC(316, "SYNCERROR_SLOW_SYNC", false),
    SYNCERROR_ILLEGAL_APPID(302, "SYNCERROR_ILLEGAL_APPID", false),
    SYNCERROR_UNKNOWN(303, "SYNCERROR_UNKNOWN", false),
    MOVELET_SYNC(117, "MOVELET_SYNC", false),
    MOVELET_ACK_SYNC(304, "MOVELET_ACK_SYNC", false),
    META_MOVELET_SYNC(121, "META_MOVELET_SYNC", false),
    META_MOVELET_ACK_SYNC(306, "META_MOVELET_ACK_SYNC", false),
    MOVELET_DELETE_SYNC(122, "MOVELET_DELETE_SYNC", false),
    META_MOVELET_REJECT(123, "META_MOVELET_REJECT", false),
    META_MOVELET_ACCEPT(124, "META_MOVELET_ACCEPT", false),
    REPLY_SYNC(305, "REPLY_SYNC", false),
    REPLY_DUPLICATE(307, "REPLY_DUPLICATE", false),
    REPLY_PORTAL_ARRIVED(350, "REPLY_PORTAL_ARRIVED", false),
    MASTERDATA_SYNC(308, "MASTERDATA_SYNC", false),
    MASTERDATA_DELETE_SYNC(314, "MASTERDATA_DELETE_SYNC", false),
    MASTERDATA_SYNC_ACK(309, "MASTERDATA_SYNC_ACK", false),
    PUSH_ERROR_UNKNOWN(370, "PUSH_ERROR_UNKNOWN", false),
    PUSH_SENT(371, "PUSH_SENT", false),
    PUSH_PENDING(372, "PUSH_PENDING", false),
    PUSH_REGISTER(373, "PUSH_REGISTER", false),
    ONLINESYNCERROR_CALLBACK(380, "ONLINESYNCERROR_CALLBACK", false),
    ONLINESYNCERROR_SYNC(381, "ONLINESYNCERROR_SYNC", false),
    ONLINESYNC_REQUEST(382, "ONLINESYNC_REQUEST", false),
    ONLINESYNC_RESPONSE(383, "ONLINESYNC_RESPONSE", false),
    REQUEST_PROCESSED(111, "REQUEST_PROCESSED", false),
    LONG_REQUEST(508, "LONG_REQUEST", false),
    RESPONSE_SENT(501, "RESPONSE_SENT", false),
    PERFORMED_CALLBACK(502, "PERFORMED_CALLBACK", false),
    TESTCALL_PERFORMED(504, "TESTCALL_PERFORMED", false),
    BACKEND_MESSAGE(505, "BACKEND_MESSAGE", false),
    RESPONSE_ACK(506, "RESPONSE_ACK", false),
    CALLBACK_FAILED(503, "CALLBACK_FAILED", true),
    REQUEST_IN_PROGRESS(507, "REQUEST_IN_PROGRESS", false),
    UNKNOWN_EVENT_TYPE(000, "UNKNOWN_EVENT_TYPE", false),
    SYSTEM_ERROR(402, "SYSTEM_ERROR", true),
    SECURITY_VIOLATION(403, "SECURITY_VIOLATION", true),
    STATISTICS_EXCEPTION(404, "STATISTICS_EXCEPTION", false),
    RMI_ERROR(403, "RMI_ERROR", true);

    private final int messageType;
    private final String description;
    private final boolean isError;

    MovilizerCloudMessages(final int messageType, final String description, final boolean isError) {
        this.messageType = messageType;
        this.description = description;
        this.isError = isError;
    }

    public static MovilizerCloudMessages fromType(short messageType) {
        for (MovilizerCloudMessages message : MovilizerCloudMessages.values()) {
            if ((short) message.getMessageType() == messageType) return message;
        }
        return UNKNOWN_EVENT_TYPE;
    }

    public static boolean isError(short messageType) {
        if (1000 < messageType && messageType < 2000) { // MovilizerMoveletError
            return true;
        } else if (2000 < messageType && messageType < 3000) { // MovilizerParticipantError
            return true;
        } else if (3000 < messageType && messageType < 4000) { // MovilizerMasterdataError
            return true;
        }
        //Check individual errors
        return fromType(messageType).isError();
    }

    @Override
    public String toString() {
        return "MovilizerCloudMessages{" +
                "messageType=" + messageType +
                ", description='" + description + '\'' +
                ", isError=" + isError +
                '}';
    }

    public int getMessageType() {
        return messageType;
    }

    public String getDescription() {
        return description;
    }

    public boolean isError() {
        return isError;
    }
}
