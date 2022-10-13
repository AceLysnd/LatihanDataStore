package com.ace.datastore.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ace.datastore.data.AccountDataStoreManager
import com.ace.datastore.data.Prefs
import kotlinx.coroutines.launch
import java.util.prefs.Preferences

class MainViewModel(private val pref: AccountDataStoreManager) : ViewModel() {

    fun setAccount(username: String,password: String) {
        viewModelScope.launch {
            pref.setAccount(username, password)
        }
    }

    fun saveLoginInfo(loginInfo: Boolean) {
        viewModelScope.launch {
            pref.setLoginInfo(loginInfo)
        }
    }

    fun getAccount(): LiveData<Prefs> {
        return pref.getAccount().asLiveData()
    }


    fun getLoginInfo(): LiveData<Boolean> {
        return pref.getLoginInfo().asLiveData()
    }


}