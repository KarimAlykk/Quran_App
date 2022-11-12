package com.example.customproject

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BookmarkViewModel:ViewModel() {


    var markedVerses = mutableListOf<Verse>()
    var verseUtilities = VerseUtilities()
    //var adapter:VerseMarkedAdapter? = null
    //var bookmarksFragment:BookmarksFragment? = null


    /*fun initAdapter(dbHelper: DBHelper, tvNoBookmarks:TextView){
        adapter = VerseMarkedAdapter(markedVerses) {verse, pos ->
            val verseUtilities = VerseUtilities.newInstance(verse)
            verseUtilities.show(bookmarksFragment!!.parentFragmentManager, "verseUtilities")
            verseUtilities.deleteMarkedVerse = {deleteMarkedVerse(dbHelper, verse.verseID, pos, adapter)}
                if(markedVerses.isEmpty())
                {
                    tvNoBookmarks.visibility = View.VISIBLE
                }
        }
    }

*/

    //this function will add all the bookmared verses to the list from the bookmarks table from the database
     fun addVersesToList(dbHelper: DBHelper?, quranList: MutableList<Verse>) {
        val cursor = dbHelper!!.getMarkedVerses()
        quranList.clear()
        with(cursor)
        {
            while (moveToNext()) {
                val verse = Verse(getString(0), getString(1), getString(2), getInt(3))
                quranList.add(verse)
            }
        }
        cursor.close()
    }


    //this function will delete the marked verse from the verseMarkedAdapter recycler view and from the bookmarks table
    fun deleteMarkedVerse(dbHelper: DBHelper, verse: Verse, verseID:String, adapter: VerseMarkedAdapter?)
    {
        //Log.i("listSize", "markedVerses = ${markedVerses.size}")
        //Log.i("listSize", "adapterVerses = ${adapter?.mVerses?.size}")

        adapter?.mVerses?.remove(verse)
        adapter?.notifyDataSetChanged()

        //Log.i("listSize", "adapterVersesAfter = ${adapter?.mVerses?.size}")
        //Log.i("listSize", "markedVersesAfter = ${markedVerses.size}")

        viewModelScope.launch {
            dbHelper.deleteVerseMarked(verseID)
        }
    }
}