package com.oliver_curtis.firestoreexampleproject.common.processor

interface ViewProcessor<V> {

    fun attachView(view: V)

    fun detachView()
}