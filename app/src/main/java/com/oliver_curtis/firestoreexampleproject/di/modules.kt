package com.oliver_curtis.firestoreexampleproject.di

import com.google.firebase.firestore.FirebaseFirestore
import com.oliver_curtis.firestoreexampleproject.data.db.Database
import com.oliver_curtis.firestoreexampleproject.data.db.NoteDatabase
import com.oliver_curtis.firestoreexampleproject.data.repo.FirestoreNoteRepository
import com.oliver_curtis.firestoreexampleproject.domain.interactor.NoteUseCase
import com.oliver_curtis.firestoreexampleproject.domain.interactor.NoteUseCaseImpl
import com.oliver_curtis.firestoreexampleproject.domain.repo.NoteRepository
import com.oliver_curtis.firestoreexampleproject.view.processor.NoteViewProcessor
import com.oliver_curtis.firestoreexampleproject.view.processor.impl.NoteViewProcessorImpl
import com.oliver_curtis.firestoreexampleproject.view.viewmodel.NoteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module{

    single { FirebaseFirestore.getInstance() }
    single {NoteDatabase(get()) }
    factory<NoteRepository>{ FirestoreNoteRepository(get()) }
    single<NoteUseCase> { NoteUseCaseImpl(get())  }
    viewModel { NoteViewModel(get()) }
    factory <NoteViewProcessor>{ NoteViewProcessorImpl()}
}