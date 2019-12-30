package com.racovita.wow.features.produts.repo

import com.racovita.wow.data.db.AppDatabase
import com.racovita.wow.data.models.ApiProduct
import com.racovita.wow.features.base.repo.BaseRepo
import com.racovita.wow.utils.network.RestClient
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ProductsRepo(
    private val client: RestClient,
    database: AppDatabase
) : BaseRepo(database) {

    fun getProducts(offset: Int): Single<Array<ApiProduct>> =
        client.retrofit.getProductsCall(offset)
            .subscribeOn(Schedulers.io())

    fun getFavoriteIds(): List<Int> = database.productsDao().selectFavoriteProductIds()


    companion object {
        const val MAX_PAGE_ITEMS = 10
    }
}