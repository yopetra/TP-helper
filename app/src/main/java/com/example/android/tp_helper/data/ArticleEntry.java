package com.example.android.tp_helper.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "article")
public class ArticleEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String content;

    public ArticleEntry(int id, String name, String content){
        this.id = id;
        this.name = name;
        this.content = content;
    }

    @Ignore
    public ArticleEntry(String name, String content){
        this.name = name;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
