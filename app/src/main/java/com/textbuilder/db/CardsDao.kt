package com.textbuilder.db

import androidx.room.*

@Dao
interface CardsDao {
    @Query("SELECT * FROM cards_entity")
    fun getAll(): List<CardEntity>

    @Query("SELECT * FROM cards_entity WHERE title LIKE :title")
    fun findByTitle(title: String): CardEntity

    @Query("SELECT * FROM cards_entity WHERE id LIKE :id")
    fun findById(id: Int): CardEntity

    @Insert
    fun insert(vararg todo: CardEntity)

    @Delete
    fun delete(todo: CardEntity)

    @Query ("DELETE FROM cards_entity where id NOT IN (SELECT id from cards_entity ORDER BY id DESC LIMIT 20)")
    fun deleteOverLimit()

    @Query("DELETE FROM cards_entity")
    fun nukeTable()

    @Update
    fun updateCards(vararg todos: CardEntity)
}