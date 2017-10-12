# Movilizer Webservice

This is a non-official non supported compilation of the Movilizer WSDL. More info about Movilizer and their webservice
at https://devtools.movilizer.com/confluence/display/DOC25/Introduction+to+the+Movilizer+Web+Service

[![Circle CI](https://img.shields.io/circleci/project/github/Movilizer/movilizer-webservice/master.svg?style=flat-square)](https://circleci.com/gh/Movilizer/movilizer-webservice/tree/master)
[![Dependency Status](https://www.versioneye.com/user/projects/59db355c2de28c21ceb95f13/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/59db355c2de28c21ceb95f13)
[![Codacy Badge](https://img.shields.io/codacy/grade/f7c2d7254e4d4b0cb102247d6a1f51f8.svg?style=flat-square)](https://www.codacy.com/app/demula/movilizer-webservice?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Movilizer/movilizer-webservice&amp;utm_campaign=Badge_Grade)
[![Download](https://api.bintray.com/packages/movilizer/maven/movilizer-webservice/images/download.svg?style=flat-square) ](https://bintray.com/movilizer/maven/movilizer-webservice/_latestVersion)

## Adding it into your project

Either use this as `jar` in your libs folder or add it using maven as follows:

```xml
<dependencies>
    <dependency>
        <groupId>com.movilizer.mds</groupId>
        <artifactId>movilizer-webservice</artifactId>
        <version>15.11.2.0</version>
    </dependency>
    <!-- Extra libs not included -->
    <!-- Upload documents -->
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.5.3</version>
    </dependency>
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpmime</artifactId>
        <version>4.5.3</version>
    </dependency>
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>fluent-hc</artifactId>
        <version>4.5.3</version>
    </dependency>
    <!-- Online sync -->
    <dependency>
        <groupId>org.apache.cxf</groupId>
        <artifactId>cxf-rt-frontend-jaxws</artifactId>
        <version>3.2.0</version>
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

You can also fetch this from the Movilizer internal Maven repository if you have access to it.

```xml
<repositories>
    <repository>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
        <id>lighthouse</id>
        <name>lighthouse</name>
        <url>https://source.movilizer.com/artifactory/lighthouse/</url>
    </repository>
</repositories>
```

Using Gradle you can use it with the following `build.gradle`.

```groovy
repositories {
	jcenter()
	mavenLocal()
    maven {
        credentials {
            username movilizerRepoUsername
            password movilizerRepoPassword
        }
        url 'https://source.movilizer.com/artifactory/lighthouse/'
    }
}

ext {
    movilizerWebserviceVersion = '15.11.2.0'
    apacheHttpcomponentsVersion = '4.5.3'
    apacheCxfVersion = '3.2.0'
}

dependencies {
    compile group: 'com.movilizer.mds', name: 'movilizer-webservice', version: "$movilizerWebserviceVersion"
    compile group: 'org.apache.httpcomponents', name: 'httpclient', version: "$apacheHttpcomponentsVersion"
    compile group: 'org.apache.httpcomponents', name: 'httpmime', version: "$apacheHttpcomponentsVersion"
    compile group: 'org.apache.httpcomponents', name: 'fluent-hc', version: "$apacheHttpcomponentsVersion"
    compile(group: 'org.apache.cxf', name: 'cxf-rt-frontend-jaxws', version: "$apacheCxfVersion") {
        exclude group: 'com.sun.xml.bind', module: "jaxb-impl"
    }
    compile group: 'org.apache.cxf', name: 'cxf-rt-transports-http', version: "$apacheCxfVersion"
}

```

## Using the web service

Once the dependencies are in place the Movilizer web service can be called the following way inside your project.

```java
// Getting an instance of the web service 
MovilizerDistributionService mds = Movilizer.getService();

// Loading a .mxml request file from the filesystem
MovilizerRequest request = mds.getRequestFromFile(Paths.get("requests", "loginMovelet.mxml"));

// Loading a .mxml request files from a folder
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

In case you're using a multi-threading environment and you're not using a Movilzer service instance per thread,
you can set the safe multi-threading on to avoid possible serialization issues.

```java
MovilizerDistributionService mds = Movilizer.buildConf()
                                            .setThreadSafe(true)
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

## Contributing

Some of the tests will require your Movilizer system id and password. Please add the following environment variables:

```bash
export MOV_SYSTEM_ID=1234
export MOV_PASSWORD=secret
```
