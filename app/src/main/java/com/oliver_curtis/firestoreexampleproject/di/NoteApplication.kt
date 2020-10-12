package com.oliver_curtis.firestoreexampleproject.di

import android.app.Application

class NoteApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startDependencyInjectionFramework(this)
    }
}