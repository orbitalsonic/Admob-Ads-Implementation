package com.orbitalsonic.admobadsconfigurations.adsconfig.callbacks

interface BannerCallBack {
    fun onAdFailedToLoad(adError:String)
    fun onAdLoaded()
    fun onAdImpression()
}