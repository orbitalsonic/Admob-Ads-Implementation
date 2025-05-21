package com.orbitalsonic.admobads.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.orbitalsonic.admobads.R
import com.orbitalsonic.admobads.databinding.ActivitySplashBinding

/**
 * @Author: Muhammad Yaqoob
 * @Date: 14,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkCaseType()
    }

    private fun checkCaseType() {
        navigateScreen()
    }

    fun nextActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun navigateScreen() {
        val navController = (supportFragmentManager.findFragmentById(binding.fcvContainerSplash.id) as NavHostFragment).navController
        val navInflater = navController.navInflater
        val navGraph = navInflater.inflate(R.navigation.nav_graph_splash)
        if (diComponent.sharedPreferenceUtils.showFirstScreen) {
            navGraph.setStartDestination(R.id.fragmentSplashStart)
        }else{
            navGraph.setStartDestination(R.id.fragmentSplash)
        }
        navController.graph = navGraph
    }
}