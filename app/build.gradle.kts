plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.sacnnerui"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sacnnerui"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.androidx.compose.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //noinspection UseTomlInstead
    implementation("androidx.compose.material:material-icons-extended:1.6.7")
    //noinspection UseTomlInstead
    implementation("androidx.navigation:navigation-compose:2.7.7")

    //noinspection UseTomlInstead
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //noinspection UseTomlInstead
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //noinspection GradleDependency,UseTomlInstead
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")
    //noinspection UseTomlInstead
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    //noinspection UseTomlInstead


    // Coil
    //noinspection UseTomlInstead
    implementation("io.coil-kt:coil-compose:2.4.0")


    //DataWedge

    //noinspection UseTomlInstead
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")

    //noinspection UseTomlInstead
    implementation ("androidx.appcompat:appcompat:1.7.0")
    //noinspection UseTomlInstead
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

}