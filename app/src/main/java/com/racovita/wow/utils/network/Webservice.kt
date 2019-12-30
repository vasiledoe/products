package com.racovita.wow.utils.network

import com.racovita.wow.data.models.ApiProduct
import com.racovita.wow.features.products.repo.ProductsRepo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * For reviewers
 *
 * We can use coroutines instead of Rx, so we could set functions with
 * [suspend] directly (allowed with retrofit 2.6+ )
 */
interface Webservice {

    @GET("products")
    fun getProductsCall(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = ProductsRepo.MAX_PAGE_ITEMS
    ): Single<Array<ApiProduct>>

    @GET("product")
    fun getProductDetailsCall(
        @Query("id") id: Int
    ): Single<ApiProduct>
}