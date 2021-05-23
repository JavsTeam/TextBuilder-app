package com.example.textbuilder.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CardsEntity::class],
    version = 1
)
abstract class CardsDatabase : RoomDatabase(){
    abstract fun cardsDao(): CardsDao

    companion object {
        @Volatile private var instance: CardsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            CardsDatabase::class.java, "cards-list.db").build()
    }
}