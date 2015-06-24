# Movilizer Webservice

This is a non-official non supported compilation of the Movilizer WSDL. More info about Movilizer and their webservice
at https://devtools.movilizer.com/confluence/display/DOC22/Introduction+to+the+Movilizer+Web+Service

[![Circle CI](https://circleci.com/gh/Movilizer/movilizer-webservice/tree/master.svg?style=svg)](https://circleci.com/gh/Movilizer/movilizer-webservice/tree/master)

## Adding it into your project

Either use this as `jar` in your libs folder or add it using maven as follows:

```xml
<dependencies>
    <dependency>
        <groupId>com.movilizer.mds</groupId>
        <artifactId>movilizer-webservice</artifactId>
        <version>12.11.1.3</version>
    </dependency>
    <!-- Extra libs not included -->
    <!-- Upload documents -->
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.4</version>
    </dependency>
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpmime</artifactId>
        <version>4.4</version>
    </dependency>
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>fluent-hc</artifactId>
        <version>4.4</version>
    </dependency>
    <!-- Online sync -->
    <dependency>
        <groupId>org.apache.cxf</groupId>
        <artifactId>cxf-rt-frontend-jaxws</artifactId>
        <version>3.1.1</version>
    </dependency>
</dependencies>
```

It may happen that the latest version is not in the public maven repo. In this case, clone this repository and execute a
maven install in the root of the cloned repository.

```bash
git clone https://github.com/Movilizer/movilizer-webservice.git
cd movilizer-webservice
mvn install
```

## Using the web service

Once the dependencies are in place the Movilizer web service can be called the following way inside your project.

```java
// Getting an instance of the web service 
MovilizerDistributionService mds = Movilizer.getService();

// Loading a .mxml request file from the filesystem
MovilizerRequest request = mds.getRequestFromFile(Paths.get("requests", "loginMovelet.mxml"));

// Loading a request from string (hook for Velocity templates and the like)
MovilizerRequest request = mds.getRequestFromString(myVelocityTemplateOutputString);

// Prepare an only upload request (no replies whatsoever)
request = mds.prepareUploadRequest(1234L, "super-secret-password", request);

// Perform a synchronous call to the Movilizer Cloud
MovilizerResponse response = mds.getReplyFromCloudSync(request);

// See errors in response
String responseString = mds.responseToString(response);

// Perform a synchronous uploads of documents to the Movilizer Cloud
UploadResponse uploadResponse = mds.uploadDocumentSync(Paths.get("myCatalogue1.pdf"), 1234L, "super-secret-password", 
                                                       "cataloguePool", "myCatalogue1", "EN", null);
```

For private clouds the following config can be set:

```java
MovilizerDistributionService mds = Movilizer.buildConf()
                                            .setEndpoint("https://movilizer.mycloud.com/WebService/",
                                                        "https://movilizer.mycloud.com/mds/document")
                                            .setOutputEncoding(Charset.defaultCharset())
                                            .getService();
```

Asynchronous calls are also possible:

```java
// Perform a asynchronous call to the Movilizer Cloud
MovilizerResponse response = mds.getReplyFromCloud(request, new FutureCallback<MovilizerResponse> {
    void onSuccess(MovilizerResponse response){
        logger.info("Yay! I got my response!");
    }
    void onComplete(MovilizerResponse response, Exception futureException) {}
    void onFailure(Exception futureException) {
        logger.error("Something went terribly wrong.");
    }
});

// Perform a asynchronous upload of a document to the Movilizer Cloud
UploadResponse uploadResponse = mds.uploadDocument(Paths.get("myCatalogue1.pdf"), 1234L, "super-secret-password", 
                                                   "cataloguePool", "myCatalogue1", "EN", null, new FutureCallback<UploadResponse> {
    void onSuccess(UploadResponse response){
        logger.info("Yay! I got my response!");
    }
    void onComplete(UploadResponse response, Exception futureException) {}
    void onFailure(Exception futureException) {
        logger.error("Something went terribly wrong.");
    }
});
```
