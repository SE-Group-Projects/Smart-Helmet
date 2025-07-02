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

1. Before the development make sure to download this drivers
   ![Drivers For ESP32 DevKit](Assets/Drives/CP210x_VCP_Windows.zip)
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
   
