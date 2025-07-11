plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.hoainam.unitynativeandroid"
    compileSdk = 35

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    compileOnly(fileTree("C:\\Program Files\\Unity\\Hub\\Editor\\6000.0.31f1\\Editor\\Data\\PlaybackEngines\\AndroidPlayer\\Variations\\mono\\Release\\Classes"))
    implementation("com.google.android.gms:play-services-ads-identifier:18.2.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}