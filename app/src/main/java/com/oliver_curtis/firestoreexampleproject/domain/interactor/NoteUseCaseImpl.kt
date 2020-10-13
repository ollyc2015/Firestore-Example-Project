package com.oliver_curtis.firestoreexampleproject.domain.interactor


import com.oliver_curtis.firestoreexampleproject.domain.model.Note
import com.oliver_curtis.firestoreexampleproject.domain.repo.NoteRepository
import io.reactivex.Single


class NoteUseCaseImpl(private val repository: NoteRepository) : NoteUseCase {

    override fun addNote(note: Note): Single<Boolean> = repository.addNote(note)

    override fun fetchNotesUnordered(): Single<List<Note>> = repository.fetchNotesUnordered()

    override fun fetchNotesOrdered(): Single<List<Note>> = repository.fetchNotesOrdered()

    override fun updateNoteDescription(id: CharSequence, description: String): Single<Boolean> = repository.updateNoteDescription(id, description)

    override fun deleteDescription(id: CharSequence): Single<Boolean> = repository.deleteDescription(id)

    override fun deleteNote(id: CharSequence): Single<Boolean> = repository.deleteNote(id)
}