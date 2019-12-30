package com.racovita.wow.features.favorites.view

import com.racovita.wow.data.models.Product
import com.racovita.wow.features.base.view.BaseProductsAdapter

class FavoritesAdapter(
    val items: MutableList<Product>,
    onClickItemListener: (Int) -> Unit,
    onFavoriteListener: (Product) -> Unit,
    private val onEmtyListListener: () -> Unit
) : BaseProductsAdapter(items, onClickItemListener, onFavoriteListener) {

    override fun onChangeFavoriteState(
        position: Int,
        product: Product,
        favoriteListener: (Product) -> Unit
    ) {
        items.removeAt(position)
        notifyItemRemoved(position)
        favoriteListener(product)

        if (itemCount == 0)
            onEmtyListListener()
    }

    override fun updateItemsFavState(metaToUpdate: HashMap<Int, Boolean>) {
        for ((productId, _) in metaToUpdate) {
            val indexPosition = items.indexOf(items.find { it.id == productId })
            items.removeAt(indexPosition)
            notifyItemRemoved(indexPosition)
        }

        if (itemCount == 0)
            onEmtyListListener()
    }
}