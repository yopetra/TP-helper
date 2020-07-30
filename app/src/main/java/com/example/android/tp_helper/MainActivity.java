package com.example.android.tp_helper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.android.tp_helper.data.AppDatabase;
import com.example.android.tp_helper.data.ArticleEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements ArticlesAdapter.ArticleAdapterOnClickHandler{

    FloatingActionButton fab;
    ArticlesAdapter mAdapter;
    RecyclerView mRecyclerView;
    private AppDatabase mDb;
    private Paint p = new Paint();
    private List<Integer> idsOfArticles = new ArrayList<>();
    AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alertBuilder = new AlertDialog.Builder(this);

        fab = findViewById(R.id.fab_add_article);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra(getString(R.string.add_edit_action), getString(R.string.add_new_article));
                startActivity(intent);
            }
        });

        mRecyclerView = findViewById(R.id.rv_articles_list);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new ArticlesAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        enableSwipe();
    }

    @Override
    public void onClick(int finalId) {
        Toast.makeText(this, "CLicked item" + finalId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(int finalId) {
        Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
        intent.putExtra(getString(R.string.add_edit_action), getString(R.string.edit_article));
        intent.putExtra(getString(R.string.article_id), finalId);
        startActivity(intent);
    }

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

            // Fill ids if articles to list
            idsOfArticles.clear();
            int arrSize = articleEntries.size();

            for(int i = 0; i < arrSize; i++){
                int id = articleEntries.get(i).getId();
                idsOfArticles.add(id);
            }

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

    private class ReadArticleTask extends AsyncTask<Integer, Void, ArticleEntry>{
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

    private Context getMainContext(){
        return this;
    }

    private void enableSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) { //| ItemTouchHelper.RIGHT

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }



            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){

                    showAlertDialog(position);

                    //                        int idInDb = idsOfArticles.get(position);
//
//                        // Seek article for delete and save it in the variable
//                        ReadArticleTask readArticleTask = new ReadArticleTask();
//                        readArticleTask.execute(idInDb);
//
//                        mAdapter.removeItem(idInDb);
//                        mAdapter.clearData();
//                        loadArticlesData();
//
//                        Toast.makeText(getMainContext(), "Article deleted", Toast.LENGTH_SHORT).show();


                }
            }

            private void showAlertDialog(final int position) {

                final int positionInAlert = position;

                alertBuilder.setMessage("message text")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int idInDb = idsOfArticles.get(positionInAlert);

                                // Seek article for delete and save it in the variable
                                ReadArticleTask readArticleTask = new ReadArticleTask();
                                readArticleTask.execute(idInDb);

                                mAdapter.removeItem(idInDb);
                                mAdapter.clearData();
                                loadArticlesData();

                                Toast.makeText(getMainContext(), "Article deleted", Toast.LENGTH_SHORT).show();
//                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getMainContext(), "Deleting have canceled", Toast.LENGTH_SHORT).show();
                                mAdapter.clearData();
                                loadArticlesData();
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog alert = alertBuilder.create();
                alert.setTitle("Title of alert");
                alert.show();

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void loadArticlesData(){
        new FetchAllArticlesTask().execute();
    }
}