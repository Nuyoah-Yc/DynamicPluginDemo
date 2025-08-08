plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.example.host"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.host"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release { isMinifyEnabled = false }
        debug { isMinifyEnabled = false }
    }
    buildFeatures {
        viewBinding = true    // 或者 dataBinding true
    }

    // 统一 Java 语言级别为 21
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    // Kotlin 编译目标字节码为 21
    kotlinOptions {
        jvmTarget = "21"
    }
}

// 指定 Kotlin 使用 JDK 21 Toolchain（推荐）
kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.10")
    implementation("androidx.activity:activity-ktx:1.9.2")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation(project(":plugin_api"))
}
