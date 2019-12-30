package com.racovita.wow.features.favorites

import com.racovita.wow.features.favorites.repo.FavoritesRepo
import com.racovita.wow.features.favorites.view_model.FavoritesViewModel
import org.koin.android.experimental.dsl.viewModel
import org.koin.dsl.module
import org.koin.experimental.builder.single

val favoriteModule = module {

    viewModel<FavoritesViewModel>()
    single<FavoritesRepo>()
}