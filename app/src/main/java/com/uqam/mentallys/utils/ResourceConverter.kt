package com.uqam.mentallys.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.uqam.mentallys.model.Days
import com.uqam.mentallys.model.resource.*

class ResourceConverter {

    private val gson = Gson()

    @TypeConverter
    fun activitiesFromJson(json: String?): List<Activity> {
        val type = object : TypeToken<List<Activity>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun categoriesFromJson(json: String?): List<Category> {
        val type = object : TypeToken<List<Category>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun clientsFromJson(json: String?): List<Client> {
        val type = object : TypeToken<List<Client>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun costFromJson(json: String?): Cost {
        val type = object : TypeToken<Cost>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun languagesFromJson(json: String?): List<Language> {
        val type = object : TypeToken<List<Language>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun modalitiesFromJson(json: String?): List<Modality> {
        val type = object : TypeToken<List<Modality>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun openingHoursFromJson(json: String?): List<Pair<Days, List<Pair<Pair<Int, Int>, Pair<Int, Int>>>>> {
        val type = object :
            TypeToken<List<Pair<Days, List<Pair<Pair<Int, Int>, Pair<Int, Int>>>>>>() {}.type
        val temp = gson.fromJson(json,
            type) as List<Pair<String, List<LinkedTreeMap<LinkedTreeMap<Int, Int>, LinkedTreeMap<Int, Int>>>>>
        return temp.map { original ->
            Pair(Days.valueOf(original.first),
                original.second.map { item ->
                    Pair(
                        Pair(item.values.first().values.first().toInt(),
                            item.values.first().values.last().toInt()),
                        Pair(item.values.last().values.first().toInt(),
                            item.values.last().values.last().toInt())
                    )
                })
        }

    }

    @TypeConverter
    fun tagsFromJson(json: String?): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun activitiesFromJson(item: List<Activity>?): String {
        return gson.toJson(item)
    }

    @TypeConverter
    fun categoriesFromJson(item: List<Category>?): String {
        return gson.toJson(item)
    }

    @TypeConverter
    fun clientsFromJson(item: List<Client>?): String {
        return gson.toJson(item)
    }

    @TypeConverter
    fun costFromJson(item: Cost?): String {
        return gson.toJson(item)
    }

    @TypeConverter
    fun languagesFromJson(item: List<Language>?): String {
        return gson.toJson(item)
    }

    @TypeConverter
    fun modalitiesFromJson(item: List<Modality>?): String {
        return gson.toJson(item)
    }

    @TypeConverter
    fun openingHoursFromJson(item: List<Pair<Days, List<Pair<Pair<Int, Int>, Pair<Int, Int>>>>>?): String {
        return gson.toJson(item)
    }

    @TypeConverter
    fun tagsFromJson(item: List<String>): String {
        return gson.toJson(item)
    }


}