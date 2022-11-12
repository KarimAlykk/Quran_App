package com.example.customproject

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val VERSE = "VERSE"

/**
 * A simple [Fragment] subclass.
 * Use the [ReadVerseBookmark.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReadVerseBookmark : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters
    private var verse: Verse? = null

    private lateinit var tvVerse:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            verse = it.getParcelable(VERSE)

        }
    }


    override fun onResume() {
        super.onResume()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@ReadVerseBookmark.requireContext())
        tvVerse.textSize = sharedPreferences.getInt("text_size",20).toFloat()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_read_verse_bookmark, container, false)

        //val tvTitle:TextView = view.findViewById(R.id.tvVbookmarkTtitle)
        //val tvNumber:TextView = view.findViewById(R.id.tvVbookmarkNumber)
        tvVerse = view.findViewById(R.id.tvVBookmark)
        val ivExit:ImageView = view.findViewById(R.id.ivExit)
        val copyBtn:ImageButton = view.findViewById(R.id.ibCpy)
        val ibNoting:ImageButton = view.findViewById(R.id.ibNoting)
        val ibRemoveBookmark:ImageButton = view.findViewById(R.id.ibBookmark)

        val dbHelper = DBHelper(requireContext())

        //tvTitle.text = verse?.chapterName
        //tvNumber.text = verse?.number.toString()
        tvVerse.text = verse?.content





        copyBtn.setOnClickListener{
            val clipBoard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("simple Text", verse?.content)
            clipBoard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "Verse copied", Toast.LENGTH_SHORT).show()
        }


        ibNoting.setOnClickListener{
            val intent = Intent(this.context, NotingActivity::class.java).apply {
                putExtra("verse", verse)
            }
            startActivity(intent)

        }


        ibRemoveBookmark.setOnClickListener{


            deleteMarkedVerse()
            Toast.makeText(requireContext(), "Bookmark removed", Toast.LENGTH_SHORT).show()
            //checkVerseStatus()
            isCancelable = true
            dismiss()
        }


        ivExit.setOnClickListener{
            isCancelable = true
            dismiss()
        }

        return view

    }



    var deleteMarkedVerse= fun(){}

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReadVerseBookmark.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(verse: Verse) =
            ReadVerseBookmark().apply {
                arguments = Bundle().apply {
                   putParcelable(VERSE, verse)
                }
            }


    }
}