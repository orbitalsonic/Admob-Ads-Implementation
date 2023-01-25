package com.orbitalsonic.admobadsconfigurations.adsconfig.callbacks

interface InterstitialOnLoadCallBack {
    fun onAdFailedToLoad(adError:String)
    fun onAdLoaded()
    fun onPreloaded()
}