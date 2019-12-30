package com.racovita.wow.data.models

import androidx.room.*

/**
 * Used in adapter & DB
 */

@Entity(tableName = FAVORITE_PRODUCTS)
data class Product(
    @PrimaryKey val id: Int,
    val title: String,
    val shortDescr: String,
    val image: String,
    val price: String,
    val sale: String,
    val details: String,
    var favorite: Boolean
)