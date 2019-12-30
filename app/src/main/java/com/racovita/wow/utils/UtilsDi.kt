package com.racovita.wow.utils

import com.racovita.wow.utils.helper.ResUtil
import com.racovita.wow.utils.network.RestClient
import org.koin.dsl.module
import org.koin.experimental.builder.single

val utilsModule = module {
    single<ResUtil>()
    single<RestClient>()
}