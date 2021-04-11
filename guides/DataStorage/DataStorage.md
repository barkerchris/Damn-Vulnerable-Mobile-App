# Insecure Data Storage

## Introduction
In a recent [review of mobile application security](https://www.ptsecurity.com/ww-en/analytics/mobile-application-security-threats-and-vulnerabilities-2019/), Insecure Data Storage was found to be the most common issue, found in 76% of apps. This is a huge number, and clearly shows that there is a lack of developer knowledge when it comes to storing information.
As stated in the [documentation](https://developer.android.com/training/data-storage/), Android provides many ways to store data locally on the phone. The different options all have their purpose and it is important to use the correct type of storage for a given scenario. By default, Android provides no native way of encrypting data and this responsibility falls to you as the developer.


## Vulnerability
In this app, when the user logs in their username and password are saved, unencrypted, in Internal Storage, External Storage, SharedPreferences and a SQLite Database.
The code to save the details is below:
```kotlin
private fun storeUser(view: View) {
    if (binding.edtUsername.editText!!.text.isEmpty() ||
        binding.edtPassword.editText!!.text.isEmpty()) {
        binding.edtUsername.error = getString(R.string.txt_empty)
        binding.edtPassword.error = getString(R.string.txt_empty)
    } else {
        binding.edtUsername.error = null
        binding.edtPassword.error = null
        val username = binding.edtUsername.editText!!.text.toString()
        val password = binding.edtPassword.editText!!.text.toString()

        sharedPreferences(username, password)
        internalStorage(username, password)
        externalStorage(username, password)
        database(username, password)

        Snackbar.make(view, R.string.snk_saved_credentials, Snackbar.LENGTH_SHORT).show()
    }
}

private fun sharedPreferences(username: String, password: String) {
    val sharedPrefs: SharedPreferences = requireActivity().getSharedPreferences("userProfileSP", Context.MODE_PRIVATE)
    val sharedPrefsEdit: SharedPreferences.Editor = sharedPrefs.edit()
    sharedPrefsEdit.putString("username", username)
    sharedPrefsEdit.putString("password", password)
    sharedPrefsEdit.apply()
}

private fun internalStorage(username: String, password: String) {
    val myFile = File(requireContext().filesDir, "userProfileIS.txt")
    fileWriter(myFile, username, password)
}

private fun externalStorage(username: String, password: String) {
    if(Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
        val myFile = File(requireContext().getExternalFilesDir(null), "userProfileES.txt")
        fileWriter(myFile, username, password)
    }
}

private fun fileWriter(myFile: File, username: String, password: String) {
    try {
        val fileOutputStream = FileOutputStream(myFile)
        fileOutputStream.write("username: $username\n".toByteArray())
        fileOutputStream.write("password: $password\n".toByteArray())
        fileOutputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

private fun database(username: String, password: String) {
    val db = DatabaseHelper(requireContext())
    db.addUser(username, password)
}
```

The first issue is the most obvious one - there is no encryption being applied to the data being saved anywhere. Any PII should be encrypted independent of which storage option is chosen. However, the issue with encryption is that it falls to the developer to implement. In the [Mitigations section](#Mitigations), I will cover what to do but in this section I will talk about what not to do.
As mentioned in [this](https://www.youtube.com/watch?v=W3mwSnF1n50) video, one of the first results on StackOverflow when you look up how to use AES encryption in Android ends with this ![Top StackOverflow answer](/images/guides/DataStorage/stack_overflow.PNG). There are several issues with this answer. The answer itself is covered in warnings saying it is unsafe and not suitable to be used - but people will do so anyway. Furthermore, the answer also uses now-deprecated functions and does not make use of Android's [Keystore](https://developer.android.com/training/articles/keystore).

The problem with not encrypting data is that it is always accessible. External Storage is an inappropriate storage method for storing app-specific sensitive information because it is globally readable by other apps. While the other methods are not able to be read by other apps, if the physical device is obtained then it is trivial to access all of these files. 

## What does this look like
The code snippet for storing the data is included above. The key thing to note is the lack of encryption function or library that can handle it for us. The data being inserted is just the raw user data as shown in the following line:
```kotlin
fileOutputStream.write("username: $username\n".toByteArray())
fileOutputStream.write("password: $password\n".toByteArray())
```

## Where this can be found
Insecure Data Storage can be found anywhere but will be particularly common in areas of the app that handle user information such as a log in screen, or a place where the user inputs data into a form.

## Mitigations
Google realised that the lack of integrated encryption was an issue and created a library - [JetpackSecurity](https://developer.android.com/jetpack/androidx/releases/security) - to help developers implement secure encryption in their apps. In order to promote this library, Google released [this video](https://www.youtube.com/watch?v=W3mwSnF1n50) as part of the launch, along with [this video](https://www.youtube.com/watch?v=2y9Ol2N1I4k) as part of the Enterprise Dev Training series they promote. They also released [this](https://android-developers.googleblog.com/2020/02/data-encryption-on-android-with-jetpack.html) blog post showing developers how to use the library to encrypt data.

Jetpack Security provides key creation and verification along with 2 classes to help developers store their data securely.
The first thing I will talk about is the introduction of [EncryptedSharedPreferences](https://developer.android.com/reference/androidx/security/crypto/EncryptedSharedPreferences). This is the same as the regular SharedPreferences you will be used to using. For more information on SharedPreferences visit [this link](https://developer.android.com/training/data-storage/shared-preferences). The difference is in the name - one is encrypted and one is not.
In order to use the EncryptedSharedPreferences, after ensuring you have the correct gradle dependencies, you must first create a key.
There are many different options for creating keys such as requiring user authentication and setting a time window that the key is valid for. However, for most applications Google recommends the following:
```kotlin
mainKey = MasterKey.Builder(requireContext())
    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
    .build()
```

Once a key has been created you can then use EncryptedSharedPreferences like you usually would, but with the addition of the key and encryption schemes passed in. For example:
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

Retrieving the values is very much the same as usual with the above modifications.

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