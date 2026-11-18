import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.secrets.gradle.plugin)
    id("com.google.devtools.ksp")
}

configurations.configureEach {
    exclude(group = "com.google.android.gms", module = "play-services-maps")
}


android {
    namespace = "com.example.AndroMoto"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.andromoto"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        manifestPlaceholders["redirectSchemeName"] = "andromoto"
        manifestPlaceholders["redirectHostName"] = "callback"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation("com.google.maps.android:maps-compose:8.4.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.5")
    implementation("com.google.android.libraries.navigation:navigation:8.0.0")
    implementation(libs.androidx.compose.ui.text.google.fonts)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs_nio:2.1.5")
    implementation("androidx.room:room-ktx:2.8.5")
    ksp("androidx.room:room-compiler:2.8.5")
    implementation("com.google.android.libraries.places:places:3.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.3.5")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.11.0")
    implementation("io.coil-kt.coil3:coil-compose:3.6.3")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.12.1")
    //Spotify
    implementation(files("libs/spotify-app-remote-release-0.8.0.aar"))
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.spotify.android:auth:2.1.0")

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}