package com.orbitalsonic.admobadsconfigurations.helpers.koin

import com.orbitalsonic.admobadsconfigurations.adsconfig.*
import com.orbitalsonic.admobadsconfigurations.helpers.firebase.RemoteConfiguration
import com.orbitalsonic.admobadsconfigurations.helpers.preferences.SharedPreferenceUtils
import com.orbitalsonic.admobadsconfigurations.helpers.managers.InternetManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DIComponent : KoinComponent {

    // Utils
    val sharedPreferenceUtils by inject<SharedPreferenceUtils>()

    // Managers
    val internetManager by inject<InternetManager>()

    // Remote Configuration
    val remoteConfiguration by inject<RemoteConfiguration>()

    // Ads
    val admobBannerAds by inject<AdmobBannerAds>()
    val admobNativeAds by inject<AdmobNativeAds>()
    val admobPreLoadNativeAds by inject<AdmobPreLoadNativeAds>()
    val admobInterstitialAds by inject<AdmobInterstitialAds>()
    val admobOpenApp by inject<AdmobOpenApp>()

}