package com.example.android.books.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.books.MainActivity;
import com.example.android.books.R;

/**
 * Created by Mahmoud on 26-Aug-17.
 */

public class BooksWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int appWidgetId: appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.books_widget);
            Intent titleIntent = new Intent(context, MainActivity.class);
            PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_title, titlePendingIntent);

            Intent intent = new Intent(context, BooksRemoteViewsService.class);
            views.setRemoteAdapter(R.id.widgetListView, intent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, BooksWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.widgetListView);
        }
        super.onReceive(context, intent);
    }

}
