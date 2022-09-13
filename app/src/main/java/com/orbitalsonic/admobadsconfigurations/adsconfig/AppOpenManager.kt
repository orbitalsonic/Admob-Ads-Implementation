package com.orbitalsonic.admobadsconfigurations.adsconfig

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.orbitalsonic.admobadsconfigurations.GeneralUtils.IS_AD_SHOWING
import com.orbitalsonic.admobadsconfigurations.GeneralUtils.IS_APP_PURCHASED
import com.orbitalsonic.admobadsconfigurations.GeneralUtils.IS_OPEN_APP_ACTIVE
import com.orbitalsonic.admobadsconfigurations.MainApplication
import com.orbitalsonic.admobadsconfigurations.R
import com.orbitalsonic.admobadsconfigurations.SplashActivity
import java.util.*

class AppOpenManager(private val myApplication: MainApplication) : LifecycleObserver,
    ActivityLifecycleCallbacks {
    private var mAppOpenAd: AppOpenAd? = null
    private var loadTime: Long = 0
    private var currentActivity: Activity? = null
    private var isFromSplash = false
    private val TAG = "AdsInformation"


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        try {
            if (!isFromSplash && !IS_APP_PURCHASED && IS_OPEN_APP_ACTIVE) {
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
                Log.d(TAG, "open is loaded")

                mAppOpenAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        mAppOpenAd = null
                        isShowingAd = false
//                        open_ad_showing.value = false
//                        AppClass.blackView=true
                        fetchAd()
                        if (isForCustom && appOpenListener != null) {
                            appOpenListener!!.onOpenAdClosed()
                        }
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        if (isForCustom && appOpenListener != null) {
                            appOpenListener!!.onOpenAdClosed()
                            Log.d(TAG, "open is FailedToShow")

//                            open_ad_showing.value = false
                        }
                    }

                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
//                        open_ad_showing.value = true

                    }
                }
                loadTime = Date().time
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                Log.d(TAG, "open Ad is FailedToLoad")

                mAppOpenAd = null
            }
        }


        if (IS_OPEN_APP_ACTIVE) {
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
                if (!IS_AD_SHOWING) {
                    isForCustom = false

                    if (currentActivity is SplashActivity)
                        return

                    mAppOpenAd!!.show(currentActivity!!)
                }
            } else {
                fetchAd()
            }
        } catch (ignored: Exception) {
        }
    }

    private var appOpenListener: AppOpenListener? = null
    private var isForCustom = false

    fun showAdCustomIfAvailable(openListener: AppOpenListener) {
        try {
            if (IS_APP_PURCHASED) {
                openListener.onOpenAdClosed()
                return
            }
            if (!isShowingAd && isAdAvailable) { //idhar purchase ka check lga d
                if (!IS_AD_SHOWING) {
                    isForCustom = true
                    appOpenListener = openListener
                    mAppOpenAd!!.show(currentActivity!!)
                }
            } else {
                openListener.onOpenAdClosed()
                fetchAd()
            }
        } catch (ignored: Exception) {
            openListener.onOpenAdClosed()
        }
    }

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

        Log.i(TAG,"onActivityCreated")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.i(TAG,"onActivityStarted")

        currentActivity = activity
        isFromSplash = currentActivity is SplashActivity
    }

    override fun onActivityResumed(activity: Activity) {
        Log.i(TAG,"onActivityResumed")

        currentActivity = activity
        isFromSplash = currentActivity is SplashActivity
        //        isFromCrop = currentActivity instanceof CropImageActivity;
//        isFromOCR = currentActivity instanceof OcrActivity;
    }

    override fun onActivityStopped(activity: Activity) {
        Log.i(TAG,"onActivityStopped")

        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        Log.i(TAG,"onActivityPaused")

        currentActivity = activity
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
        Log.i(TAG,"onActivitySaveInstanceState")


    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.i(TAG,"onActivityDestroyed")
        currentActivity = null
        isFromSplash = false
    }

    companion object {
        private var isShowingAd = false
    }

    init {
        this.myApplication.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }
}