package com.example.textbuilder.db

import com.example.textbuilder.db.CardsDao

interface CardsOperational {
    fun cardsDao(): CardsDao
}