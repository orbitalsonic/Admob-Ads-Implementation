package com.orbitalsonic.admobads.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.orbitalsonic.admobads.R
import com.orbitalsonic.admobads.adsconfig.rewarded.AdmobRewarded
import com.orbitalsonic.admobads.adsconfig.rewarded.callbacks.RewardedOnLoadCallBack
import com.orbitalsonic.admobads.adsconfig.rewarded.callbacks.RewardedOnShowCallBack
import com.orbitalsonic.admobads.common.firebase.RemoteConstants.rcvRewardAd
import com.orbitalsonic.admobads.common.network.InternetManager
import com.orbitalsonic.admobads.common.preferences.SharedPrefManager
import com.orbitalsonic.admobads.databinding.FragmentHomeBinding
import com.orbitalsonic.admobads.helpers.listener.RapidSafeListener.setOnRapidClickSafeListener
import com.orbitalsonic.admobads.helpers.navigation.navigateTo
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
        binding.btnAdaptiveBanner.setOnRapidClickSafeListener {
            navigateTo(R.id.fragmentHome, R.id.action_fragmentHome_to_fragmentBannerSample)
        }
        binding.btnCollapsibleBanner.setOnRapidClickSafeListener {
            navigateTo(R.id.fragmentHome, R.id.action_fragmentHome_to_fragmentCollapsibleSample)
        }
        binding.btnSmallNative.setOnRapidClickSafeListener {
            navigateTo(R.id.fragmentHome, R.id.action_fragmentHome_to_fragmentNativeSmallSample)
        }
        binding.btnLargeNative.setOnRapidClickSafeListener {
            navigateTo(R.id.fragmentHome, R.id.action_fragmentHome_to_fragmentNativeLargeSample)
        }
        binding.btnInterstitial.setOnRapidClickSafeListener {
            (activity as MainActivity).checkCounter()
        }

        binding.btnRewarded.setOnRapidClickSafeListener {
            binding.btnRewarded.isEnabled = false
            loadRewardedAd()
        }
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
                    binding.btnRewarded.isEnabled = true
                }

                override fun onAdLoaded() {
                    showRewardedAd()
                    binding.btnRewarded.isEnabled = true
                }

                override fun onPreloaded() {
                    showRewardedAd()
                    binding.btnRewarded.isEnabled = true
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

}