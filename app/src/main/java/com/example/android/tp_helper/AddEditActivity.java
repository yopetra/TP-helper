package com.example.android.tp_helper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.tp_helper.data.AppDatabase;
import com.example.android.tp_helper.data.ArticleEntry;
import com.example.android.tp_helper.utils.AppExecutors;

public class AddEditActivity extends AppCompatActivity {

    TextView actionBarNameTextView;
    Button saveButton;
    EditText nameOfArticleEditText;
    EditText contentOfArticleEditText;
    int articleId = -1;
    final String SAVE_NEW = "1";
    final String SAVE_EDITED = "2";

    AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        String action =  intent.getStringExtra(getString(R.string.add_edit_action));

        final String savingType = identifyTypeOf(action);
        articleId = intent.getExtras().getInt(getString(R.string.article_id));
        actionBarNameTextView = findViewById(R.id.tv_action_bar_name);
        actionBarNameTextView.setText(action);

        nameOfArticleEditText = findViewById(R.id.et_article_name);
        contentOfArticleEditText = findViewById(R.id.et_article_content);

        saveButton = findViewById(R.id.bt_save_article);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameOfArticleEditText.getText().toString().length() > 0 &&
                contentOfArticleEditText.getText().toString().length() > 0){
                    saveArticle(savingType);
                    AddEditActivity.super.onBackPressed();
                }else{
                    Toast.makeText(getApplicationContext(), "Name and content should not be blank.", Toast.LENGTH_LONG).show();
                }
            }
        });

        if(action.equals(getString(R.string.edit_article))){
            new ReadArticleTask().execute(articleId);
        }
    }

    private String identifyTypeOf(String action) {
        if(action.equals(getString(R.string.add_new_article))){return SAVE_NEW;}
        if(action.equals(getString(R.string.edit_article))){return SAVE_EDITED;}

        return null;
    }

    private class SaveArticleTask extends AsyncTask<String, Void, ArticleEntry>{

        String tSavingType = null;
        @Override
        protected ArticleEntry doInBackground(String... articleData) {

            String name = articleData[0];
            String content = articleData[1];
            tSavingType = articleData[2];

            final ArticleEntry articleEntry = new ArticleEntry(articleId, name, content);

            return articleEntry;
        }

        @Override
        protected void onPostExecute(final ArticleEntry articleEntry) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if(tSavingType == SAVE_NEW){
                        mDb.articleDao().insertArticle(articleEntry);}
                    if(tSavingType == SAVE_EDITED){
                        mDb.articleDao().updateArticle(articleEntry);}
                }
            });
        }
    }

    private class ReadArticleTask extends AsyncTask<Integer, Void, ArticleEntry>{
        @Override
        protected ArticleEntry doInBackground(Integer... integers) {

            int articleId = integers[0];

            ArticleEntry articleEntry = mDb.articleDao().readArticleById(articleId);

            return articleEntry;
        }

        @Override
        protected void onPostExecute(ArticleEntry articleEntry) {
            String name = articleEntry.getName();
            String content = articleEntry.getContent();

            nameOfArticleEditText.setText(name);
            contentOfArticleEditText.setText(content);
        }
    }

    private void saveArticle(String savingType){
        String nameOfArticle = nameOfArticleEditText.getText().toString();
        String contentOfArticle = contentOfArticleEditText.getText().toString();

        new SaveArticleTask().execute(nameOfArticle, contentOfArticle, savingType);

        Toast.makeText(getApplicationContext(), R.string.article_saved, Toast.LENGTH_SHORT).show();
    }
}