package com.example.customproject

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {

    var chapterList = arrayListOf<Chapter>()
    //lateinit var adapter:SearchAdapter
    var verseUtilities:VerseUtilities? =VerseUtilities()
    var searchedVerses = arrayListOf<Verse>()

    /*fun initAdapter(searchFragment: SearchFragment)
    {
        *//*adapter = SearchAdapter(chapterList){
            verseUtilities = VerseUtilities.newInstance(it)
            verseUtilities?.deleteMarkedVerse = {deleteMarkedVerse(it, searchFragment.requireContext())}
            verseUtilities?.show(searchFragment.childFragmentManager, null)
        }*//*
    }
*/
    fun deleteMarkedVerse(verse: Verse, context: Context)
    {
        viewModelScope.launch {
            val dbHelper = DBHelper(context)
            dbHelper.deleteVerseMarked(verse.verseID)
            dbHelper.close()
        }

    }

}