package com.example.android.tp_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

    SharedPreferences sPref;
    private int textSpeed;
    private int textSpeedResult;
    private int speedCoeff = 40;
    private int textSize;
    private int initTextSize = 10; // text size in SP
    private int textSizeResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_text);

        loadPreferences();
        textSpeedResult = speedCoeff / textSpeed;
        textSizeResult = ((initTextSize * textSize) / 3) + 10; // magic where final text size have calculated

        vsTextView = findViewById(R.id.tvScrollingContent);
        vsTextView.setTextSize(textSizeResult);
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
        tvContent.setSpeed(textSpeedResult);

        playImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvContent.setSpeed(textSpeedResult);
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
                tvContent.setSpeed(textSpeedResult);
                tvContent.scroll();
            }
        });
    }

    private void loadPreferences() {
        sPref = getSharedPreferences(getString(R.string.settings_pref), MODE_PRIVATE);
        textSpeed = sPref.getInt(getString(R.string.speed), 5);
        textSize = sPref.getInt(getString(R.string.size), 5);
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