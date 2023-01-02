package com.orbitalsonic.admobadsconfigurations

import android.app.Application
import com.orbitalsonic.admobadsconfigurations.adsconfig.AppOpenManager

class MainApplication:Application() {

    companion object {
        private var instance: MainApplication? = null

        fun getContext(): MainApplication {
            return instance!!
        }
    }

//    private var appOpenManager: AppOpenManager? = null

    override fun onCreate() {
        super.onCreate()
        instance = this@MainApplication
//        appOpenManager = AppOpenManager(this)

    }

}