package com.orbitalsonic.admobads.ui.samples

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.orbitalsonic.admobads.R
import com.orbitalsonic.admobads.adsconfig.banners.AdmobBanner
import com.orbitalsonic.admobads.adsconfig.banners.enums.BannerType
import com.orbitalsonic.admobads.common.firebase.RemoteConstants.rcvBannerAd
import com.orbitalsonic.admobads.common.network.InternetManager
import com.orbitalsonic.admobads.common.preferences.SharedPrefManager
import com.orbitalsonic.admobads.databinding.FragmentBannerSampleBinding
import com.orbitalsonic.admobads.helpers.utils.getResString
import com.orbitalsonic.admobads.ui.base.fragments.BaseFragment

/**
 * @Author: Muhammad Yaqoob
 * @Date: 14,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
class FragmentBannerSample :
    BaseFragment<FragmentBannerSampleBinding>(FragmentBannerSampleBinding::inflate) {

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

    private val admobBanner by lazy { AdmobBanner() }

    override fun onViewCreated() {
        loadAds()
    }

    private fun loadAds() {
        admobBanner.loadBannerAds(
            activity,
            binding.adsBannerPlaceHolder,
            getResString(R.string.admob_banner_ids),
            rcvBannerAd,
            sharedPrefManager.isAppPurchased,
            internetManager.isInternetConnected,
            BannerType.ADAPTIVE_BANNER
        )
    }

    override fun onPause() {
        admobBanner.bannerOnPause()
        super.onPause()
    }

    override fun onResume() {
        admobBanner.bannerOnResume()
        super.onResume()
    }

    override fun onDestroy() {
        admobBanner.bannerOnDestroy()
        super.onDestroy()
    }
}