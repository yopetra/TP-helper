package com.example.android.tp_helper;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class TPWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager, i);
        }
    }

    void updateWidget(Context context, AppWidgetManager appWidgetManager,
                      int appWidgetId) {
        RemoteViews rv = new RemoteViews(context.getPackageName(),
                R.layout.t_p_widget);

        setUpdateTV(rv, context, appWidgetId);
        setList(rv, context, appWidgetId);

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

//    private static final String ACTION_CHANGE = "action-change";
//
//    @Override
//    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
//                         int[] appWidgetIds) {
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
//        // There may be multiple widgets active, so update all of them
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }
//    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        super.onReceive(context, intent);
//
//        if(intent.getAction().equalsIgnoreCase(ACTION_CHANGE)){
//
//            int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
//            Bundle extras = intent.getExtras();
//            if(extras != null){
//                mAppWidgetId = extras.getInt(
//                        AppWidgetManager.EXTRA_APPWIDGET_ID,
//                        AppWidgetManager.INVALID_APPWIDGET_ID
//                );
//            }
//            if(mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID){
//                updateAppWidget(context, AppWidgetManager.getInstance(context), mAppWidgetId);
//            }
//        }
//    }
//
//    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
//                         int appWidgetId) {
//
//        RemoteViews rv = new RemoteViews(context.getPackageName(),
//                R.layout.t_p_widget);
//
//        setUpdateTV(rv, context, appWidgetId);
//        setList(rv, context, appWidgetId);
//
//        appWidgetManager.updateAppWidget(appWidgetId, rv);
//        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,
//                R.id.lv_articles_list);
//    }
//
//    private void setUpdateTV(RemoteViews rv, Context context, int appWidgetId) {
//        Intent updIntent = new Intent(context, TPWidget.class);
//        updIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//        updIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
//                new int[]{appWidgetId});
//        PendingIntent updPIntent = PendingIntent.getBroadcast(context,
//                appWidgetId, updIntent, 0);
//        rv.setOnClickPendingIntent(R.id.tv_refresh_articles, updPIntent);
//    }
//
//    private void setList(RemoteViews rv, Context context, int appWidgetId) {
//        Intent adapter = new Intent(context, WidgetService.class);
//        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
//        rv.setRemoteAdapter(R.id.lv_articles_list, adapter);
//    }



//    @Override
//    public void onEnabled(Context context) {
//        // Enter relevant functionality for when the first widget is created
//    }
//
//    @Override
//    public void onDisabled(Context context) {
//        // Enter relevant functionality for when the last widget is disabled
//    }
}

