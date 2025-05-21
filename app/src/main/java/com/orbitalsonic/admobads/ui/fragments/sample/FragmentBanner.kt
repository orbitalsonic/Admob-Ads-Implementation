package com.orbitalsonic.admobads.ui.fragments.sample

import com.orbitalsonic.admobads.R
import com.orbitalsonic.admobads.adsconfig.banners.AdmobBanner
import com.orbitalsonic.admobads.adsconfig.banners.callbacks.BannerCallBack
import com.orbitalsonic.admobads.adsconfig.banners.enums.BannerType
import com.orbitalsonic.admobads.databinding.FragmentBannerBinding
import com.orbitalsonic.admobads.helpers.firebase.RemoteConstants
import com.orbitalsonic.admobads.helpers.observers.SingleLiveEvent
import com.orbitalsonic.admobads.ui.fragments.base.BaseFragment

/**
 * @Author: Muhammad Yaqoob
 * @Date: 14,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
class FragmentBanner : BaseFragment<FragmentBannerBinding>(R.layout.fragment_banner) {

    private val admobBanner by lazy { AdmobBanner() }
    private val adsObserver = SingleLiveEvent<Boolean>()
    private var isCollapsibleOpen = false
    private var isBackPressed = false

    override fun onViewCreatedOneTime() {
        loadAds()
    }

    override fun onViewCreatedEverytime() {
        initObserver()
    }

    private fun initObserver(){
        adsObserver.observe(this){
            if (it){
                onBack()
            }
        }
    }

    override fun navIconBackPressed() {
        onBackPressed()
    }

    override fun onBackPressed() {
        if (isAdded){
            try {
                if (!isBackPressed){
                    isBackPressed = true
                    if (isCollapsibleOpen){
                        admobBanner.bannerOnDestroy()
                        binding.adsBannerPlaceHolder.removeAllViews()
                    }else{
                        onBack()
                    }
                }
            }catch (ex:Exception){
                isBackPressed = false
            }
        }
    }

    private fun onBack(){
        popFrom(R.id.fragmentBanner)
    }

    private fun loadAds(){
        admobBanner.loadBannerAds(
            activity,
            binding.adsBannerPlaceHolder,
            getResString(R.string.admob_banner_ids),
            RemoteConstants.rcvBannerAd,
            diComponent.sharedPreferenceUtils.isAppPurchased,
            diComponent.internetManager.isInternetConnected,
            BannerType.COLLAPSIBLE_BOTTOM,
            object : BannerCallBack {
                override fun onAdClosed() {
                    isCollapsibleOpen = false

                    if (isBackPressed){
                        adsObserver.value = true
                    }
                }

                override fun onAdOpened() {
                    isCollapsibleOpen = true
                }


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