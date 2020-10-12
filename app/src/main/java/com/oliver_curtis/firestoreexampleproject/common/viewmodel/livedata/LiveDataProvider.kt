package com.oliver_curtis.firestoreexampleproject.common.viewmodel.livedata

import androidx.lifecycle.MutableLiveData
import com.oliver_curtis.firestoreexampleproject.common.viewmodel.CallResult

interface LiveDataProvider {
    fun <T> liveDataInstance(): MutableLiveData<CallResult<T>>

}

interface LiveDataObserver {

    fun <T> MutableLiveData<T>.notifyObserver()
}