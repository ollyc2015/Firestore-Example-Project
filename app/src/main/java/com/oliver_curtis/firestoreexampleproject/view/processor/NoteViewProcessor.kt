package com.oliver_curtis.firestoreexampleproject.view.processor

import android.os.Bundle
import com.oliver_curtis.firestoreexampleproject.common.processor.ViewProcessor
import com.oliver_curtis.firestoreexampleproject.view.NoteView
import java.util.*

interface NoteViewProcessor : ViewProcessor<NoteView> {

    fun shouldDialogBeOpen(savedInstanceState: Bundle?)
    fun handleAddNoteClick(title: String, description: String, format: Date)
    fun checkValuesOfNoteClicked(
        dateAdded: CharSequence?,
        noteTitle: CharSequence?,
        noteDescription: CharSequence?
    )
    fun handleScreenOrientation(orientation: Int)

}