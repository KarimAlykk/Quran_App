package com.example.customproject

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private const val ARG_PARAM1 = "verse"
private const val ARG_PARAM2 = "fragmentName"


class VerseUtilities : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters
    private var verse: Verse? = null

    var isShowing = false
    //private var note: Note? = null
    private lateinit var dbHelper: DBHelper
    private lateinit var bookmarkBtn:Button
    private var isVerseExists = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DBHelper(requireContext())
        arguments?.let {
            verse = it.getParcelable(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_verse_utilities, container, false)



        val toNoteBtn:Button = view.findViewById(R.id.btnToNote)
        val copyBtn:Button = view.findViewById(R.id.btnCopy)

        bookmarkBtn = view.findViewById(R.id.btnBookmark)

        isVerseExists = dbHelper.checkDataExists(verse!!.verseID)
        checkVerseStatus()

        copyBtn.setOnClickListener{
            val clipBoard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip:ClipData = ClipData.newPlainText("simple Text", verse?.content)
            clipBoard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "Verse copied", Toast.LENGTH_SHORT).show()
            dismiss()
        }


        toNoteBtn.setOnClickListener{
            val intent = Intent(this.context, NotingActivity::class.java).apply {
                putExtra("verse", verse)
            }
            startActivity(intent)
            dismiss()
            //parentFragmentManager.popBackStack()

        }


        bookmarkBtn.setOnClickListener{
            if(isVerseExists)
            {
               //dbHelper.deleteVerseMarked(verse!!.verseID)
                deleteMarkedVerse()
                isVerseExists = false
                Toast.makeText(requireContext(), "Bookmark removed", Toast.LENGTH_SHORT).show()

            }
            else{
                verse?.let { it1 -> dbHelper.addData(it1) }
                isVerseExists = true
                Toast.makeText(requireContext(), "Verse bookmarked", Toast.LENGTH_SHORT).show()

            }
            //checkVerseStatus()
            dismiss()
        }

        return view
    }

    private fun checkVerseStatus() {
        if (isVerseExists) {
            bookmarkBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                R.drawable.ic_bookmark_remove_fill,
                0,
                0
            )

        } else {
            bookmarkBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                R.drawable.ic_bookmark_add_fill,
                0,
                0
            )


        }
    }

    var deleteMarkedVerse = fun(){
    }



    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
        isShowing = true
    }

    override fun dismiss() {
        super.dismiss()
        isShowing = false
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }

    companion object {

        @JvmStatic
        fun newInstance(verse: Verse) =
            VerseUtilities().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, verse)

                    //putParcelable(ARG_PARAM2, note)
                }
            }

    }
}