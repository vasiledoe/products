package com.racovita.wow.features.favorites.repo

import com.racovita.wow.data.db.AppDatabase
import com.racovita.wow.data.models.Product
import com.racovita.wow.features.base.repo.BaseRepo

class FavoritesRepo(
    val db: AppDatabase
) : BaseRepo(db) {

    fun getFavoriteProducts(): List<Product> =
        database.productsDao().selectProducts()
}