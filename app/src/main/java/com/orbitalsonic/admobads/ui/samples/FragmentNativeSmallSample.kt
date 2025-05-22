package com.orbitalsonic.admobads.ui.samples

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.orbitalsonic.admobads.R
import com.orbitalsonic.admobads.adsconfig.natives.AdmobNative
import com.orbitalsonic.admobads.adsconfig.natives.callbacks.NativeCallBack
import com.orbitalsonic.admobads.adsconfig.natives.enums.NativeType
import com.orbitalsonic.admobads.common.firebase.RemoteConstants.rcvNativeAd
import com.orbitalsonic.admobads.common.network.InternetManager
import com.orbitalsonic.admobads.common.preferences.SharedPrefManager
import com.orbitalsonic.admobads.databinding.FragmentNativeSmallSampleBinding
import com.orbitalsonic.admobads.helpers.utils.getResString
import com.orbitalsonic.admobads.ui.base.fragments.BaseFragment

/**
 * @Author: Muhammad Yaqoob
 * @Date: 14,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
class FragmentNativeSmallSample : BaseFragment<FragmentNativeSmallSampleBinding>(FragmentNativeSmallSampleBinding::inflate) {

    private val sharedPrefManager by lazy {
        SharedPrefManager(
            requireActivity().getSharedPreferences(
                "app_preferences",
                MODE_PRIVATE
            )
        )
    }

    private val internetManager by lazy {
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        InternetManager(connectivityManager)
    }

    private val admobNative by lazy { AdmobNative() }

    override fun onViewCreated() {
        loadAds()
    }

    private fun loadAds() {
        admobNative.loadNativeAds(
            activity,
            binding.adsPlaceHolder,
            getResString(R.string.admob_native_ids),
            rcvNativeAd,
            sharedPrefManager.isAppPurchased,
            internetManager.isInternetConnected,
            NativeType.SMALL,
            object : NativeCallBack {
                override fun onAdFailedToLoad(adError: String) {}
                override fun onAdLoaded() {}
                override fun onAdImpression() {}
                override fun onPreloaded() {}
            }
        )
    }
}