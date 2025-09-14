package com.example.krishimitra.data.local.json

import android.content.Context
import com.example.krishimitra.domain.StateModel
import com.example.krishimitra.R
import com.google.gson.Gson

fun loadStatesAndDistricts(context: Context): List<StateModel> {
    val inputStream = context.resources.openRawResource(R.raw.states_districts)
    val jsonString = inputStream.bufferedReader().use { it.readText() }

    return Gson().fromJson(jsonString, Array<StateModel>::class.java).toList()
}
