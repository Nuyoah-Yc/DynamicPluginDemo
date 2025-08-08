package com.example.host

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Build
import dalvik.system.DexClassLoader
import java.io.File

object PluginManager {
    @Volatile var classLoader: DexClassLoader? = null
        private set
    @Volatile var resources: Resources? = null
        private set
    @Volatile var assets: AssetManager? = null
        private set
    @Volatile var themeResId: Int = 0
        private set

    @Volatile var pluginInfo: PluginInfo? = null
        private set

    fun isLoaded(): Boolean = classLoader != null
    
    fun load(context: Context, apkPath: String) {
        val apkFile = File(apkPath)
        require(apkFile.exists()) { "Plugin apk not found: $apkPath" }

        // 1) DexClassLoader
        val optDir = File(context.filesDir, "plugin_opt").apply { mkdirs() }
        classLoader = DexClassLoader(apkPath, optDir.absolutePath, null, context.classLoader)

        // 2) 合并宿主与插件 Asset 路径（宿主先、插件后，后者优先）
        val am = AssetManager::class.java.newInstance()
        val addAssetPath = AssetManager::class.java.getMethod("addAssetPath", String::class.java)
        addAssetPath.invoke(am, context.applicationInfo.sourceDir) // 宿主 apk
        addAssetPath.invoke(am, apkPath) // 插件 apk
        assets = am

        // 3) 用合并后的 AssetManager 构造 Resources
        val hostRes = context.resources
        resources = Resources(am, hostRes.displayMetrics, hostRes.configuration)

        // 4) 读取插件 Application 主题 id（给 ProxyActivity 叠加用）以及插件信息
        val pm = context.packageManager
        val pkgInfo = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_META_DATA)
        val appInfo = pkgInfo?.applicationInfo?.apply {
            // 这两句很关键，否则读不到正确的 meta/theme
            sourceDir = apkPath
            publicSourceDir = apkPath
        }
        themeResId = appInfo?.theme ?: 0
        pluginInfo = pkgInfo?.let {
            val versionCode = if (Build.VERSION.SDK_INT >= 28) it.longVersionCode else it.versionCode.toLong()
            PluginInfo(it.packageName ?: "", it.versionName, versionCode)
        }
    }
}
