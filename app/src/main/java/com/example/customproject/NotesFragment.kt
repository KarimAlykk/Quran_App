package com.example.customproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toolbar
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isNotEmpty
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import kotlinx.coroutines.launch


class NotesFragment : Fragment() {

    private  var notesViewModel: NotesViewModel? = null
    private lateinit var adapter: NotesAdapter
    private lateinit var rvItems:RecyclerView
    private var notesList = mutableListOf<Note>()
    private lateinit var getContent:ActivityResultLauncher<Intent>
    private var actionMode: ActionMode? = null
    private lateinit var dbHelper: DBHelper
    private lateinit var tvNoNotes:TextView
    private lateinit var notesToBeDeleted:MutableList<Note>
    private var rgColorFilters:RadioGroup? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbHelper = DBHelper(requireContext())

        getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result:ActivityResult ->

            //Log.i("register", "out")
            if(result.resultCode == Activity.RESULT_OK)
            {
                val data = result.data
                val note = data?.getParcelableExtra<Note>("note")

                adapter.mNotes[notesViewModel?.notePos!!].apply {
                    if(color == note?.color || rgColorFilters?.checkedRadioButtonId == R.id.rbAllFilter || rgColorFilters?.checkedRadioButtonId == -1){
                        title = note!!.title
                        notes = note.notes
                        color = note.color
                        adapter.notifyItemChanged(notesViewModel?.notePos!!)
                    }else if(rgColorFilters?.checkedRadioButtonId != R.id.rbAllFilter && color != note?.color){
                        adapter.mNotes.remove(this)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notes, container, false)

        notesViewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]


        rvItems = view.findViewById(R.id.rvNoteItems)
        tvNoNotes = view.findViewById(R.id.tvEmptyNotes)
        notesToBeDeleted = mutableListOf()
        rgColorFilters = view.findViewById(R.id.rgColorFilters)


        val stateSharedPref = activity?.getSharedPreferences("states_pref", Context.MODE_PRIVATE)

        rgColorFilters?.check(stateSharedPref!!.getInt("filter_color_checked",R.id.rbAllFilter))

        /*if(rgColorFilters.checkedRadioButtonId == -1){
            addDataToList(notesList, dbHelper)
        }*/




        adapter  = NotesAdapter(notesList,this,{note, pos ->//open the notes activity
            val intent = Intent(this.context, NotingActivity::class.java).apply {
                putExtra("note",note)
                //notesViewModel.isViewSelected = rvItems.getChildAt(pos).isSelected
                notesViewModel?.notePos = pos
            }
            getContent.launch(intent)
            if(actionMode != null){
                actionMode?.finish()
            }
        },{note, v -> //description in the notesAdapter
            when(actionMode){
                null -> {
                    if(!notesToBeDeleted.contains(note)){
                        notesToBeDeleted.add(note)
                    }
                    actionMode = activity?.startActionMode(actionModeCallback)
                    v.isSelected = true
                    notesViewModel?.noteFromAdapter = note
                    true
                }
                else -> {

                    if(!notesToBeDeleted.contains(note)){
                        notesToBeDeleted.add(note)
                        v.isSelected = true
                    }else{

                        notesToBeDeleted.remove(note)
                        v.isSelected = false
                    }

                    if(notesToBeDeleted.size == 0){
                        actionMode?.finish()
                    }

                    actionMode?.title = notesToBeDeleted.size.toString()

                    false
                }

            }

        })

        //Log.i("test3","inn")

        rvItems.adapter = adapter
        rvItems.layoutManager =LinearLayoutManager(this.context)
        (rvItems.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false



        when (rgColorFilters?.checkedRadioButtonId){
            R.id.rbRedFilter ->{ notesViewModel?.getColoredNotes(dbHelper, adapter,notesList, R.drawable.red_colorground)}
            R.id.rbBrownFilter -> {notesViewModel?.getColoredNotes(dbHelper,adapter, notesList, R.drawable.brown_colorground)}
            R.id.rbGreenFilter -> {notesViewModel?.getColoredNotes(dbHelper, adapter,notesList, R.drawable.green_colorground)}
            R.id.rbDefaultFilter -> {notesViewModel?.getColoredNotes(dbHelper, adapter,notesList, R.drawable.default_colorground)}
            R.id.rbAllFilter -> {notesViewModel?.getAllNotes(dbHelper, notesList, adapter)}
        }



        checkTvNoteView()
        //filter the notes based on the color
        rgColorFilters?.setOnCheckedChangeListener{rg, id ->

            if(actionMode!= null){
                actionMode?.finish()
            }
            when(id){
                R.id.rbRedFilter ->{ notesViewModel?.getColoredNotes(dbHelper, adapter,notesList, R.drawable.red_colorground)}
                R.id.rbBrownFilter -> {notesViewModel?.getColoredNotes(dbHelper,adapter, notesList, R.drawable.brown_colorground)}
                R.id.rbGreenFilter -> {notesViewModel?.getColoredNotes(dbHelper, adapter,notesList, R.drawable.green_colorground)}
                R.id.rbDefaultFilter -> {notesViewModel?.getColoredNotes(dbHelper, adapter,notesList, R.drawable.default_colorground)}
                R.id.rbAllFilter -> {notesViewModel?.getAllNotes(dbHelper, notesList, adapter)}
            }


            with(stateSharedPref!!.edit()){
                putInt("filter_color_checked", id)
                apply()
            }
            checkTvNoteView()
        }



        return view
    }


    // this actionModeCallback is for the contextual action menu
    //it have a bin icon that when pressed it opens NoteDeleteDialog to check if the user wants to delete these selected notes.
    //when it is destroyed if the mNotes list in the NotesAdapter is not empty
    //it will make sure that views are reset to their original colors
    private val actionModeCallback = object: ActionMode.Callback{
        var isBinPressed = false

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            val inflater:MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.notes_opt, menu)

            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, p1: Menu): Boolean {
            mode.title = notesToBeDeleted.size.toString()
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when(item.itemId) {
                R.id.binOpt -> {
                    isBinPressed = true
                    noteDeleteDialog.deleteNote = { deleteNote()
                        checkTvNoteView()
                    mode.finish()}

                    noteDeleteDialog.cancelDelete = {
                        notesToBeDeleted.clear()
                        mode.finish()
                    }
                    noteDeleteDialog.show(childFragmentManager, null)
                    //mode.finish()
                    true
                }
                else ->  false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {

            adapter.apply {
                if(mNotes.isNotEmpty()){
                    for (i in 0 until mNotes.size){
                        rvItems.getChildAt(mNotes.indexOf(mNotes[i])).setBackgroundResource(mNotes[i].color)
                        //rvItems.getChildAt(mNotes.indexOf(mNotes[i])).isSelected = false
                        adapter.notifyItemChanged(mNotes.indexOf(mNotes[i]))
                    }
                }
            }

            if(!isBinPressed){
                notesToBeDeleted.clear()
            }
            isBinPressed = false
            actionMode = null
        }
    }

    //extract the notes from the database table for Notes
/*    private fun addDataToList(list: MutableList<Note>, dbHelper: DBHelper) {
        val cursor = dbHelper.getNotes()
        list.clear()
        with(cursor) {
            while(moveToNext()) {
                val note = Note(getString(1), getString(2), getString(3), getString(4),getInt(5))
                list.add(note)
            }
        }
        cursor.close()
    }*/


    override fun onResume() {
        super.onResume()
        checkTvNoteView()
    }
    override fun onPause() {
        super.onPause()
        if(noteDeleteDialog.isShowing) {
            noteDeleteDialog.dismiss()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if(actionMode!= null)
        {
            actionMode?.finish()
        }
    }


    companion object{
        var noteDeleteDialog = NoteDeleteDialog()
    }

    //this function delete the selected notes from the table and from the recycler view
    private fun deleteNote()
    {
        for(note in notesToBeDeleted){
            //adapter.mNotes.remove(note)
           notesList.remove(note)
        }
        adapter.notifyDataSetChanged()

        lifecycleScope.launch {
            for(note in notesToBeDeleted){
                dbHelper.deleteNote(note.verseID!!)

            }
            notesToBeDeleted.clear()
            //viewsSelected.clear()
        }

    }


     //will check if the noteList empty and will set message based on the current color selected

     fun checkTvNoteView(){
        if(notesList.isEmpty()){
            when(rgColorFilters?.checkedRadioButtonId){
                R.id.rbRedFilter -> {tvNoNotes.text = getText(R.string.no_red)}
                R.id.rbGreenFilter -> {tvNoNotes.text = getText(R.string.no_green)}
                R.id.rbBrownFilter -> {tvNoNotes.text = getText(R.string.no_brown)}
                R.id.rbDefaultFilter -> {tvNoNotes.text =getText(R.string.no_defualt)}
                R.id.rbAllFilter -> {tvNoNotes.text = getText(R.string.no_notes)}
            }
            tvNoNotes.visibility = View.VISIBLE
        }else{
            tvNoNotes.visibility = View.GONE
        }
    }

}

