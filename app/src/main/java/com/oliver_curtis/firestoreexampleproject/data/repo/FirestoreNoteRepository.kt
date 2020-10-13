package com.oliver_curtis.firestoreexampleproject.data.repo

import com.oliver_curtis.firestoreexampleproject.data.db.NoteDatabase
import com.oliver_curtis.firestoreexampleproject.data.entities.NoteEntity
import com.oliver_curtis.firestoreexampleproject.domain.model.Note
import com.oliver_curtis.firestoreexampleproject.domain.repo.NoteRepository
import io.reactivex.Single

class FirestoreNoteRepository(private val noteDatabase: NoteDatabase) : NoteRepository {

    override fun addNote(note: Note): Single<Boolean> {
        return noteDatabase.addNote(NoteEntity(note.title, note.description, note.dateAdded)).onErrorReturn { false }
    }

    override fun fetchNotesUnordered(): Single<List<Note>> {
        return noteDatabase.getNotes().map { it.map { toNote(it) } }.onErrorReturn { emptyList() }
    }

    override fun fetchNotesOrdered(): Single<List<Note>> {
        return noteDatabase.getNotes().map { it.map { toNote(it) } }
            .map { it.sortedByDescending { it.dateAdded.time } }.onErrorReturn { emptyList() }
    }

    override fun updateNoteDescription(id: CharSequence, description: String): Single<Boolean> {
        return noteDatabase.updateNoteDescription(id, description).onErrorReturn { false }
    }

    override fun deleteDescription(id: CharSequence): Single<Boolean> {
        return noteDatabase.deleteNoteDescription(id).onErrorReturn { false }
    }

    override fun deleteNote(id: CharSequence): Single<Boolean> {
        return noteDatabase.deleteNote(id).onErrorReturn { false }
    }

    private fun toNote(entity: NoteEntity): Note {
        val id = entity.documentId
        checkNotNull(id) { "Missing `documentId` from NoteEntity." }

        val title = entity.title
        checkNotNull(title) { "Missing `title` from NoteEntity." }

        val dateAdded = entity.dateAdded
        checkNotNull(dateAdded) { "Missing `dateAdded` from NoteEntity." }

        return Note(id, title, entity.description ?: "", dateAdded)
    }
}
