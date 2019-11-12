import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.kgbier.kgbmd"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "API_KEY_OMDB", "\"\"")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")
    kapt("androidx.lifecycle:lifecycle-compiler:2.1.0")
    implementation("androidx.core:core-ktx:1.1.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.2")

    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")

    implementation("com.squareup.okhttp3:okhttp:4.2.1")

    implementation("com.squareup.moshi:moshi:1.9.1")
    implementation("com.squareup.moshi:moshi-kotlin:1.9.1")

    implementation("com.github.bumptech.glide:glide:4.9.0")
    kapt("com.github.bumptech.glide:compiler:4.9.0")
}
