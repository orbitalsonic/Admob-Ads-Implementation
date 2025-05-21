package com.orbitalsonic.admobads.ui.fragments.splash

import com.orbitalsonic.admobads.R
import com.orbitalsonic.admobads.adsconfig.interstitial.AdmobInterstitial
import com.orbitalsonic.admobads.adsconfig.natives.AdmobNativePreload
import com.orbitalsonic.admobads.adsconfig.interstitial.callbacks.InterstitialOnShowCallBack
import com.orbitalsonic.admobads.adsconfig.natives.enums.NativeType
import com.orbitalsonic.admobads.databinding.FragmentSplashLanguageBinding
import com.orbitalsonic.admobads.ui.activities.SplashActivity
import com.orbitalsonic.admobads.ui.fragments.base.BaseFragment

/**
 * @Author: Muhammad Yaqoob
 * @Date: 14,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
class FragmentSplashLanguage : BaseFragment<FragmentSplashLanguageBinding>(R.layout.fragment_splash_language) {

    private val admobInterstitial by lazy { AdmobInterstitial() }
    private val admobNativePreload by lazy { AdmobNativePreload() }

    override fun onViewCreatedOneTime() {
        binding.mbContinueLanguage.setOnClickListener { onContinueClick() }

        showNativeAd()
    }

    override fun onViewCreatedEverytime() {}


    /**
     * Add Service in Manifest first
     */

    private fun onContinueClick() {
        if (isAdded){
            diComponent.sharedPreferenceUtils.showFirstScreen = false
            (activity as SplashActivity).nextActivity()
            admobInterstitial.showInterstitialAd(activity,object : InterstitialOnShowCallBack {
                override fun onAdDismissedFullScreenContent() {}
                override fun onAdFailedToShowFullScreenContent() {}
                override fun onAdShowedFullScreenContent() {}
                override fun onAdImpression() {}

            })
        }
    }

    private fun showNativeAd(){
        if (isAdded){
            admobNativePreload.showNativeAds(
                activity,
                binding.adsPlaceHolder,
                NativeType.LARGE_ADJUSTED
            )
        }
    }

    override fun navIconBackPressed() {}

    override fun onBackPressed() {}
}