package com.oliver_curtis.firestoreexampleproject.view.viewmodel

import Event
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.oliver_curtis.firestoreexampleproject.common.viewmodel.CallResult
import com.oliver_curtis.firestoreexampleproject.common.viewmodel.livedata.DefaultLiveDataProvider
import com.oliver_curtis.firestoreexampleproject.common.viewmodel.rx.DefaultSchedulerProvider
import com.oliver_curtis.firestoreexampleproject.data.model.Note
import com.oliver_curtis.firestoreexampleproject.repo.FirestoreRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

class NoteViewModelTest {

    companion object {
        const val NOTE_ID_ONE = "NOTE_ID_ONE"
        const val NOTE_DESCRIPTION_ONE = "NOTE_DESCRIPTION_ONE"
    }

    @get:Rule val rule = InstantTaskExecutorRule()

    // mocked repository since that isn't the class under test, we can
    @Mock lateinit var defaultSchedulerProvider: DefaultSchedulerProvider
    @Mock lateinit var repository: FirestoreRepository
    @Mock lateinit var defaultLiveDataProvider: DefaultLiveDataProvider

    @Mock lateinit var updatedNoteObserver: Observer<CallResult<Event<Boolean>>>
    @Mock lateinit var unorderedNotesObserver: Observer<CallResult<List<Note>>>
    private val updatedNoteLiveData = MutableLiveData<CallResult<Event<Boolean>>>()
    private val unorderedNotesLiveData = MutableLiveData<CallResult<List<Note>>>()

    // creating real Note objects not mocking them because we can.
    private val noteOne = Note("title_one", "description_one", Date(100))
    private val noteTwo = Note("title_two", "description_two", Date(200))

    // the actual class under test
    private lateinit var viewModel: NoteViewModel

    // runs once before each test method is run
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(defaultSchedulerProvider.io()).thenReturn(Schedulers.trampoline())
        Mockito.`when`(defaultSchedulerProvider.ui()).thenReturn(Schedulers.trampoline())

        viewModel = NoteViewModel(repository, defaultLiveDataProvider, defaultSchedulerProvider)
    }

    @Test
    fun `givenSingleNewNote_whenObservingNotesUnordered_thenChangesObserved`() {
        val expected = listOf(noteOne)

        Mockito.`when`(defaultLiveDataProvider.liveDataInstance<List<Note>>()).thenReturn(unorderedNotesLiveData)
        Mockito.`when`(repository.fetchNotesUnordered()).thenReturn(Single.just(expected))

        // call our methods under test and apply our observer whilst we are doing it.
        viewModel.getNotesUnordered().observeForever(unorderedNotesObserver)

        // run assertion against observer
        argumentCaptor<CallResult<List<Note>>>().apply {
            verify(unorderedNotesObserver).onChanged(capture())
        }.run { Assert.assertEquals(expected, firstValue.result()) }
    }

    @Test
    fun `givenMultipleNewNotes_whenObservingNotesUnordered_thenChangesObserved`() {
        val expectedFirst = listOf(noteOne)

        Mockito.`when`(defaultLiveDataProvider.liveDataInstance<List<Note>>()).thenReturn(unorderedNotesLiveData)
        Mockito.`when`(repository.fetchNotesUnordered()).thenReturn(Single.just(expectedFirst))

        viewModel.getNotesUnordered().observeForever(unorderedNotesObserver)

        argumentCaptor<CallResult<List<Note>>>().apply {
            verify(unorderedNotesObserver).onChanged(capture())
        }.run { Assert.assertEquals(expectedFirst, firstValue.result()) }

        val expectedSecond = listOf(noteOne, noteTwo)

        Mockito.`when`(defaultLiveDataProvider.liveDataInstance<List<Note>>()).thenReturn(unorderedNotesLiveData)
        Mockito.`when`(repository.fetchNotesUnordered()).thenReturn(Single.just(expectedSecond))

        viewModel.getNotesUnordered().observeForever(unorderedNotesObserver)

        argumentCaptor<CallResult<List<Note>>>().apply {
            verify(unorderedNotesObserver, times(2)).onChanged(capture())
        }.run { Assert.assertEquals(expectedFirst, firstValue.result()) }
    }


    @Test
    fun `givenErrorWhilstObservingNotesUnordered_thenErrorObserved`() {
        val throwable = Throwable("Nasty Error")

        Mockito.`when`(defaultLiveDataProvider.liveDataInstance<List<Note>>()).thenReturn(unorderedNotesLiveData)
        // manipulate the response to trigger an error state "Single.error(throwable)"
        Mockito.`when`(repository.fetchNotesUnordered()).thenReturn(Single.error(throwable))

        // call our methods under test and apply our observer whilst we are doing it.
        viewModel.getNotesUnordered().observeForever(unorderedNotesObserver)

        // run assertion against observer
        argumentCaptor<CallResult<List<Note>>>().apply {
            verify(unorderedNotesObserver).onChanged(capture())
        }.run { Assert.assertEquals(throwable, firstValue.error()) }
    }

    @Test
    fun `givenSingleNoteDeleted_whenObservingNotes_thenChangesObserved`() {

    }

    @Test
    fun `givenMultipleNotesDeleted_whenObservingNotes_thenChangesObserved`() {

    }

    @Test
    fun `givenNoteDescriptionUpdated_whenOperationSuccess_thenObserveSuccessfulResponse`() {
        Mockito.`when`(defaultLiveDataProvider.liveDataInstance<Event<Boolean>>()).thenReturn(updatedNoteLiveData)
        Mockito.`when`(repository.updateNoteDescription(NOTE_ID_ONE, NOTE_DESCRIPTION_ONE)).thenReturn(Single.just(true))

        viewModel.updateNoteDescription(NOTE_ID_ONE, NOTE_DESCRIPTION_ONE).observeForever(updatedNoteObserver)

        argumentCaptor<CallResult<Event<Boolean>>>().apply {
            verify(updatedNoteObserver).onChanged(capture())
        }.run { org.junit.Assert.assertEquals(true, firstValue.result().getContentIfNotHandled()) }
    }

    @Test
    fun `givenNoteDescriptionUpdated_whenOperationObservedTwice_thenDoNotObserveTwice`() {
        Mockito.`when`(defaultLiveDataProvider.liveDataInstance<Event<Boolean>>()).thenReturn(updatedNoteLiveData)
        Mockito.`when`(repository.updateNoteDescription(NOTE_ID_ONE, NOTE_DESCRIPTION_ONE)).thenReturn(Single.just(true))

        viewModel.updateNoteDescription(NOTE_ID_ONE, NOTE_DESCRIPTION_ONE).observeForever(updatedNoteObserver)

        argumentCaptor<CallResult<Event<Boolean>>>().apply {
            verify(updatedNoteObserver).onChanged(capture())
        }.run { Assert.assertEquals(true, firstValue.result().getContentIfNotHandled()) }

        // observe second time
        argumentCaptor<CallResult<Event<Boolean>>>().apply {
            verify(updatedNoteObserver).onChanged(capture())
        }.run { Assert.assertNull(firstValue.result().getContentIfNotHandled()) }
    }

    @Test
    fun `givenNoteDescriptionUpdated_whenOperationFailure_thenObserveFailedResponse`() {

    }
}