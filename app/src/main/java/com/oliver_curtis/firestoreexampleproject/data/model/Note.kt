package com.oliver_curtis.firestoreexampleproject.data.model

import com.google.firebase.firestore.Exclude
import java.util.*


class Note {
    @get:Exclude
    var documentId: String? = null
    var title: String? = null
        private set
    var description: String? = null
        private set
    var dateAdded: Date? = null

    constructor() {
        //public no-arg constructor needed
    }

    constructor(title: String?, description: String?, timeStamp: Date) {
        this.title = title
        this.description = description
        this.dateAdded = timeStamp
    }
}
