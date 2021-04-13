# Insecure Data Logging

## Introduction
Android Studio allows you to read and write system and app-specific messages to the [Logcat window](https://developer.android.com/studio/debug/am-logcat). While logging can be indispensable in debugging and development, it has the potential to leak Personally Identifiable Information (PII), or other sensitive information.

## Vulnerability
In this app, when the user logs in, the username and password are logged to the Logcat console and then this console is dumped to a file in Internal Storage.
While other apps cannot access another apps Internal Storage, we as users can. This means that if someone were to gain access to the phone - or if it is lost, it would be trivial to retrieve these stored file. This could then give the attacker user credentials, or whatever sensitive information that is being logged by the app.

## What this looks like
In order to write a message you must use the [Log API](https://developer.android.com/reference/android/util/Log). There are many different options for the different types of messages that can be written. In this instance, the .d() command is being used to write a DEBUG message.
The specific line for logging the username is as follows:
```kotlin
Log.d("Username: ", binding.edtUsername.editText!!.text.toString())
```

The first argument is the tag that can be searched for in Logcat. The second argument is the actual message that should be written.

## Where this can be found
Logging statements can be found anywhere inside an app, but will usually be found around code which performs key actions - such as verifying user information or storing information locally.

## Mitigation
As a developer you should be very careful about what is being logged (if logging is kept in the production builds) so that sensitive information or PII can not be leaked to other applications or malicious actors. The simplest solutions include removing logging from the production builds, or to go through and ensure no sensitive information is saved inside these logs. In particular, avoid saving logs to file.