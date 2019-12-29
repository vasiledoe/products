package com.racovita.wow.utils.network

import com.racovita.wow.data.models.Product
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
    fun getProducts(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Single<Array<Product>>

    @GET("product")
    fun getProductDetails(
        @Query("id") id: Int
    ): Single<Array<Product>>
}