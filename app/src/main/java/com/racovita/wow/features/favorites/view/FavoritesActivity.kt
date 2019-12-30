package com.racovita.wow.features.favorites.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.racovita.wow.R
import com.racovita.wow.data.models.Product
import com.racovita.wow.features.base.view.BaseActivity
import com.racovita.wow.features.details.view.DetailsActivity
import com.racovita.wow.features.favorites.view_model.FavoritesViewModel
import com.racovita.wow.features.products.view.ProductsActivity
import com.racovita.wow.utils.extensions.hide
import com.racovita.wow.utils.extensions.show
import kotlinx.android.synthetic.main.content_products.*
import org.koin.android.viewmodel.ext.android.viewModel

class FavoritesActivity : BaseActivity() {

    private val mViewModel by viewModel<FavoritesViewModel>()
    private lateinit var mItemsAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        setupActionBar(resources.getString(R.string.action_favorite), true)

        setupItemsAdapter()

        onBindModel()

        mViewModel.getFavProducts()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Listen to data to update UI
     */
    private fun onBindModel() {
        mViewModel.products.observe(this, Observer { items ->
            loadItems(items)
        })
    }

    /**
     * Bind [rv_items] with [FavoritesAdapter]
     */
    private fun setupItemsAdapter() {
        val linearLayoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )

        mItemsAdapter =
            FavoritesAdapter(
                items = mutableListOf(),
                onClickItemListener = { goToDetails(it) },
                onFavoriteListener = { mViewModel.deleteDbFavorite(it.id) },
                onEmtyListListener = { showError(resources.getString(R.string.err_no_data)) })

        rv_items.layoutManager = linearLayoutManager
        rv_items.adapter = mItemsAdapter
    }

    /**
     * Add items to adapter.
     */
    private fun loadItems(products: List<Product>) {
        progress.hide()

        if (products.isNotEmpty()) {
            tv_error.hide()
            rv_items.show()

            mItemsAdapter.addItems(products)

        } else {
            showError(resources.getString(R.string.err_no_data))
        }
    }

    /**
     * Show error if something's gone wrong or no data
     */
    private fun showError(err: String?) {
        err?.let {
            progress.hide()
            rv_items.hide()
            tv_error.show()
            tv_error.text = err
        }
    }

    /**
     * Send all changed favorite statuses to [ProductsActivity] - this way is safer and more
     * optimised then sending events for each action, because user can exit app here and no need
     * to consume extra resource to update that unused screen
     */
    override fun finish() {
        val returnIntent = Intent()
        returnIntent.putExtra(FAVORITE_META_EXTRA, mViewModel.chagedFavoriteMeta)
        setResult(Activity.RESULT_OK, returnIntent)
        super.finish()
    }

    /**
     * Here we receive a [HashMap] with products that changed status from [DetailsActivity]
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CODE_RECEIVED_FAVORITE_META && resultCode == Activity.RESULT_OK) {
            data?.let {
                val meta = it.getSerializableExtra(FAVORITE_META_EXTRA) as HashMap<Int, Boolean>
                mViewModel.updateRepoDataFavState(meta)
                mItemsAdapter.updateItemsFavState(meta)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}