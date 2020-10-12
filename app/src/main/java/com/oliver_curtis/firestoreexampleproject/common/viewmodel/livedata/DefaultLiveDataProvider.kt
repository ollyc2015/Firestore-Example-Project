package com.oliver_curtis.firestoreexampleproject.common.viewmodel.livedata

import androidx.lifecycle.MutableLiveData
import com.oliver_curtis.firestoreexampleproject.common.viewmodel.CallResult

class DefaultLiveDataProvider : LiveDataProvider {
    override fun <T> liveDataInstance(): MutableLiveData<CallResult<T>> = MutableLiveData()

}