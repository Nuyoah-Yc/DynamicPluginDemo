// Host: com/example/host/MainActivity.kt
package com.example.host

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {

    private val requestPerm = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* ignore; 我们走 assets 拷贝，不依赖外部存储 */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1) 将 assets 里的 plugin.apk 拷贝到内部存储（避免分区存储麻烦）
        val pluginApk = File(filesDir, "plugin.apk")
        if (!pluginApk.exists()) {
            assets.open("plugin.apk").use { ins ->
                FileOutputStream(pluginApk).use { outs -> ins.copyTo(outs) }
            }
        }

        // 2) 加载插件
        PluginManager.load(this, pluginApk.absolutePath)

        // 3) 启动 ProxyActivity，并指定插件类名（完整类名）
        findViewById<android.view.View>(R.id.btnLaunch)?.setOnClickListener {
            val intent = Intent(this, ProxyActivity::class.java).apply {
                putExtra("plugin_class", "com.example.plugin.PluginMain")
            }
            startActivity(intent)
        }
    }
}
