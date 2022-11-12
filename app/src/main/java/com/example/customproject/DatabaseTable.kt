package com.example.customproject

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns


open class DatabaseTable{

    open val TABLE_NAME: String? = null
    open val SQL_CREATE_ENTERIES:String? = null
    open val SQL_DELETE_ENTRIES: String? = null

}

object MarkedObject
{
    const val VERSE_ID = "verseID"
    const val COLUMN_NAME_CHAPTER = "chapter"
    const val COLUMN_NAME_VERSE = "verse"
    const val COLUMN_NAME_NUMBER = "number"

    class MarkedVerseTable: DatabaseTable() {


        override val TABLE_NAME = "versesMarked"
        override val SQL_CREATE_ENTERIES = "CREATE TABLE  $TABLE_NAME (" + " $VERSE_ID TEXT PRIMARY KEY, " + " $COLUMN_NAME_CHAPTER TEXT," + " $COLUMN_NAME_VERSE TEXT, " + " $COLUMN_NAME_NUMBER INTEGER) "
        override val SQL_DELETE_ENTRIES = "DROP TABLE IF EXIST $TABLE_NAME"


        //This function will add the bookmarked verse to the table
        fun addData(dbHelper:DBHelper, verse:Verse) :Boolean
        {

            val values = ContentValues().apply {
                put(VERSE_ID, verse.verseID)
                put(COLUMN_NAME_CHAPTER, verse.chapterName)
                put(COLUMN_NAME_VERSE, verse.content)
                put(COLUMN_NAME_NUMBER, verse.number)
            }
            val db = dbHelper.writableDatabase


            val newId = db?.insert(TABLE_NAME, null, values)

            return newId?.toInt() != -1
        }


        //This function return a cursor that contains all the bookmarked verses
        fun getMarkedVerses(dbHelper: DBHelper): Cursor {
            val db = dbHelper.readableDatabase

            val projection = arrayOf(
                VERSE_ID,
                COLUMN_NAME_VERSE,
                COLUMN_NAME_CHAPTER,
                COLUMN_NAME_NUMBER
            )
            val sortOrder = "${COLUMN_NAME_VERSE} ASC"
            //cursor.moveToNext()
            // Log.i("ReadDataTest", cursor.getString(1))
            return db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
            )
        }





        // this function will check if the verse is bookmarekd or not
        fun checkDataExists(dbHelper: DBHelper,verseID: String) :Boolean
        {
            val db = dbHelper.readableDatabase

            val projection = arrayOf(COLUMN_NAME_CHAPTER, COLUMN_NAME_NUMBER)

            val selection = "${VERSE_ID} = ?"
            val selectionArgs = arrayOf(verseID)

            val cursor=  db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
            )

            //cursor.close()

            return (cursor.count > 0)

        }


        //the function will delete bookmarked verse from the table
        fun deleteVerseMarked(dbHelper: DBHelper,verseID:String): Int {
            val db = dbHelper.writableDatabase
            val selection = "${VERSE_ID} LIKE ?"
            val selectionArgs = arrayOf(verseID)

            return db.delete(TABLE_NAME, selection, selectionArgs)
        }



    }
}

////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////

object NoteObject
{
    const val NOTE_ID = "noteID"
    const val VERSE_ID = "verseID"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_NOTE = "note"
    const val COLUMN_VERSE = "verse"
    const val COLUMN_COLOR = "color"
    const val COLUMN_NAME_NUMBER = "number"
    const val COLUMN_NAME_CHAPTER_NAME = "chapterName"



    class NotesTable : DatabaseTable()
    {
        override val TABLE_NAME = "verseNotes"
        override val SQL_CREATE_ENTERIES = "CREATE TABLE $TABLE_NAME (" + " $NOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "$VERSE_ID TEXT, " +"$COLUMN_NAME_TITLE TEXT, "+ "$COLUMN_VERSE TEXT, " +
                "$COLUMN_NAME_NOTE TEXT, " + "$COLUMN_COLOR INTEGER, " + "$COLUMN_NAME_NUMBER INTEGER, " + "$COLUMN_NAME_CHAPTER_NAME TEXT)"
        override val SQL_DELETE_ENTRIES = "DROP TABLE IF EXIST $TABLE_NAME"

        //this function will insert new row in the table for new note.
        fun insertNoteToTable(dbHelper: DBHelper, note:Note) : Boolean
        {
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put(VERSE_ID, note.verseID)
                put(COLUMN_NAME_TITLE, note.title)
                put(COLUMN_NAME_NOTE, note.notes)
                put(COLUMN_VERSE, note.verseCont)
                put(COLUMN_COLOR,note.color)
                put(COLUMN_NAME_NUMBER, note.verseNumber)
                put(COLUMN_NAME_CHAPTER_NAME, note.chapterName)

            }

            val newRowId = db.insert(TABLE_NAME, null, values)
            //Log.i("newRowIdNoteInsertion", newRowId.toString())

            return newRowId.toInt() != -1
        }

        // I am not using this function any more
        fun getVerseNote(dbHelper: DBHelper, verseID:String) : Cursor
        {
            val db = dbHelper.readableDatabase

            val projection = arrayOf(VERSE_ID, COLUMN_NAME_TITLE, COLUMN_NAME_NOTE, COLUMN_VERSE, COLUMN_COLOR, COLUMN_NAME_NUMBER, COLUMN_NAME_CHAPTER_NAME)
            val selection = "$VERSE_ID = ?"
            val selectionArgs = arrayOf(verseID)

            val cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
            )

            //Log.i("cursorCount", cursor.count.toString())

            //cursor.close()
            return cursor

        }


        // this function will return a specific note for specific verse using the verseID
        //it will return a note that is empty if there is no note for this verse
        fun getNoteForVerse(dbHelper: DBHelper, verseID:String) : Note
        {
            val db = dbHelper.readableDatabase

            val projection = arrayOf(VERSE_ID, COLUMN_NAME_TITLE, COLUMN_NAME_NOTE, COLUMN_VERSE,
                COLUMN_COLOR, COLUMN_NAME_NUMBER, COLUMN_NAME_CHAPTER_NAME)
            val selection = "$VERSE_ID = ?"
            val selectionArgs = arrayOf(verseID)

            val cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
            )

            //Log.i("cursorCount", cursor.count.toString())

            var note = Note(null)

                if(cursor.moveToFirst()) {
                note = Note(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5), cursor.getString(6))
           }
            cursor.close()

            return note

        }

        //this function will update a note in the table.
        fun updateNote(dbHelper: DBHelper, note: String, verseID:String?, title: String, color:Int): Int {
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put(COLUMN_NAME_NOTE, note)
                put(COLUMN_NAME_TITLE, title )
                put(COLUMN_COLOR, color)
            }

            val selection = "$VERSE_ID = ?"
            val selectionArgs = arrayOf(verseID)


            return db.update(TABLE_NAME, values, selection, selectionArgs)

        }



        //This function returns all the notes in the notes table
        //@return Cursor
        fun getNotes(dbHelper: DBHelper): Cursor {
            val db = dbHelper.readableDatabase

            val projection = arrayOf(NOTE_ID, VERSE_ID, COLUMN_NAME_TITLE, COLUMN_NAME_NOTE, COLUMN_VERSE, COLUMN_COLOR, COLUMN_NAME_NUMBER, COLUMN_NAME_CHAPTER_NAME)
            val sortOrder = "$NOTE_ID ASC"

            return db.query(TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder)

        }

        //the function will delete a note from the table based on the verse id
        fun deleteNote(dbHelper: DBHelper, verseID: String) {
            val db = dbHelper.writableDatabase
            val selection = "$VERSE_ID LIKE ?"
            val selectionArgs = arrayOf(verseID)

            db.delete(TABLE_NAME, selection, selectionArgs)
        }

    }



}

object ProgressObject{

    const val COLUMN_NAME_DATE = "date"
    const val COLUMN_PROGRESS_ID = "Id"
    const val COLUMN_NAME_TOTAL_VERSES = "total_verses"
    const val COLUMN_NAME_TOTAL_HOURS = "total_hours"
    const val COLUMN_NAME_TOTAL_CHAPTERS = "total_chapters"

    class ProgressTable:DatabaseTable() {

         override val TABLE_NAME = "progress_table"

         override val SQL_CREATE_ENTERIES =  "CREATE TABLE $TABLE_NAME (" + "$COLUMN_PROGRESS_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                 "$COLUMN_NAME_DATE TEXT," +  "$COLUMN_NAME_TOTAL_VERSES INTEGER, "+
                 "$COLUMN_NAME_TOTAL_HOURS INTEGER, " + "$COLUMN_NAME_TOTAL_CHAPTERS INTEGER )"

        override val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"


        // this function will insert new progress into the table
        fun insert(dbHelper: DBHelper, progress: Progress) : Boolean
        {
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {

                put(COLUMN_NAME_DATE, progress.date)
                put(COLUMN_NAME_TOTAL_VERSES, progress.totalVerses)
                put(COLUMN_NAME_TOTAL_HOURS, progress.totalHours)
                put(COLUMN_NAME_TOTAL_CHAPTERS, progress.totalChapters)
            }

            val newId = db.insert(TABLE_NAME, null, values)

            return newId.toInt() != -1

        }

        // this function will return the last progress.
        fun getLastRow(dbHelper: DBHelper) : Cursor
        {
            val db = dbHelper.readableDatabase

            val query = "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_PROGRESS_ID DESC LIMIT 1"

            val cursor = db.rawQuery(query, null)

            //cursor.close()

            return cursor

        }

        //this function will return all the recorded progress in the table
        fun getAllProgress(dbHelper: DBHelper):Cursor
        {
            val db = dbHelper.readableDatabase

            val query = "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_PROGRESS_ID DESC"

            val cursor = db.rawQuery(query, null)


            return cursor
        }

        //the function will update the last progress inseted.
        fun updateLastRow(dbHelper: DBHelper, progress: Progress) : Int
        {
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put(COLUMN_NAME_TOTAL_VERSES, progress.totalVerses)
                put(COLUMN_NAME_TOTAL_HOURS, progress.totalHours)
                put(COLUMN_NAME_TOTAL_CHAPTERS, progress.totalChapters)
            }


            val selection = "$COLUMN_NAME_DATE = ?"
            val selectionArgs = arrayOf(progress.date)

            val count = db.update(TABLE_NAME, values, selection, selectionArgs)

            //Log.i("rowsUpdated", count.toString())

            return count

        }

    }
}







