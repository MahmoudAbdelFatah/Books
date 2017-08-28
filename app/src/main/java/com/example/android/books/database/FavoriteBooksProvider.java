package com.example.android.books.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.android.books.database.FavoriteBooksContract.CONTENT_AUTHORITY;
import static com.example.android.books.database.FavoriteBooksContract.FavoriteBooksEntry.TABLE_NAME;
import static com.example.android.books.database.FavoriteBooksContract.PATH_FAVORITE;

/**
 * Created by Mahmoud on 8/23/2017.
 */

public class FavoriteBooksProvider extends ContentProvider {
    private FavoriteBooksDbHelper mFavoriteBooksDbHelper;
    public static final int FAVORITE_BOOKS = 100;
    public static final UriMatcher sUriMatcher= buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_AUTHORITY , PATH_FAVORITE , FAVORITE_BOOKS);
        return uriMatcher;
    }

    //initialize the provider
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoriteBooksDbHelper = new FavoriteBooksDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        SQLiteDatabase db = mFavoriteBooksDbHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_BOOKS: {
                retCursor = db.query(
                        TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {

            case FAVORITE_BOOKS:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +PATH_FAVORITE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = mFavoriteBooksDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case FAVORITE_BOOKS:
                long id = db.insert(TABLE_NAME , null , contentValues);
                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(uri, id);
                }else {
                    throw new android.database.SQLException("Field to insert into "+id);
                }
                break;
            default:
                throw new UnsupportedOperationException("unknown URI" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = mFavoriteBooksDbHelper.getWritableDatabase();
        int numRowsDeleted;
        int match = sUriMatcher.match(uri);
        if(s==null) {
            s = "1";
        }
        switch (match) {
            case FAVORITE_BOOKS:
                numRowsDeleted = db.delete(TABLE_NAME , s, strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI " + uri);
        }
        if(numRowsDeleted !=0)
            getContext().getContentResolver().notifyChange(uri,null);
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = mFavoriteBooksDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case FAVORITE_BOOKS:
                rowsUpdated = db.update(TABLE_NAME,
                        contentValues,
                        s,
                        strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = mFavoriteBooksDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsInserted=0;
        switch (match) {
            case FAVORITE_BOOKS:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}