package com.orbitalsonic.admobadsconfigurations.ui.fragments.home

import com.orbitalsonic.admobadsconfigurations.R
import com.orbitalsonic.admobadsconfigurations.adsconfig.AdmobBannerAds
import com.orbitalsonic.admobadsconfigurations.adsconfig.callbacks.BannerCallBack
import com.orbitalsonic.admobadsconfigurations.adsconfig.enums.CollapsiblePositionType
import com.orbitalsonic.admobadsconfigurations.adsconfig.enums.NativeType
import com.orbitalsonic.admobadsconfigurations.databinding.FragmentHomeBinding
import com.orbitalsonic.admobadsconfigurations.helpers.firebase.RemoteConstants
import com.orbitalsonic.admobadsconfigurations.helpers.listeners.DebounceListener.setDebounceClickListener
import com.orbitalsonic.admobadsconfigurations.ui.activities.MainActivity
import com.orbitalsonic.admobadsconfigurations.ui.fragments.base.BaseFragment

class FragmentHome : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    /**
     * Don't use AdmobBannerAds in DI
     */
    private val admobBannerAds by lazy { AdmobBannerAds() }

    override fun onViewCreatedOneTime() {
        binding.mbClickHome.setDebounceClickListener {
            navigateTo(R.id.fragmentHome, R.id.action_fragmentHome_to_fragmentSample)
            (activity as MainActivity).checkCounter()
        }
        binding.mbClickBanner.setDebounceClickListener {
            navigateTo(R.id.fragmentHome, R.id.action_fragmentHome_to_fragmentBanner)
            (activity as MainActivity).checkCounter()
        }

        loadAds()
    }

    override fun onViewCreatedEverytime() {}

    override fun navIconBackPressed() {
        onBackPressed()
    }

    override fun onBackPressed() {

    }

    private fun loadAds() {
        admobBannerAds.loadBannerAds(
            activity,
            binding.adsBannerPlaceHolder,
            getResString(R.string.admob_banner_home_ids),
            RemoteConstants.rcvBannerHome,
            diComponent.sharedPreferenceUtils.isAppPurchased,
            diComponent.internetManager.isInternetConnected,
            CollapsiblePositionType.none,
            object : BannerCallBack {
                override fun onAdFailedToLoad(adError: String) {}
                override fun onAdLoaded() {}
                override fun onAdImpression() {}
                override fun onPreloaded() {}
                override fun onAdClicked() {}
                override fun onAdClosed() {}
                override fun onAdOpened() {}
                override fun onAdSwipeGestureClicked() {}
            }
        )

        diComponent.admobNativeAds.loadNativeAds(
            activity,
            binding.adsNativePlaceHolder,
            getResString(R.string.admob_native_home_ids),
            RemoteConstants.rcvNativeHome,
            diComponent.sharedPreferenceUtils.isAppPurchased,
            diComponent.internetManager.isInternetConnected,
            NativeType.SMALL,
            object : BannerCallBack {
                override fun onAdFailedToLoad(adError: String) {}
                override fun onAdLoaded() {}
                override fun onAdImpression() {}
                override fun onPreloaded() {}
                override fun onAdClicked() {}
                override fun onAdClosed() {}
                override fun onAdOpened() {}
                override fun onAdSwipeGestureClicked() {}
            }
        )
    }

    override fun onPause() {
        admobBannerAds.bannerOnPause()
        super.onPause()
    }

    override fun onResume() {
        admobBannerAds.bannerOnResume()
        super.onResume()
    }

    override fun onDestroy() {
        admobBannerAds.bannerOnDestroy()
        super.onDestroy()
    }


}