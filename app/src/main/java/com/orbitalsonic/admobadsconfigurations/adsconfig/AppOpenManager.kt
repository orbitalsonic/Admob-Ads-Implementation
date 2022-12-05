package com.orbitalsonic.admobadsconfigurations.adsconfig

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.orbitalsonic.admobadsconfigurations.utils.GeneralUtils.AD_TAG
import com.orbitalsonic.admobadsconfigurations.utils.GeneralUtils.IS_APP_PURCHASED
import com.orbitalsonic.admobadsconfigurations.MainApplication
import com.orbitalsonic.admobadsconfigurations.R
import com.orbitalsonic.admobadsconfigurations.utils.RemoteConfigConstants.isOpenAppActive
import com.orbitalsonic.admobadsconfigurations.activities.SplashActivity
import java.util.*

class AppOpenManager(private val myApplication: MainApplication) : LifecycleObserver,
    ActivityLifecycleCallbacks {
    private var mAppOpenAd: AppOpenAd? = null
    private var currentActivity: Activity? = null
    private var loadTime: Long = 0

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        try {
            if (!IS_APP_PURCHASED && isOpenAppActive) {
                showAdIfAvailable()
            }
        } catch (ignored: Exception) {
        }
    }

    fun fetchAd() {
        // Have unused ad, no need to fetch another.
        if (isAdAvailable) {
            return
        }
        val loadCallback: AppOpenAdLoadCallback = object : AppOpenAdLoadCallback() {
            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                super.onAdLoaded(appOpenAd)
                mAppOpenAd = appOpenAd
                Log.d(AD_TAG, "open is loaded")

                mAppOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        mAppOpenAd = null
                        isShowingAd = false
                        fetchAd()
                        if (appOpenListener != null) {
                            appOpenListener?.onOpenAdClosed()
                        }
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        if (appOpenListener != null) {
                            appOpenListener?.onOpenAdClosed()
                            Log.d(AD_TAG, "open is FailedToShow")
                        }
                    }

                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                    }
                }
                loadTime = Date().time
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                Log.d(AD_TAG, "open Ad is FailedToLoad")
                mAppOpenAd = null
            }
        }

        if (!IS_APP_PURCHASED && isOpenAppActive) {
            AppOpenAd.load(
                myApplication,
                myApplication.getString(R.string.admob_open_app_ids),
                AdRequest.Builder().build(),
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                loadCallback
            )
        }
    }

    private fun showAdIfAvailable() {
        try {
            if (!isShowingAd && isAdAvailable) {
                if (currentActivity is SplashActivity || currentActivity is AdActivity)
                    return

                mAppOpenAd?.show(currentActivity!!)
            } else {
                fetchAd()
            }
        } catch (ignored: Exception) {
        }
    }

    private var appOpenListener: AppOpenListener? = null

    interface AppOpenListener {
        fun onOpenAdClosed()
    }

    private fun wasLoadTimeLessThanNHoursAgo(): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * 4.toLong()
    }

    private val isAdAvailable: Boolean
        get() = mAppOpenAd != null && wasLoadTimeLessThanNHoursAgo()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityStopped(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }

    companion object {
        private var isShowingAd = false
    }

    init {
        this.myApplication.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }
}