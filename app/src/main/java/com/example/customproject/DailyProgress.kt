package com.example.customproject

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DailyProgress.newInstance] factory method to
 * create an instance of this fragment.
 */
class DailyProgress : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_daily_progress, container, false)
        val dbHelper = DBHelper(requireContext())

        val tvLastVersesProgress:TextView = view.findViewById(R.id.tvNumberVRead2)
        val tvLastChaptersProgress:TextView = view.findViewById(R.id.tvChaptersRead2)
        val tvLastTimeProgress:TextView = view.findViewById(R.id.tvTotalHours2)
        val tvLastProgressTitle:TextView = view.findViewById(R.id.tvLastProg)

       val cursor = dbHelper.getLastProgress()

        if(cursor.moveToFirst()) {
            tvLastProgressTitle.text = getString(R.string.last_progress, cursor.getString(1))
            tvLastVersesProgress.text = cursor.getInt(2).toString()
            tvLastChaptersProgress.text = cursor.getInt(4).toString()
            tvLastTimeProgress.text = cursor.getInt(3).toString()
        }


        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DailyProgress().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}