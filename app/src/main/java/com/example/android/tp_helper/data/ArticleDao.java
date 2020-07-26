package com.example.android.tp_helper.data;

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
    List<ArticleEntry> loadAllArticles();

    @Insert
    void insertArticle(ArticleEntry articleEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateArticle(ArticleEntry articleEntry);

    @Delete
    void deleteArticle(ArticleEntry articleEntry);
}
