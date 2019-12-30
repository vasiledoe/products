package com.racovita.wow.data.db

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val storageModule = module {
    single { AppDatabase.getInstance(androidContext()) }
}