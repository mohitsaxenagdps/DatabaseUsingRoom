package com.example.databaseusingroom.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriberDao {

    @Insert
    suspend fun insert(subscriber: Subscriber): Long

    @Update
    suspend fun update(subscriber: Subscriber): Int

    @Delete
    suspend fun delete(subscriber: Subscriber): Int

    @Query("DELETE FROM subscriber_table")
    suspend fun deleteAll(): Int

    @Query("Select * From subscriber_table")
    fun getAllSubscriber(): LiveData<List<Subscriber>>

}