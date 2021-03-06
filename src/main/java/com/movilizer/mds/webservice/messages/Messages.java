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

public final class Messages {
    public static final String PERFORMING_REQUEST = "Performing request for system id %d";
    public static final String PERFORMING_UPLOAD = "Performing upload for system id %d";
    public static final String RESPONSE_RECEIVED = "Received response from movilizer cloud with" +
            " system id %d";
    public static final String UPLOAD_ERROR = "Could not upload document: %s";
    public static final String UPLOAD_COMPLETE = "Document upload complete successfully";
    public static final String RESPONSE_HAS_ERRORS = "Last response had the following errors:";
    public static final String DOCUMENT_ERRORS = "Document errors";
    public static final String MASTERDATA_ERRORS = "Masterdata errors";
    public static final String MOVELET_ERRORS = "Movelet errors";
    public static final String MESSAGES_ERROR = "Error messages:";
    public static final String SYSTEM_ID_IN_MESSAGE = ". Related in systemId ";
    public static final String REQUEST_CANCELLED_ERROR = "Request was cancelled";
    public static final String FAILED_REQUEST_ERROR = "Failed upload request with status '%d'" +
            " with reason '%s'";
    public static final String UNMARSHALLING_XML_ERROR = "The unmarshaller could not process" +
            " Movilizer request: ";
    public static final String READING_REQUEST_FROM_FILE = "Reading request from file %s";
    public static final String SUCCESSFUL_REQUEST_FROM_FILE = "Request from file %s read" +
            " successfully";
    public static final String SAVING_REQUEST_TO_FILE = "Saving request to file %s";
    public static final String SUCCESSFUL_REQUEST_TO_FILE = "Request saved to file %s successfully";
    public static final String REQUEST_FILE_NOT_FOUND = "Request file not found for path: ";
    public static final String CANNOT_CLOSE_FILE = "Cannot close file writer for file: ";
    public static final String MISSING_FILE_EXTENSION = "Invalid filename '%s'. Missing file" +
            " extension";
    public static final String FAILED_FILE_UPLOAD = "Failed upload request with status '%d' with" +
            " reason '%s'";
    public static final String FAILED_FILE_UPLOAD_CREDENTIALS = ". This bad request can also be" +
            " due to using wrong credentials, please verify";
    public static final String HANDLING_HTTP_RESPONSE = "Handling response from the document" +
            " upload service";
    public static final String SUCCESSFUL_HTTP_RESPONSE = "Successfully handled response with" +
            " status code %d";
    public static final String SUCCESSFUL_WEB_RESPONSE = "Successfully handled response for" +
            " class %s";
    public static final String HANDLING_WEB_RESPONSE = "Handling response from the web service";
    public static final String WEB_RESPONSE_CANCELED = "Response was cancelled";
    public static final String WEB_RESPONSE_NOT_DONE = "Tried to handle a response which is no" +
            " done yet.";
    public static final String BUILDING_CONFIG = "Building service from configuration";
    public static final String USING_ENCODING = "Using encoding '%s'";
    public static final String USING_PRIVATE_CONFIG = "Using private cloud configuration type" +
            " with url '%s'";
    public static final String USING_PUBLIC_CONFIG = "Using public '%s' cloud configuration";
    public static final String SET_ENDPOINT = "Set endpoint to '%s'";
    public static final String SET_PRIVATE_ENDPOINT = "Set endpoint to private cloud '%s'";
    public static final String SET_ENCODING = "Set encoding '%s'";
    public static final String SET_CONNECTION_TIMEOUT = "Set default connection timeout to '%d'";
    public static final String SET_RECEIVE_TIMEOUT = "Set default receive timeout to '%d'";
    public static final String SET_USER_AGENT = "Set user agent to '%s'";
    public static final String SET_THREAD_SAFE = "Set thread safety to '%s'";
    public static final String PREPARE_UPLOAD_REQUEST = "Preparing request for upload for system" +
            " id %d";
    public static final String PREPARE_DOWNLOAD_REQUEST = "Preparing request for download for" +
            " system id %d with %d responses";
    public static final String REQUEST_STRING_MUST_NOT_BE_NULL = "Request String must not be" +
            " null";
    public static final String SUCCESSFUL_REQUEST_FROM_STRING = "Request from String created" +
            " successfully";
    public static final String READING_REQUEST_FROM_STRING = "Reading request from string";
    public static final String MARSHALLING_ERROR = "There was an error marshalling %s";
    public static final String FOLDERS_CREATED = "Created folders for file: %s";
    public static final String PARSING_XML = "Parsing instance of class %s from XML";
    public static final String PRINTING_XML = "Printing instance of class %s to XML";
    public static final String PASSWORD_SUCCESSFULY_CHANGED = "Password for participant '%s'" +
            " successfully changed using system '%s'";
    public static final String LOADING_MAF_METADATA = "Loading metadata from file '%s'";
    public static final String MAF_UPLOAD_FAILED = "Maf upload failed. Reason: %s";
    public static final String MAF_UPLOAD_FAILED_WITH_CODE = "Maf upload failed with code '%d'." +
            " Reason: %s";
    public static final String READ_MAF_SCRIPT_FILE = "Loading MAF source from file '%s'";
    public static final String PROCESSING_MAF_SOURCE = "Processing MAF source '%s' for uploading";
    public static final String FINISHED_UPLOADING_MAF_SOURCE = "Finished uploading MAF source" +
            " '%s' to system id '%d'";
    public static final String PROCESSING_MAF_SOURCE_FILE = "Processing MAF source file '%s'" +
            " for uploading";
    public static final String FINISHED_UPLOADING_MAF_SOURCE_FILE = "Finished uploading MAF" +
            " source file '%s' to system id '%d'";

    private Messages() {
    }
}
