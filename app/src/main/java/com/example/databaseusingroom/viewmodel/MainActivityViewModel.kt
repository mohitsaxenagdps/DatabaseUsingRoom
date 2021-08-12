package com.example.databaseusingroom.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.databaseusingroom.db.Subscriber
import com.example.databaseusingroom.db.SubscriberRepository
import com.example.databaseusingroom.util.Event
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: SubscriberRepository) : ViewModel() {

    val inputName = MutableLiveData<String?>()
    val inputEmail = MutableLiveData<String?>()
    val saveOrUpdate = MutableLiveData<String>()
    val clearAllOrDelete = MutableLiveData<String>()
    val subscriberList = repository.subscriber
    private val toastMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = toastMessage
    private var isUpdate = false
    private var isDelete = false
    private var subscriberId: Int = 0

    init {
        saveOrUpdate.value = "Save"
        clearAllOrDelete.value = "Clear All"
    }

    fun insertSubscriberOrUpdate() {

        if (inputName.value == null) {
            toastMessage.value = Event("Please enter subscriber's name")
        } else if (inputEmail.value == null) {
            toastMessage.value = Event("Please enter subscriber's email")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            toastMessage.value = Event("Please enter correct email address")
        } else {
            if (!isUpdate) {
                viewModelScope.launch {
                    val rowId = repository.insertSubscriber(
                        Subscriber(
                            0,
                            inputName.value.toString(),
                            inputEmail.value.toString()
                        )
                    )
                    if (rowId > -1) {
                        toastMessage.value = Event("Subscriber Inserted Successfully at Id $rowId")
                    } else {
                        toastMessage.value = Event("Error Occurred")
                    }
                    inputName.value = null
                    inputEmail.value = null
                }

            } else {
                viewModelScope.launch {
                    val noOfRows = repository.updateSubscriber(
                        Subscriber(
                            subscriberId,
                            inputName.value.toString(),
                            inputEmail.value.toString()
                        )
                    )
                    if (noOfRows > 0) {
                        inputName.value = null
                        inputEmail.value = null
                        isDelete = false
                        isUpdate = false
                        saveOrUpdate.value = "Save"
                        clearAllOrDelete.value = "Clear All"
                        toastMessage.value = Event("$noOfRows Subscriber Updated Successfully")
                    } else {
                        toastMessage.value = Event("Error Occurred")
                    }
                }
            }
        }
    }

    fun deleteAllOrDelete() {
        if (!isDelete) {
            viewModelScope.launch {
                val noOfRows = repository.deleteAll()
                if (noOfRows > 0) {
                    toastMessage.value = Event("$noOfRows Subscribers Deleted Successfully")
                } else {
                    toastMessage.value = Event("Error Occurred")
                }
            }
        } else {
            viewModelScope.launch {
                val noOfRows = repository.deleteSubscriber(
                    Subscriber(
                        subscriberId,
                        inputName.value.toString(),
                        inputEmail.value.toString()
                    )
                )
                if (noOfRows > 0) {
                    inputName.value = null
                    inputEmail.value = null
                    isDelete = false
                    isUpdate = false
                    saveOrUpdate.value = "Save"
                    clearAllOrDelete.value = "ClearAll"
                    toastMessage.value = Event("$noOfRows Subscriber Deleted Successfully")
                } else {
                    toastMessage.value = Event("Error Occurred")
                }
            }
        }
    }

    fun listItemClick(subscriber: Subscriber) {
        isDelete = true
        isUpdate = true
        saveOrUpdate.value = "Update"
        clearAllOrDelete.value = "Delete"
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        subscriberId = subscriber.id
    }

}