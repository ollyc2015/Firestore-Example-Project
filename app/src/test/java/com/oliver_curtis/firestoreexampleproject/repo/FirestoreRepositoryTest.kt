package com.oliver_curtis.firestoreexampleproject.repo

import com.oliver_curtis.firestoreexampleproject.data.db.NoteDatabase
import com.oliver_curtis.firestoreexampleproject.data.model.Note
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*


class FirestoreRepositoryTest {

    companion object {
        const val NOTE_ID_ONE = "NOTE_ID_ONE"
        const val NOTE_DESCRIPTION_ONE = "NOTE_DESCRIPTION_ONE"
    }

    @Mock lateinit var noteDatabase: NoteDatabase

    private val noteDescriptionDeletedTestObserver = TestObserver<Boolean>()
    private val addNoteTestObserver = TestObserver<Boolean>()
    private val notesTestObserver = TestObserver<List<Note>>()

    private val noteOne = Note("n1", "d1", Date(500))
    private val noteTwo = Note("n2", "d2", Date(300))
    private val noteThree = Note("n3", "d3", Date(200))

    private lateinit var firestoreRepository: FirestoreRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        firestoreRepository = FirestoreRepository(noteDatabase)
    }

    @Test
    fun givenNoteAdded_whenNoteAddedSuccessfully_thenReturnTrue() {
        // WHEN added successfully
        Mockito.`when`(noteDatabase.addNote(noteOne)).thenReturn(Single.just(true))
        // GIVEN node added
        firestoreRepository.addNote(noteOne).subscribe(addNoteTestObserver)
        // THEN true
        addNoteTestObserver.assertValue(true)
    }

    @Test
    fun givenNoteAdded_whenNoteAddedFailed_thenReturnFalse() {
        // WHEN added unsuccessfully
        Mockito.`when`(noteDatabase.addNote(noteOne)).thenReturn(Single.just(false))
        // GIVEN node added
        firestoreRepository.addNote(noteOne).subscribe(addNoteTestObserver)
        // THEN false
        addNoteTestObserver.assertValue(false)
    }

    @Test
    fun givenSingleUnorderedNote_whenGettingNote_thenReturnNote() {
        val expected = listOf(noteOne)
        // WHEN get notes
        Mockito.`when`(noteDatabase.getNotes()).thenReturn(Single.just(expected))
        // GIVEN notes requested
        firestoreRepository.fetchNotesUnordered().subscribe(notesTestObserver)
        // THEN return notes
        notesTestObserver.assertValue(expected)
    }

    @Test
    fun givenMultipleUnorderedNotes_whenGettingNotes_thenReturnNotes() {
        val expected = listOf(noteOne, noteTwo, noteThree)
        // WHEN get notes
        Mockito.`when`(noteDatabase.getNotes()).thenReturn(Single.just(expected))
        // GIVEN notes requested
        firestoreRepository.fetchNotesUnordered().subscribe(notesTestObserver)
        // THEN return notes
        notesTestObserver.assertValue(expected)
    }

    @Test
    fun givenGetNoteRequestError_whenGettingNotes_thenReturnEmptyList() {
        // WHEN get notes
        Mockito.`when`(noteDatabase.getNotes()).thenReturn(Single.error(Throwable("error")))
        // GIVEN notes requested
        firestoreRepository.fetchNotesUnordered().subscribe(notesTestObserver)
        // THEN return notes
        notesTestObserver.assertValue(emptyList())
    }

    @Test
    fun givenMultipleOrderedNotes_whenGettingNotes_thenReturnNotesDescending() {
        val expected = listOf(noteOne, noteTwo, noteThree)
        // WHEN get ordered notes
        Mockito.`when`(noteDatabase.getNotes()).thenReturn(Single.just(listOf(noteTwo, noteOne, noteThree)))
        // GIVEN notes requested
        firestoreRepository.fetchNotesOrdered().subscribe(notesTestObserver)
        // THEN return ordered notes
        notesTestObserver.assertValue(expected)
    }

    @Test
    fun givenNoteDescriptionUpdated_whenNoteDescription_updatedSuccessfully_thenReturnTrue(){

        //WHEN delete a note description, we expect to get back the result true
        Mockito.`when`(noteDatabase.updateNoteDescription(NOTE_ID_ONE, NOTE_DESCRIPTION_ONE)).thenReturn(Single.just(true))
        //GIVEN delete description
        firestoreRepository.updateNoteDescription(NOTE_ID_ONE, NOTE_DESCRIPTION_ONE).subscribe(noteDescriptionDeletedTestObserver)
        //THEN true
        noteDescriptionDeletedTestObserver.assertValue(true)
    }

    @Test
    fun givenNoteDescriptionUpdated_whenNoteDescription_updateFailure_thenReturnFalse(){

        //WHEN delete a note description, we expect to get back the result true
        Mockito.`when`(noteDatabase.updateNoteDescription(NOTE_ID_ONE, NOTE_DESCRIPTION_ONE)).thenReturn(Single.just(false))
        //GIVEN delete description
        firestoreRepository.updateNoteDescription(NOTE_ID_ONE, NOTE_DESCRIPTION_ONE).subscribe(noteDescriptionDeletedTestObserver)
        //THEN true
        noteDescriptionDeletedTestObserver.assertValue(false)
    }

    @Test
    fun givenNoteDescriptionDeleted_whenNoteDescription_deletedSuccessfully_thenReturnTrue(){

        //WHEN delete a note description, we expect to get back the result true
        Mockito.`when`(noteDatabase.deleteNoteDescription(NOTE_ID_ONE)).thenReturn(Single.just(true))
        //GIVEN delete description
        firestoreRepository.deleteDescription(NOTE_ID_ONE).subscribe(noteDescriptionDeletedTestObserver)
        //THEN true
        noteDescriptionDeletedTestObserver.assertValue(true)
    }

    @Test
    fun givenNoteDescriptionDeleted_whenNoteDescription_deleteFailure_thenReturnFalse(){

        //WHEN delete a note description failure, we expect to get back the result false
        Mockito.`when`(noteDatabase.deleteNoteDescription(NOTE_ID_ONE)).thenReturn(Single.just(false))
        //GIVEN delete description
        firestoreRepository.deleteDescription(NOTE_ID_ONE).subscribe(noteDescriptionDeletedTestObserver)
        //THEN false
        noteDescriptionDeletedTestObserver.assertValue(false)
    }

    @Test
    fun givenNoteDeleted_whenNote_deletedSuccessfully_thenReturnTrue(){

        //WHEN delete a note, we expect to get back the result true
        Mockito.`when`(noteDatabase.deleteNote(NOTE_ID_ONE)).thenReturn(Single.just(true))
        //GIVEN delete note
        firestoreRepository.deleteNote(NOTE_ID_ONE).subscribe(noteDescriptionDeletedTestObserver)
        //THEN true
        noteDescriptionDeletedTestObserver.assertValue(true)

    }

    @Test
    fun givenNoteDeleted_whenNote_deletedFailure_thenReturnFalse(){

        //WHEN delete a note failure, we expect to get back the result false
        Mockito.`when`(noteDatabase.deleteNote(NOTE_ID_ONE)).thenReturn(Single.just(false))
        //GIVEN delete description
        firestoreRepository.deleteNote(NOTE_ID_ONE).subscribe(noteDescriptionDeletedTestObserver)
        //THEN false
        noteDescriptionDeletedTestObserver.assertValue(false)
    }
}