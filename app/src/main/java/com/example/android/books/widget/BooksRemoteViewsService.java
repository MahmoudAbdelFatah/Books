package com.example.android.books.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Mahmoud on 26-Aug-17.
 */

public class BooksRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BooksRemoteViewsFactory(this.getApplicationContext());
    }
}
