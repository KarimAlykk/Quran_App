package com.example.customproject

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private val CHAPTER_LIST = "chapterList"


class SearchFragment : Fragment() {


    private var searchViewModel: SearchViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchViewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]

        val searchView: SearchView = view.findViewById(R.id.idSV)
        val rvSItems: RecyclerView = view.findViewById(R.id.rvSearchedItems)

       /* if(!(arguments?.isEmpty)!!){
            searchViewModel?.chapterList = arguments?.getParcelableArrayList(CHAPTER_LIST)!!
        }
*/

        if(searchViewModel?.chapterList!!.isEmpty()){
            this.context?.let { parseXml(it) }
        }

        val adapter = SearchAdapter(searchViewModel!!.searchedVerses){

            searchViewModel?.verseUtilities = VerseUtilities.newInstance(it)
            searchViewModel?.verseUtilities?.deleteMarkedVerse = {searchViewModel?.deleteMarkedVerse(it, requireContext())}
            searchViewModel?.verseUtilities?.show(childFragmentManager, null)

        }

        //val adapter = SearchAdapter(searchViewModel.chapterList)

        rvSItems.adapter = adapter

        rvSItems.layoutManager = LinearLayoutManager(requireContext())

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText?.length!! > 2)
                {

                    adapter.filter(newText, searchViewModel!!.chapterList)
                }
                if (newText.isBlank()) {
                    //rvSItems.recycledViewPool.clear()
                    adapter.verses.clear()
                    adapter.notifyDataSetChanged()

                }

                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

        })





        //arguments?.clear()
        //arguments?.getParcelableArrayList<Chapter>(ARG_PARAM1)?.clear()
        return view
    }


    private fun parseXml(context: Context)
    {
        val parseFactory: XmlPullParserFactory

        try {
            parseFactory = XmlPullParserFactory.newInstance()
            val parser: XmlPullParser = parseFactory.newPullParser()
            val inputStream: InputStream = context.assets!!.open("quran.xml")
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)


            processParsing(parser)

        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    //will read the chapters from assets/quran.xml and store to the chapterList
    private fun processParsing(parser: XmlPullParser)
    {
        var eventType: Int = parser.eventType
        lateinit var chapter:Chapter
        lateinit var chapterName: String
        var chapterID:String? = ""

        while(eventType != XmlPullParser.END_DOCUMENT)
        {
            var eltName: String?
            if(eventType == XmlPullParser.START_TAG) {
                eltName = parser.name

                if(eltName == "Chapter")
                {
                    //Log.i("loop", "in")
                    chapter = Chapter("")
                    chapterID = parser.getAttributeValue(null, "ChapterID")
                    chapterName = parser.getAttributeValue(null, "ChapterName")
                    chapter.name = chapterName
                    searchViewModel?.chapterList!!.add(chapter)

                }else if (eltName == "Verse") {
                    val verseNum = parser.getAttributeValue(null, "VerseID")
                    val verseID = chapterID + verseNum
                    val verseContent = parser.nextText()
                    val verse = Verse(verseID, verseContent, chapterName, verseNum.toInt())
                    // Log.i("verseID", verseID)

                    chapter.verses.add(verse)
                    //Log.i("VerseXML", verse.content.toString())
                }

            }
            //Log.i("loop", "insideLoop")
            eventType = parser.next()
        }

        /* var versesNumber = 0
        for (i in chaptersList.indices)
        {

            versesNumber += chaptersList[i].verses.size

        }
         Log.i("versesNumber", versesNumber.toString())*/

    }





    override fun onPause() {
        super.onPause()
        if(searchViewModel?.verseUtilities?.isShowing == true){
            searchViewModel?.verseUtilities!!.dismiss()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(chapterList: ArrayList<Chapter>) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    if(chapterList.isNotEmpty()){
                        putParcelableArrayList(CHAPTER_LIST, chapterList)
                    }


                }
            }
    }


}

