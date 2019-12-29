package com.racovita.wow.features.produts.view

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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_product_item.view.*

class ProductsAdapter(
    private var items: MutableList<Product>,
    private val onClickItemListener: (Int) -> Unit
) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    /**
     * add items from pagination - so use a smart notify for the new range only
     */
    fun addItems(newItems: List<Product>) {
        val listSize = items.size
        items.addAll(newItems)
        notifyItemRangeChanged(listSize, items.size - 1)
    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_product_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.fillUpItem(items[position], onClickItemListener)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val wholeZone: MaterialCardView = view.whole_item
        private val ivThumb: ImageView = view.iv_thumb
        private val tvTitle: TextView = view.tv_title
        private val tvShortDescr: TextView = view.tv_short_description
        private val tvPrice: TextView = view.tv_price
        private val tvOldPrice: TextView = view.tv_price_old
        private val btnFav: ImageButton = view.btn_favorite

        fun fillUpItem(product: Product, listener: (Int) -> Unit) {
            tvTitle.text = product.title
            tvShortDescr.text = product.shortDescr
            tvPrice.text = product.price
            tvOldPrice.text = product.sale
            tvOldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

            Picasso.get().load(product.image).error(R.color.divider).into(ivThumb)

            wholeZone.setOnClickListener { listener(product.id) }

            if (product.favorite) {
                btnFav.setImageResource(R.drawable.ic_favorite_on)

            } else {
                btnFav.setImageResource(R.drawable.ic_favorite_off)
            }
        }
    }
}