plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

}

android {
    namespace = "com.example.daybuddy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.daybuddy"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-places:17.0.0")
    implementation("com.google.android.libraries.places:places:3.4.0")
    implementation ("com.google.android.gms:play-services-location:21.2.0")
    implementation ("com.google.maps:google-maps-services:0.18.0")
    implementation ("com.google.maps.android:android-maps-utils:2.2.0")

    implementation ("com.intuit.sdp:sdp-android:1.1.1")
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-analytics")

    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("androidx.annotation:annotation:1.7.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("com.google.firebase:firebase-database:21.0.0")

    implementation ("com.airbnb.android:lottie:3.7.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit:converter-gson:2.0.0-beta2")
    implementation("androidx.test.ext:junit:1.1.5")
    implementation("com.google.firebase:firebase-firestore:25.0.0")
    implementation("androidx.activity:activity:1.9.0")
    implementation("androidx.work:work-runtime:2.9.0")
    androidTestImplementation("junit:junit:4.13.2")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")


    implementation ("androidx.core:core:1.7.0")
    implementation ("androidx.core:core-ktx:1.7.0")

}