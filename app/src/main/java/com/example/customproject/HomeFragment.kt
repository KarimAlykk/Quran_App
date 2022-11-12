package com.example.customproject

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream

import java.time.LocalDate



private val CHAPTER_LIST = "chapterList"

class HomeFragment : Fragment() {


    // TODO: Rename and change types of parameters


    private var homeViewModel: HomeViewModel? = null
    // private lateinit var viewModelFactory: HomeViewModelFactory
    private lateinit var toolsBtn:ImageButton
    private lateinit var dbHelper:DBHelper
    private lateinit var verseTxt:TextView
    //private lateinit  var verseUtilities:VerseUtilities
    private lateinit var  timerButton: Button
    //lateinit var dbProgress: DBProgress.ProgressDatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper =  DBHelper(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)


        verseTxt = view.findViewById(R.id.currentVerse)
        val nextVerseButton: ImageButton = view.findViewById(R.id.nextVerseBtn)
        val previousVerseButton: ImageButton = view.findViewById(R.id.previousVerseBtn)
        toolsBtn = view.findViewById(R.id.toolsBtn)
        //val noteBtn: ImageButton = view.findViewById(R.id.noteBtn)
        val timerText:TextView = view.findViewById(R.id.tvTimer)
        timerButton = view.findViewById(R.id.timerButton)
        val tvName:TextView = view.findViewById(R.id.tvHtitle)
        val tvNum:TextView = view.findViewById(R.id.tvHNumber)
        val resetTimerBtn:Button = view.findViewById(R.id.resetButton)

        //viewModelFactory = HomeViewModelFactory(timerText)
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

       /* if(!(arguments?.isEmpty)!!){
            homeViewModel?.chapterList = arguments?.getParcelableArrayList(CHAPTER_LIST)!!
        }*/


        if(homeViewModel?.chapterList!!.isEmpty()){
            this.context?.let { parseXml(it) }
        }

        //homeViewModel.chaptersList.addAll(arguments?.getParcelableArrayList(ARG_PARAM1)!!)
        //Log.i("ChaptersListTest", chapters!![0].name)

        homeViewModel?.time?.observe(viewLifecycleOwner, Observer {
            timerText.text = it
        })

        val stateSharedPref = activity?.getSharedPreferences("states_pref", Context.MODE_PRIVATE)

        //arguments?.getParcelableArrayList<Chapter>(ARG_PARAM1)?.clear()

        if(stateSharedPref?.all?.size != 0) {
            homeViewModel?.apply {
                currentChapter = stateSharedPref?.getInt("last_chapter", 0)!!
                currentVerse = stateSharedPref.getInt("last_verse", 0)

            }
        }


       // verseTxt.movementMethod = ScrollingMovementMethod()

        homeViewModel?.apply {
            //verseTxt.text = chaptersList[currentChapter].verses[currentVerse].content
            verseTxt.text = chapterList[currentChapter].verses[currentVerse].content
            tvName.text = chapterList[currentChapter].name
            tvNum.text = chapterList[currentChapter].verses[currentVerse].number.toString()


            //timerText.text = timer.time
        }


        //checkVerseStatus()


        nextVerseButton.setOnClickListener{
            homeViewModel?.nextVerse()
            homeViewModel?.apply {
                verseTxt.text = chapterList[currentChapter].verses[currentVerse].content
                tvName.text = chapterList[currentChapter].name
                tvNum.text = chapterList[currentChapter].verses[currentVerse].number.toString()
            }

        }

        previousVerseButton.setOnClickListener{
            homeViewModel?.previousVerse()
            homeViewModel?.apply {
                verseTxt.text = chapterList[currentChapter].verses[currentVerse].content
                tvName.text = chapterList[currentChapter].name
                tvNum.text = chapterList[currentChapter].verses[currentVerse].number.toString()
            }


        }

        toolsBtn.setOnClickListener{
            homeViewModel?.apply {
                val verseUtilities = VerseUtilities.newInstance(chapterList[currentChapter].verses[currentVerse])
                verseUtilities.deleteMarkedVerse = {deleteMarkedVerse(dbHelper)}
                verseUtilities.show(parentFragmentManager, "verseUtilities")
            }
        }

        timerButton.setOnClickListener{
            if (!homeViewModel?.timerOn!!) {
                //homeViewModel.timer.startTimer(timerText)
                homeViewModel?.timerOn = true
                homeViewModel?.startTimer()
                timerButton.text = getText(R.string.pauseTime)
                timerButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,R.drawable.pause_icon,0)
            }
            else {
                //homeViewModel.timer.stopTimer()
                homeViewModel?.timerOn = false
                homeViewModel?.stopTimer()
                timerButton.text = getText(R.string.startTime)
                timerButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,R.drawable.play_icon,0)
            }
        }

        resetTimerBtn.setOnClickListener{
            homeViewModel?.resetTimer()
            if(!homeViewModel?.timerOn!!){
                timerText.text = "00:00:00"
            }
        }

        // in every day this dailyProgress will appear to the user to show him his last progress.V
        dbHelper.getLastProgress().apply {
            if( moveToFirst() && getString(1) != LocalDate.now().toString()) {
                val dailyProgress = DailyProgress()
                activity?.supportFragmentManager?.let { dailyProgress.show(it, null) }
            }
        }



        //arguments?.clear()

        return view
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = this@HomeFragment.context?.let {
            PreferenceManager.getDefaultSharedPreferences(it)
        }
        if(sharedPreferences?.getBoolean("theme_mode", true) == true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        verseTxt.textSize = sharedPreferences?.getInt("text_size", 20)?.toFloat() ?: return
    }


    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()



        //Log.i("fragmentDestroy", "destroy")
        //homeViewModel.timer.stopTimer()
    }



    override fun onPause() {
        super.onPause()
        //Log.i("testPause", "Paused")

            homeViewModel?.apply {

            val tv = totalVerses
            val ta = timeAmount
            val progress = Progress(LocalDate.now().toString(), tv, ta,0 )
            updateProgressInTable(dbHelper, progress)
            updateOverallProgressPref(this@HomeFragment)
            timerOn = false
            stopTimer()
            timerButton.text = getText(R.string.startTime)
            timerButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,R.drawable.play_icon,0)
            reset()
        }

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
                    homeViewModel?.chapterList?.add(chapter)

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




    companion object {
        @JvmStatic
        fun newInstance(chapterList: ArrayList<Chapter>) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    if(chapterList.isNotEmpty()){
                        putParcelableArrayList(CHAPTER_LIST, chapterList)
                    }


                }
            }
    }

}