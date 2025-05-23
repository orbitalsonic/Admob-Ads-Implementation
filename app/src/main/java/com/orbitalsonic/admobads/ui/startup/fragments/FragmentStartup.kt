package com.orbitalsonic.admobads.ui.startup.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.orbitalsonic.admobads.R
import com.orbitalsonic.admobads.adsconfig.interstitial.AdmobInterstitial
import com.orbitalsonic.admobads.adsconfig.interstitial.callbacks.InterstitialOnLoadCallBack
import com.orbitalsonic.admobads.adsconfig.interstitial.callbacks.InterstitialOnShowCallBack
import com.orbitalsonic.admobads.common.firebase.RemoteConfiguration
import com.orbitalsonic.admobads.common.firebase.RemoteConstants.rcvInterAd
import com.orbitalsonic.admobads.common.network.InternetManager
import com.orbitalsonic.admobads.common.preferences.SharedPrefManager
import com.orbitalsonic.admobads.databinding.FragmentStartupBinding
import com.orbitalsonic.admobads.helpers.handlers.withDelay
import com.orbitalsonic.admobads.helpers.lifecycle.launchWhenResumed
import com.orbitalsonic.admobads.helpers.utils.getResString
import com.orbitalsonic.admobads.ui.base.fragments.BaseFragment
import com.orbitalsonic.admobads.ui.startup.StartupActivity

/**
 * @Author: Muhammad Yaqoob
 * @Date: 14,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
class FragmentStartup : BaseFragment<FragmentStartupBinding>(FragmentStartupBinding::inflate) {

    private val admobInterstitial by lazy { AdmobInterstitial() }

    private val mHandler = Handler(Looper.getMainLooper())
    private val adsRunner = Runnable { checkAdvertisement() }
    private var isInterLoadOrFailed = false
    private var mCounter: Int = 0

    private var startTime = 0L

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

    private val remoteConfiguration by lazy {
        RemoteConfiguration(
            internetManager, requireActivity().getSharedPreferences(
                "firebase_preferences",
                MODE_PRIVATE
            )
        )
    }

    override fun onViewCreated() {
        fetchRemoteConfiguration()
    }

    private fun fetchRemoteConfiguration() {
        remoteConfiguration.checkRemoteConfig { fetchSuccessfully ->
            if (isAdded) {
                if (fetchSuccessfully) {
                    mCounter = 0
                    loadAds()
                } else {
                    mHandler.removeCallbacks { adsRunner }
                    moveNext(2000)
                }
            }
        }
    }

    private fun loadAds() {
        if (isAdded) {
            startTime = System.currentTimeMillis()
            when (rcvInterAd) {
                0 -> {
                    isInterLoadOrFailed = true
                }

                1 -> {
                    Log.d("AdsInformation", "Call Admob Splash Interstitial")
                    admobInterstitial.loadInterstitialAd(
                        activity,
                        getResString(R.string.admob_inter_ids),
                        rcvInterAd,
                        sharedPrefManager.isAppPurchased,
                        internetManager.isInternetConnected,
                        object : InterstitialOnLoadCallBack {
                            override fun onAdFailedToLoad(adError: String) {
                                isInterLoadOrFailed = true
                            }

                            override fun onAdLoaded() {
                                isInterLoadOrFailed = true
                                val endTime = System.currentTimeMillis()
                                val loadingTime: Int = ((endTime - startTime) / 1000).toInt()
                                Log.d("AdsInformation", "InterLoadingTime: ${loadingTime}s")
                            }

                            override fun onPreloaded() {
                                isInterLoadOrFailed = true
                            }

                        })
                }

                else -> {
                    isInterLoadOrFailed = true
                }
            }
        }
    }

    private fun checkAdvertisement() {
        if (internetManager.isInternetConnected) {
            if (mCounter < 16) {
                try {
                    mCounter++
                    if (isInterLoadOrFailed) {
                        moveNext()
                        mHandler.removeCallbacks { adsRunner }
                    } else {
                        mHandler.removeCallbacks { adsRunner }
                        mHandler.postDelayed(
                            adsRunner,
                            (1000)
                        )
                    }

                } catch (e: Exception) {
                    Log.e("checkAdvertisementTAG", "${e.message}")
                }
            } else {
                moveNext()
                mHandler.removeCallbacks { adsRunner }
            }
        } else {
            moveNext(3000)
        }

    }

    private fun moveNext(timeMilli: Long = 500) {
        withDelay(timeMilli) {
            launchWhenResumed {
                if (isAdded) {
                    (activity as StartupActivity).nextActivity()
                    admobInterstitial.showInterstitialAd(activity, object :
                        InterstitialOnShowCallBack {
                        override fun onAdDismissedFullScreenContent() {}
                        override fun onAdFailedToShowFullScreenContent() {}
                        override fun onAdShowedFullScreenContent() {}
                        override fun onAdImpression() {}

                    })
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        stopHandler()
    }

    override fun onResume() {
        super.onResume()
        resumeHandler()
    }

    private fun stopHandler() {
        mHandler.removeCallbacks(adsRunner)
    }

    private fun resumeHandler() {
        mHandler.post(adsRunner)
    }

}