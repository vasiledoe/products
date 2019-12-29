package com.racovita.wow.data.models

import com.google.gson.annotations.SerializedName

/**
 * For reviewers
 *
 * Usually a production service should provide some metadata about pagination,
 * so we could understand from the request if we have next page
 */


/**
 * Received from REST API
 */
data class ApiProduct(
    val id: Int,
    val title: String?,
    @SerializedName("short_description") val shortDescr: String?,
    val image: String?,
    val price: Int?,
    @SerializedName("sale_precent") val sale: Int?,
    val details: String?
)

/**
 * Used in adapter
 */
data class Product(
    val id: Int,
    val title: String,
    val shortDescr: String,
    val image: String,
    val price: String,
    val sale: String,
    val details: String,
    val favorite: Boolean
)