package com.racovita.wow.utils.extensions

import com.racovita.wow.data.models.ApiProduct
import com.racovita.wow.data.models.Product

/**
 * Format object data as is required to consume it in app.
 */
fun ApiProduct.toDomain(): Product {
    return Product(
        id = id,
        title = title ?: "",
        shortDescr = shortDescr ?: "",
        image = image.getSafeImgUrl(),
        price = "$".plus(" ").plus(price.getSafeValue().toString()),
        sale = getOldPrice(price, sale),
        details = details ?: "",
        favorite = false
    )
}


fun getOldPrice(curPrice: Int?, sale: Int?): String {
    val ampuntFromPercents = curPrice.getSafeValue() * sale.getSafeValue() / 100
    val priceNoDiscount = curPrice.getSafeValue() + ampuntFromPercents
    return "$".plus(" ").plus(priceNoDiscount.toString())
}

fun Int?.getSafeValue() = this ?: 0

fun String?.getSafeImgUrl(): String {
    return if (this.isNullOrEmpty()) {
        "unknown_url"
    } else {
        this
    }
}