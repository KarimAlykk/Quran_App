package com.example.customproject

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class NotingActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noting)

        dbHelper = DBHelper(this)


        val saveBtn: ImageButton = findViewById(R.id.saveNoteBtn)
        val cancelBtn: ImageButton = findViewById(R.id.cancelNoteBtn)
        val noteTitleLayout: TextInputLayout = findViewById(R.id.tlNoteTitle)
        val noteCont: EditText = findViewById(R.id.etNoteDesc)
        //val verseTV:TextView = findViewById(R.id.tvVerseNoting)
        val noteTitle:TextInputEditText = findViewById(R.id.etTitle)
        val rgColors:RadioGroup = findViewById(R.id.rgColors)
        val btnViewVerse:Button = findViewById(R.id.btnViewVerse)

        //verseTV.movementMethod = ScrollingMovementMethod()


        var isNoted = false
        var color = R.drawable.default_colorground

        //select a color for the note
        rgColors.setOnCheckedChangeListener { radioGroup, id ->
            when (id) {
                R.id.rbGold -> {
                    color = R.drawable.brown_colorground
                }
                R.id.rbGreen -> {
                    color = R.drawable.green_colorground
                }
                R.id.rbRed -> {
                    color = R.drawable.red_colorground
                }
                R.id.rbDefault -> {
                    color = R.drawable.default_colorground
                }
            }

        }

        val verse: Verse? = intent.getParcelableExtra("verse")

        if(verse != null) {
            note = dbHelper.getNoteForVerse(verse.verseID)
            //verseTV.text = verse.content
        }
        else {
            note = intent.getParcelableExtra("note")!!
            Log.i("verse", note.verseCont)
            //verseTV.text = note.verseCont
            isNoted = true
        }

        // will check the colors radio buttons in the notes activity bases on the current color of the note
        when (note.color) {
            R.drawable.red_colorground -> {
                rgColors.check(rgColors.getChildAt(0).id)
            }
            R.drawable.brown_colorground -> {
                rgColors.check(rgColors.getChildAt(1).id)
            }
            R.drawable.green_colorground -> {
                rgColors.check(rgColors.getChildAt(2).id)
            }
            R.drawable.default_colorground -> {
                rgColors.check(rgColors.getChildAt(3).id)
            }
        }

        if(note.verseID != null)
            isNoted = true

        noteTitle.setText(note.title)
        noteCont.setText(note.notes)


        btnViewVerse.setOnClickListener{
            if(verse != null){
                val viewVerse = ViewVerse.newInstance(verse.content, verse.number, verse.chapterName)
                viewVerse.isCancelable = false
                viewVerse.show(supportFragmentManager, null)
            }else{
                val viewVerse = ViewVerse.newInstance(note.verseCont, note.verseNumber, note.chapterName!!)
                viewVerse.isCancelable = false
                viewVerse.show(supportFragmentManager, null)
            }

        }


        // the saveBtn will update the note if the note already exists and insert new note it is not.
        // after saving or not saving the onBackPressed() will be called.
        //user can't save the note without title.
        saveBtn.setOnClickListener{
            if(noteTitle.text!!.isNotBlank()) {
                if(isNoted) {
                    if(verse != null) {
                        dbHelper.updateNote(noteCont.text.toString(), verse.verseID, noteTitle.text.toString(), color)
                    }
                    else {
                        note = Note(note.verseID, noteTitle.text.toString(), noteCont.text.toString(), note.verseCont, color, note.verseNumber, note.chapterName)
                        dbHelper.updateNote(note.notes, note.verseID, note.title, color)
                    }
                    Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show()
                    //Log.i("update", "verseUpdate")
                }
                else{
                    note = Note(verse!!.verseID,noteTitle.text.toString(), noteCont.text.toString(), verse.content, color, verse.number, verse.chapterName)
                    dbHelper.insertNoteToTable(note)

                    Toast.makeText(this, "Note Inserted", Toast.LENGTH_SHORT).show()
                }
                onBackPressed()
            }else {
                noteTitleLayout.error = getString(R.string.titleError)
            }

        }

        cancelBtn.setOnClickListener{
            onBackPressed()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences = this@NotingActivity.let {
            PreferenceManager.getDefaultSharedPreferences(it)
        }

        if(sharedPreferences?.getBoolean("theme_mode", true) == true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    override fun onBackPressed() {

        intent.putExtra("note", note)
        setResult(Activity.RESULT_OK, intent)
        //finish()
        super.onBackPressed()

    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}
