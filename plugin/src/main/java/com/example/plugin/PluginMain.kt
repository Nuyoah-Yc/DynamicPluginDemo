// PluginApk: com/example/plugin/PluginMain.kt
package com.example.plugin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.plugin_api.IPluginActivity

class PluginMain : IPluginActivity {
    private lateinit var host: Activity

    override fun attach(hostActivity: Activity) {
        this.host = hostActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 直接用插件 R；宿主 ProxyActivity 已把资源环境切到插件
        host.setContentView(R.layout.activity_plugin)

        val tv = host.findViewById<TextView>(R.id.tvTitle)
        val btn = host.findViewById<Button>(R.id.btnAction)

        tv?.text = "这是插件里的界面（来自 plugin.apk 的资源）"
        btn?.setOnClickListener {
            android.widget.Toast.makeText(host, "Plugin 按钮点击", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {}
    override fun onResume() {}
    override fun onPause() {}
    override fun onStop() {}
    override fun onDestroy() {}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}
}
