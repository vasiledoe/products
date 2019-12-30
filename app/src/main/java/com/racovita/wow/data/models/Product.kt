package com.racovita.wow.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.racovita.wow.data.db.FAVORITE_PRODUCTS

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