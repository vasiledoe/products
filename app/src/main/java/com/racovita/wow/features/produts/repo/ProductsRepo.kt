package com.racovita.wow.features.produts.repo

import com.racovita.wow.data.models.ApiProduct
import com.racovita.wow.utils.network.RestClient
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ProductsRepo(private val client: RestClient) {

    fun getProducts(offset: Int): Single<Array<ApiProduct>> =
        client.retrofit.getProductsCall(offset)
            .subscribeOn(Schedulers.io())

    companion object {
        const val MAX_PAGE_ITEMS = 10
    }
}