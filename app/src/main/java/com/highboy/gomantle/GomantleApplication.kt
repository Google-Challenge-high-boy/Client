package com.highboy.gomantle

import android.app.Application
import android.content.Context

class GomantleApplication : Application() {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: GomantleApplication
        fun applicationContext(): Context {
            return instance.applicationContext
        }
    }
}