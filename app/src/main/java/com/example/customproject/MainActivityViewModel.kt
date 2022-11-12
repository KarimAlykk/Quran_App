package com.example.customproject

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream

class MainActivityViewModel(context: Context) : ViewModel() {

   // var chaptersList : ArrayList<Chapter> = arrayListOf()
    var homeFragment : HomeFragment = HomeFragment()
    val bookmarksFragment = BookmarksFragment()
    val notesFragment = NotesFragment()
    val progressFragment = ProgressFragment()
    var searchFragment:SearchFragment = SearchFragment()
    var currentFragment: Fragment = HomeFragment()
    //var isConfigurationChanges = false


   /*init{
       viewModelScope.launch {
           parseXml(context)
       }
       homeFragment = HomeFragment.newInstance(chaptersList)
       searchFragment = SearchFragment.newInstance(chaptersList)
       currentFragment = homeFragment
   }*/




   /*  fun parseXml(context: Context)
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
                    chaptersList.add(chapter)

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

       *//* var versesNumber = 0
       for (i in chaptersList.indices)
       {

           versesNumber += chaptersList[i].verses.size

       }
        Log.i("versesNumber", versesNumber.toString())*//*

    }
*/

}
