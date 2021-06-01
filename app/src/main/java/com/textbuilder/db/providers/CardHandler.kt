package com.textbuilder.db.providers

import com.textbuilder.db.CardEntity
import com.textbuilder.db.CardsOperational
import com.textbuilder.ui.display.recyclerview.Card
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CardHandler(private val database: CardsOperational) {
    fun addCard(card: CardEntity) {
        GlobalScope.launch {
            database.cardsDao().insert(card)
        }
        Thread.sleep(50)
    }

    fun deleteCard(card: CardEntity) {
        GlobalScope.launch {
            database.cardsDao().delete(card)
        }
    }

    fun deleteOverLimit() {
        GlobalScope.launch {
            database.cardsDao().deleteOverLimit()
        }
    }

    fun getAllCards(): ArrayList<Card> {
        val data = ArrayList<Card>()
        GlobalScope.launch {
            val dataFromDB = database.cardsDao().getAll()
            dataFromDB.forEach {
                data.add(
                    Card(
                        it.id,
                        it.title,
                        it.content,
                        it.isFavorite
                    )
                )
            }
        }
        Thread.sleep(50)
        return data
    }

    fun getCard(cardId: Int): CardEntity {
        var entity = CardEntity(-1, "temp", "", false)
        GlobalScope.launch {
            entity = database.cardsDao().findById(cardId)
        }
        Thread.sleep(50)
        return entity
    }

    fun updateCardData(updatedCardEntity: CardEntity) {
        GlobalScope.launch {
            database.cardsDao().updateCards(updatedCardEntity)
        }
    }

    fun updateCardData(updatedCardId: Int) { // iwueyriweryiuwr!!!!
        updateCardData(database.cardsDao().findById(updatedCardId))
    }

    fun changeFavoriteStatus(cardId: Int) {
        val card = getCard(cardId)
        if(card != null) { // card doesnt exist
            card.isFavorite = !card.isFavorite
            updateCardData(card)
        }
    }
}