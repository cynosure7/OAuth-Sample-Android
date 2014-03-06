##Introduction

The goal of this sample project is to show you how you can do OAuth2 from an Android application using the [Google OAuth Client Library for Java](https://code.google.com/p/google-oauth-java-client/).

The sample application allows you to access data from the following OAuth2 providers

- Foursquare (using the Google OAuth Client Library for Java to access a non-Google API).
- Google Tasks  (using the Google OAuth Client Library for Java to access this Google API).
- Google Plus (using Google APIs Client Library for Java, offering a higher level abstraction for interacting with Google APIs) 

##Project setup

This project is built using the [m2e-android plugin](http://rgladwell.github.io/m2e-android/index.html) to handle its external dependencies.

When using Eclipse ADT, it assumes that the following components are installed :

- Eclipse Market Client
- m2e-android plugin

If you don't have the Eclipse Marker Client installed, you can install it by clicking on 

```Help → Install new Software → Switch to the Juno Repository → General Purpose Tools → Marketplace Client```

Once you have the Eclipse Market Client installed, you can proceed to install the m2e-android plugin

```Help -> Eclipse Marketplace... and search for "android m2e".```

More instructions can be found on the [m2e-android plugin](http://rgladwell.github.io/m2e-android/index.html) site.

## Project dependences

This project depends on the following libraries. (automatically pulled in when using the m2e-android plugin).

- commons-logging/commons-logging/1.1.1/commons-logging-1.1.1.jar
- org/apache/httpcomponents/httpclient/4.0.1/httpclient-4.0.1.jar
- org/apache/httpcomponents/httpcore/4.0.1/httpcore-4.0.1.jar
- commons-codec/commons-codec/1.3/commons-codec-1.3.jar
- com/google/apis/google-api-services-plus/v1-rev72-1.15.0-rc/google-api-services-plus-v1-rev72-1.15.0-rc.jar
- com/google/api-client/google-api-client/1.15.0-rc/google-api-client-1.15.0-rc.jar
- com/google/oauth-client/google-oauth-client/1.15.0-rc/google-oauth-client-1.15.0-rc.jar
- com/google/http-client/google-http-client-jackson2/1.15.0-rc/google-http-client-jackson2-1.15.0-rc.jar
- com/google/http-client/google-http-client/1.15.0-rc/google-http-client-1.15.0-rc.jar
- com/google/code/findbugs/jsr305/1.3.9/jsr305-1.3.9.jar
- com/fasterxml/jackson/core/jackson-core/2.1.3/jackson-core-2.1.3.jar

## References

- [m2e-android plugin](http://rgladwell.github.io/m2e-android/index.html)
- [Google APIs Client Library for Java](http://code.google.com/p/google-api-java-client/)
- [Google OAuth Client Library for Java](https://code.google.com/p/google-oauth-java-client/)
- [Google HTTP Client Library for Java](https://code.google.com/p/google-http-java-client/)
- [OAuth2 Playground](https://developers.google.com/oauthplayground)