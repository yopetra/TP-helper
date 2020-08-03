package com.example.android.tp_helper;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import com.example.android.tp_helper.data.AppDatabase;
import com.example.android.tp_helper.data.ArticleEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Implementation of App Widget functionality.
 */
public class TPWidget extends AppWidgetProvider {

    final String ACTION_ON_CLICK = "com.example.android.tp_helper.itemonclick";
    final static String ITEM_POSITION = "item position";
    private List<Integer> idsOfArticles = new ArrayList<>();
    private AppDatabase mDb;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager, i);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equalsIgnoreCase(ACTION_ON_CLICK)){
            mDb = AppDatabase.getInstance(context);
            int itemPos = intent.getIntExtra(ITEM_POSITION, -1);
            if(itemPos != -1){
                
                // Getting an ID of the article which saved in DB
                FetchAllArticlesTask fetchAllArticles = new FetchAllArticlesTask();
                fetchAllArticles.execute();
                try {
                    fetchAllArticles.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int articleIdInDb = idsOfArticles.get(itemPos);

                Intent scrollIntent = new Intent(context, ScrollingTextActivity.class);
                scrollIntent.putExtra(context.getString(R.string.article_id), articleIdInDb);
                scrollIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(scrollIntent);
            }
        }
    }

    private class FetchAllArticlesTask extends AsyncTask<Void, Void, List<Integer>> {
        @Override
        protected List<Integer> doInBackground(Void... voids) {

            final List<ArticleEntry> articleEntries = mDb.articleDao().loadAllArticles();

            // Fill ids if articles to list
            idsOfArticles.clear();
            int arrSize = articleEntries.size();

            for(int i = 0; i < arrSize; i++){
                int id = articleEntries.get(i).getId();
                idsOfArticles.add(id);
            }

            return idsOfArticles;
        }

        @Override
        protected void onPostExecute(List<Integer> articleEntries) {
            super.onPostExecute(articleEntries);
        }
    }

    void updateWidget(Context context, AppWidgetManager appWidgetManager,
                      int appWidgetId) {
        RemoteViews rv = new RemoteViews(context.getPackageName(),
                R.layout.t_p_widget);

        setUpdateTV(rv, context, appWidgetId);
        setList(rv, context, appWidgetId);
        setListCLick(rv, context, appWidgetId);

        appWidgetManager.updateAppWidget(appWidgetId, rv);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,
                R.id.lv_articles_list);
    }

    void setUpdateTV(RemoteViews rv, Context context, int appWidgetId) {
        Intent updIntent = new Intent(context, TPWidget.class);
        updIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                new int[] { appWidgetId });
        PendingIntent updPIntent = PendingIntent.getBroadcast(context,
                appWidgetId, updIntent, 0);
        rv.setOnClickPendingIntent(R.id.tv_refresh_articles, updPIntent);
    }

    void setList(RemoteViews rv, Context context, int appWidgetId) {
        Intent adapter = new Intent(context, WidgetService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        rv.setRemoteAdapter(R.id.lv_articles_list, adapter);
    }

    void setListCLick(RemoteViews rv, Context context, int appWidgetId){
        Intent listClickIntent = new Intent(context, TPWidget.class);
        listClickIntent.setAction(ACTION_ON_CLICK);
        PendingIntent listClickPIntent = PendingIntent.getBroadcast(context, 0,
                listClickIntent, 0);
        rv.setPendingIntentTemplate(R.id.lv_articles_list, listClickPIntent);
    }
}

