package com.example.android.tp_helper;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.example.android.tp_helper.data.ListOfArticles;

import java.util.ArrayList;

public class WidgetFactory implements RemoteViewsFactory {

    ArrayList<String> data;
    Context context;
    int widgetId;

    WidgetFactory(Context ctx, Intent intent){
        context = ctx;
        widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        data = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_item);
        remoteViews.setTextViewText(R.id.tvItemText, data.get(position));
        return remoteViews;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        data.clear();

        ArrayList<String> selectedArticle = ListOfArticles.getData();
        int arrSize = selectedArticle.size();
        for(int i = 0; i < arrSize; i++){
            data.add(selectedArticle.get(i));
        }
    }

    @Override
    public void onDestroy() {

    }


}
