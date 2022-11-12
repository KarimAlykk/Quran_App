package com.example.customproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "verse_cont"
private const val ARG_PARAM2 = "number"
private const val ARG_PARAM3 = "chapter_name"



/**
 * A simple [Fragment] subclass.
 * Use the [ViewVerse.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewVerse : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters
    private var verseCont: String? = null
    private var chapterName: String? = null
    private var number: Int? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            verseCont = it.getString(ARG_PARAM1)
            chapterName = it.getString(ARG_PARAM3)
            number = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_verse, container, false)
        val tvVerse:TextView = view.findViewById(R.id.tvVNote)
        val tvNumber:TextView = view.findViewById(R.id.tvViewVerseNumber)
        val tvName:TextView = view.findViewById(R.id.tvViewVersName)
        val exitBtn: ImageView = view.findViewById(R.id.ivExitVVNote)


        tvVerse.text = verseCont
        tvNumber.text = number.toString()
        tvName.text = chapterName


        exitBtn.setOnClickListener{
            isCancelable=true
            dismiss()
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewVerse.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(verseCont:String, number:Int, chapterName:String) =
            ViewVerse().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, verseCont)
                    putInt(ARG_PARAM2, number)
                    putString(ARG_PARAM3, chapterName)



                }
            }
    }
}