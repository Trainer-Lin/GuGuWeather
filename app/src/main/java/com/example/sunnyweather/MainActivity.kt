package com.example.sunnyweather

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("FUCK" , "onCreateSuccess")
        enableEdgeToEdge()
        makeStatusBarTransparent()
        setContentView(R.layout.activity_main)
    }

    private fun makeStatusBarTransparent() {
        // 使用WindowCompat确保最佳兼容性
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 使用WindowInsetsController控制状态栏
        val controller = ViewCompat.getWindowInsetsController(window.decorView)
        controller?.let {
            // 隐藏状态栏
            it.hide(WindowInsetsCompat.Type.statusBars())
            // 设置状态栏图标为浅色（适合深色背景）
            it.isAppearanceLightStatusBars = false
        }

        // 设置状态栏颜色为透明
        window.statusBarColor = Color.TRANSPARENT
    }
}