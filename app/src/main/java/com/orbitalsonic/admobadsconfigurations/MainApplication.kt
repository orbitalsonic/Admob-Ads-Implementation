package com.orbitalsonic.admobadsconfigurations

import android.app.Application
import com.orbitalsonic.admobadsconfigurations.adsconfig.AppOpenManager

class MainApplication:Application() {

    private var appOpenManager: AppOpenManager? = null

    override fun onCreate() {
        super.onCreate()

        appOpenManager = AppOpenManager(this)

    }

}