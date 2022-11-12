package com.example.customproject

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView




class NotesAdapter(var mNotes: MutableList<Note>, notesFragment: NotesFragment, private val listener:(Note, Int) -> Unit, private val longListener: (Note, View) -> Unit): RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    class NotesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
            val title:TextView = itemView.findViewById(R.id.noteTitleItem)
            val imageView:ImageView = itemView.findViewById(R.id.ivNoteSelected)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
       holder.apply {
           title.text = mNotes[position].title
           holder.itemView.setBackgroundResource(mNotes[position].color)
           holder.itemView.isSelected = false
       }

        //this listener will open the noting activity
        holder.itemView.setOnClickListener{
            listener(mNotes[position], position)
        }


        // this long listener will select notes to be deleted from the recycler view or unselect notes
        holder.itemView.setOnLongClickListener{
            longListener(mNotes[position],  holder.itemView)

            if(holder.itemView.isSelected){
                holder.itemView.setBackgroundResource(R.drawable.note_rv_selected)
            }else{
                holder.itemView.setBackgroundResource(mNotes[position].color)
            }
            true
        }
    }

    override fun getItemCount():Int = mNotes.size

}

//class onClickListener(val clickListener : (note: Note) -> Unit)
//{
//    fun onClick(note: Note) = clickListener(note)
//}
