# Changelog

## 12.11.1.0 (2015-03-24)

Initial public release

- Compiled WSDL to Java classes
- Add HTTP POST method to upload documents to the Movilizer Cloud
- Convenient method for upload only request
- File access and persistence to load and save Movilizer requests
- toString methods for Movilizer requests

## 12.11.1.1 (2015-04-01)

Add missing features

- Compiled Online WSDL to Java classes for Movilizer Online Sync Cloud Feature
- Convenient method to marshall requests from String for Velocity and other template engines integration

## 12.11.1.2 (2015-05-19)

Refactor messages

Add features

- Movilizer OnlineSync manages asynchronously
- Serializable artifacts compiled from the wsdl

## 12.11.1.3 (2015-06-24)

Add features

- Add new API method to marshal any Movilizer element
- Add new API method to unmarshal any Movilizer element
- Add new API method to check for errors in a webservice response
- Add PasswordHashTypes enum to ease participant configuration in webservice calls

Fixes
- Config bug when creating serializable artifacts compiled from the wsdl

## 14.11.1.3 (2017-08-18)

Add features

- Update webservice to the protocol version 14

## 15.11.1.3 (2017-04-10)

Add features

- Update webservice to the protocol version 15

## 15.11.1.4 (2017-08-01)

Add features

- Add thread safe XML conversions

## 15.11.1.5 (2017-09-27)

Add features

- Add send all requests from a folder

Minor code clean up

## 15.11.2.0 (2017-10-09)

Breaking changes

- Minimum Java updated to 1.8
- CXF updated to 3.2.0
- Apache httpclient updated to 4.5.3
- Logback updated to 1.2.3

Fixes

- Upload documents give a confusing "Bad Request" message when credentials are incorrect.
- Ignored tests have been "un-ignored"
- Updated libraries
- initial setup for CI/CD 

## 15.11.2.1 (2017-10-26)

Breaking changes

- Now configuration only takes the base url of the cloud and infer the specific endpoints (as they already are standard)
- Logging level lowered to debug to all messages

Features

- MAF capabilities

Configuration changes

- PMD and checkstyle are now specified for static quality analysis

## 15.11.2.2 (2017-11-xx)

Breaking changes

- Change async operations to use Java 8 CompletableFuture

Minor fixes

- Clean up code to comply with checkstyle and PMD

## 16.11.2.2 (2018-12-xx)

Features

- Movilizer protocol version updated to v16

Minor fixes

- Add Jacoco
- MAF tests
- Use context inCircle CI for handling secrets needed during tests
