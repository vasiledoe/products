package com.racovita.wow.features.details.view_model

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.racovita.wow.R
import com.racovita.wow.data.models.ApiProduct
import com.racovita.wow.data.models.Product
import com.racovita.wow.features.details.repo.ProductDetailsRepo
import com.racovita.wow.utils.extensions.getPrettyErrorMessage
import com.racovita.wow.utils.extensions.safelyDispose
import com.racovita.wow.utils.extensions.toDomain
import com.racovita.wow.utils.helper.ResUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailsViewModel(
    private val repo: ProductDetailsRepo,
    private val resUtil: ResUtil
) : ViewModel() {

    /**
     * Used to publish [Product] to UI
     *
     */
    val product = MutableLiveData<Product>()

    /**
     * Used to track loading progress bar state & notify it to UI.
     */
    private val loadingState = MutableLiveData<Boolean>()

    /**
     * Used to publish to UI any API error.
     */
    val error = MutableLiveData<String>()

    /**
     * Store here all disposables and cancel them all when [DetailsViewModel] is destroyed.
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
     * Activity will receive item from existing LiveData, else initiate API request.
     */
    fun getProduct(productId: Int) {
        /**
         *  nullify error value because after screen rotation if there was any errors,
         *  UI will get the last one
         */
        error.value = null

        /**
         * Clear existing disposables
         */
        mDisposables.clear()

        /**
         * Do request on;y if data is missing
         */
        if (product.value == null) {
            tryGetData(productId)
        }
    }

    /**
     * Check if has Internet connection, if no then wait to connect and then do request.
     */
    @SuppressLint("CheckResult")
    fun tryGetData(productId: Int) {
        ReactiveNetwork.observeInternetConnectivity()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mDisposables.add(it)
                mConnectionDisposable = it
            }
            .subscribe { isConnectedToInternet ->

                if (isConnectedToInternet) {
                    getData(productId)
                    mConnectionDisposable.safelyDispose()

                } else {
                    error.value = resUtil.getStringRes(R.string.err_no_connection)
                }
            }
    }

    /**
     * Do Api request.
     */
    private fun getData(productId: Int) {
        loadingState.value = true

        mDisposables.add(
            repo.getProducts(productId)
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
     * Manage successfully server response
     */
    private fun onHandleSuccess(product: ApiProduct) {
        val item = product.toDomain()
        this.product.value = item
    }

    /**
     * Manage error server response and notify UI.
     */
    private fun onHandleError(t: Throwable) {
        error.value = t.getPrettyErrorMessage(resUtil)
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