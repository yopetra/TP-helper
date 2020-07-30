package com.example.android.tp_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;

import com.example.android.tp_helper.data.AppDatabase;
import com.example.android.tp_helper.data.ArticleEntry;
import com.example.android.tp_helper.utils.VerticalScrollingTextView;

import java.util.concurrent.ExecutionException;

public class ScrollingTextActivity extends AppCompatActivity {

    private AppDatabase mDb;
    private VerticalScrollingTextView vsTextView;
    ImageView playImageView;
    ImageView pauseImageView;
    ImageView beginImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_text);
        vsTextView = findViewById(R.id.tvScrollingContent);
        mDb = AppDatabase.getInstance(this);
        final int[] currentY = new int[1];

        playImageView = findViewById(R.id.iv_play);
        pauseImageView = findViewById(R.id.iv_pause);
        beginImageView = findViewById(R.id.iv_to_begin);

        Intent intent = getIntent();

        int articleId = intent.getExtras().getInt(getString(R.string.article_id));
        String content = getArticleContentById(articleId);
        vsTextView.setText(content);

        final VerticalScrollingTextView tvContent = (VerticalScrollingTextView) findViewById(R.id.tvScrollingContent);
        tvContent.setMovementMethod(new ScrollingMovementMethod());
        tvContent.setSpeed(10);

        playImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvContent.setSpeed(10);
                tvContent.scrollFrom(currentY[0]);
            }
        });

        pauseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentY[0] = tvContent.getCurrentY();
                tvContent.setSpeed(100000);
                tvContent.scrollFrom(currentY[0]);
            }
        });

        beginImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvContent.setSpeed(10);
                tvContent.scroll();
            }
        });
    }

    private String getArticleContentById(int articleId) {
        String content = null;

        ReadArticleTask readArticleTask = new ReadArticleTask();
        readArticleTask.execute(articleId);
        ArticleEntry articleEntry = null;
        try {
            articleEntry = readArticleTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        content = articleEntry.getContent();

        return content;
    }

    private class ReadArticleTask extends AsyncTask<Integer, Void, ArticleEntry> {
        @Override
        protected ArticleEntry doInBackground(Integer... integers) {

            int articleId = integers[0];

            ArticleEntry articleEntry = mDb.articleDao().readArticleById(articleId);

            return articleEntry;
        }

        @Override
        protected void onPostExecute(ArticleEntry articleEntry) {
            super.onPostExecute(articleEntry);
        }
    }
}