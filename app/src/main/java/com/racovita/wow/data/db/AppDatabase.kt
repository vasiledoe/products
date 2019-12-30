package com.racovita.wow.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.racovita.wow.data.models.Product

const val DB_NAME = "fav_db"
const val DB_VERSION = 1

@Database(
    entities = [Product::class],
    exportSchema = false,
    version = DB_VERSION
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}