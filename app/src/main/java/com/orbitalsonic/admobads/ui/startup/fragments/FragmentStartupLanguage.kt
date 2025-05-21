package com.orbitalsonic.admobads.ui.startup.fragments

import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.orbitalsonic.admobads.adsconfig.interstitial.AdmobInterstitial
import com.orbitalsonic.admobads.adsconfig.interstitial.callbacks.InterstitialOnShowCallBack
import com.orbitalsonic.admobads.adsconfig.natives.AdmobNativePreload
import com.orbitalsonic.admobads.adsconfig.natives.enums.NativeType
import com.orbitalsonic.admobads.common.preferences.SharedPrefManager
import com.orbitalsonic.admobads.databinding.FragmentStartupLanguageBinding
import com.orbitalsonic.admobads.ui.base.fragments.BaseFragment
import com.orbitalsonic.admobads.ui.startup.StartupActivity

/**
 * @Author: Muhammad Yaqoob
 * @Date: 14,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
class FragmentStartupLanguage :
    BaseFragment<FragmentStartupLanguageBinding>(FragmentStartupLanguageBinding::inflate) {

    private val admobInterstitial by lazy { AdmobInterstitial() }
    private val admobNativePreload by lazy { AdmobNativePreload() }

    private val sharedPrefManager by lazy {
        SharedPrefManager(
            requireActivity().getSharedPreferences(
                "app_preferences",
                MODE_PRIVATE
            )
        )
    }

    override fun onViewCreated() {
        binding.mbContinueLanguage.setOnClickListener { onContinueClick() }

        showNativeAd()
    }

    /**
     * Add Service in Manifest first
     */

    private fun onContinueClick() {
        if (isAdded) {
            sharedPrefManager.isFirstTimeEntrance = false
            (activity as StartupActivity).nextActivity()
            admobInterstitial.showInterstitialAd(activity, object : InterstitialOnShowCallBack {
                override fun onAdDismissedFullScreenContent() {}
                override fun onAdFailedToShowFullScreenContent() {}
                override fun onAdShowedFullScreenContent() {}
                override fun onAdImpression() {}

            })
        }
    }

    private fun showNativeAd() {
        if (isAdded) {
            admobNativePreload.showNativeAds(
                activity,
                binding.adsPlaceHolder,
                NativeType.LARGE_ADJUSTED
            )
        }
    }
}