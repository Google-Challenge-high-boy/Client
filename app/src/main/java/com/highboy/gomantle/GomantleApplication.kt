package com.highboy.gomantle

import android.app.Application
import com.highboy.gomantle.data.AppContainer
import com.highboy.gomantle.data.DefaultAppContainer

class GomantleApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}