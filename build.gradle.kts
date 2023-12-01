buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath(libs.safeArgs)
    }
}

plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.21" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("convention.detekt")
}
