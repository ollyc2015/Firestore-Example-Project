package com.oliver_curtis.firestoreexampleproject.view

import com.oliver_curtis.firestoreexampleproject.domain.model.Note

interface NoteView {

    fun showNoteDialog(id: CharSequence, title: CharSequence, description: CharSequence)
    fun addNote(noteEntity: Note)
    fun toast(string: String)
    fun handleLandscapeView()
    fun handlePortraitView()
}