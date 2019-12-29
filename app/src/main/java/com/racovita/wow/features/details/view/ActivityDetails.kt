package com.racovita.wow.features.details.view

import android.graphics.Paint
import android.os.Bundle
import androidx.lifecycle.Observer
import com.racovita.wow.R
import com.racovita.wow.data.models.Product
import com.racovita.wow.features.base.view.BaseActivity
import com.racovita.wow.features.details.view_model.DetailsViewModel
import com.racovita.wow.features.produts.view.ProductsAdapter
import com.racovita.wow.utils.extensions.debouncedClick
import com.racovita.wow.utils.extensions.hide
import com.racovita.wow.utils.extensions.show
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*
import org.koin.android.viewmodel.ext.android.viewModel


class ActivityDetails : BaseActivity() {

    private val mViewModel by viewModel<DetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setupViews()

        onBindModel()

        val productId = intent.getIntExtra(EXTRA_PROD_ID, 0)
        mViewModel.getProduct(productId)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupViews() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = resources.getString(R.string.details)
        }

        btn_favorite.debouncedClick {
            //todo add or remove from DB
        }
    }

    /**
     * Listen to data pushed from [ProductsAdapter]
     */
    private fun onBindModel() {
        mViewModel.error.observe(this, Observer { errorStr ->
            showError(errorStr)
        })

        mViewModel.product.observe(this, Observer { item ->
            loadData(item)
        })
    }

    private fun loadData(product: Product) {
        progress.hide()
        tv_error.hide()
        zone_details.show()

        tv_title.text = product.title
        tv_short_description.text = product.shortDescr
        tv_description.text = product.details
        tv_price.text = product.price

        tv_price_old.text = product.sale
        tv_price_old.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

        Picasso.get().load(product.image).error(R.color.divider).into(iv_thumb)

        if (product.favorite) {
            btn_favorite.setImageResource(R.drawable.ic_favorite_on)

        } else {
            btn_favorite.setImageResource(R.drawable.ic_favorite_off)
        }
    }

    /**
     * Show error if something's gone wrong.
     */
    private fun showError(err: String?) {
        err?.let {
            progress.hide()
            zone_details.hide()
            tv_error.show()
            tv_error.text = err
        }
    }


    companion object {
        const val EXTRA_PROD_ID = "EXTRA_PROD_ID"
    }
}