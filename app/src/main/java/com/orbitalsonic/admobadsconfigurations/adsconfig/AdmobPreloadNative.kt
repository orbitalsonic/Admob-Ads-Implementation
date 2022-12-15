package com.orbitalsonic.admobadsconfigurations.adsconfig

import android.annotation.SuppressLint
import android.app.Activity
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.orbitalsonic.admobadsconfigurations.R
import com.orbitalsonic.admobadsconfigurations.adsconfig.callbacks.BannerCallBack
import com.orbitalsonic.admobadsconfigurations.utils.GeneralUtils.AD_TAG


class AdmobPreloadNative(private val mActivity: Activity) {

    companion object {
        var adMobNativeAd: NativeAd? = null
    }

    private var adLoader: AdLoader? = null

    /**
     * 1 = Small Native
     * 2 = Large Native
     * 3 = Small Native (replacement of Large Native in small screen)
     */
    @SuppressLint("LogNotTimber")
    fun loadNativeAds(
        admobNativeIds: String,
        isAdActive: Boolean,
        isAppPurchased: Boolean,
        isInternetConnected: Boolean,
        bannerCallBack: BannerCallBack
    ) {
        if (!isAppPurchased && isAdActive && isInternetConnected) {
            val builder: AdLoader.Builder = AdLoader.Builder(mActivity, admobNativeIds)
            adLoader =
                builder.forNativeAd { unifiedNativeAd: NativeAd? ->
                    adMobNativeAd = unifiedNativeAd
                }
                    .withAdListener(object : AdListener() {

                        override fun onAdImpression() {
                            super.onAdImpression()
                            Log.d(AD_TAG, "admob native onAdImpression")
                            bannerCallBack.onAdImpression()

                            adMobNativeAd = null
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            Log.e(AD_TAG, "admob native onAdFailedToLoad: " + loadAdError.message)
                            bannerCallBack.onAdFailedToLoad(loadAdError.message)
                            adMobNativeAd = null
                            super.onAdFailedToLoad(loadAdError)
                        }

                        override fun onAdLoaded() {
                            super.onAdLoaded()
                            Log.d(AD_TAG, "admob native onAdLoaded")
                            bannerCallBack.onAdLoaded()
                        }

                    }).withNativeAdOptions(
                        com.google.android.gms.ads.nativead.NativeAdOptions.Builder()
                            .setAdChoicesPlacement(
                                NativeAdOptions.ADCHOICES_TOP_RIGHT
                            ).build()
                    )
                    .build()
            adLoader?.loadAd(AdRequest.Builder().build())
        } else {
            Log.e(
                AD_TAG,
                "Internet not Connected or App is Purchased or ad is not active from Firebase"
            )
            bannerCallBack.onAdFailedToLoad("Internet not Connected or App is Purchased or ad is not active from Firebase")

        }

    }

    fun showNativeAds(
        adsPlaceHolder: FrameLayout,
        nativeNo: Int,
    ) {
        adMobNativeAd?.let {
            adsPlaceHolder.visibility = View.VISIBLE
            populateUnifiedNativeAdView(adsPlaceHolder, nativeNo)
        } ?: kotlin.run {
            adsPlaceHolder.visibility = View.GONE
        }
    }

    fun makeNativeNull(){
        adMobNativeAd = null
    }

    private fun populateUnifiedNativeAdView(
        adMobNativeContainer: FrameLayout,
        nativeNo: Int
    ) {

        adMobNativeAd?.let { ad ->
            val inflater = LayoutInflater.from(mActivity)
            val adView: NativeAdView = if (nativeNo == 1) {
                inflater.inflate(R.layout.admob_native_small, null) as NativeAdView
            } else if (nativeNo == 2) {
                inflater.inflate(R.layout.admob_native_medium, null) as NativeAdView
            } else if (nativeNo == 3) {
                if (isSupportFullScreen(mActivity)) {
                    inflater.inflate(R.layout.admob_native_medium, null) as NativeAdView
                } else {
                    inflater.inflate(R.layout.admob_native_small2, null) as NativeAdView
                }

            } else {
                inflater.inflate(R.layout.admob_native_medium, null) as NativeAdView
            }
            adMobNativeContainer.removeAllViews()
            adMobNativeContainer.addView(adView)

            if (nativeNo == 2) {
                val mediaView: MediaView = adView.findViewById(R.id.media_view)
                adView.mediaView = mediaView
            }
            if (nativeNo == 3) {
                if (isSupportFullScreen(mActivity)) {
                    val mediaView: MediaView = adView.findViewById(R.id.media_view)
                    adView.mediaView = mediaView
                }
            }

            // Set other ad assets.
            adView.headlineView = adView.findViewById(R.id.ad_headline)
            adView.bodyView = adView.findViewById(R.id.ad_body)
            adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
            adView.iconView = adView.findViewById(R.id.ad_app_icon)

            //Headline
            adView.headlineView?.let { headline ->
                (headline as TextView).text = ad.headline
                headline.isSelected = true
            }

            //Body
            adView.bodyView?.let { bodyView ->
                if (ad.body == null) {
                    bodyView.visibility = View.INVISIBLE
                } else {
                    bodyView.visibility = View.VISIBLE
                    (bodyView as TextView).text = ad.body
                }

            }

            //Call to Action
            adView.callToActionView?.let { ctaView ->
                if (ad.callToAction == null) {
                    ctaView.visibility = View.INVISIBLE
                } else {
                    ctaView.visibility = View.VISIBLE
                    (ctaView as Button).text = ad.callToAction
                }

            }

            //Icon
            adView.iconView?.let { iconView ->
                if (ad.icon == null) {
                    iconView.visibility = View.GONE
                } else {
                    (iconView as ImageView).setImageDrawable(ad.icon?.drawable)
                    iconView.visibility = View.VISIBLE
                }

            }

            adView.advertiserView?.let { adverView ->

                if (ad.advertiser == null) {
                    adverView.visibility = View.GONE
                } else {
                    (adverView as TextView).text = ad.advertiser
                    adverView.visibility = View.GONE
                }
            }

            adView.setNativeAd(ad)
        }
    }

    fun isSupportFullScreen(activity: Activity): Boolean {
        try {
            val outMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(outMetrics)
            if (outMetrics.heightPixels > 1280) {
                return true
            }
        } catch (ignored: Exception) {
        }
        return false
    }

}