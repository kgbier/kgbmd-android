// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.10.1")
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

plugins {
    val kotlinVersion = "2.1.21"
    id("com.google.devtools.ksp") version "2.1.21-2.0.1" apply false
    kotlin("android") version kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
