# Insecure Data Storage

## Introduction
In a recent [review of mobile application security](https://www.ptsecurity.com/ww-en/analytics/mobile-application-security-threats-and-vulnerabilities-2019/), Insecure Data Storage was found to be the most common issue, found in 76% of apps. This is a huge number, and clearly shows that there is a lack of developer knowledge when it comes to storing information.
As stated in the [documentation](https://developer.android.com/training/data-storage/), Android provides many ways to store data locally on the phone. The different options all have their purpose and it is important to use the correct type of storage for a given scenario. By default, Android provides no native way of encrypting data and this responsibility falls to you as the developer.

## Vulnerability
In this app, when the user logs in their username and password are saved - unencrypted - in Internal and External Storage, SharedPreferences and an SQLite Database.

The first issue with this is the most obvious one - the data is stored completely unencrypted. Any PII should be encrypted independent of where the data is stored.
As mentioned in [this](https://www.youtube.com/watch?v=W3mwSnF1n50) video, when you search for AES encryption in Android one of the first results on StackOverflow is this ![Top StackOverflow answer](/images/guides/DataStorage/stack_overflow.PNG). There are several issues with this answer. The answer itself is covered in warnings saying it is unsafe and not suitable to be used - but people will do so anyway. The answer uses now-deprecated functions and does not make use of Android's [Keystore](https://developer.android.com/training/articles/keystore).

The second issue is where this data is stored. External Storage is an inappropriate storage method for storing app-specific sensitive information because it is globally readable by other apps.
As mentioned in the [DataLogging guide](/guides/DataLogging/DataLogging.md), while the other storage locations are not able to be read by other apps, if the physical device is obtained then it is easy to access all of these files. 

## What this looks like
The code snippet for storing the data is included below. The key thing to note is the lack of encryption used.
```kotlin
fileOutputStream.write("username: $username\n".toByteArray())
fileOutputStream.write("password: $password\n".toByteArray())
```

## Where this can be found
Insecure Data Storage can be found anywhere but will be particularly common in areas of the app that handle user information such as a log in screen, or a place where the user inputs data into a form.

## Mitigation
Traditionally in Android, encryption falls to the developer to implement. However, Google realised that the lack of integrated solution was problematic and created a library - [JetpackSecurity](https://developer.android.com/jetpack/androidx/releases/security) - to help developers implement secure encryption in their apps. In order to promote this library, Google released [this video](https://www.youtube.com/watch?v=W3mwSnF1n50) as part of the launch, along with [this video](https://www.youtube.com/watch?v=2y9Ol2N1I4k) as part of the Enterprise Dev Training series they run. They also released [this](https://android-developers.googleblog.com/2020/02/data-encryption-on-android-with-jetpack.html) blog post showing developers how to use the library to encrypt data.

Jetpack Security provides key creation and verification along with two classes to help developers store their data securely.
The first thing I will talk about is the introduction of [EncryptedSharedPreferences](https://developer.android.com/reference/androidx/security/crypto/EncryptedSharedPreferences). This is the encrypted form of the regular SharedPreferences you will be used to using. For more information on SharedPreferences visit [the documentation](https://developer.android.com/training/data-storage/shared-preferences).
In order to use the EncryptedSharedPreferences, after ensuring you have added the correct gradle dependencies, you must first create a key.
This library gives many different options for creating keys such as requiring user authentication and setting a time window that the key is valid for. However, for most applications Google recommends the following:
```kotlin
mainKey = MasterKey.Builder(requireContext())
    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
    .build()
```

Once a key has been created you can then create an instance of EncryptedSharedPreferences. This is like creating a SharedPreference instance, except the key and encryption scheme are passed in. For example:
```kotlin
val sharedPrefsFile = "encryptedUserProfileSP"
val sharedPrefs: SharedPreferences = EncryptedSharedPreferences.create(
    requireContext(),
    sharedPrefsFile,
    mainKey,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)
with (sharedPrefs.edit()) {
    putString("username", username)
    putString("password", password)
    apply()
}
```

Retrieving the values is very much the same as with SharedPreferences.

The other class that is included is [EncryptedFile](https://developer.android.com/reference/androidx/security/crypto/EncryptedFile). Much like EncryptedSharedPreferences this is intended to be a drop-in replacement for usual file operations. The only change is passing in a key and encryption scheme. An example of writing to an encrypted file is below:
```kotlin
val myFile = File(requireContext().filesDir, "encryptedUserProfileFile.txt")
if (myFile.exists()) {
    myFile.delete()
}
val myEncryptedFile = EncryptedFile.Builder(
    requireContext(),
    myFile,
    mainKey,
    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
).build()

val fileContent = "$username,$password".toByteArray(StandardCharsets.UTF_8)
myEncryptedFile.openFileOutput().apply {
    write(fileContent)
    flush()
    close()
}
```