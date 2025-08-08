plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.plugin_api"
    compileSdk = 36

    defaultConfig {
        minSdk = 23          // ✅ 与 :app 对齐，别再写 29 了
        targetSdk = 34
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21   // ✅ 统一到 21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions { jvmTarget = "21" }
}

kotlin { jvmToolchain(21) }

// ✅ 建议：接口库不引任何实现库，保持纯净
dependencies {
    // 空即可；如需注解可用 compileOnly("androidx.annotation:annotation:1.8.1")，但通常不需要
}
