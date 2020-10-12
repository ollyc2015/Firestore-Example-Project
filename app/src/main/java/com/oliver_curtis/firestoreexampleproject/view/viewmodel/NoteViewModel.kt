package com.oliver_curtis.firestoreexampleproject.view.viewmodel

import Event
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oliver_curtis.firestoreexampleproject.common.viewmodel.CallResult
import com.oliver_curtis.firestoreexampleproject.common.viewmodel.livedata.DefaultLiveDataProvider
import com.oliver_curtis.firestoreexampleproject.common.viewmodel.livedata.LiveDataProvider
import com.oliver_curtis.firestoreexampleproject.common.viewmodel.rx.DefaultSchedulerProvider
import com.oliver_curtis.firestoreexampleproject.common.viewmodel.rx.SchedulerProvider
import com.oliver_curtis.firestoreexampleproject.data.model.Note
import com.oliver_curtis.firestoreexampleproject.repo.Repository
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class NoteViewModel(
    private val repository: Repository,
    private val liveDataProvider: LiveDataProvider = DefaultLiveDataProvider(),
    private val schedulerProvider: SchedulerProvider = DefaultSchedulerProvider(),
) : ViewModel() {

    fun getNotesUnordered(): MutableLiveData<CallResult<List<Note>>> {

        return liveDataProvider.liveDataInstance<List<Note>>().apply {

            repository.fetchNotesUnordered()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(object : SingleObserver<List<Note>> {
                    override fun onSuccess(t: List<Note>) {

                        this@apply.postValue(CallResult(t))
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onError(e: Throwable) {

                        this@apply.postValue(CallResult(e))
                    }
                })
        }
    }

    fun getNotesOrdered(): MutableLiveData<CallResult<List<Note>>> {

        return liveDataProvider.liveDataInstance<List<Note>>().apply {

            repository.fetchNotesOrdered()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(object : SingleObserver<List<Note>> {
                    override fun onSuccess(t: List<Note>) {

                        this@apply.postValue(CallResult(t))
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onError(e: Throwable) {

                        this@apply.postValue(CallResult(e))
                    }
                })
        }
    }


    fun updateNoteDescription(
        id: CharSequence,
        description: String,
    ): MutableLiveData<CallResult<Event<Boolean>>> {

        return liveDataProvider.liveDataInstance<Event<Boolean>>().apply {

            repository.updateNoteDescription(id, description)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(object : SingleObserver<Boolean> {
                    override fun onSuccess(t: Boolean) {

                        this@apply.postValue(CallResult(Event(t)))
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onError(e: Throwable) {

                        this@apply.postValue(CallResult(e))
                    }
                })
        }
    }

    fun deleteDescription(id: CharSequence): MutableLiveData<CallResult<Event<Boolean>>> {

        return liveDataProvider.liveDataInstance<Event<Boolean>>().apply {
            repository.deleteDescription(id)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(object : SingleObserver<Boolean> {
                    override fun onSuccess(t: Boolean) {

                        this@apply.postValue(CallResult(Event(t)))
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onError(e: Throwable) {

                        this@apply.postValue(CallResult(e))
                    }
                })
        }
    }

    fun deleteNote(id: CharSequence): MutableLiveData<CallResult<Event<Boolean>>> {

        return liveDataProvider.liveDataInstance<Event<Boolean>>().apply {
            repository.deleteNote(id)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(object : SingleObserver<Boolean> {
                    override fun onSuccess(t: Boolean) {

                        this@apply.postValue(CallResult(Event(t)))
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onError(e: Throwable) {

                        this@apply.postValue(CallResult(e))
                    }
                })
        }
    }

    fun addNote(note: Note): MutableLiveData<CallResult<Event<Boolean>>> {

        return liveDataProvider.liveDataInstance<Event<Boolean>>().apply {
            repository.addNote(note)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(object : SingleObserver<Boolean> {
                    override fun onSuccess(t: Boolean) {

                        this@apply.postValue(CallResult(Event(t)))
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onError(e: Throwable) {

                        this@apply.postValue(CallResult(e))
                    }
                })
        }
    }
}