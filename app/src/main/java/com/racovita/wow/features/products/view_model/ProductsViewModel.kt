package com.racovita.wow.features.products.view_model

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.racovita.wow.R
import com.racovita.wow.data.models.ApiProduct
import com.racovita.wow.data.models.Product
import com.racovita.wow.features.products.repo.ProductsRepo
import com.racovita.wow.utils.extensions.getPrettyErrorMessage
import com.racovita.wow.utils.extensions.plusAssign
import com.racovita.wow.utils.extensions.safelyDispose
import com.racovita.wow.utils.extensions.toDomain
import com.racovita.wow.utils.helper.ResUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val repo: ProductsRepo,
    private val resUtil: ResUtil
) : ViewModel() {

    /**
     * Used to store all items and show them when screen orientation change.
     */
    private val productsTemp = MutableLiveData<ArrayList<Product>>()

    /**
     * Used to publish items to UI, each page or value of [productsTemp] when screen
     * orientation change.
     */
    val products = MutableLiveData<List<Product>>()

    /**
     * Used to keep pagination data simple to use.
     */
    var hasNextPage: Boolean = false
    var offset: Int = 0

    /**
     * Used to track loading progress bar state & notify it to UI.
     */
    private val loadingState = MutableLiveData<Boolean>()

    /**
     * Used to publish to UI any API error.
     */
    val error = MutableLiveData<String>()

    /**
     * Store here all disposables and cancel them all when [ProductsViewModel] is destroyed.
     */
    private var mDisposables = CompositeDisposable()

    /**
     * Disposable to keep a reference to current network connectivity check,
     * in case it's connected  just dispose it.
     */
    private var mConnectionDisposable: Disposable? = null


    /**
     * Called in owner Activity when it's started or screen orientation is changed so
     * if already have data - ignore calling API because
     * Activity will receive items from existing LiveData, else initiate API request.
     */
    fun getProducts() {
        /**
         *  nullify error value because after screen rotation if there was any errors,
         *  UI will get the last one
         */
        error.value = null

        /**
         * Clear existing disposables
         */
        mDisposables.clear()

        if (productsTemp.value != null) {
            products.value = productsTemp.value

        } else {
            tryGetData()
        }
    }

    /**
     * Check if has Internet connection, if no then wait to connect and then do request.
     */
    @SuppressLint("CheckResult")
    fun tryGetData() {
        ReactiveNetwork.observeInternetConnectivity()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mDisposables.add(it)
                mConnectionDisposable = it
            }
            .subscribe { isConnectedToInternet ->

                if (isConnectedToInternet) {
                    getData()
                    mConnectionDisposable.safelyDispose()

                } else {
                    error.value = resUtil.getStringRes(R.string.err_no_connection)
                }
            }
    }

    /**
     * Do Api request.
     */
    private fun getData() {
        if (offset == 0)
            loadingState.value = true

        mDisposables.add(
            repo.getProducts(offset)
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate {
                    loadingState.value = false
                }
                .subscribe(
                    this::onHandleSuccess,
                    this::onHandleError
                )
        )
    }

    /**
     * Manage successfully server response storing pagination data to local vars &
     * adding items to [productsTemp] to notify UI &
     * storing items to [productsTemp] for screen orientation backup.
     *
     * Detect pagination details: if array contain [ProductsRepo.MAX_PAGE_ITEMS] items
     * it means there could be pagination
     */
    private fun onHandleSuccess(productsArray: Array<ApiProduct>) {
        hasNextPage = productsArray.size == ProductsRepo.MAX_PAGE_ITEMS
        offset += ProductsRepo.MAX_PAGE_ITEMS

        viewModelScope.launch(Dispatchers.Default) {
            val favoritesProdIds = repo.getFavoriteIds()
            val items = productsArray.map {
                it.toDomain(favoritesProdIds.contains(it.id))
            }

            launch(Dispatchers.Main) {
                products.value = items
                productsTemp += items
            }
        }
    }

    /**
     * Manage error server response and notify UI.
     */
    private fun onHandleError(t: Throwable) {
        error.value = t.getPrettyErrorMessage(resUtil)
    }


    fun changeDbFavoriteStatus(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            if (product.favorite) {
                repo.addFavorite(product)

            } else {
                repo.removeFavorite(product.id)
            }
        }

        updateRepoDataFavState(hashMapOf(product.id to product.favorite))
    }

    fun updateRepoDataFavState(metaToUpdate: HashMap<Int, Boolean>) {
        for ((productId, isFavorite) in metaToUpdate) {
            productsTemp.value?.let { items ->
                items.find { it.id == productId }?.favorite = isFavorite
            }
        }
    }

    /**
     * Clear all disposables if ViewModel is cleared. It happens when Activity owner doesn't
     * need it anymore - when it's destroyed.
     *
     * */
    override fun onCleared() {
        super.onCleared()

        mDisposables.clear()
    }
}