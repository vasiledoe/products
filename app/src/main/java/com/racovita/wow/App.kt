package com.racovita.wow

import android.app.Application
import com.racovita.wow.data.db.storageModule
import com.racovita.wow.features.details.detailsModule
import com.racovita.wow.features.favorites.favoriteModule
import com.racovita.wow.features.products.productsModule
import com.racovita.wow.utils.utilsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            // use Koin logger
            printLogger(Level.DEBUG)

            // Android context
            androidContext(this@App)

            // modules
            modules(utilsModule + productsModule + detailsModule + favoriteModule + storageModule)
        }
    }
}