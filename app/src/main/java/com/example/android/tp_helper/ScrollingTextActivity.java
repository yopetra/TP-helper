package com.example.android.tp_helper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.android.tp_helper.data.AppDatabase;
import com.example.android.tp_helper.data.ArticleEntry;
import com.example.android.tp_helper.utils.VerticalScrollingTextView;

import java.util.concurrent.ExecutionException;

public class ScrollingTextActivity extends AppCompatActivity {

    private AppDatabase mDb;
    private VerticalScrollingTextView vsTextView;
    private LinearLayout rootLinearLayout;
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

    private int textColor;
    private final int BLACK = 11;
    private final int WHITE = 12;
    private final int YELLOW = 13;
    private final int BLUE = 14;
    private int textColorResult;

    private int backColor;
    private int backColorResult;

    private boolean isTextMirrored;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_text);

        loadPreferences();
        textSpeedResult = speedCoeff / textSpeed;
        textSizeResult = ((initTextSize * textSize) / 3) + 10; // magic where final text size have calculated
        textColorResult = convertColor(textColor);
        backColorResult = convertColor(backColor);

        vsTextView = findViewById(R.id.tvScrollingContent);
        vsTextView.setTextSize(textSizeResult);
        vsTextView.setTextColor(ContextCompat.getColor(this, textColorResult));
        if(isTextMirrored){vsTextView.setScaleY(-1);}

        rootLinearLayout = findViewById(R.id.ll_scrolling_root);
        rootLinearLayout.setBackgroundColor((ContextCompat.getColor(this, backColorResult)));

        mDb = AppDatabase.getInstance(this);
        final int[] currentY = new int[1];

        playImageView = findViewById(R.id.iv_play);
        pauseImageView = findViewById(R.id.iv_pause);
        beginImageView = findViewById(R.id.iv_to_begin);

        Intent intent = getIntent();

        int articleId = intent.getExtras().getInt(getString(R.string.article_id));
        String content = getArticleContentById(articleId);
        vsTextView.setText(content);

        final VerticalScrollingTextView tvContent = findViewById(R.id.tvScrollingContent);
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

    private int convertColor(int textColor) {
        switch(textColor){
            case BLACK:
                return R.color.black;
            case WHITE:
                return R.color.white_grey;

            case YELLOW:
                return R.color.yellow;

            case BLUE:
                return R.color.blue;
        }

        return R.color.black;
    }

    private void loadPreferences() {
        sPref = getSharedPreferences(getString(R.string.settings_pref), MODE_PRIVATE);
        textSpeed = sPref.getInt(getString(R.string.speed), 5);
        textSize = sPref.getInt(getString(R.string.size), 5);
        textColor = sPref.getInt(getString(R.string.textColor), BLACK);
        backColor = sPref.getInt(getString(R.string.backColor), WHITE);
        isTextMirrored = sPref.getBoolean(getString(R.string.is_text_mirrored), false);
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