// AGP 9's built-in Kotlin support ships with Kotlin Gradle Plugin 2.2.10 by
// default; bump it to the latest stable KGP so the project uses the newest
// Kotlin compiler (see https://kotl.in/gradle/agp-built-in-kotlin).
buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
