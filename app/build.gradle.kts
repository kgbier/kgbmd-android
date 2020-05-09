import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(29)
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    defaultConfig {
        applicationId = "com.kgbier.kgbmd"
        minSdkVersion(21)
        targetSdkVersion(29)
        buildToolsVersion = "29.0.3"
        versionCode = 1
        versionName = "1.0"


        buildConfigField("Boolean", "INTERNAL", "false")
        buildConfigField("String", "API_KEY_OMDB", "\"e6cce6b\"")
    }

    flavorDimensions("distribution")
    productFlavors {
        create("prod") { setDimension("distribution") }
        create("internal") {
            setDimension("distribution")

            applicationIdSuffix = ".internal"
            versionNameSuffix = "-internal"

            buildConfigField("Boolean", "INTERNAL", "true")
        }
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
    implementation(kotlin("stdlib-jdk7"))
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.2.0")

    implementation("androidx.activity:activity:1.1.0")
    implementation("androidx.activity:activity-ktx:1.1.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    kapt("androidx.lifecycle:lifecycle-common-java8:2.2.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.5")

    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.1.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.0.0")

    implementation("com.google.android.material:material:1.1.0")

    implementation("com.squareup.okhttp3:okhttp:4.5.0")
    debugImplementation("com.squareup.okhttp3:logging-interceptor:4.5.0")

    implementation("com.squareup.moshi:moshi:1.9.2")
    implementation("com.squareup.moshi:moshi-kotlin:1.9.2")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.9.2")

    implementation("com.github.bumptech.glide:glide:4.9.0")
    kapt("com.github.bumptech.glide:compiler:4.9.0")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.0")
    debugImplementation("com.jakewharton.timber:timber:4.7.1")
}
