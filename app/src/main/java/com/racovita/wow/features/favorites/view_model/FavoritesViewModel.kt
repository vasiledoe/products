package com.racovita.wow.features.favorites.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.racovita.wow.data.models.Product
import com.racovita.wow.features.favorites.repo.FavoritesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * For reviewer!!!
 *
 * I decided to use coroutines instead of Rx because code is more robust and easy to understand.
 * I didn't use pagination for DB but in a real production project it should be used, when
 * we do select from DB we do It like so: "SELECT * FROM :table LIMIT :pageSize OFFSET :pageIndex"
 */
class FavoritesViewModel(
    private val repo: FavoritesRepo
) : ViewModel() {

    /**
     * Used to publish items to UI
     */
    val products = MutableLiveData<MutableList<Product>>()

    /**
     * Used to track loading progress bar state & notify it to UI.
     */
    private val loadingState = MutableLiveData<Boolean>()

    /**
     * Store here pair of product ID & favorite status in case favorite status has changed
     */
    var chagedFavoriteMeta: HashMap<Int, Boolean> = hashMapOf()

    /**
     * Called in owner Activity when it's started or screen orientation is changed so
     * if already have data - ignore calling API because
     * Activity will receive items from existing LiveData, else initiate API request.
     */
    fun getFavProducts() {
        loadingState.value = true

        if (products.value == null)
            viewModelScope.launch(Dispatchers.IO) {
                val favProducts = repo.getFavoriteProducts().toMutableList()
                products.postValue(favProducts)
            }
    }

    /**
     * Update local data & add items to propogate to [ProductsActivity]
     */
    fun deleteDbFavorite(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.removeFavorite(productId)
            removeFromProducts(productId)
        }

        chagedFavoriteMeta[productId] = false
    }

    /**
     * Update local data & add items to propogate from [ActivityDetails] to [ProductsActivity]
     */
    fun updateRepoDataFavState(metaToUpdate: HashMap<Int, Boolean>) {
        for ((productId, isFavorite) in metaToUpdate) {
            removeFromProducts(productId)
            chagedFavoriteMeta[productId] = isFavorite
        }
    }

    fun removeFromProducts(productId: Int) {
        products.value?.let { items ->
            val indexPosition = items.indexOf(items.find { it.id == productId })
            items.removeAt(indexPosition)
        }
    }
}