package com.oliver_curtis.firestoreexampleproject.repo

import com.oliver_curtis.firestoreexampleproject.data.model.Note
import io.reactivex.Observable
import io.reactivex.Single

interface Repository {

    fun addNote(note: Note): Single<Boolean>
    fun fetchNotesUnordered(): Single<List<Note>>
    fun fetchNotesOrdered():  Single<List<Note>>
    fun updateNoteDescription(id: CharSequence, description: String): Single<Boolean>
    fun deleteDescription(id: CharSequence): Single<Boolean>
    fun deleteNote(id: CharSequence): Single<Boolean>
}