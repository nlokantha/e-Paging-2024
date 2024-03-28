import android.databinding.tool.writer.ViewBinding

plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.e_paging2024"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.e_paging2024"
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
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

//    Room Database..................
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")
    // optional - RxJava2 support for Room
    implementation ("androidx.room:room-rxjava2:2.6.1")

    // optional - RxJava3 support for Room
    implementation ("androidx.room:room-rxjava3:2.6.1")

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation ("androidx.room:room-guava:2.6.1")

    // optional - Test helpers
    testImplementation ("androidx.room:room-testing:2.6.1")

    // optional - Paging 3 Integration
    implementation ("androidx.room:room-paging:2.6.1")
}