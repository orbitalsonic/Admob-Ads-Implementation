package com.orbitalsonic.admobads.ui.samples

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.orbitalsonic.admobads.R
import com.orbitalsonic.admobads.adsconfig.banners.AdmobBanner
import com.orbitalsonic.admobads.adsconfig.banners.callbacks.BannerCallBack
import com.orbitalsonic.admobads.adsconfig.banners.enums.BannerType
import com.orbitalsonic.admobads.common.firebase.RemoteConstants.rcvBannerAd
import com.orbitalsonic.admobads.common.network.InternetManager
import com.orbitalsonic.admobads.common.observers.SingleLiveEvent
import com.orbitalsonic.admobads.common.preferences.SharedPrefManager
import com.orbitalsonic.admobads.databinding.FragmentCollapsibleSampleBinding
import com.orbitalsonic.admobads.helpers.navigation.popFrom
import com.orbitalsonic.admobads.helpers.ui.goBackPressed
import com.orbitalsonic.admobads.helpers.utils.getResString
import com.orbitalsonic.admobads.ui.base.fragments.BaseFragment

/**
 * @Author: Muhammad Yaqoob
 * @Date: 14,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
class FragmentCollapsibleSample : BaseFragment<FragmentCollapsibleSampleBinding>(FragmentCollapsibleSampleBinding::inflate) {

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
    private val adsObserver = SingleLiveEvent<Boolean>()
    private var isCollapsibleOpen = false
    private var isBackPressed = false

    override fun onViewCreated() {
        loadAds()
        initObserver()

        goBackPressed {
            onBackPressed()
        }
    }

    private fun initObserver(){
        adsObserver.observe(viewLifecycleOwner){
            if (it){
                onBack()
            }
        }
    }

    private fun onBackPressed() {
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
        popFrom(R.id.fragmentCollapsibleSample)
    }

    private fun loadAds(){
        admobBanner.loadBannerAds(
            activity,
            binding.adsBannerPlaceHolder,
            getResString(R.string.admob_banner_ids),
            rcvBannerAd,
            sharedPrefManager.isAppPurchased,
            internetManager.isInternetConnected,
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