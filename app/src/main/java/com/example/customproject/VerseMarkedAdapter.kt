package com.example.customproject

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import java.text.Normalizer

class VerseMarkedAdapter(var mVerses: MutableList<Verse>, var listener: (Verse) -> Unit ) : RecyclerView.Adapter<VerseMarkedAdapter.VerseMarkedViewHolder>() {

    class VerseMarkedViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val chapterName:TextView = itemView.findViewById(R.id.mChapterName)
        val verseNumber: TextView = itemView.findViewById(R.id.mVerseNumber)
        val verseContent: TextView = itemView.findViewById(R.id.mVerseContent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerseMarkedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_marked_item, parent, false)

        return VerseMarkedViewHolder(view)

    }

    override fun onBindViewHolder(holder: VerseMarkedViewHolder, position: Int) {
        holder.apply {
            chapterName.text = mVerses[position].chapterName
            verseNumber.text = mVerses[position].number.toString()
            verseContent.text = mVerses[position].content
        }



        // will open ReadVerseBookmark fragment to read the full verse and to show the other verse utilities
        holder.itemView.setOnClickListener{
            listener(mVerses[position])
        }

    }

    override fun getItemCount(): Int = mVerses.size

}