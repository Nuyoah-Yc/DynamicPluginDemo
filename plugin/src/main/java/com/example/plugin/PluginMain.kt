// PluginApk: com/example/plugin/PluginMain.kt
package com.example.plugin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.plugin_api.IPluginActivity

class PluginMain : IPluginActivity {
    companion object { private const val TAG = "PluginMain" }
    private lateinit var host: Activity

    override fun attach(hostActivity: Activity) {
        host = hostActivity
        Log.d(TAG, "attach: $hostActivity")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        // 直接用插件 R；宿主 ProxyActivity 已把资源环境切到插件
        host.setContentView(R.layout.activity_plugin)

        val tv = host.findViewById<TextView>(R.id.tvTitle)
        val btn = host.findViewById<Button>(R.id.btnAction)

        tv?.text = "这是插件里的界面（来自 plugin.apk 的资源）"
        btn?.setOnClickListener {
            Log.d(TAG, "button clicked")
            android.widget.Toast.makeText(host, "Plugin 按钮点击", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() { Log.d(TAG, "onStart") }
    override fun onResume() { Log.d(TAG, "onResume") }
    override fun onPause() { Log.d(TAG, "onPause") }
    override fun onStop() { Log.d(TAG, "onStop") }
    override fun onDestroy() { Log.d(TAG, "onDestroy") }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult: req=$requestCode res=$resultCode")
    }
}
