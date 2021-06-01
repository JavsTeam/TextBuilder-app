package com.textbuilder.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CardEntity::class],
    version = 1
)
abstract class FavoriteCardsDatabase : RoomDatabase(), CardsOperational {
    abstract override fun cardsDao(): CardsDao

    companion object {
        @Volatile private var instance: FavoriteCardsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            FavoriteCardsDatabase::class.java, "fav-cards-list.db").build()
    }
}