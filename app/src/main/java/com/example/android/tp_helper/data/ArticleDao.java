package com.example.android.tp_helper.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ArticleDao {

    @Query("SELECT * FROM article")
    LiveData<List<ArticleEntry>> loadAllArticles();

    @Query("SELECT * FROM article WHERE id =:id")
    ArticleEntry readArticleById(int id);

    @Insert
    void insertArticle(ArticleEntry articleEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateArticle(ArticleEntry articleEntry);

    @Delete
    void deleteArticle(ArticleEntry articleEntry);
}
