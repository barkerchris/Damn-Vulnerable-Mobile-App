# Improper System Use - Activity

## Introduction
An [Activity](https://developer.android.com/reference/android/app/Activity) is a building block of an app. You can think of an Activity as often representing a single screen in an app. They are responsible for loading the UI, and contain all business logic for the current screen. Activities can be opened from other Activities using [startActivity()](https://developer.android.com/reference/android/app/Activity#startActivity(android.content.Intent)) and passing in an [Intent](https://developer.android.com/reference/android/content/Intent).

*Note:* Google has been pushing the single Activity, multiple Fragment way of creating apps for several years now with the introduction of [Jetpack Navigation](https://developer.android.com/guide/navigation/). This will eventually lead to a reduction of apps based around the multiple Activity framework which will hopefully lead to less Activity vulnerabilities seen in the wild.

## Vulnerability
In this app, there is a button which when pressed opens up a new Activity. For example, in a banking app there could be a home page, and then a button which will open up a screen for transferring money to other accounts.
The issue in this app is the call to open the Activity uses an Implicit Intent and the Activity to be opened has Intent Filters. This is a similar issue that is explored in the [Broadcasts guide](/guides/Broadcast/Broadcast.md). Because it uses an Implicit Intent, the Activity being opened needs Intent Filters to help the OS identify which Activity should be opened given an action. As soon as an Intent Filter is declared in the Manifest, the 'android:exported' value is set to true, allowing outside applications to open the Activity.

## What this looks like
The call to open the new Activity is:
```kotlin
private fun openActivity() {
    val intent = Intent("com.example.damnvulnerablemobileapp.OPEN_INFO")
    startActivity(intent)
}
```

The AndroidManifest code for the new Activity:
```xml
<activity android:name=".VulnerabilitiesActivityPageActivity">
    <intent-filter>
        <action android:name="com.example.damnvulnerablemobileapp.OPEN_INFO" />
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</activity>
```

By using a tool such as [jadx](https://github.com/skylot/jadx), we can retrieve the original source code from an APK of the app. Using jadx we can then find the AndroidManifest.xml and search fo all exported Activities. Once we have the name of the target Activity, we can open it from another project like so:
```kotlin
binding.btnHelpersActivityOpen.setOnClickListener {
    val intent = Intent("com.example.damnvulnerablemobileapp.OPEN_INFO")
    startActivity(intent)
}
```

This code opens up the Activity from the main app. If there was sensitive information displayed then it would now be loaded on the screen. Also, if there are dangerous functions accessible then these could now be used.

## Where this can be found
Firstly, the app must still be using the multiple Activity model. Furthermore, in the Manifest the Activities must either have the 'android:exported' tag set to true or have intent filters set, allowing external apps to launch the Activity.

## Mitigation
If the Activity is not meant to be accessed from outside the app then it should have the 'android:exported' property set to 'false', and should not have any Intent Filters. You should then use Explicit Intents in order to specify the class name when starting the Activity. If the Activity needs to be accessed externally, then you could pass extra data with the Intent and have a check for this data in the app to avoid rogue calls from other apps.