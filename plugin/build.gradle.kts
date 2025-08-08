plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.example.plugin"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.plugin"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release { isMinifyEnabled = false }
        debug { isMinifyEnabled = false }
    }

    // 统一到 Java 21
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
        // 如果需要在低 API 使用新 JDK API，再打开下面这一行并加 desugar 依赖
        // isCoreLibraryDesugaringEnabled = true
    }

    // Kotlin 目标字节码 21
    kotlinOptions {
        jvmTarget = "21"
    }
}

// 推荐：使用 JDK 21 Toolchain
kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.10")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    compileOnly(project(":plugin_api"))
}
