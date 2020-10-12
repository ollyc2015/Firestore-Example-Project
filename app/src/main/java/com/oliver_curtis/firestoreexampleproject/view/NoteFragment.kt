package com.oliver_curtis.firestoreexampleproject.view

import Event
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oliver_curtis.firestoreexampleproject.R
import com.oliver_curtis.firestoreexampleproject.common.dialog.NoteDialogFragment
import com.oliver_curtis.firestoreexampleproject.common.viewmodel.CallResult
import com.oliver_curtis.firestoreexampleproject.data.model.Note
import com.oliver_curtis.firestoreexampleproject.view.adapter.NoteAdapter
import com.oliver_curtis.firestoreexampleproject.view.processor.NoteViewProcessor
import com.oliver_curtis.firestoreexampleproject.view.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.note_fragment.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class NoteFragment : Fragment(), NoteView {

    private var editTextTitle: EditText? = null
    private var editTextDescription: EditText? = null
    private var dialog: NoteDialogFragment? = null
    private lateinit var adapter: NoteAdapter
    private val viewModel: NoteViewModel by viewModel()
    private val processor: NoteViewProcessor by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.note_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {

        processor.attachView(this)
        editTextTitle = activity?.findViewById(R.id.edit_text_title)
        editTextDescription = activity?.findViewById(R.id.edit_text_description)
        initNoteRecyclerView()
        checkScreenOrientation()
        addNoteClickListener()
        loadNotesByDateButton()
        scrollToTopWhenNewItemsAdded()
        checkSavedInstanceState(savedInstanceState)
    }

    private fun checkSavedInstanceState(savedInstanceState: Bundle?) {

        processor.shouldDialogBeOpen(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        //Make sure we are only fetching notes/updates when our activity is in the foreground.
        //This is triggered when changes are detected in our document - due to the listener in NoteDatabase
        if (!ORDER_BY_DATE) {
            getUnorderedNotesFromDatabase().observe(viewLifecycleOwner, getUnorderedNotesObserver)
        } else {
            getOrderedNotesFromDatabase().observe(viewLifecycleOwner, getOrderedNotesObserver)
        }
    }

    private val getUnorderedNotesObserver = object : Observer<CallResult<List<Note>>> {
        override fun onChanged(t: CallResult<List<Note>>?) {
            processUnorderedNotesResponse(t)
        }
    }

    private val getOrderedNotesObserver = object : Observer<CallResult<List<Note>>> {
        override fun onChanged(t: CallResult<List<Note>>?) {
            processOrderedNoteResponse(t)
        }
    }

    private fun processUnorderedNotesResponse(callResult: CallResult<List<Note>>?) {

        if (callResult != null) {

            if (callResult.hasResult()) {
                notesRetrieved(callResult.result())
            } else {
                toast(callResult.error().toString())
            }
        }
    }

    private fun processOrderedNoteResponse(callResult: CallResult<List<Note>>?) {

        if (callResult != null) {

            if (callResult.hasResult()) {
                notesRetrieved(callResult.result())
            } else {
                toast(callResult.error().toString())
            }
        }
    }

    private fun initNoteRecyclerView() {

        note_list.layoutManager = LinearLayoutManager(context)
        adapter = NoteAdapter(arrayListOf())
        note_list.adapter = adapter
        noteClickListener(adapter)

    }

    private fun addNoteClickListener() {

        add_note_button.setOnClickListener {

            val title = editTextTitle?.text.toString().trim()
            val description = editTextDescription?.text.toString().trim()
            val date: Date = Calendar.getInstance().time

            processor.handleAddNoteClick(title, description, date)
        }
    }

    override fun addNote(note: Note) {

        addNoteToDatabase(note).observe(viewLifecycleOwner, addNoteObserver)
    }

    private val addNoteObserver = object : Observer<CallResult<Event<Boolean>>> {
        override fun onChanged(t: CallResult<Event<Boolean>>?) {
            processNoteAddedResult(t)
        }
    }

    fun processNoteAddedResult(callResult: CallResult<Event<Boolean>>?) {

        if (callResult != null) {
            if (callResult.hasResult()) {

                callResult.result().getContentIfNotHandled()?.let {
                    onStart()
                    toast("Note Added")
                }

            } else {
                toast(callResult.error().toString())
            }
        }
    }

    private fun loadNotesByDateButton() {

        load_By_Date_Added_button.setOnClickListener {
            ORDER_BY_DATE = true
            getOrderedNotesFromDatabase().observe(viewLifecycleOwner, getOrderedNotesObserver)
        }
    }

    private fun noteClickListener(noteNoteAdapter: NoteAdapter) {

        noteNoteAdapter.setOnNoteClickListener(object :
            NoteAdapter.OnRecyclerViewNoteClickListener {
            override fun onNoteSelectedClickListener(view: View?, position: Int) {

                val noteID = view?.findViewById<TextView>(R.id.note_id)?.text
                val noteTitle = view?.findViewById<TextView>(R.id.note_title)?.text
                val noteDescription = view?.findViewById<TextView>(R.id.note_description)?.text

                processor.checkValuesOfNoteClicked(noteID, noteTitle, noteDescription)
            }
        })
    }

    override fun showNoteDialog(
        id: CharSequence,
        title: CharSequence,
        description: CharSequence
    ) {

        dialog = NoteDialogFragment.newInstance(id, title, description)
        activity?.supportFragmentManager?.let { dialog?.show(it, "dialog") }

        dialogUpdateNoteDescriptionListener(dialog!!)
        dialogDeleteNoteDescriptionListener(dialog!!)
        dialogDeleteNoteListener(dialog!!)
        dialogCloseListener(dialog!!)
    }

    private fun dialogUpdateNoteDescriptionListener(noteDialog: NoteDialogFragment) {

        noteDialog.dialogUpdateNote(object : NoteDialogFragment.OnDialogOptions.OnDialogUpdateNoteDescription{

            override fun onUpdateClicked(
                id: CharSequence,
                description: EditText?
            ) {
                updateDescription(id, description?.text!!)
            }
        })
    }

    private fun dialogDeleteNoteDescriptionListener(noteDialog: NoteDialogFragment) {

        noteDialog.dialogDeleteNoteDescription(object : NoteDialogFragment.OnDialogOptions.OnDialogDeleteNoteDescription{
            override fun onDeleteDescriptionClicked(id: CharSequence) {
                deleteDescription(id)
            }
        })
    }

    private fun dialogDeleteNoteListener(noteDialog: NoteDialogFragment) {

        noteDialog.dialogDeleteNote(object : NoteDialogFragment.OnDialogOptions.OnDialogDeleteNote{
            override fun deleteNoteClicked(id: CharSequence) {
                deleteNote(id)
            }
        })
    }

    private fun dialogCloseListener(noteDialog: NoteDialogFragment) {

        noteDialog.dialogClose(object : NoteDialogFragment.OnDialogOptions.OnDialogClose{
            override fun closeDialogClicked() {
                noteDialog.dismiss()
            }
        })
    }

    private fun updateDescription(
        id: CharSequence,
        text: Editable
    ) {
        val description = text.toString()

        updateNoteDescriptionInDatabase(id, description).observe(
            viewLifecycleOwner,
            updateNoteDescriptionInDatabase
        )
    }

    private val updateNoteDescriptionInDatabase = object : Observer<CallResult<Event<Boolean>>> {
        override fun onChanged(t: CallResult<Event<Boolean>>?) {
            handleUpdateNoteDescriptionResponse(t)
        }
    }

    private fun handleUpdateNoteDescriptionResponse(callResult: CallResult<Event<Boolean>>?) {

        if (callResult != null) {
            if (callResult.hasResult()) {

                callResult.result().getContentIfNotHandled()?.let {

                    toast("Note Description Updated")
                    onStart()
                    dialog?.dismissDialog()
                }

            } else {
                toast(callResult.error().toString())
            }
        }
    }

    private fun deleteDescription(id: CharSequence) {
        deleteNoteDescriptionFromDatabase(id).observe(
            viewLifecycleOwner,
            deleteNoteDescriptionObserver
        )
    }

    private val deleteNoteDescriptionObserver = object : Observer<CallResult<Event<Boolean>>> {
        override fun onChanged(t: CallResult<Event<Boolean>>?) {
            handleDeleteNoteDescriptionResponse(t)
        }
    }

    private fun handleDeleteNoteDescriptionResponse(callResult: CallResult<Event<Boolean>>?) {

        if (callResult != null) {
            if (callResult.hasResult()) {

                callResult.result().getContentIfNotHandled()?.let {
                    toast("Description Deleted")
                    onStart()
                    dialog?.dismissDialog()
                }

            } else {
                toast(callResult.error().toString())
            }
        }
    }

    private fun deleteNote(id: CharSequence) {
        deleteNoteFromDatabase(id).observe(viewLifecycleOwner, deleteNoteObserver)
    }

    private val deleteNoteObserver = object : Observer<CallResult<Event<Boolean>>> {
        override fun onChanged(t: CallResult<Event<Boolean>>?) {
            handleDeleteNoteResponse(t)
        }
    }

    private fun handleDeleteNoteResponse(callResult: CallResult<Event<Boolean>>?) {

        if (callResult != null) {
            if (callResult.hasResult()) {

                callResult.result().getContentIfNotHandled()?.let {
                    toast("Note Deleted")
                    onStart()
                    dialog?.dismissDialog()
                }

            } else {
                toast(callResult.error().toString())
            }
        }
    }

    private fun checkScreenOrientation() {

        val orientation = resources.configuration.orientation
        processor.handleScreenOrientation(orientation)
    }


    override fun toast(string: String) {

        Toast.makeText(requireContext(), string, Toast.LENGTH_LONG).show()
    }

    override fun handleLandscapeView() {
        editTextTitle?.visibility = View.GONE
        editTextDescription?.visibility = View.GONE
        load_By_Date_Added_button?.visibility = View.GONE
        add_note_button?.visibility = View.GONE
    }

    override fun handlePortraitView() {
        editTextTitle?.visibility = View.VISIBLE
        editTextDescription?.visibility = View.VISIBLE
        load_By_Date_Added_button?.visibility = View.VISIBLE
        add_note_button?.visibility = View.VISIBLE
    }

    private fun addNoteToDatabase(note: Note): MutableLiveData<CallResult<Event<Boolean>>> {

        return viewModel.addNote(note)
    }

    private fun deleteNoteFromDatabase(id: CharSequence): MutableLiveData<CallResult<Event<Boolean>>> {

        return viewModel.deleteNote(id)
    }

    private fun deleteNoteDescriptionFromDatabase(id: CharSequence): MutableLiveData<CallResult<Event<Boolean>>> {

        return viewModel.deleteDescription(id)
    }

    private fun updateNoteDescriptionInDatabase(
        id: CharSequence,
        description: String
    ): MutableLiveData<CallResult<Event<Boolean>>> {

        return viewModel.updateNoteDescription(id, description)

    }

    private fun getUnorderedNotesFromDatabase(): MutableLiveData<CallResult<List<Note>>> {

        return viewModel.getNotesUnordered()
    }

    private fun getOrderedNotesFromDatabase(): MutableLiveData<CallResult<List<Note>>> {

        return viewModel.getNotesOrdered()
    }

    private fun notesRetrieved(notes: List<Note>) {

        adapter.updateAll(notes)
    }

    private fun scrollToTopWhenNewItemsAdded() {

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                if (ORDER_BY_DATE) {
                    note_list.smoothScrollToPosition(0)
                }
            }
        })
    }

    override fun onPause() {
        try {
            processor.detachView()
        } finally {
            super.onPause()
        }
    }

    companion object {
        private var ORDER_BY_DATE = false
    }
}