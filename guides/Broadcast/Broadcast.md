# Improper System Use - Broadcast

## Introduction
Android provides the ability for apps to send and receive messages to other apps when certain system or in-app events occur. These messages are called [Broadcasts](https://developer.android.com/guide/components/broadcasts). There are two types of Broadcast. Implicit Broadcast and Explicit Broadcast.
Implicit Broadcasts are broadcasts that do not target your app specifically. They are sent out with the intention of letting the system decide which apps may receive the message based off the action specified in the code.
Implicit Broadcast:
```kotlin
val intent = Intent("com.example.damnvulnerablemobileapp.SEND_BROADCAST")
activity?.sendBroadcast(intent)
```

Explicit Broadcasts are broadcasts that are sent with the specific component that they wish to trigger.
Explicit Broadcast:
```kotlin
val intent = Intent(requireContext(), BroadcastReceiver::class.java)
activity?.sendBroadcast(intent)
```

## Vulnerability
In this app, when the user logs in an implicit broadcast is sent out with sensitive information. Because it is implicit, any app could intercept this broadcast given they have the correct action.
Using a tool such as [jadx](https://github.com/skylot/jadx), it is possible to decompile APKs in order to retrieve source code. This will allow someone to see the broadcast calls and determine if they are vulnerable to interception. Under this assumption, the content of any *implicit* broadcast is accessible leaving the data vulnerable to snooping.

## Where this can be found
Anywhere in code where sensitive data meant for a specific target is instead sent implicitly leaving the data world-readable.

## Mitigations
If there is a known target, then always use explicit broadcasts as these stop the system from determining the correct recipient which will avoid the chance of another app intercepting the data.
If a Implicit Broadcast is required, then always assume the data being sent is readable by everything. Determine what data is truly necessary and then encrypt any sensitive parts. You can also send broadcasts with a permission parameter which will limit receivers to those who have requested that permission.

Google realised that often broadcasts are sent and intended for another component in-app. This means that sending them globally is unsafe, and inefficient. Due to this, it is possible to send local broadcasts with [LocalBroadcastManager](https://developer.android.com/reference/androidx/localbroadcastmanager/content/LocalBroadcastManager). This will limit broadcasts to just your app which will be safer and more performant. Sending and receiving broadcasts is very much the same, just with a different manager. For example:
```kotlin
val localBroadcastManager = LocalBroadcastManager.getInstance(requireContext())

val intent = Intent("com.example.damnvulnerablemobileapp.SEND_BROADCAST")
localBroadcastManager.sendBroadcast(intent)
```