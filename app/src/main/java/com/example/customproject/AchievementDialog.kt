package com.example.customproject

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private const val ARG_PARAM1 = "achievement"


class AchievementDialog : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters
    private var achievement: Achievement? = null
    var isShowing = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            achievement = it.getParcelable(ARG_PARAM1)
            isShowing = true

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_acheivement_dialog, container, false)

        val ivAcheiv:ImageView = view.findViewById(R.id.ivAcheivDialog)
        val tvAcheiv: TextView = view.findViewById(R.id.tvAcheivDescDialog)
        tvAcheiv.text = achievement?.desc

        if(achievement?.isVisible == true)
        {
            ivAcheiv.setImageResource(achievement!!.image)
            ivAcheiv.alpha = 1.0f
        }else{
            ivAcheiv.setImageResource(achievement!!.image)
            ivAcheiv.alpha = 0.5f
        }




        return view
    }


    override fun dismiss() {
        super.dismiss()
        isShowing = false
    }


    companion object {

        @JvmStatic
        fun newInstance(achievement: Achievement ) =
            AchievementDialog().apply {
                arguments = Bundle().apply {
                   putParcelable(ARG_PARAM1, achievement)
                }
            }
    }
}