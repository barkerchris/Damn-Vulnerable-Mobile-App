# Insecure Data Logging

## Introduction
Android Studio allows you to read and write system and app-specific messages to the [Logcat window](https://developer.android.com/studio/debug/am-logcat). While logging can be indispensable in debugging and development, it has the potential to leak Personally Identifiable Information (PII), or other sensitive information.

## Vulnerability
In this app, the log files are being written to a file and dumped in Internal Storage. As I cover in the Insecure Data Storage guide, this is potentially dangerous and can lead to the leaking of private information.
In this case, the following code is the suspect:
''''kotlin
binding.btnVulnerabilitiesLoggingLogIn.setOnClickListener {
    if (binding.edtUsername.editText!!.text.isEmpty() ||
        binding.edtPassword.editText!!.text.isEmpty()) {
        binding.edtUsername.error = getString(R.string.txt_empty)
        binding.edtPassword.error = getString(R.string.txt_empty)
    } else {
        binding.edtUsername.error = null
        binding.edtPassword.error = null
        //clear logcat before logging
        Runtime.getRuntime().exec("logcat -c")
        //log user input
        Log.d("Username: ", binding.edtUsername.editText!!.text.toString())
        Log.d("Password: ", binding.edtPassword.editText!!.text.toString())
        dumpLogs()
        
        Snackbar.make(view, R.string.snk_saved_credentials, Snackbar.LENGTH_SHORT).show()
    }
}

private fun dumpLogs() {
    val fileName = "logs.txt"
    val logFile = File(requireContext().filesDir, fileName)
    try {
        val logcat = Runtime.getRuntime().exec("logcat -d")
        val reader = BufferedReader(InputStreamReader(logcat.inputStream))
        val writer = BufferedWriter(FileWriter(logFile))
        while (true) {
            //If null then break
            val line = reader.readLine() ?: break
            writer.append(line).append('\n')
        }
        writer.flush()
        writer.close()
        reader.close()
    } catch (e: IOException) {
        throw RuntimeException(e)
    }
}
''''

## What does this look like
In order to write a message you must use the [Log API](https://developer.android.com/reference/android/util/Log). There are many different options for the different types of messages that can be written. In the app I have used the .d() command in order to write a DEBUG message. In the app, the user's username and password are logged to the console, and every time the user presses 'Log In' the log files are written to a file and saved in Internal Storage.
The specific line for logging the username is as follows:
''''kotlin
Log.d("Username: ", binding.edtUsername.editText!!.text.toString())
''''

The first argument is the tag that can be searched for in Logcat. The second argument is the actual message that should be written.

## Where this can be found
Logging statements can be found anywhere inside an app, but will usually be found around code which performs key actions - such as verifying user information or storing information locally.

## Mitigations
As a developer you should be very careful about what is being logged (if logging is kept in the production builds) so that sensitive information or PII can not be leaked to other applications or malicious actors. The simplest solutions include removing logging from the production builds, or to go through and ensure no sensitive information is saved inside these logs. In particular, avoid saving logs to file.