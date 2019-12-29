package com.racovita.wow.features.splash.view

import android.content.Intent
import android.os.Bundle
import com.racovita.wow.MainActivity
import com.racovita.wow.features.base.view.BaseActivity

/**
 * Simple splash screen background & icon is shown from <style>. Don't use any XML layout
 * because it will take some time to render and user could see white screen for the first
 * run, especially on low memory devices or when app is opened for the first time
 * or was not opened for a long time.
 */
class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        goToNextActivity()
    }

    /**
     * Go to home screen and terminate this activity, so when user will press back button
     * from [MainActivity], this <SplashActivity> will not be shown because is not in
     * back stack anymore
     */
    private fun goToNextActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}