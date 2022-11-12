package com.example.customproject

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import java.io.Serializable


class NoteDeleteDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        isShowing = true

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Are you sure you want to delete this note")
                .setPositiveButton("Yes", DialogInterface.OnClickListener{dialog, id ->
                    Log.i("yes", "yes")
                    isShowing= false
                    deleteNote()

                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener{dialog,id->
                    isShowing = false
                    cancelDelete()

                })
            builder.create()
        }?: throw IllegalStateException("Activity Cannot Be Null")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_delete_dialog, container, false)
    }

    //this function will be initialised to deleteNote() fun in NotesFragment



    var isShowing = false
    var deleteNote = {}
    var cancelDelete = {}

}