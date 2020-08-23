import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

tasks.withType(KotlinCompile::class).all {
    kotlinOptions {
        // Enable Experimental Inline Classes support
        freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
    }
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

    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.core:core-ktx:1.3.1")
    implementation("androidx.activity:activity-ktx:1.1.0")

    val lifecycleVersion = "2.2.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
    kapt("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")

    val coroutinesVersion = "1.3.9"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.1.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.transition:transition:1.3.1")

    implementation("com.google.android.material:material:1.2.0")

    val okhttpVersion = "4.8.1"
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    debugImplementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")

    val moshiVersion = "1.9.3"
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
