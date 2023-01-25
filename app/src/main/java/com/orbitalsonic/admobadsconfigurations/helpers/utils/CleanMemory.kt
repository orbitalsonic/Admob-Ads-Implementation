package com.orbitalsonic.admobadsconfigurations.helpers.utils

import com.orbitalsonic.admobadsconfigurations.adsconfig.constants.AdsConstants
import com.orbitalsonic.admobadsconfigurations.helpers.firebase.RemoteConstants

object CleanMemory {

    fun clean() {
        RemoteConstants.reset()
        AdsConstants.reset()
    }

}