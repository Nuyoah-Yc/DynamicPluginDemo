// Host: com/example/host/MainActivity.kt
package com.example.host

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    companion object { private const val TAG = "MainActivity" }

    private val requestPerm = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* ignore; 我们走 assets 拷贝，不依赖外部存储 */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate")

        // 1) 将 assets 里的 plugin.apk 拷贝到内部存储（避免分区存储麻烦）
        val pluginApk = File(filesDir, "plugin.apk")
        if (!pluginApk.exists()) {
            assets.open("plugin.apk").use { ins ->
                FileOutputStream(pluginApk).use { outs -> ins.copyTo(outs) }
            }
        }

        // 2) 加载插件
        PluginManager.load(this, pluginApk.absolutePath)
        Log.d(TAG, "Plugin loaded")

        // 显示插件信息
        val infoText = PluginManager.pluginInfo?.let {
            "Plugin: ${it.packageName}\nVersion: ${it.versionName ?: "unknown"} (${it.versionCode})"
        } ?: "插件信息读取失败"
        findViewById<TextView>(R.id.tvInfo)?.text = infoText

        // 3) 启动 ProxyActivity，并指定插件类名（完整类名）
        findViewById<android.view.View>(R.id.btnLaunch)?.setOnClickListener {
            Log.d(TAG, "Launch plugin")
            val intent = Intent(this, ProxyActivity::class.java).apply {
                putExtra("plugin_class", "com.example.plugin.PluginMain")
            }
            startActivity(intent)
        }
    }
}
