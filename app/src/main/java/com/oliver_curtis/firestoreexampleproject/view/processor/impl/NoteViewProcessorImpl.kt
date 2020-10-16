package com.oliver_curtis.firestoreexampleproject.view.processor.impl

import android.content.res.Configuration
import android.os.Bundle
import com.oliver_curtis.firestoreexampleproject.view.dialog.NoteDialogFragment.Companion.KEY_DESCRIPTION
import com.oliver_curtis.firestoreexampleproject.view.dialog.NoteDialogFragment.Companion.KEY_ID
import com.oliver_curtis.firestoreexampleproject.view.dialog.NoteDialogFragment.Companion.KEY_TITLE
import com.oliver_curtis.firestoreexampleproject.domain.model.Note
import com.oliver_curtis.firestoreexampleproject.view.NoteView
import com.oliver_curtis.firestoreexampleproject.view.processor.NoteViewProcessor
import java.util.*

class NoteViewProcessorImpl : NoteViewProcessor {

    private var view: NoteView? = null

    override fun shouldDialogBeOpen(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val dateAdded = savedInstanceState.getCharSequence(KEY_ID)
            val title = savedInstanceState.getCharSequence(KEY_TITLE)
            val description = savedInstanceState.getCharSequence(KEY_DESCRIPTION)

            if (dateAdded != null && title != null && description != null) {

                view?.showNoteDialog(dateAdded, title, description)
            }
        }
    }

    override fun handleAddNoteClick(title: String, description: String, dateAdded: Date) {
        if (title.isNotEmpty() && description.isNotEmpty()) {
            view?.addNote(Note.instance(title, description, dateAdded))

        } else {
            view?.toast("Please add a title and a description")
        }
    }

    override fun checkValuesOfNoteClicked(
        dateAdded: CharSequence?,
        noteTitle: CharSequence?,
        noteDescription: CharSequence?
    ) {
        if (dateAdded != null && noteTitle != null && noteDescription != null) {
            view?.showNoteDialog(dateAdded, noteTitle, noteDescription)
        }
    }

    override fun handleScreenOrientation(orientation: Int) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            view?.handleLandscapeView()

        } else {
            // In portrait
           view?.handlePortraitView()
        }
    }


    override fun attachView(view: NoteView) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }
}