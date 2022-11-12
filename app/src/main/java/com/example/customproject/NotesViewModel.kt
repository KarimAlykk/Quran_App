package com.example.customproject

import android.annotation.SuppressLint
import android.util.Log
import android.view.ActionMode
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView


class NotesViewModel:ViewModel() {

    //var notesList = mutableListOf<Note>()

    //var isActionModeOn = false
    var noteFromAdapter: Note? = null
    var notePos = 0
    //var noteDeleteDialog:NoteDeleteDialog = NoteDeleteDialog()



    fun changeViewOnNotDelete(view:View)
    {
        view.alpha = 1.0f
    }


    //this fun will get only the notes based on the color from the database
    //it will then updates the adapter
    fun getColoredNotes(dbHelper: DBHelper, adapter: NotesAdapter, list:MutableList<Note>, drawable:Int=0){
        val cursor = dbHelper.getNotes()
        //adapter.mNotes.clear()
        list.clear()
        with(cursor){
            while (moveToNext()){
                if(cursor.getInt(5) ==drawable){
                    val note = Note(getString(1), getString(2), getString(3), getString(4),getInt(5), getInt(6), getString(7))
                    //adapter.mNotes.add(note)
                    list.add(note)
                }
            }
        }

       adapter.notifyDataSetChanged()
        cursor.close()
    }


    //this function will get all the notes from the database
    //it will then updates the apdapter
    fun getAllNotes(dbHelper: DBHelper, list: MutableList<Note>, adapter: NotesAdapter){

        val cursor = dbHelper.getNotes()
        list.clear()
        with(cursor) {
            while(moveToNext()) {
                val note = Note(getString(1), getString(2), getString(3), getString(4),getInt(5), getInt(6), getString(7))
                list.add(note)
            }
        }
        adapter.notifyDataSetChanged()
        cursor.close()
    }


}