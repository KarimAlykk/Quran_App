package com.example.customproject


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import java.lang.Math.round
import java.time.LocalDate
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProgressFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProgressFragment : Fragment() {

    private lateinit var dbHelper: DBHelper
    private lateinit var tvVersesRead:TextView
    private lateinit var tvTotalHours:TextView
    private lateinit var tvActiveDays:TextView
    //private lateinit var progressViewModel: ProgressViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DBHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_progress, container, false)

         //progressViewModel = ViewModelProvider(requireActivity())[ProgressViewModel::class.java]

         lateinit var acheiv_100 :Achievement
         lateinit var acheiv_1500:Achievement
         lateinit var acheiv_All:Achievement

        val sharedPreferences = activity?.getSharedPreferences(getString(R.string.shared_pref_achiv),Context.MODE_PRIVATE)
        val overallProgressPref = activity?.getSharedPreferences("overall_progress", Context.MODE_PRIVATE)
        val stateSharedPref = activity?.getSharedPreferences("states_pref", Context.MODE_PRIVATE)


        //val cursor = dbHelper.getLastProgress()

        tvVersesRead = view.findViewById(R.id.tvNumberVRead)
        tvTotalHours = view.findViewById(R.id.tvTotalHours)
        tvActiveDays = view.findViewById(R.id.tvActiveDays)
        val tvLocalDate:TextView = view.findViewById(R.id.tvCrrntDate)

        tvLocalDate.text = LocalDate.now().toString()

        val ivRead_100:ImageView = view.findViewById(R.id.iv100V)
        val ivRead_1500:ImageView = view.findViewById(R.id.iv1500V)
        val ivRead_All:ImageView = view.findViewById(R.id.ivAllV)
        val rg:RadioGroup = view.findViewById(R.id.rg)


        rg.check(stateSharedPref!!.getInt("progress_checked", R.id.rbAll))
        when (rg.checkedRadioButtonId) {
            R.id.rbAll -> {
                getAll(overallProgressPref)
                Log.i("test10", "all")
            }
            R.id.rbWeekly -> {

               getWeekly()
                Log.i("test10", "week")
            }
            R.id.rbToday -> {
                getToday()
                Log.i("test10", "today")
            }
        }


        rg.setOnCheckedChangeListener{group, checkedId ->
            when (checkedId) {
                R.id.rbAll -> {
                    getAll(overallProgressPref)
                    Log.i("test10", "all")
                }
                R.id.rbWeekly -> {

                   getWeekly()
                    Log.i("test10", "week")
                }
                R.id.rbToday -> {
                    getToday()
                    Log.i("test10", "today")
                }
            }

            with(stateSharedPref.edit()){
                this?.putInt("progress_checked", rg.checkedRadioButtonId)
                this?.apply()
            }


        }



        val achievIV = mutableListOf(ivRead_100, ivRead_1500, ivRead_All)

         overallProgressPref?.apply {
             acheiv_100 = Achievement("read_100p", context?.getString(R.string.read_100A), getInt("total_verses", 0) > 100, R.drawable.reading_book)
             acheiv_1500 = Achievement("read_1500",context?.getString(R.string.read_1500A), getInt("total_verses", 0) > 1500, R.drawable.reading_book2)
             acheiv_All = Achievement("read_1500", context?.getString(R.string.read_all), getInt("total_verses", 0) >= 6236, R.drawable.reading_book3)

             val achievements = mutableListOf(acheiv_100, acheiv_1500, acheiv_All)

             for(i in achievements.indices) {
                 with(sharedPreferences!!.edit()) {
                     putBoolean(achievements[i].id, achievements[i].isVisible)
                 }
                 if(achievements[i].isVisible) {
                     achievIV[i].alpha = 1.0f
                 }else{
                     achievIV[i].alpha = 0.5f
                 }

                 achievIV[i].setOnClickListener{
                     val achievementDialog = AchievementDialog.newInstance(achievements[i])
                     //progressViewModel.achievement = achievements[i]
                     // progressViewModel.isDialogVisible = true
                     achievementDialog.show(childFragmentManager, null)

                 }
             }

           /*  tvVersesRead.text = "${getInt("total_verses", 0)}"
             tvTotalHours.text = "${getInt("total_time",0)}"
             if(cursor.moveToFirst()) {
                 tvActiveDays.text = "${cursor.getInt(0)}"
             }
*/
         }

        return view
    }





    //this function will get the progress in the last seven days including today. today + 6 = 7
    //it will also set each text view to the correct data
    private fun getWeekly(){
        val  cursor = dbHelper.getAllProgress()
        cursor.apply {

            val dateLastWeek = LocalDate.now().minusWeeks(1)

            Log.i("cursor", cursor.count.toString())
            var totalVerses = 0
            var totalTime = 0
            var i = 0
            while(moveToNext() && getString(1) != dateLastWeek.toString()) {

                totalVerses += getInt(2)
                totalTime += getInt(3)
                i++
            }

            tvVersesRead. text = totalVerses.toString()
            tvTotalHours.text = totalTime.toString()
            tvActiveDays.text = i.toString()
        }
    }


    //this function will get all the progress data from "overall_progress" shared preferences
    //it will also set each text view to the correct data
    private fun getAll(overallProgressPref:SharedPreferences?){
        val c = dbHelper.getLastProgress()
        tvVersesRead.text = "${overallProgressPref?.getInt("total_verses", 0)}"
        tvTotalHours.text = "${overallProgressPref?.getInt("total_time",0)}"
        if(c.moveToFirst()) {
            tvActiveDays.text = "${c.getInt(0)}"
        }
    }


    //this function will get the progress for today
    //it will also set each text view to the correct data
    private fun getToday(){
        val c = dbHelper.getLastProgress()
        if(c.moveToFirst()) {
            tvVersesRead.text = c.getInt(2).toString()
            tvTotalHours.text = c.getInt(3).toString()
            tvActiveDays.text = "1"
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProgressFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}