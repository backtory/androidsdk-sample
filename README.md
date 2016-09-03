# Backtory Android SDK Sample
Sample usage of [Backtory] (https://backtory.com) mbaas Android SDK

### What's this sample for?
This project shows the recommended way of using backtory-sdk in an Android project. It will be kept updated by the
SDK developers.

### Building sample
This project currently needs no particular dependency. Just let gradle sync itself. Export an APK and install it on
a device to see the backtory-sdk services in action.

### Backtory SDK Installation
Put below lines in your module's build.gradle
```java
repositories {
    maven {
        url 'https://dl.bintray.com/pegah-backtory/maven/'
    }
}
```
and this line to its dependencies segment
```java
compile 'com.backtory.android:backtorysdk:<latest-version>'
```
Latest version: [ ![Download](https://api.bintray.com/packages/pegah-backtory/maven/backtorysdk/images/download.svg) ](https://bintray.com/pegah-backtory/maven/backtorysdk/_latestVersion)

Checkout Backtory Android sdk [documentations] (https://backtory.com/docs/android/intro.html)
for more info about sdk installation and using backtory services in Android applications.

