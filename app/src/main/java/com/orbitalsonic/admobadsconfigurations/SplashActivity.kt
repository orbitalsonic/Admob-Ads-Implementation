package com.orbitalsonic.admobadsconfigurations

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.orbitalsonic.admobadsconfigurations.GeneralUtils.AD_TAG
import com.orbitalsonic.admobadsconfigurations.GeneralUtils.isInternetConnected
import com.orbitalsonic.admobadsconfigurations.adsconfig.AdmobBannerAds
import com.orbitalsonic.admobadsconfigurations.adsconfig.AdmobInterstitialAds
import com.orbitalsonic.admobadsconfigurations.adsconfig.callbacks.BannerCallBack
import com.orbitalsonic.admobadsconfigurations.adsconfig.callbacks.InterstitialOnLoadCallBack
import com.orbitalsonic.admobadsconfigurations.adsconfig.callbacks.InterstitialOnShowCallBack
import com.orbitalsonic.admobadsconfigurations.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    private lateinit var admobBannerAds: AdmobBannerAds
    private lateinit var admobInterstitialAds: AdmobInterstitialAds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        initAds()

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()

            showInterstitial()

        }
    }

    private fun initAds(){
        admobBannerAds = AdmobBannerAds(this)
        admobInterstitialAds = AdmobInterstitialAds(this)
        
        loadAds()
    }

    private fun loadAds() {

        Log.d(AD_TAG, "Call Admob Splash Interstitial")
        admobInterstitialAds.loadInterstitialAd(getString(R.string.admob_interstitial_splash_ids),
            true,
            false,
            isInternetConnected(this),
            object : InterstitialOnLoadCallBack {
                override fun onAdFailedToLoad(adError: String) {
                  
                }

                override fun onAdLoaded() {
           
                }

            })


        Log.d(AD_TAG, "Call Admob Native")
        admobBannerAds.loadNativeAds(binding.adsContainerLayout,
            binding.admobPlaceHolder,
            binding.shimmerLayout,
            getString(R.string.admob_native_splash_ids),
            true,
            false,2,
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

    private fun showInterstitial(){
        admobInterstitialAds.showInterstitialAd(object :InterstitialOnShowCallBack{
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