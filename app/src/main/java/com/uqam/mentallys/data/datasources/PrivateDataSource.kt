package com.uqam.mentallys.data.datasources

import com.uqam.mentallys.R
import com.uqam.mentallys.model.Private
import javax.inject.Inject

class PrivateDataSource @Inject constructor() {

    fun loadPrivate(): List<Private> {
        return listOf(
            Private(title = "Info-Social 811", icon = R.drawable.ic_baseline_call_24),
            Private(title = "Contacter un CLSC", icon = R.drawable.home)
        )
    }
}
