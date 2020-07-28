package com.example.android.tp_helper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.android.tp_helper.data.AppDatabase;
import com.example.android.tp_helper.data.ArticleEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ArticlesAdapter.ArticleAdapterOnClickHandler{

    FloatingActionButton fab;
    ArticlesAdapter mAdapter;
    RecyclerView mRecyclerView;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab_add_article);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra(getString(R.string.add_edit_action), "Add a new article");
                startActivity(intent);
            }
        });

        mRecyclerView = findViewById(R.id.rv_articles_list);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new ArticlesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(JSONObject articleItem) {
        int name = -1;
        try {
            name = articleItem.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "CLicked item" + name, Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onLongClick(JSONObject articleItem) {
//        Toast.makeText(this, "On long click", Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onResume() {
        mAdapter.clearData();
        mDb = AppDatabase.getInstance(getApplicationContext());
        loadArticlesData();
        super.onResume();
    }

    private class FetchAllArticlesTask extends AsyncTask<Void, Void, List<ArticleEntry>> {
        @Override
        protected List<ArticleEntry> doInBackground(Void... voids) {
            mDb = AppDatabase.getInstance(getApplicationContext());
            final JSONArray[] articleJsonData = null;
            final List<ArticleEntry> articleEntries = mDb.articleDao().loadAllArticles();

            return articleEntries;
        }

        @Override
        protected void onPostExecute(List<ArticleEntry> articleEntries) {
            if(articleEntries.size() > 0){
                JSONArray jsonArray = new JSONArray();
                int sizeOfList = articleEntries.size();

                for(int i = 0; i < sizeOfList; i++){
                    int id = articleEntries.get(i).getId();
                    String name = articleEntries.get(i).getName();
                    String content = articleEntries.get(i).getContent();
                    JSONObject jsonObject= null;
                    try {
                        jsonObject = new JSONObject()
                                .put("id", id)
                                .put("name", name)
                                .put("content", content);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    jsonArray.put(jsonObject);
                }

                mAdapter.setArticlesData(jsonArray);
            }
        }
    }

    private void loadArticlesData(){
        new FetchAllArticlesTask().execute();
    }
}