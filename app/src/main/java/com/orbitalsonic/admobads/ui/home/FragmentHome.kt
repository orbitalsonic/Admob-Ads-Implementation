package com.orbitalsonic.admobads.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.orbitalsonic.admobads.R
import com.orbitalsonic.admobads.adsconfig.banners.AdmobBanner
import com.orbitalsonic.admobads.adsconfig.banners.callbacks.BannerCallBack
import com.orbitalsonic.admobads.adsconfig.banners.enums.BannerType
import com.orbitalsonic.admobads.adsconfig.natives.AdmobNative
import com.orbitalsonic.admobads.adsconfig.natives.callbacks.NativeCallBack
import com.orbitalsonic.admobads.adsconfig.natives.enums.NativeType
import com.orbitalsonic.admobads.adsconfig.rewarded.AdmobRewarded
import com.orbitalsonic.admobads.adsconfig.rewarded.callbacks.RewardedOnLoadCallBack
import com.orbitalsonic.admobads.adsconfig.rewarded.callbacks.RewardedOnShowCallBack
import com.orbitalsonic.admobads.common.firebase.RemoteConstants.rcvBannerAd
import com.orbitalsonic.admobads.common.firebase.RemoteConstants.rcvNativeAd
import com.orbitalsonic.admobads.common.firebase.RemoteConstants.rcvRewardAd
import com.orbitalsonic.admobads.common.network.InternetManager
import com.orbitalsonic.admobads.common.preferences.SharedPrefManager
import com.orbitalsonic.admobads.databinding.FragmentHomeBinding
import com.orbitalsonic.admobads.helpers.listener.RapidSafeListener.setOnRapidClickSafeListener
import com.orbitalsonic.admobads.helpers.navigation.navigateTo
import com.orbitalsonic.admobads.helpers.utils.getResString
import com.orbitalsonic.admobads.ui.MainActivity
import com.orbitalsonic.admobads.ui.base.fragments.BaseFragment

/**
 * @Author: Muhammad Yaqoob
 * @Date: 14,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
class FragmentHome : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val admobBanner by lazy { AdmobBanner() }
    private val admobNative by lazy { AdmobNative() }
    private val admobRewarded by lazy { AdmobRewarded() }

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

    override fun onViewCreated() {
        binding.mbClickSample.setOnRapidClickSafeListener {
            navigateTo(R.id.fragmentHome, R.id.action_fragmentHome_to_fragmentSample)
            (activity as MainActivity).checkCounter()
        }
        binding.mbClickBanner.setOnRapidClickSafeListener {
            navigateTo(R.id.fragmentHome, R.id.action_fragmentHome_to_fragmentBanner)
            (activity as MainActivity).checkCounter()
        }

        binding.mbClickRewarded.setOnRapidClickSafeListener {
            binding.mbClickRewarded.isEnabled = false
            loadRewardedAd()
        }

        loadAds()
    }

    fun loadRewardedAd() {
        Log.d("AdsInformation", "Call Admob Rewarded")
        admobRewarded.loadRewardedAd(
            activity,
            getString(R.string.admob_rewarded_ids),
            rcvRewardAd,
            sharedPrefManager.isAppPurchased,
            internetManager.isInternetConnected,
            object : RewardedOnLoadCallBack {
                override fun onAdFailedToLoad(adError: String) {
                    binding.mbClickRewarded.isEnabled = true
                }

                override fun onAdLoaded() {
                    showRewardedAd()
                    binding.mbClickRewarded.isEnabled = true
                }

                override fun onPreloaded() {
                    showRewardedAd()
                    binding.mbClickRewarded.isEnabled = true
                }
            }
        )
    }

    fun showRewardedAd() {
        admobRewarded.showRewardedAd(
            activity,
            object : RewardedOnShowCallBack {
                override fun onAdDismissedFullScreenContent() {}
                override fun onAdFailedToShowFullScreenContent() {}
                override fun onAdShowedFullScreenContent() {}
                override fun onUserEarnedReward() {}
            }
        )
    }

    private fun loadAds() {
        Log.d("AdsInformation", "Call Admob Banner")
        admobBanner.loadBannerAds(
            activity,
            binding.adsBannerPlaceHolder,
            getResString(R.string.admob_banner_ids),
            rcvBannerAd,
            sharedPrefManager.isAppPurchased,
            internetManager.isInternetConnected,
            BannerType.ADAPTIVE_BANNER,
            object : BannerCallBack {
                override fun onAdFailedToLoad(adError: String) {}
                override fun onAdLoaded() {}
                override fun onAdImpression() {}
            }
        )

        Log.d("AdsInformation", "Call Admob Native")
        admobNative.loadNativeAds(
            activity,
            binding.adsNativePlaceHolder,
            getResString(R.string.admob_native_ids),
            rcvNativeAd,
            sharedPrefManager.isAppPurchased,
            internetManager.isInternetConnected,
            NativeType.BANNER,
            object : NativeCallBack {
                override fun onAdFailedToLoad(adError: String) {}
                override fun onAdLoaded() {}
                override fun onAdImpression() {}
            }
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