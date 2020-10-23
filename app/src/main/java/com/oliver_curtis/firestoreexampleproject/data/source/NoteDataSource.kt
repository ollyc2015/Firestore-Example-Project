package com.oliver_curtis.firestoreexampleproject.data.source

import com.oliver_curtis.firestoreexampleproject.data.entities.NoteEntity
import io.reactivex.Single

interface NoteDataSource {

    fun addNote(noteEntity: NoteEntity): Single<Boolean>
    fun updateNoteDescription(id: CharSequence, description: String): Single<Boolean>
    fun deleteNoteDescription(id: CharSequence): Single<Boolean>
    fun deleteNote(id: CharSequence): Single<Boolean>
    fun getNotes(): Single<List<NoteEntity>>
}