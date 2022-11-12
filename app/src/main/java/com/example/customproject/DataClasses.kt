package com.example.customproject


import android.os.Parcelable


import kotlinx.parcelize.Parcelize

//These are all the data classes that will be used in the application. Any data class will be added here for later access


@Parcelize
data class Chapter(var name: String, var verses: ArrayList<Verse> = arrayListOf()) : Parcelable

@Parcelize
data class Verse(var verseID:String, var content:String, var chapterName:String, var number:Int):Parcelable

@Parcelize
data class Note(var verseID:String?, var title:String="", var notes:String="", var verseCont: String="", var color:Int=R.drawable.default_colorground,
                var verseNumber:Int=0, var chapterName:String? = null):Parcelable

@Parcelize
data class Achievement(var id:String, var desc:String?, var isVisible:Boolean = false, var image: Int =0) :Parcelable

data class Progress(var date: String, var totalVerses:Int, var totalHours:Int,  var totalChapters: Int)