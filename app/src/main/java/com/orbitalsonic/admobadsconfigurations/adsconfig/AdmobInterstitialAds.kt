package com.orbitalsonic.admobadsconfigurations.adsconfig

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.orbitalsonic.admobadsconfigurations.utils.GeneralUtils.AD_TAG
import com.orbitalsonic.admobadsconfigurations.adsconfig.callbacks.InterstitialOnLoadCallBack
import com.orbitalsonic.admobadsconfigurations.adsconfig.callbacks.InterstitialOnShowCallBack

class AdmobInterstitialAds(private val mActivity: Activity) {
    companion object{
        private var mInterstitialAd: InterstitialAd? = null
    }
    private var adRequest: AdRequest = AdRequest.Builder().build()
    var isLoadingAd = false

    fun loadInterstitialAd(
        admobInterstitialIds: String,
        isAdActive: Boolean,
        isAppPurchased: Boolean,
        isInternetConnected:Boolean,
        mListener: InterstitialOnLoadCallBack
    ) {

        if (isInternetConnected && !isAppPurchased && isAdActive) {
            if (mInterstitialAd == null && !isLoadingAd) {
                isLoadingAd = true
                InterstitialAd.load(
                    mActivity,
                    admobInterstitialIds,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.e(AD_TAG, "admob Interstitial onAdFailedToLoad")
                            isLoadingAd = false
                            mInterstitialAd = null
                            mListener.onAdFailedToLoad(adError.toString())
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            Log.d(AD_TAG, "admob Interstitial onAdLoaded")
                            isLoadingAd = false
                            mInterstitialAd = interstitialAd
                            mListener.onAdLoaded()

                        }
                    })
            }
        }else{
            Log.e(AD_TAG, "Internet not Connected or App is Purchased or ad is not active from Firebase")
            mListener.onAdFailedToLoad("Internet not Connected or App is Purchased or ad is not active from Firebase")
        }
    }

    fun showInterstitialAd( mListener: InterstitialOnShowCallBack) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(AD_TAG, "admob Interstitial onAdDismissedFullScreenContent")
                    mListener.onAdDismissedFullScreenContent()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e(AD_TAG, "admob Interstitial onAdFailedToShowFullScreenContent")
                    mListener.onAdFailedToShowFullScreenContent()
                    mInterstitialAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(AD_TAG, "admob Interstitial onAdShowedFullScreenContent")
                    mListener.onAdShowedFullScreenContent()
                    mInterstitialAd = null
                }

                override fun onAdImpression() {
                    Log.d(AD_TAG, "admob Interstitial onAdImpression")
                    mListener.onAdImpression()
                }
            }
            mInterstitialAd?.show(mActivity)
        }
    }

    fun showAndLoadInterstitialAd(admobInterstitialIds: String, mListener: InterstitialOnShowCallBack) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(AD_TAG, "admob Interstitial onAdDismissedFullScreenContent")
                    mListener.onAdDismissedFullScreenContent()
                    loadAgainInterstitialAd(admobInterstitialIds)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e(AD_TAG, "admob Interstitial onAdFailedToShowFullScreenContent")
                    mListener.onAdFailedToShowFullScreenContent()
                    mInterstitialAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(AD_TAG, "admob Interstitial onAdShowedFullScreenContent")
                    mListener.onAdShowedFullScreenContent()
                    mInterstitialAd = null
                }

                override fun onAdImpression() {
                    Log.d(AD_TAG, "admob Interstitial onAdImpression")
                    mListener.onAdImpression()
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
                        Log.e(AD_TAG, "admob Interstitial onAdFailedToLoad: $adError")
                        isLoadingAd = false
                        mInterstitialAd = null
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        Log.d(AD_TAG, "admob Interstitial onAdLoaded")
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