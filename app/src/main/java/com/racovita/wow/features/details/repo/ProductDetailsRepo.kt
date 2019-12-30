package com.racovita.wow.features.details.repo

import com.racovita.wow.data.db.AppDatabase
import com.racovita.wow.data.models.ApiProduct
import com.racovita.wow.features.base.repo.BaseRepo
import com.racovita.wow.utils.network.RestClient
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ProductDetailsRepo(
    private val client: RestClient,
    db: AppDatabase
) : BaseRepo(db) {

    fun getProductDetails(id: Int): Single<ApiProduct> =
        client.retrofit.getProductDetailsCall(id)
            .subscribeOn(Schedulers.io())

    fun isFavorite(id:Int):Boolean= database.productsDao().isItemFavorite(id)
}