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
