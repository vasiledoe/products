package com.racovita.wow.features.details.repo

import com.racovita.wow.data.models.ApiProduct
import com.racovita.wow.utils.network.RestClient
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ProductDetailsRepo(private val client: RestClient) {

    fun getProducts(id: Int): Single<ApiProduct> =
        client.retrofit.getProductDetailsCall(id)
            .subscribeOn(Schedulers.io())

}