package com.uqam.mentallys.utils

import androidx.lifecycle.MutableLiveData

object SharedState {

    private val savedState: MutableLiveData<MutableMap<String, Any>> = MutableLiveData(HashMap())

    fun saveState(key: String, value: Any) {
        savedState.value?.set(key, value)
    }

    fun getState(key: String): Any? {
        return savedState.value?.get(key)
    }

    fun eraseState(key: String) {
        savedState.value?.remove(key)
    }
}