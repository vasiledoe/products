package com.racovita.wow.features.details

import com.racovita.wow.features.details.repo.ProductDetailsRepo
import com.racovita.wow.features.details.view_model.DetailsViewModel
import org.koin.android.experimental.dsl.viewModel
import org.koin.dsl.module
import org.koin.experimental.builder.single


val detailsModule = module {
    viewModel<DetailsViewModel>()
    single<ProductDetailsRepo>()
}