plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.zybooks.journalapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.zybooks.journalapp"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.play.services.location)
    implementation(libs.play.services.tasks)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation(libs.play.services.tasks)
    implementation(libs.androidx.runtime.livedata)
    implementation("io.coil-kt:coil-compose:2.6.0") // Use the latest version
    implementation("androidx.camera:camera-camera2:1.2.3") // or a newer version
    implementation("androidx.camera:camera-lifecycle:1.2.3")
    implementation("androidx.camera:camera-view:1.2.3")
    implementation("androidx.compose.material:material-icons-extended:1.3.0") // or the appropriate version you're using
    implementation("androidx.compose.material:material:1.3.0") // For core material components

    var room_version = "2.6.1"

    // Room components
    implementation ("androidx.room:room-runtime:$room_version")
    annotationProcessor ("androidx.room:room-compiler:$room_version")

    // Optional - Kotlin support and coroutines support for Room
    implementation ("androidx.room:room-ktx:$room_version")

    // Optional - Test helpers
    testImplementation ("androidx.room:room-testing:$room_version")

    // LiveData
    implementation ("androidx.lifecycle:lifecycle-livedata:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-common-java8:2.7.0")

}