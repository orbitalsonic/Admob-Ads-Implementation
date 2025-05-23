package com.orbitalsonic.admobads.ui.base.activities

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding

/**
 * @Author: Muhammad Yaqoob
 * @Date: 14,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
abstract class BaseActivity<T : ViewBinding>(private val bindingFactory: (LayoutInflater) -> T) :
    AppCompatActivity() {

    protected val binding: T by lazy { bindingFactory(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        initSplashScreen()
        super.onCreate(savedInstanceState)

        fullScreen()
        onCreated()
    }

    /**
     * Note:
     *  -> 'statusBarHeight' is a variable to preserve non-zero value
     *  -> 'windowInsets.displayCutout' null if your device does not have any round edges.
     */

    private fun fullScreen() {
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())

            // Check if the IME (keyboard) is visible
            val isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())

            if (isKeyboardVisible) {
                // When keyboard is visible, add padding for IME
                v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    imeInsets.bottom // Adjust for keyboard
                )
            } else {
                // When keyboard is not visible, use system bars
                v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
                )
            }
            WindowInsetsCompat.CONSUMED
        }
    }

    abstract fun onCreated()
    open fun initSplashScreen() {}

}