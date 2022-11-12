package com.example.customproject

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class SearchAdapter(var verses : ArrayList<Verse> = arrayListOf(), private val listener: (Verse) -> Unit) : RecyclerView.Adapter<SearchAdapter.SearchedItemViewHolder>() {

    //private var text:String = "DSF"
    inner class SearchedItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var verseText: TextView = itemView.findViewById(R.id.tvSearchedVerse)
        val verseNumber: TextView = itemView.findViewById(R.id.sVerseNumber)
        val chapterName: TextView = itemView.findViewById(R.id.sChapterName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.searched_item, parent, false)

        return SearchedItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchedItemViewHolder, position: Int) {


        holder.verseText.text = verses[position].content
        holder.verseNumber.text = verses[position].number.toString()
        holder.chapterName.text = verses[position].chapterName

        holder.itemView.setOnClickListener{
            listener(verses[position])
        }
    }

    override fun getItemCount() = verses.size



    // the filter function is for the searching over the verses. it takes @param query to search in the @param chapters for
    // the matching in the list of verses.

    fun filter(query: String, chapters:MutableList<Chapter>) : Boolean
    {
        //text = query
        var result = false

        verses.clear()
        for(chapter in chapters) {
            for(verse in chapter.verses) {
                val arabicNormaliser = ArabicNormaliser(verse.content)
                val verseCont = arabicNormaliser.output

                result = if(verseCont.contains(query)||verse.content.contains(query)) {
                    verses.add(verse)
                    true
                }else {
                    false
                }
            }
        }

        notifyDataSetChanged()

        return result
    }

}