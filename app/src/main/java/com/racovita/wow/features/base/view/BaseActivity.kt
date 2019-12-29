package com.racovita.wow.features.base.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.racovita.wow.features.details.view.ActivityDetails


/**
 * Common functional for all Activities
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    protected fun goToDetails(productId: Int) {
        val intent = Intent(this, ActivityDetails::class.java)
        intent.putExtra(ActivityDetails.EXTRA_PROD_ID, productId)
        startActivity(intent)
    }

}