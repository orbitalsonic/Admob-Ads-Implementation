package com.orbitalsonic.admobadsconfigurations

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.orbitalsonic.admobadsconfigurations.GeneralUtils.IS_APP_PURCHASED
import com.orbitalsonic.admobadsconfigurations.GeneralUtils.isInternetConnected
import com.orbitalsonic.admobadsconfigurations.RemoteConfigConstants.isMainBannerActive
import com.orbitalsonic.admobadsconfigurations.RemoteConfigConstants.isMainInterstitialActive
import com.orbitalsonic.admobadsconfigurations.RemoteConfigConstants.isMainNativeActive
import com.orbitalsonic.admobadsconfigurations.RemoteConfigConstants.remoteCounter
import com.orbitalsonic.admobadsconfigurations.RemoteConfigConstants.totalCount
import com.orbitalsonic.admobadsconfigurations.adsconfig.AdmobBannerAds
import com.orbitalsonic.admobadsconfigurations.adsconfig.AdmobInterstitialAds
import com.orbitalsonic.admobadsconfigurations.adsconfig.callbacks.BannerCallBack
import com.orbitalsonic.admobadsconfigurations.adsconfig.callbacks.InterstitialOnLoadCallBack
import com.orbitalsonic.admobadsconfigurations.adsconfig.callbacks.InterstitialOnShowCallBack
import com.orbitalsonic.admobadsconfigurations.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        loadAds()

        binding.btnShow.setOnClickListener {
            checkCounter()
        }
    }

    private fun checkCounter(){
        try {
            if (AdmobInterstitialAds(this).isInterstitialLoaded()){
                showInterstitial()
                totalCount += 1
            }else{
                if (totalCount == remoteCounter) {
                    totalCount = 1
                    loadInterstitialAds()
                }else{
                    totalCount += 1
                }
            }
        }catch (e:Exception){
            Log.d(GeneralUtils.AD_TAG,"${e.message}")
        }
    }

    private fun loadAds() {
        Log.d(GeneralUtils.AD_TAG, "Call Admob Native")
        AdmobBannerAds(this).loadNativeAds(
            binding.nativePlaceHolder,
            getString(R.string.admob_native_main_ids),
            isMainNativeActive,
            IS_APP_PURCHASED,
            isInternetConnected(this),
            1,
            object : BannerCallBack {
                override fun onAdFailedToLoad(adError: String) {
                }

                override fun onAdLoaded() {
                }

                override fun onAdImpression() {
                }

            })

        Log.d(GeneralUtils.AD_TAG, "Call Admob Banner")
        AdmobBannerAds(this).loadBannerAds(
            binding.bannerPlaceHolder,
            getString(R.string.admob_banner_main_ids),
            isMainBannerActive,
            IS_APP_PURCHASED,
            isInternetConnected(this),
            object : BannerCallBack {
                override fun onAdFailedToLoad(adError: String) {
                }

                override fun onAdLoaded() {
                }

                override fun onAdImpression() {
                }

            })

    }

    private fun loadInterstitialAds() {

        Log.d(GeneralUtils.AD_TAG, "Call Admob Main Interstitial")
        AdmobInterstitialAds(this).loadInterstitialAd(getString(R.string.admob_interstitial_main_ids),
            isMainInterstitialActive,
            IS_APP_PURCHASED,
            isInternetConnected(this),
            object : InterstitialOnLoadCallBack {
                override fun onAdFailedToLoad(adError: String) {

                }

                override fun onAdLoaded() {

                }

            })

    }

    private fun showInterstitial(){
        AdmobInterstitialAds(this).showInterstitialAd(object : InterstitialOnShowCallBack {
            override fun onAdDismissedFullScreenContent() {

            }

            override fun onAdFailedToShowFullScreenContent() {

            }

            override fun onAdShowedFullScreenContent() {

            }

            override fun onAdImpression() {

            }

        })
    }
}