plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization")
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.kgbier.kgbmd"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kgbier.kgbmd"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        buildConfigField("Boolean", "INTERNAL", "false")
    }
    flavorDimensions += listOf("distribution")

    productFlavors {
        create("prod") { dimension = "distribution" }
        create("internal") {
            dimension = "distribution"

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
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.activity:activity-ktx:1.10.1")

    val lifecycleVersion = "2.9.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    kapt("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")

    val coroutinesVersion = "1.10.2"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")

    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.3.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.transition:transition:1.6.0")

    implementation("com.google.android.material:material:1.12.0")

    val okhttpVersion = "4.12.0"
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    debugImplementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")

    val glideVersion = "4.14.2"
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    ksp("com.github.bumptech.glide:ksp:$glideVersion")

    implementation("org.jsoup:jsoup:1.14.3")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.13")
    debugImplementation("com.jakewharton.timber:timber:5.0.1")

    testImplementation(kotlin("test-junit5"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
