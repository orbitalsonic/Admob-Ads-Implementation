package com.orbitalsonic.admobadsconfigurations

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.orbitalsonic.admobadsconfigurations.adsconfig.AdmobBannerAds
import com.orbitalsonic.admobadsconfigurations.adsconfig.AdmobInterstitialAds
import com.orbitalsonic.admobadsconfigurations.adsconfig.callbacks.BannerCallBack
import com.orbitalsonic.admobadsconfigurations.adsconfig.callbacks.InterstitialOnLoadCallBack
import com.orbitalsonic.admobadsconfigurations.adsconfig.callbacks.InterstitialOnShowCallBack
import com.orbitalsonic.admobadsconfigurations.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var admobBannerAds: AdmobBannerAds
    private lateinit var admobInterstitialAds: AdmobInterstitialAds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initAds()

        binding.btnShow.setOnClickListener {
            showInterstitial()
        }
    }

    private fun initAds(){
        admobBannerAds = AdmobBannerAds(this)
        admobInterstitialAds = AdmobInterstitialAds(this)

        loadAds()
    }

    private fun loadAds() {

        Log.d(GeneralUtils.AD_TAG, "Call Admob Main Interstitial")
        admobInterstitialAds.loadInterstitialAd(getString(R.string.admob_interstitial_main_ids),
            true,
            false,
            GeneralUtils.isInternetConnected(this),
            object : InterstitialOnLoadCallBack {
                override fun onAdFailedToLoad(adError: String) {

                }

                override fun onAdLoaded() {

                }

            })


        Log.d(GeneralUtils.AD_TAG, "Call Admob Native")
        admobBannerAds.loadNativeAds(binding.adsContainerLayoutNative,
            binding.admobPlaceHolderNative,
            binding.shimmerLayoutNative,
            getString(R.string.admob_native_main_ids),
            true,
            false,1,
            GeneralUtils.isInternetConnected(this),
            object : BannerCallBack {
                override fun onAdFailedToLoad(adError: String) {
                }

                override fun onAdLoaded() {
                }

                override fun onAdImpression() {
                }

            })

        Log.d(GeneralUtils.AD_TAG, "Call Admob Banner")
        admobBannerAds.loadBannerAds(binding.adsContainerLayoutBanner,
            binding.admobPlaceHolderBanner,
            binding.shimmerLayoutBanner,
            getString(R.string.admob_banner_main_ids),
            true,
            false,
            GeneralUtils.isInternetConnected(this),
            object : BannerCallBack {
                override fun onAdFailedToLoad(adError: String) {
                }

                override fun onAdLoaded() {
                }

                override fun onAdImpression() {
                }

            })

    }

    private fun showInterstitial(){
        admobInterstitialAds.showAndLoadInterstitialAd(getString(R.string.admob_interstitial_main_ids),object : InterstitialOnShowCallBack {
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