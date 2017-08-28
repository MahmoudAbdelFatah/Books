package com.example.android.books.widget;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.books.R;
import com.example.android.books.database.FavoriteBooksContract;
import com.example.android.books.model.Books;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.books.MainActivity.booksList;

/**
 * Created by Mahmoud on 26-Aug-17.
 */

public class BooksRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private Cursor cursor;
    public BooksRemoteViewsFactory(Context context) {
        this.context = context;
        cursor = context.getContentResolver().query(FavoriteBooksContract.FavoriteBooksEntry.CONTENT_URI,
                new String[]{FavoriteBooksContract.FavoriteBooksEntry.BOOK_TITLE}
                , null, null, null);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
       if(cursor!=null)
           return cursor.getCount();
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.books_widget_list_item);
        if(cursor!=null) {
            cursor.moveToPosition(i);
            rv.setTextViewText(R.id.tv_book_name,
                    cursor.getString(cursor.getColumnIndex(FavoriteBooksContract.FavoriteBooksEntry.BOOK_TITLE)));
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
