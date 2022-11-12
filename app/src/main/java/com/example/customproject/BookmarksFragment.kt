package com.example.customproject

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.SearchManager;
import android.content.Intent
import android.util.Log
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch





class BookmarksFragment : Fragment() {

    private lateinit var adapter: VerseMarkedAdapter
    //private lateinit var markedVerses: MutableList<Verse>
    private lateinit var dbHelper:DBHelper
    private var bookmarkViewModel: BookmarkViewModel? = null
    private lateinit var rvItems:RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper =  DBHelper(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_bookmarks, container, false)
        bookmarkViewModel = ViewModelProvider(requireActivity())[BookmarkViewModel::class.java]

        //markedVerses = mutableListOf()
        dbHelper = DBHelper(requireContext())
        rvItems= view.findViewById(R.id.rvMarkedVerses)
        val tvNoBookmarks:TextView = view.findViewById(R.id.tvEmptyBookmarks)

       //bookmarkViewModel.bookmarksFragment = this

        bookmarkViewModel?.addVersesToList(dbHelper, bookmarkViewModel!!.markedVerses)
        adapter = VerseMarkedAdapter(bookmarkViewModel!!.markedVerses) {
            val readVerseBookmark =ReadVerseBookmark.newInstance(it)
            readVerseBookmark.isCancelable = false
            readVerseBookmark.deleteMarkedVerse ={bookmarkViewModel!!.deleteMarkedVerse(dbHelper, it, it.verseID, adapter)
                if(bookmarkViewModel!!.markedVerses.isEmpty())
                {
                    tvNoBookmarks.visibility = View.VISIBLE
                }}
            readVerseBookmark.show(childFragmentManager, null)

        }

       /* if(bookmarkViewModel.adapter == null){
            Log.i("test17", "in")
            bookmarkViewModel.addVersesToList(dbHelper, bookmarkViewModel.markedVerses)
            bookmarkViewModel.initAdapter(dbHelper, tvNoBookmarks)
        }
*/

        rvItems.adapter = adapter
        rvItems.layoutManager = this.context?.let { LinearLayoutManager(it) }
        (rvItems.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        if(bookmarkViewModel?.markedVerses!!.isNotEmpty()) {
            tvNoBookmarks.visibility=View.GONE
        }

        return view
    }



    override fun onPause() {
        if(bookmarkViewModel?.verseUtilities!!.isShowing)
        {
            bookmarkViewModel?.verseUtilities!!.dismiss()
        }
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}




