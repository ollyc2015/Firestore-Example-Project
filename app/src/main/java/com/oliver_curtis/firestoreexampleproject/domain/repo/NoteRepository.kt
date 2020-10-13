package com.oliver_curtis.firestoreexampleproject.domain.repo

import com.oliver_curtis.firestoreexampleproject.domain.model.Note
import io.reactivex.Single

interface NoteRepository {

    fun addNote(note: Note): Single<Boolean>
    fun fetchNotesUnordered(): Single<List<Note>>
    fun fetchNotesOrdered():  Single<List<Note>>
    fun updateNoteDescription(id: CharSequence, description: String): Single<Boolean>
    fun deleteDescription(id: CharSequence): Single<Boolean>
    fun deleteNote(id: CharSequence): Single<Boolean>
}