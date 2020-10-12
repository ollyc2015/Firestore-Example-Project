package com.oliver_curtis.firestoreexampleproject.data.db

import com.oliver_curtis.firestoreexampleproject.data.model.Note
import io.reactivex.Single

interface Database {

    fun addNote(note: Note): Single<Boolean>
    fun updateNoteDescription(id: CharSequence, description: String): Single<Boolean>
    fun deleteNoteDescription(id: CharSequence): Single<Boolean>
    fun deleteNote(id: CharSequence): Single<Boolean>
    fun getNotes(): Single<List<Note>>
}