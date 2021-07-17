plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(30)

    defaultConfig {
        applicationId = "com.kgbier.kgbmd"
        minSdkVersion(21)
        targetSdkVersion(30)
        buildToolsVersion = "30.0.2"
        versionCode = 1
        versionName = "1.0"

        buildConfigField("Boolean", "INTERNAL", "false")
    }

    flavorDimensions("distribution")
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
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.activity:activity-ktx:1.1.0")

    val lifecycleVersion = "2.2.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    kapt("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")

    val coroutinesVersion = "1.4.0"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.1.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.transition:transition:1.3.1")

    implementation("com.google.android.material:material:1.2.1")

    val okhttpVersion = "4.9.0"
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    debugImplementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")

    val moshiVersion = "1.12.0"
    implementation("com.squareup.moshi:moshi:$moshiVersion")
    implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")

    val glideVersion = "4.11.0"
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    kapt("com.github.bumptech.glide:compiler:$glideVersion")

    implementation("org.jsoup:jsoup:1.13.1")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.4")
    debugImplementation("com.jakewharton.timber:timber:4.7.1")

    testImplementation(kotlin("test-junit5"))
}
