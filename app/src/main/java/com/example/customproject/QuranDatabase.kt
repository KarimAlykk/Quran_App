

package com.example.customproject

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log




class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    private val markedTable: MarkedObject.MarkedVerseTable = MarkedObject.MarkedVerseTable()
    private val notesTable: NoteObject.NotesTable = NoteObject.NotesTable()
    private val progressTable:ProgressObject.ProgressTable = ProgressObject.ProgressTable()

    override fun onCreate(db: SQLiteDatabase) {
        Log.i("testDatabase", "create")
        db.execSQL(markedTable.SQL_CREATE_ENTERIES)
        db.execSQL(notesTable.SQL_CREATE_ENTERIES)
        db.execSQL(progressTable.SQL_CREATE_ENTERIES)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(markedTable.SQL_DELETE_ENTRIES)
        db.execSQL(notesTable.SQL_DELETE_ENTRIES)
        db.execSQL(progressTable.SQL_DELETE_ENTRIES)
        onCreate(db)

    }


    fun addData(verse:Verse) :Boolean = markedTable.addData(this, verse)
    fun getMarkedVerses(): Cursor = markedTable.getMarkedVerses(this)
    fun checkDataExists(verseID: String) :Boolean = markedTable.checkDataExists(this, verseID)
    fun deleteVerseMarked(verseID:String): Int = markedTable.deleteVerseMarked(this, verseID)


    fun getNotes() = notesTable.getNotes(this)
    fun insertNoteToTable(note:Note) = notesTable.insertNoteToTable(this, note)
    fun getVerseNote(verseID: String) = notesTable.getVerseNote(this,verseID)
    fun updateNote(note:String, verseID:String?, title:String, color:Int) = notesTable.updateNote(this, note, verseID, title, color)
    fun getNoteForVerse(verseID: String) = notesTable.getNoteForVerse(this, verseID)
    fun deleteNote(verseID:String) = notesTable.deleteNote(this, verseID)


    fun insertProgress(progress: Progress) = progressTable.insert(this, progress)
    fun getLastProgress() = progressTable.getLastRow(this)
    fun updateLastRow(progress: Progress) = progressTable.updateLastRow(this, progress)
    fun getAllProgress() = progressTable.getAllProgress(this)


    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "QuranDataBase.db"


    }
}










