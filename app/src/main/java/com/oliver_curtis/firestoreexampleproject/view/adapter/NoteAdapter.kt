package com.oliver_curtis.firestoreexampleproject.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.oliver_curtis.firestoreexampleproject.R
import com.oliver_curtis.firestoreexampleproject.data.entities.NoteEntity
import com.oliver_curtis.firestoreexampleproject.domain.model.Note
import kotlinx.android.synthetic.main.item_note.view.*

class NoteAdapter(private val notes: MutableList<Note>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var noteClickListener: OnRecyclerViewNoteClickListener

    // specify the row layout file and click for each row
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)

        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        onBindViewHolder(holder, position, ArrayList(0))
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {

        NoteViewHolder(holder.itemView).bind(notes[position])
        addOnClickListener(holder.itemView.note_parent, position)

    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun updateAll(updated: List<Note>) {
        val diffResult = DiffUtil.calculateDiff(NoteDiff(notes, updated), false)
        notes.clear()
        notes.addAll(updated)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun addOnClickListener(noteParent: LinearLayout?, position: Int) {
        noteParent?.setOnClickListener {
            noteClickListener.onNoteSelectedClickListener(it, position)
        }
    }

    fun setOnNoteClickListener(mNoteClickListener: OnRecyclerViewNoteClickListener) {
        this.noteClickListener = mNoteClickListener
    }

    interface OnRecyclerViewNoteClickListener {
        fun onNoteSelectedClickListener(view: View?, position: Int)
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note) {
            itemView.note_parent.note_id.text = note.id
            itemView.note_parent.note_title.text = note.title
            itemView.note_parent.note_description.text = note.description
            itemView.note_parent.note_added.text = note.dateAdded.toLocaleString()
        }
    }
}