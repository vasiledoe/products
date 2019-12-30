package com.racovita.wow.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.racovita.wow.data.models.Product

const val FAVORITE_PRODUCTS: String = "fav_products"

@Dao
interface ProductDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(vararg product: Product)

    @Query("SELECT * FROM $FAVORITE_PRODUCTS")
    fun selectProducts(): List<Product>

    @Query("SELECT $FAVORITE_PRODUCTS.id FROM $FAVORITE_PRODUCTS WHERE $FAVORITE_PRODUCTS.favorite = :status")
    fun selectFavoriteProductIds(status: Boolean = true): List<Int>

    @Query("SELECT $FAVORITE_PRODUCTS.favorite FROM $FAVORITE_PRODUCTS WHERE $FAVORITE_PRODUCTS.id = :productId")
    fun isItemFavorite(productId: Int): Boolean

    @Query("DELETE FROM  $FAVORITE_PRODUCTS WHERE id = :productId")
    fun deleteProduct(productId: Int)
}