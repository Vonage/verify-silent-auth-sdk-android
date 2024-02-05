# Vonage Android Silent Auth SDK

Vonage Verify Silent Authentication uses a mobile phone's Subscriber Identity Module (SIM) to prove a user's identity, without any user input. This SDK enables making a HTTP request over cellular even when on WiFi.

## Installation

Add the maven public repository `https://gitlab.com/api/v4/projects/40053021/packages/maven` to your gradle configuration e.g

```
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://gitlab.com/api/v4/projects/40053021/packages/maven")
    }
}
```

build.gradle -> dependencies add

```
implementation 'com.vonage:client-sdk-silent-auth:1.0.0'
```

## Permissions

This SDK makes use of the following permissions as defined in the SDKs [AndroidManifest.xml](https://github.com/Vonage/verify-silent-auth-sdk-android/blob/main/client-sdk-silent-auth/src/main/AndroidManifest.xml)

```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
 ```

These permissions are used to check the current network (WIFI vs cellular), and to make the require HTTP request over the cellular network. 

## Compatibility

 * **Minimum Android SDK**: The SDK requires a minimum API level of 21 (Android 5)
 * **Compile Android SDK**: The SDK requires you to compile against API 31  (Android 12) or later.
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

## Responses

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




