package com.example.host

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.plugin_api.IPluginActivity

class ProxyActivity : AppCompatActivity() {
    private var plugin: IPluginActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // 先给稳定的 AppCompat 主题，防止崩
        setTheme(androidx.appcompat.R.style.Theme_AppCompat_DayNight_NoActionBar)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

        // 如果插件主题是 AppCompat 派生，再叠加
        val pluginTheme = PluginManager.themeResId
        if (pluginTheme != 0 && isAppCompatDerived(pluginTheme)) {
            setTheme(pluginTheme)
        }

        super.onCreate(savedInstanceState)

        val pluginClass = intent.getStringExtra("plugin_class") ?: error("Missing plugin_class")
        val cl = PluginManager.classLoader ?: error("Plugin not loaded")
        val instance =
            cl.loadClass(pluginClass).getDeclaredConstructor().newInstance() as IPluginActivity

        plugin = instance
        instance.attach(this)
        instance.onCreate(savedInstanceState)
    }

    /** 判断主题是否为 AppCompat 派生：能解析出 appcompat 的 colorPrimary 就算通过 */
    private fun isAppCompatDerived(themeResId: Int): Boolean {
        if (themeResId == 0) return false
        val newTheme = resources.newTheme()
        newTheme.setTo(theme)                // 基于当前主题
        newTheme.applyStyle(themeResId, true) // 叠加插件主题
        val tv = TypedValue()
        return newTheme.resolveAttribute(androidx.appcompat.R.attr.colorPrimary, tv, true)
    }

    override fun getAssets() = PluginManager.assets ?: super.getAssets()
    override fun getResources() = PluginManager.resources ?: super.getResources()

    override fun onStart() { super.onStart(); plugin?.onStart() }
    override fun onResume() { super.onResume(); plugin?.onResume() }
    override fun onPause() { plugin?.onPause(); super.onPause() }
    override fun onStop() { plugin?.onStop(); super.onStop() }
    override fun onDestroy() { plugin?.onDestroy(); super.onDestroy() }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        plugin?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
