package com.example.plugin_api

import android.app.Activity
import android.content.Intent
import android.os.Bundle

interface IPluginActivity {
    fun attach(host: Activity)           // 宿主上下文注入
    fun onCreate(savedInstanceState: Bundle?)
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDestroy()
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}