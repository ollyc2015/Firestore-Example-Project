package com.oliver_curtis.firestoreexampleproject.domain.model

import java.util.*

data class Note(val id: String = "", val title: String, val description: String, val dateAdded: Date) {

    companion object {
        fun instance(title: String, description: String, dateAdded: Date): Note {
            return Note(title = title, description = description, dateAdded = dateAdded)
        }
    }
}