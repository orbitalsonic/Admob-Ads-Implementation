package com.orbitalsonic.admobadsconfigurations.helpers.koin

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.orbitalsonic.admobadsconfigurations.adsconfig.*
import com.orbitalsonic.admobadsconfigurations.helpers.firebase.RemoteConfiguration
import com.orbitalsonic.admobadsconfigurations.helpers.preferences.SharedPreferenceUtils
import com.orbitalsonic.admobadsconfigurations.helpers.managers.InternetManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val managerModules = module {
    single { InternetManager(androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager) }
}

private val utilsModules = module {
    single { SharedPreferenceUtils(androidContext().getSharedPreferences("app_preferences", Application.MODE_PRIVATE)) }
}

private val firebaseModule = module {
    single { RemoteConfiguration(get()) }
}

private val adsModule = module {
    single { AdmobOpenApp(androidContext() as Application) }
    single { AdmobInterstitialAds() }
    single { AdmobPreLoadNativeAds() }
    factory { AdmobNativeAds() }
    factory { AdmobBannerAds() }
}

val modulesList = listOf(utilsModules, managerModules, firebaseModule,adsModule)