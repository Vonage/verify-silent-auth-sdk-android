# Vonage Android Silent Auth SDK

[![License][license-image]][license-url]

The only purpose of the SDK is to force the data cellular connectivity prior to call a public URL, and will return the following JSON response

* **Success**
  When the data connectivity has been achieved and a response has been received from the url endpoint
```
{
"http_status": string, // HTTP status related to the url
"response_body" : { // optional depending on the HTTP status
           ... // the response body of the opened url 
           ... // see API doc for /device_ip and /redirect
        },
"debug" : {
    "device_info": string, 
    "url_trace" : string
    }
}
```

* **Error**
  When data connectivity is not available and/or an internal SDK error occurred

```
{
"error" : string,
"error_description": string,
"debug" : {
    "device_info": string, 
    "url_trace" : string
    }
}
```
Potential error codes: `sdk_no_data_connectivity`, `sdk_connection_error`, `sdk_redirect_error`, `sdk_error`.

## Installation

build.gradle -> dependencies add

```
    implementation 'com.vonage::client-sdk-silent-auth:1.0.0'
    implementation 'commons-io:commons-io:2.4'
```

## Compatibility

 * **Minimum Android SDK**: SilentAuthSDK requires a minimum API level of 21 (Android 5)
 * **Compile Android SDK**: SilentAuthSDK requires you to compile against API 31  (Android 12) or later.
 * **Important Note**: When you increase your `buildToolsVersion` to `31.0.0` within your project, your IDE might be throwing an error:
    ```
    Installed Build Tools revision 31.0.0 is corrupted. Remove and install again using the SDK Manager.
    ```
   You can follow the steps on [here](https://ourcodeworld.com/articles/read/1591/how-to-solve-android-studio-error-installed-build-tools-revision-3100-is-corrupted-remove-and-install-again-using-the-sdk-manager) to resolve.

## Usage example


```kotlin
import com.vonage.silentauth.VGSilentAuthClient

// instantiate the sdk during app startup
VGSilentAuthClient.initializeSdk(this.applicationContext)

val resp: JSONObject = VGSilentAuthClient.getInstance().openWithDataCellular(URL(endpoint), false)
 if (resp.optString("error") != "") {
    // error
} else {
    val status = resp.optInt("http_status")
    if (status == 200) {
        // 200 OK
    } else {
        // error
    }
}
```
