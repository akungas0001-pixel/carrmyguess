# Guess My Car

Native Android app built with Kotlin + Jetpack Compose (Material 3).

## Run Locally

**Prerequisites:** Android Studio (latest stable) with Android SDK Platform 37 installed.

1. Open this folder directly in Android Studio (File > Open), or run from the command line:
   ```
   ./gradlew assembleDebug
   ```
2. Click **Run** in Android Studio, or install the generated APK from
   `app/build/outputs/apk/debug/app-debug.apk`.

No Node.js, npm, bun, or Vite is required — this is a pure Gradle/Android project.

## Stack

- Kotlin (built-in AGP Kotlin support, no separate `kotlin-android` plugin)
- Jetpack Compose + Material 3
- Jetpack Navigation Compose
- Gradle Kotlin DSL (`.kts`)
