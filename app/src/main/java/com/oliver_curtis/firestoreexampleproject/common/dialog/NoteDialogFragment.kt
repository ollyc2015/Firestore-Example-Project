package com.oliver_curtis.firestoreexampleproject.common.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window.FEATURE_NO_TITLE
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.oliver_curtis.firestoreexampleproject.R


open class NoteDialogFragment : DialogFragment() {

    private lateinit var updateNoteDescription: OnDialogOptions.OnDialogUpdateNoteDescription
    private lateinit var deleteNoteDescriptionNote: OnDialogOptions.OnDialogDeleteNoteDescription
    private lateinit var deleteSelectedNote: OnDialogOptions.OnDialogDeleteNote
    private lateinit var closeDialog: OnDialogOptions.OnDialogClose

    private var dialogTitle: EditText? = null
    private var dialogDescription: EditText? = null

    // interface to handle the dialog click back to the Activity
    interface OnDialogOptions {
        interface OnDialogUpdateNoteDescription {
            fun onUpdateClicked(
                id: CharSequence,
                description: EditText?
            )
        }
        interface OnDialogDeleteNoteDescription {
            fun onDeleteDescriptionClicked(id: CharSequence)
        }
        interface OnDialogDeleteNote {
            fun deleteNoteClicked(id: CharSequence)
        }
        interface OnDialogClose {
            fun closeDialogClicked()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.view_custom_dialog, container)

        dialog?.requestWindowFeature(FEATURE_NO_TITLE)
        dialog?.setCancelable(false)

        val id = arguments?.get(KEY_ID) as CharSequence
        val title = arguments?.get(KEY_TITLE) as CharSequence
        val description = arguments?.get(KEY_DESCRIPTION) as CharSequence

        dialogTitle = view.findViewById(R.id.edit_text_title_dialog)
        dialogTitle?.setText(title)

        dialogDescription = view.findViewById(R.id.edit_text_description_dialog)
        dialogDescription?.setText(description)

        val updateBtn = view.findViewById(R.id.update_description) as Button
        updateBtn.setOnClickListener {
            updateNoteDescription.onUpdateClicked(id, dialogDescription)
        }

        val deleteNoteDescription = view.findViewById(R.id.delete_description) as Button
        deleteNoteDescription.setOnClickListener {
            deleteNoteDescriptionNote.onDeleteDescriptionClicked(id)
        }

        val deleteNote = view.findViewById(R.id.delete_note) as Button
        deleteNote.setOnClickListener {
            deleteSelectedNote.deleteNoteClicked(id)
        }

        val closeDialogBtn = view.findViewById(R.id.back) as Button
        closeDialogBtn.setOnClickListener {
            closeDialog.closeDialogClicked()
        }

        dialog?.create()

        return view
    }

    fun dialogUpdateNote(mOnDialogUpdateNote: OnDialogOptions.OnDialogUpdateNoteDescription) {
        this.updateNoteDescription = mOnDialogUpdateNote
    }

    fun dialogDeleteNoteDescription(mOnDialogDeleteNoteDescription: OnDialogOptions.OnDialogDeleteNoteDescription){
        this.deleteNoteDescriptionNote = mOnDialogDeleteNoteDescription
    }

    fun dialogDeleteNote(mOnDialogDeleteNote: OnDialogOptions.OnDialogDeleteNote){
        this.deleteSelectedNote = mOnDialogDeleteNote
    }

    fun dialogClose(mOnDialogClose: OnDialogOptions.OnDialogClose){
        this.closeDialog = mOnDialogClose
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }

    companion object {
        var KEY_ID = "id"
        var KEY_TITLE = "title"
        var KEY_DESCRIPTION = "description"

        fun newInstance(id: CharSequence?, title: CharSequence?, description: CharSequence?): NoteDialogFragment? {
            val args = Bundle()
            args.putCharSequence(KEY_ID, id)
            args.putCharSequence(KEY_TITLE, title)
            args.putCharSequence(KEY_DESCRIPTION, description)
            val f = NoteDialogFragment()
            f.arguments = args
            return f
        }
    }
}