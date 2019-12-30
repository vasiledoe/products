package com.racovita.wow.features.base.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.racovita.wow.features.details.view.DetailsActivity

/**
 * Common functional for all Activities
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    protected fun setupActionBar(toolbarTitle: String, hasBackHome: Boolean) {
        supportActionBar?.apply {
            title = toolbarTitle
            if (hasBackHome) setDisplayHomeAsUpEnabled(true)
        }
    }

    /**
     * For reviewers
     *
     * Because we receive same product data from get all products request.
     * we can just pass parcelable to details activity and no need to
     * do get details request. I guess you set different request just to see
     * how I'll do it ;)
     */
    protected fun goToDetails(productId: Int) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.EXTRA_PROD_ID, productId)
        startActivityForResult(intent, CODE_RECEIVED_FAVORITE_META)
    }

    companion object {
        const val CODE_RECEIVED_FAVORITE_META = 900
        const val FAVORITE_META_EXTRA = "FAVORITE_META_EXTRA"
    }
}