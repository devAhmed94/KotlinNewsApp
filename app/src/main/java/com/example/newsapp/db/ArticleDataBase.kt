package com.example.newsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.model.Article


/**
 * Ahmed Ali Ebaid
 * ahmedali26002844@gmail.com
 * 30/01/2021
 */
@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class ArticleDataBase : RoomDatabase() {
    abstract fun getArticleDao():ArticleDao

    companion object {
        @Volatile
        private var instance: ArticleDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK)
        {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDataBase::class.java,
                "database_name"
            )
                .build()
    }
}