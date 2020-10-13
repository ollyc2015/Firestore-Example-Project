package com.oliver_curtis.firestoreexampleproject.data.db

import android.util.Log
import com.google.firebase.firestore.*
import com.oliver_curtis.firestoreexampleproject.data.entities.NoteEntity
import io.reactivex.Single

class NoteDatabase(firestore: FirebaseFirestore) : Database {

    private val db: FirebaseFirestore = firestore
    private val noteNode = db.collection("Notebook")

    override fun getNotes(): Single<List<NoteEntity>> {

        return Single.create { emitter ->

            val listenerRegistration = noteNode
                .addSnapshotListener(EventListener { snapshot, e ->
                    if (e != null) {
                        Log.w("test", "Listen failed.", e)
                        emitter.onError(e)
                        return@EventListener
                    }

                    if (snapshot != null) {
                        Log.d("test", "Current data: " + snapshot.documents)
                        val documentModel = snapshot.toObjects(NoteEntity::class.java)
                        for (i in 0 until documentModel.size)
                        {
                            documentModel[i].documentId = snapshot.documents[i].id
                        }
                        emitter.onSuccess(documentModel)
                    } else {
                        Log.d("test", "Current data: null")

                    }
                })
            emitter.setCancellable { listenerRegistration.remove() }
        }
    }


    override fun addNote(noteEntity: NoteEntity): Single<Boolean> {

        return Single.create { emitter ->
            noteNode.add(noteEntity)
                .addOnCompleteListener {
                    emitter.onSuccess(it.isSuccessful)
                }
        }
    }


    override fun updateNoteDescription(id: CharSequence, description: String): Single<Boolean> {

        return Single.create { emitter ->
            db.document("Notebook/$id").update(KEY_DESCRIPTION, description)
                .addOnCompleteListener {
                    emitter.onSuccess(it.isSuccessful)
                }
        }
    }

    override fun deleteNoteDescription(id: CharSequence): Single<Boolean> {

        return Single.create { emitter ->
            db.document("Notebook/$id").update(KEY_DESCRIPTION, FieldValue.delete())
                .addOnCompleteListener {
                    emitter.onSuccess(it.isSuccessful)
                }
        }
    }

    override fun deleteNote(id: CharSequence): Single<Boolean> {

        return Single.create { emitter ->
            db.document("Notebook/$id").delete()
                .addOnCompleteListener {
                    emitter.onSuccess(it.isSuccessful)
                }
        }
    }


    companion object {
        private const val TAG = "NoteDatabase"
        private const val KEY_DESCRIPTION = "description"
    }
}