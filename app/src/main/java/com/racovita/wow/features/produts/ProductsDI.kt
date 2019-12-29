package com.racovita.wow.features.produts

import com.racovita.wow.features.produts.repo.ProductsRepo
import com.racovita.wow.features.produts.view_model.ProductsViewModel
import org.koin.android.experimental.dsl.viewModel
import org.koin.dsl.module
import org.koin.experimental.builder.single

val productsModule = module {

    viewModel<ProductsViewModel>()
    single<ProductsRepo>()
}