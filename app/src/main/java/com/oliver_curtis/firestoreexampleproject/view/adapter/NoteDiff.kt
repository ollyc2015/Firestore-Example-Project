package com.oliver_curtis.firestoreexampleproject.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.oliver_curtis.firestoreexampleproject.domain.model.Note

class NoteDiff(
    private val oldItemList: List<Note>,
    private val newItemList: List<Note>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldItemList.size
    }

    override fun getNewListSize(): Int {
        return newItemList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItemList[oldItemPosition].id == newItemList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemList[oldItemPosition]
        val newItem = newItemList[newItemPosition]

        return oldItem.title == newItem.title &&
                oldItem.description == newItem.description &&
                oldItem.dateAdded == newItem.dateAdded
    }
}