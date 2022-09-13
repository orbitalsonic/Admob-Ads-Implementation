package com.orbitalsonic.admobadsconfigurations.adsconfig

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.orbitalsonic.admobadsconfigurations.GeneralUtils.AD_TAG
import com.orbitalsonic.admobadsconfigurations.GeneralUtils.IS_AD_SHOWING
import com.orbitalsonic.admobadsconfigurations.adsconfig.callbacks.InterstitialOnLoadCallBack
import com.orbitalsonic.admobadsconfigurations.adsconfig.callbacks.InterstitialOnShowCallBack

class AdmobInterstitialAds(activity: Activity) {

    private var mActivity: Activity = activity
    private var mInterstitialAd: InterstitialAd? = null
    private var adRequest: AdRequest = AdRequest.Builder().build()

    private var interstitialOnLoadCallBack: InterstitialOnLoadCallBack? = null
    private var interstitialOnShowCallBack: InterstitialOnShowCallBack? = null
    var isLoadingAd = false


    fun loadInterstitialAd(
        admobInterstitialIds: String,
        isRemoteConfigActive: Boolean,
        isAppPurchased: Boolean,
        isInternetConnected:Boolean,
        mListener: InterstitialOnLoadCallBack
    ) {
        interstitialOnLoadCallBack = mListener

        if (isInternetConnected && !isAppPurchased && isRemoteConfigActive) {
            if (mInterstitialAd == null && !isLoadingAd) {
                isLoadingAd = true
                InterstitialAd.load(
                    mActivity,
                    admobInterstitialIds,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.i(AD_TAG, "admob Interstitial onAdFailedToLoad")
                            isLoadingAd = false
                            mInterstitialAd = null
                            interstitialOnLoadCallBack?.onAdFailedToLoad(adError.toString())
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            Log.i(AD_TAG, "admob Interstitial onAdLoaded")
                            isLoadingAd = false
                            mInterstitialAd = interstitialAd
                            interstitialOnLoadCallBack?.onAdLoaded()

                        }
                    })
            }
        }
    }

    fun showInterstitialAd( mListener: InterstitialOnShowCallBack) {
        interstitialOnShowCallBack = mListener
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.i(AD_TAG, "admob Interstitial onAdDismissedFullScreenContent")
                    interstitialOnShowCallBack?.onAdDismissedFullScreenContent()
                    IS_AD_SHOWING = false
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.i(AD_TAG, "admob Interstitial onAdFailedToShowFullScreenContent")
                    interstitialOnShowCallBack?.onAdFailedToShowFullScreenContent()
                    IS_AD_SHOWING = false
                }

                override fun onAdShowedFullScreenContent() {
                    Log.i(AD_TAG, "admob Interstitial onAdShowedFullScreenContent")
                    interstitialOnShowCallBack?.onAdShowedFullScreenContent()
                    mInterstitialAd = null
                    IS_AD_SHOWING = true
                }

                override fun onAdImpression() {
                    Log.i(AD_TAG, "admob Interstitial onAdImpression")
                    interstitialOnShowCallBack?.onAdImpression()
                }
            }
            mInterstitialAd?.show(mActivity)
        }
    }

    fun showAndLoadInterstitialAd(admobInterstitialIds: String, mListener: InterstitialOnShowCallBack) {
        interstitialOnShowCallBack = mListener
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.i(AD_TAG, "admob Interstitial onAdDismissedFullScreenContent")
                    interstitialOnShowCallBack?.onAdDismissedFullScreenContent()
                    loadAgainInterstitialAd(admobInterstitialIds)
                    IS_AD_SHOWING = false
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.i(AD_TAG, "admob Interstitial onAdFailedToShowFullScreenContent")
                    interstitialOnShowCallBack?.onAdFailedToShowFullScreenContent()
                    IS_AD_SHOWING = false
                }

                override fun onAdShowedFullScreenContent() {
                    Log.i(AD_TAG, "admob Interstitial onAdShowedFullScreenContent")
                    interstitialOnShowCallBack?.onAdShowedFullScreenContent()
                    mInterstitialAd = null
                    IS_AD_SHOWING = true
                }

                override fun onAdImpression() {
                    Log.i(AD_TAG, "admob Interstitial onAdImpression")
                    interstitialOnShowCallBack?.onAdImpression()
                }
            }
            mInterstitialAd?.show(mActivity)
        }
    }

    private fun loadAgainInterstitialAd(
        admobInterstitialIds: String
    ) {
        if (mInterstitialAd == null && !isLoadingAd) {
            isLoadingAd = true
            InterstitialAd.load(
                mActivity,
                admobInterstitialIds,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.i(AD_TAG, "admob Interstitial onAdFailedToLoad: $adError")
                        isLoadingAd = false
                        mInterstitialAd = null
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        Log.i(AD_TAG, "admob Interstitial onAdLoaded")
                        isLoadingAd = false
                        mInterstitialAd = interstitialAd

                    }
                })
        }
    }

    fun isInterstitialLoaded(): Boolean {
        return mInterstitialAd != null
    }

    fun dismissInterstitialLoaded() {
        mInterstitialAd = null
    }

}