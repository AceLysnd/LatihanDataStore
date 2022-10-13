package com.ace.datastore.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ace.datastore.presentation.MainViewModel
import com.ace.datastore.data.AccountDataStoreManager
import java.lang.IllegalArgumentException

class ViewModelFactory(private val pref: AccountDataStoreManager) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(pref) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
}