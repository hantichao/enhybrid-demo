# Envhybrid Demo

This demo is developed based on EnvHybrid mobile application framework. It integrates the EnOS Application Portal login function and demonstrates page jump and data persistence functions. By using EnvHybrid, you can develop mobile applications with mainstream front-end technologies including JavaScript, CSS, and HTML.


EnvHybrid encapsulates the interaction between the native development system and JavaScript, and the parsing and jumping functions of the route. You can use the API provided by EnvHybrid for development. The native navigation bar provided by EnvHybrid enhances the user experience. Meanwhile, the EnvHybrid can open local and online H5 pages without specific front-end technologies or code languages.


## System Requirements

Envhybrid supports iOS and Android systems:

- iOS: Xcode 10.0 or later

- Android: Android Studio (Gradle Plugin Version: 3.2.0, Gradle Version: 4.6) 


## Before You Start

Clone or download the demo project to local.

## Step 1: Run the Demo

- iOS: run the below application with Xcode: 
  ```
  {root-directory}/platforms/ios/hybrid-demo.xcworkspace
  ```

- Android：run the below application with Android Studio:
  ```
  {root-directory}/platforms/android` 
  ```

Where ``root-directory`` is the local root directory of the demo project.



## Step 2: Embed the Web Code

1. Put the web source code to the project.

   Place the web source code you need to embed in this directory: `{root-directory}/webapp/{module-name}` directory. Name the folder based on the module name, for example: `/webapp/demo` under `envhybrid-demo`.


   >Note: To ensure the plugin can work correctly, you must import the envcontext.js and the cordova.js to each html page in the H5 module. The sample code is as follows:

   ```html
   <html>
   ...
   <body>
   ...
   <script type="text/javascript" src="envcontext.js"></script>
   <script type="text/javascript" src="cordova.js"></script> 
    </body> 
   </html>
   ```



2. Put the resource files that was completely built into the project.

   After the web code is built, it should be placed in the corresponding directory of the operating system (you can use the script on the web side to copy the code directly to the corresponding directory):

   - iOS: `{root-directory}/platforms/ios/webapp/{module-name}`
   
   - Android: `{root-directory}/platforms/android/app/src/main/assets/webapp/{module-name}`

   For example, `/platforms/ios/webapp/demo` under `envhybrid-demo`.

   >Note: A module file contains the bundles folder and the config.json file. The bundles folder contains all the resource files which are completed built. The config.json is the router configuration file for the module, which is used for page jump. For the specific configuration, refer to the demo project.


3. Configure the first page after login.
   
   - iOS: `{root-directory}/platforms/ios/hybrid-demo/Classes/utils/Strs.h`

      ```
      #define HOME_ROUTE @"/demo/index.html"
      ```

   - Android: `{root-directory}/platforms/android/app/src/main/java/com/envision/demo/MyApplication.java`

      ```
      public static String HOME_ROUTE = "/demo/index.html"
      ```

    Where the `/demo/index.html` is an instance that you can replace with the file path of the login page.


4. If you have installed and ran this project on this local terminal, uninstall the old installation package and run the project again.

5. Debug the page.
  
   Configure `remote_server` in `config.json`, that is, read the online web address.

   - iOS: Debug with Safari developer mode
   
   - Android: Debug with Chrome Developer mode


## Introduction of the Demo Functions

The details of the demo functions are as below.

### Integrated the Login Function of Application Portal

Log in from the mobile terminal with the Application Portal account system. Application Portal is an application enablement tool provided by EnOS to unify the application interactive experience and account permissions system. For more information about Application Portal, refer to [About Application Portal](https://www.envisioniot.com/docs/app-development/en/latest/app_portal/overview.html).




### Page Jump

Page jumps in mobile hybrid applications are implemented by loading web pages from an native page container "embeded browser (WebView). There are two cases of jumps between web pages:

- Page jump in browser (WebView): same page jump in common browsers. The sample code is as follows:

  ```
  window.location.href = 'a.html';
  ```

- Jump to a web page loaded by another native container: open a new native container page, including a new browser (WebView) to load the new web page and sync the relevant data to the new browser. nativeHistory.js is needed at this point to provide the ability to do this kind of page jump. The sample code is as follows:
  ```
  import { nativeHistory } from '@enos/envhybrid-utils';
  nativeHistory.push('/demo/a.html');
  ```

If the H5 code needs to be compatible with both mobile hybrid applications and desktop web applications, the reference code is as follows:

```
import { device, nativeHistory } from '@enosenvhybrid-utils';
if (device.isMobile) {
   nativeHistory.push('demoa.html');
  } else {
    window.location.href = 'a.html';
  }
```


### Data Persistence

`sharedData Api.js` provides a storage function for persistent data, which saves the persistent data to the native layer. Also, it can extract data from the native layer to the H5 layer. Methods are as below:

|     Method         |       Description     |
|------------------|----------------|
| saveShellPersistentData | Persistently store data as key-value pair to the native layer  |
| getShellPersistentData | Get the data key value from the persistent storage on the Native layer |
| removeShellPersistentData | Delete the data key values from the persistent storage on the Native layer |


### Basic Plugins

The demo contains the following basic plugins. For all the basic plugins included in EnvHybrid, refer to [EnvHybrid Mobile Application Framework Overview](https://www.envisioniot.com/docs/app-development/en/latest/envhybrid/overview.html).


|             Plugin        |       Description     |
|---------------------------|-----------------------|
| mobile-plugin-shellrouter | Provide interface jump, replace, and rollback for hybrid applications |
| mobile-plugin-shared-data | Provide methods for operating memory variables shared among multiple webviews, including methods for storing, retrieving, and deleting data. |
| mobile-plugin-envcontext | Provide content injection function to enable the web to obtain global variables such as serverAddress |
| mobile-plugin-web-container | Base class of webview, provides a container for loading web pages |


### Extended Functions

Besides the above plugins, EnvHybrid also provides plugins that support specific features, which will be available soon.

|             Plugin        |       Description     |
|---------------------------|----------------|
| mobile-plugin-tabbar | Support customized container which has tab page |
| mobile-plugin-language | Provide in-app language switching for mobile hybrid frameworks |
| mobile-plugin-skin | Provides the function of globally switching App style themes |
| mobile-plugin-fingerprint | Provide fingerprint recognition |












