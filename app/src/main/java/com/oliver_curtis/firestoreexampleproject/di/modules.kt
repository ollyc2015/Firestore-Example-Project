package com.oliver_curtis.firestoreexampleproject.di

import com.google.firebase.firestore.FirebaseFirestore
import com.oliver_curtis.firestoreexampleproject.data.db.Database
import com.oliver_curtis.firestoreexampleproject.data.db.NoteDatabase
import com.oliver_curtis.firestoreexampleproject.repo.FirestoreRepository
import com.oliver_curtis.firestoreexampleproject.repo.Repository
import com.oliver_curtis.firestoreexampleproject.view.processor.NoteViewProcessor
import com.oliver_curtis.firestoreexampleproject.view.processor.impl.NoteViewProcessorImpl
import com.oliver_curtis.firestoreexampleproject.view.viewmodel.NoteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module{

    single { FirebaseFirestore.getInstance() }
    factory<Repository>{FirestoreRepository(get())}
    single {NoteDatabase(get())  }
    viewModel { NoteViewModel(get()) }
    factory <NoteViewProcessor>{ NoteViewProcessorImpl()}
}