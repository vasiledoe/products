package com.racovita.wow.features.base.view

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.racovita.wow.R
import com.racovita.wow.data.models.Product
import com.racovita.wow.utils.extensions.debouncedClick
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_product_item.view.*

/**
 * Base functional for products adapter common for both products and favorites,
 * the only difference is behavior for changing favorite state for each child adapter
 */
abstract class BaseProductsAdapter(
    private var items: MutableList<Product>,
    private val onClickItemListener: (Int) -> Unit,
    private val onFavoriteListener: (Product) -> Unit
) : RecyclerView.Adapter<BaseProductsAdapter.ProductViewHolder>() {

    /**
     * add items from pagination - so use a smart notify for the new range only
     */
    fun addItems(newItems: List<Product>) {
        val listSize = items.size
        items.addAll(newItems)
        notifyItemRangeChanged(listSize, newItems.size)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_product_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holderProduct: ProductViewHolder, position: Int) {
        holderProduct.fillUpItem(
            product = items[position],
            clickedListener = onClickItemListener,
            favoriteListener = onFavoriteListener,
            changeFavStateFun = this::onChangeFavoriteState
        )
    }

    abstract fun onChangeFavoriteState(
        position: Int,
        product: Product,
        favoriteListener: (Product) -> Unit
    )

    abstract fun updateItemsFavState(metaToUpdate: HashMap<Int, Boolean>)


    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val wholeZone: MaterialCardView = view.whole_item
        private val ivThumb: ImageView = view.iv_thumb
        private val tvTitle: TextView = view.tv_title
        private val tvShortDescr: TextView = view.tv_short_description
        private val tvPrice: TextView = view.tv_price
        private val tvOldPrice: TextView = view.tv_price_old
        private val btnFav: ImageButton = view.btn_favorite

        fun fillUpItem(
            product: Product,
            clickedListener: (Int) -> Unit,
            favoriteListener: (Product) -> Unit,
            changeFavStateFun: (
                position: Int,
                product: Product,
                favoriteListener: (Product) -> Unit
            ) -> Unit
        ) {
            tvTitle.text = product.title
            tvShortDescr.text = product.shortDescr
            tvPrice.text = product.price
            tvOldPrice.text = product.sale
            tvOldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

            Picasso.get().load(product.image).error(R.color.divider).into(ivThumb)

            wholeZone.debouncedClick { clickedListener(product.id) }

            btnFav.debouncedClick { changeFavStateFun(adapterPosition, product, favoriteListener) }

            if (product.favorite) {
                btnFav.setImageResource(R.drawable.ic_favorite_on)

            } else {
                btnFav.setImageResource(R.drawable.ic_favorite_off)
            }
        }
    }
}