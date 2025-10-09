package com.example.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

//TODO: 写一个类 让全局都可以访问Context变量 解决工具类没办法获取context的问题
/*  使用方法: 用一个延迟初始化的companion object 伴随内部单例类去存储applicationContext 在其他项目的调用就是SunnyWeatherApplication.context   */

class SunnyWeatherApplication: Application() {
   companion object {
       const val TOKEN = "Y5H90JK8UASwnmel"
       @SuppressLint("StaticFieldLeak")
       lateinit var context: Context
   }
}