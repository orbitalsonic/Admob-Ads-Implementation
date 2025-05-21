package com.orbitalsonic.admobads.ui.fragments.sample

import com.orbitalsonic.admobads.R
import com.orbitalsonic.admobads.adsconfig.natives.AdmobNative
import com.orbitalsonic.admobads.adsconfig.natives.callbacks.NativeCallBack
import com.orbitalsonic.admobads.adsconfig.natives.enums.NativeType
import com.orbitalsonic.admobads.databinding.FragmentSampleBinding
import com.orbitalsonic.admobads.helpers.firebase.RemoteConstants
import com.orbitalsonic.admobads.ui.fragments.base.BaseFragment

/**
 * @Author: Muhammad Yaqoob
 * @Date: 14,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
class FragmentSample : BaseFragment<FragmentSampleBinding>(R.layout.fragment_sample) {

    private val admobNative by lazy { AdmobNative() }

    override fun onViewCreatedOneTime() {
        loadAds()
    }

    override fun onViewCreatedEverytime() {}

    private fun loadAds() {
        admobNative.loadNativeAds(
            activity,
            binding.adsPlaceHolder,
            getResString(R.string.admob_native_ids),
            RemoteConstants.rcvNativeAd,
            diComponent.sharedPreferenceUtils.isAppPurchased,
            diComponent.internetManager.isInternetConnected,
            NativeType.LARGE,
            object : NativeCallBack {
                override fun onAdFailedToLoad(adError: String) {}
                override fun onAdLoaded() {}
                override fun onAdImpression() {}
                override fun onPreloaded() {}
            }
        )
    }

    override fun navIconBackPressed() {
        onBackPressed()
    }

    override fun onBackPressed() {
        popFrom(R.id.fragmentSample)
    }
}