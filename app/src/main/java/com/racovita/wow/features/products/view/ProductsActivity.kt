package com.racovita.wow.features.products.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.racovita.wow.R
import com.racovita.wow.data.models.Product
import com.racovita.wow.features.base.view.BaseActivity
import com.racovita.wow.features.details.view.DetailsActivity
import com.racovita.wow.features.favorites.view.FavoritesActivity
import com.racovita.wow.features.products.view_model.ProductsViewModel
import com.racovita.wow.utils.extensions.hide
import com.racovita.wow.utils.extensions.show
import kotlinx.android.synthetic.main.content_products.*
import org.koin.android.viewmodel.ext.android.viewModel


class ProductsActivity : BaseActivity() {

    private val mViewModel by viewModel<ProductsViewModel>()
    private lateinit var mItemsAdapter: ProductsAdapter
    private var isPaginationConsumed: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        setupActionBar(resources.getString(R.string.home), false)

        setupItemsAdapter()

        onBindModel()

        mViewModel.getProducts()
    }

    /**
     * Listen to data pushed to update UI
     */
    private fun onBindModel() {
        mViewModel.error.observe(this, Observer { errorStr ->
            showError(errorStr)
        })

        mViewModel.products.observe(this, Observer { items ->
            loadItems(items)
        })
    }

    /**
     * Bind [rv_items] with [ProductsAdapter]
     */
    private fun setupItemsAdapter() {
        val linearLayoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )

        mItemsAdapter =
            ProductsAdapter(
                items = mutableListOf(),
                onClickItemListener = { goToDetails(it) },
                onFavoriteListener = { mViewModel.changeDbFavoriteStatus(it) })

        rv_items.layoutManager = linearLayoutManager
        rv_items.adapter = mItemsAdapter
        setupRecyclerPagination(linearLayoutManager)
    }

    /**
     * Setup items list pagination by determining when last item is reached &
     * if have more data pages from API
     */
    private fun setupRecyclerPagination(linearLayoutManager: LinearLayoutManager) {
        rv_items.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentItems = linearLayoutManager.childCount
                val totalItems = linearLayoutManager.itemCount
                val scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition()

                if (scrollOutItems + currentItems == totalItems &&
                    !isPaginationConsumed &&
                    mViewModel.hasNextPage
                ) {
                    onPaginationState(true)
                    mViewModel.tryGetData()
                }
            }
        })
    }

    /**
     * Set if pagination can be launched or not if last item in list is reached & show/hide
     * pagination progress accordingly.
     */
    private fun onPaginationState(icConsumed: Boolean) {
        if (icConsumed) {
            isPaginationConsumed = true
            progress_pagination.show()

        } else {
            isPaginationConsumed = false
            progress_pagination.hide()
        }
    }

    /**
     * Add items to adapter.
     */
    private fun loadItems(products: List<Product>) {
        onPaginationState(false)
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
     * Show error if something's gone wrong. In case it's pagination then just show a toast
     * error message, no need to hide all items and show error message as we do for first page.
     */
    private fun showError(err: String?) {
        err?.let {
            if (mViewModel.offset == 0) {
                progress.hide()
                tv_error.show()
                rv_items.hide()
                tv_error.text = err

            } else {
                Toast.makeText(this, err, Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                startActivityForResult(
                    Intent(this, FavoritesActivity::class.java),
                    CODE_RECEIVED_FAVORITE_META
                )
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Here we receive a [HashMap] with products that changed status from [DetailsActivity] or
     * from [FavoritesActivity]
     *
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