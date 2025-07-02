# Smart-Helmet

## Folder Structure For the Developers

This is the folder structure you will invovled in your development
If you add or change any folder file please blog it in here or your development roadmap

- Hardware Developments

```PlatFormIO Project/
└── Smart Helmet Folder/
    ├── include
    ├── lib                   # libraries
    ├── src                   # develpment Folder
    ├── test
    ├── .gitignore
    └── platformio.ini        # Dependacies when configuring the components
```

- Before the development make sure to download this drivers
   
   ![Drivers For ESP32 DevKit](Assets/Drivers)
   
   or download from here : https://www.silabs.com/developer-tools/usb-to-uart-bridge-vcp-drivers?tab=downloads

- Software Developments
  
```SmartHelmetApp/ 
├── .idea
├── app/
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── com.example/
│   │               └── smartelmetapp/
│   │                   ├── ui/
│   │                   │   └── theme
│   │                   └── MainActivity.kt                # Main interface of the APP
│   ├── .gitignore
│   ├── build.gradle.kts                                   
│   └── proguard-rules.pro
├── gradle/
│   └── ad
├── .gitignore 
├── build.gradle.kts                                        # depedancies and build options
├── gradle.properties
├── gradlew
├── gradlew.bat
└── settings.gradle.kts                                     # Project settings 
```

## Developers Configurations

- When you are developing your part of the project make sure to use your own **Branches**.
- Do not do the changes to **Backup Branch and Main Branch**.
- Update the Projects times lines according to your progress


## Developers Documentation
- When developing make sure to document ;
      --Step you took for development
      --Configurations steps / dependanceis (so when other developers trying out they also can download them and work with them)
      --Guide (if there are)
      --Errors that can occur and how to mitigate the

   #### Documentation File
  ![Documentation](Assets/Documentation)
          
    

