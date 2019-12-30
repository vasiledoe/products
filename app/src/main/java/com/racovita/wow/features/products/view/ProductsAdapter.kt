package com.racovita.wow.features.products.view

import com.racovita.wow.data.models.Product
import com.racovita.wow.features.base.view.BaseProductsAdapter

class ProductsAdapter(
    val items: MutableList<Product>,
    onClickItemListener: (Int) -> Unit,
    onFavoriteListener: (Product) -> Unit

) : BaseProductsAdapter(items, onClickItemListener, onFavoriteListener) {

    override fun onChangeFavoriteState(
        position: Int,
        product: Product,
        favoriteListener: (Product) -> Unit
    ) {
        product.favorite = !product.favorite
        notifyItemChanged(position)

        favoriteListener(product)
    }

    override fun updateItemsFavState(metaToUpdate: HashMap<Int, Boolean>) {
        for ((productId, isFavorite) in metaToUpdate) {
            val product = items.find { it.id == productId }
            product?.let {
                it.favorite = isFavorite
                notifyItemChanged(items.indexOf(it))
            }
        }
    }

}