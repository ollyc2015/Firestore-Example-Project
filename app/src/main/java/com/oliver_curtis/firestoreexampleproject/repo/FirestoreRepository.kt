package com.oliver_curtis.firestoreexampleproject.repo

import com.oliver_curtis.firestoreexampleproject.data.db.NoteDatabase
import com.oliver_curtis.firestoreexampleproject.data.model.Note
import io.reactivex.Single

class FirestoreRepository(private val noteDatabase: NoteDatabase): Repository {

    override fun addNote(note: Note): Single<Boolean> {
        return noteDatabase.addNote(note)
    }

    override fun fetchNotesUnordered(): Single<List<Note>> {
        return noteDatabase.getUnorderedNotes()
    }

    override fun fetchNotesOrdered(): Single<List<Note>> {
        return noteDatabase.getOrderedNotes()
    }

    override fun updateNoteDescription(
        id: CharSequence,
        description: String
    ): Single<Boolean> {
        return noteDatabase.updateNoteDescription(id, description)
    }

    override fun deleteDescription(id: CharSequence): Single<Boolean> {
        return noteDatabase.deleteNoteDescription(id)
    }

    override fun deleteNote(id: CharSequence): Single<Boolean> {
        return noteDatabase.deleteNote(id)
    }
}
