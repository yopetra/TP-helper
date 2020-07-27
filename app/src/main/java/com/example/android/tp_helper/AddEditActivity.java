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

                ArticleEntry articleEntry = new ArticleEntry(nameOfArticle, contentOfArticle);

                new SaveArticleTask().execute(articleEntry);
            }
        });
    }

    private class SaveArticleTask extends AsyncTask<ArticleEntry, Void, Void>{
        @Override
        protected Void doInBackground(ArticleEntry... articleEntries) {

            final ArticleEntry articleEntry = articleEntries[0];
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.articleDao().insertArticle(articleEntry);
                }
            });
            return null;
        }
    }
}