package com.example.newsapp.db

import androidx.room.TypeConverter
import com.example.newsapp.model.Source


/**
 * Ahmed Ali Ebaid
 * ahmedali26002844@gmail.com
 * 30/01/2021
 */
class Converter {
    @TypeConverter
    fun fromSource(source: Source):String{
        return source.name
    }
    @TypeConverter
    fun toSource(name:String): Source {
        return Source(name,name)
    }
}