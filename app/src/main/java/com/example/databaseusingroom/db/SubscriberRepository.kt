package com.example.databaseusingroom.db

class SubscriberRepository(private val dao: SubscriberDao) {

    val subscriber = dao.getAllSubscriber()

    suspend fun insertSubscriber(subscriber: Subscriber): Long {
        return dao.insert(subscriber)
    }

    suspend fun updateSubscriber(subscriber: Subscriber): Int {
        return dao.update(subscriber)
    }

    suspend fun deleteSubscriber(subscriber: Subscriber): Int {
        return dao.delete(subscriber)
    }

    suspend fun deleteAll(): Int {
        return dao.deleteAll()
    }

}