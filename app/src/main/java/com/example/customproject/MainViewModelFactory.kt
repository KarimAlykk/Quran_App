package com.example.customproject

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class MainActivityViewModelFactory(private var context: Context) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainActivityViewModel::class.java))
        {
            return MainActivityViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}