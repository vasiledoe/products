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
data class Product(
    val id: Int,
    val title: String?,
    @SerializedName("short_description") val shortDescr: String?,
    val image: String?,
    @SerializedName("sale_precent") val sale: Int?,
    val details: String?
)