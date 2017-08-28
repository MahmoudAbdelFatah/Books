package com.example.android.books.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mahmoud on 8/23/2017.
 */

public class FavoriteBooksDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME ="favoritebooks.db";
    private static final int DATABASE_VERSION= 1;
    public FavoriteBooksDbHelper(Context context) {
        super(context , DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITE_TABLE =
                "CREATE TABLE " + FavoriteBooksContract.FavoriteBooksEntry.TABLE_NAME + " (" +
                        FavoriteBooksContract.FavoriteBooksEntry.BOOK_ID + " TEXT PRIMARY KEY," +
                        FavoriteBooksContract.FavoriteBooksEntry.BOOK_TITLE + " TEXT NOT NULL," +
                        FavoriteBooksContract.FavoriteBooksEntry.BOOK_DESCRIPTION + " TEXT, " +
                        FavoriteBooksContract.FavoriteBooksEntry.BOOK_IMAGE + " TEXT," +
                        FavoriteBooksContract.FavoriteBooksEntry.BOOK_PUBLISHER + " TEXT," +
                        FavoriteBooksContract.FavoriteBooksEntry.BOOK_PUBLISHER_DATE + " TEXT," +
                        FavoriteBooksContract.FavoriteBooksEntry.BOOK_WEB_READER_LINK + " TEXT," +
                        FavoriteBooksContract.FavoriteBooksEntry.BOOK_ACS_TOKEN_LINK + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteBooksContract.FavoriteBooksEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}