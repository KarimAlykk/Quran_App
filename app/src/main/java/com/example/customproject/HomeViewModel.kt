package com.example.customproject

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.Log
import android.widget.TextView
import androidx.core.os.HandlerCompat.postDelayed
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*


import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream
import java.time.LocalDate
import java.util.concurrent.Executors


class HomeViewModel : ViewModel()
{

    var chapterList: ArrayList<Chapter> = arrayListOf()
    var currentChapter:Int = 0
    var currentVerse:Int = 0

    var totalVerses = 0
    var totalChapters = 0

    //val timer = Timer()

    var timeAmount = 0
    var timerOn = false
    var time = MutableLiveData<String>()

    private var seconds = 0
    private var minutes = 0
    private var hours = 0
    private var job: Job = Job()

    //lateinit var verseUtilities: VerseUtilities

    init {
        time.value = "00:00:00"

    }

    // will stop the timer
    fun stopTimer()
    {
        job.cancel()
    }


    // is for the timer and it will increment the timer by seconds
    fun startTimer()
    {
        job = viewModelScope.launch{

            while(true)
            {
                delay(1000)

                seconds++
                if(seconds == 60)
                {
                    seconds = 0
                    minutes++
                }
                if(minutes == 60)
                {
                    minutes = 0
                    hours++
                }

                time.value = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                timeAmount++

            }

        }
    }


    fun resetTimer(){
        stopTimer()
        seconds = 0
        minutes = 0
        hours = 0

        if(timerOn){
            startTimer()
        }

    }

    //get the previous verse
    fun previousVerse()
    {
        if (currentVerse == 0 && currentChapter > 0) {
            currentChapter--
            //Log.i("c", "in")
            currentVerse = chapterList[currentChapter].verses.size-1
        }
        else if (currentVerse != 0)
        {
            currentVerse--
        }

    }

    // get the next verse
    fun nextVerse()
    {
        totalVerses++
        if(currentVerse == chapterList[currentChapter].verses.size-1)
        {
            if (currentChapter < chapterList.size-1)
            {
                currentChapter++
                currentVerse = 0
            }
            else{
                currentChapter=0
                currentVerse = 0
            }

        }else
        {
            currentVerse++
        }
    }

    //reset the attributes when the activity is destroyed.
    fun reset()
    {
        totalVerses = 0
        timeAmount = 0
        totalChapters = 0
    }


    //this function will will update the progress of the user in the database
    fun updateProgressInTable(db:DBHelper, progress: Progress)
    {
        db.getLastProgress().apply {
            val isNotEmpty = moveToFirst()

            if(!isNotEmpty){
                db.insertProgress(progress)
            }else{
                if(getString(1) == progress.date) {
                    progress.totalVerses += getInt(2)
                    progress.totalHours += getInt(3)
                    Log.i("test8", "verses" + progress.totalVerses)
                    Log.i("test8", "time" + progress.totalHours)
                    db.updateLastRow(progress)
                }else{
                    db.insertProgress(progress)
                }
            }
        }
    }


    //this function will update the progress of the app data
    fun updateOverallProgressPref(homeFragment: HomeFragment)
    {
        val progressSharedPreferences = homeFragment.activity?.getSharedPreferences("overall_progress", Context.MODE_PRIVATE)
        val stateSharedPref = homeFragment.activity?.getSharedPreferences("states_pref", Context.MODE_PRIVATE)

        if(progressSharedPreferences?.all?.size != 0) {
            val crrntTotalVerses = progressSharedPreferences?.getInt("total_verses", 0)
            val crrntTotalTime = progressSharedPreferences?.getInt("total_time", 0)

            with(progressSharedPreferences?.edit()!!) {
                putInt("total_verses", (crrntTotalVerses!! + totalVerses))
                putInt("total_time", (crrntTotalTime!! + timeAmount))
                apply()
            }

            with(stateSharedPref?.edit()!!){
                putInt("last_verse", currentVerse)!!
                putInt("last_chapter", currentChapter)
                apply()
            }

        }else{
            with(progressSharedPreferences.edit()!!) {
                putInt("total_verses", totalVerses)
                putInt("total_time",  timeAmount)
                 apply()

            }

            with(stateSharedPref?.edit()!!){
                putInt("last_verse", currentVerse)!!
                putInt("last_chapter", currentChapter)
                apply()
            }
        }
    }


    //delete a bookmark verse
    fun deleteMarkedVerse(dbHelper: DBHelper)
    {
        viewModelScope.launch {
            Log.i("test", "homeViewModel")
            dbHelper.deleteVerseMarked(chapterList[currentChapter].verses[currentVerse].verseID)
        }

    }


}




