package com.example.textbuilder.db

import androidx.room.*

@Dao
interface CardsDao {
    @Query("SELECT * FROM cards_entity")
    fun getAll(): List<CardsEntity>

    @Query("SELECT * FROM cards_entity WHERE title LIKE :title")
    fun findByTitle(title: String): CardsEntity

    @Query("SELECT * FROM cards_entity WHERE id LIKE :id")
    fun findById(id: Int): CardsEntity

    @Insert
    fun insert(vararg todo: CardsEntity)

    @Delete
    fun delete(todo: CardsEntity)

    @Query ("DELETE FROM cards_entity where id NOT IN (SELECT id from cards_entity ORDER BY id DESC LIMIT 20)")
    fun deleteOverLimit()

    @Query("DELETE FROM cards_entity")
    fun nukeTable()

    @Update
    fun updateCards(vararg todos: CardsEntity)
}