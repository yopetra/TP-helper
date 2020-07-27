package com.example.android.tp_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tp_helper.data.AppDatabase;
import com.example.android.tp_helper.data.ArticleEntry;
import com.example.android.tp_helper.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;

public class AddEditActivity extends AppCompatActivity {

    TextView actionBarNameTextView;
    Button saveButton;
    EditText nameOfArticleEditText;
    EditText contentOfArticleEditText;

    AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        mDb = AppDatabase.getInstance(getApplicationContext());

        String action =  getIntent().getStringExtra(getString(R.string.add_edit_action));
        actionBarNameTextView = findViewById(R.id.tv_action_bar_name);
        actionBarNameTextView.setText(action);

        nameOfArticleEditText = findViewById(R.id.et_article_name);
        contentOfArticleEditText = findViewById(R.id.et_article_content);

        saveButton = findViewById(R.id.bt_save_article);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Article saved", Toast.LENGTH_SHORT).show();

                String nameOfArticle = nameOfArticleEditText.getText().toString();
                String contentOfArticle = contentOfArticleEditText.getText().toString();

                new SaveArticleTask().execute(nameOfArticle, contentOfArticle);
            }
        });
    }

    private class SaveArticleTask extends AsyncTask<String, Void, ArticleEntry>{
        @Override
        protected ArticleEntry doInBackground(String... articleData) {

            String name = articleData[0];
            String content = articleData[1];

            final ArticleEntry articleEntry = new ArticleEntry(name, content);

            return articleEntry;
        }

        @Override
        protected void onPostExecute(final ArticleEntry articleEntry) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.articleDao().insertArticle(articleEntry);
                }
            });
        }
    }
}