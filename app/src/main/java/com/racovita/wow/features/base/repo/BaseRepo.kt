package com.racovita.wow.features.base.repo

import com.racovita.wow.data.db.AppDatabase
import com.racovita.wow.data.models.Product

/**
 * Common functional for all repos
 */
open class BaseRepo(
    val database: AppDatabase
) {

    fun addFavorite(product: Product) {
        database.productsDao().insertProduct(product)
    }

    fun removeFavorite(productId: Int) {
        database.productsDao().deleteProduct(productId)
    }
}