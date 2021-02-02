package com.example.newsapp.db
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp.model.Article


/**
 * Ahmed Ali Ebaid
 * ahmedali26002844@gmail.com
 * 30/01/2021
 */
@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article : Article):Long
    @Delete
    suspend fun delete(article: Article)

    @Query("select * from table_article")
    fun getAllNews():LiveData<List<Article>>
}