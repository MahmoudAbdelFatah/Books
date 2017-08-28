package com.example.android.books.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mahmoud on 8/23/2017.
 */

public class FavoriteBooksContract {
    /*
        1) Content authority,
        2) Base content URI,
        3) Path(s) to the tasks directory
        4) Content URI for data in the TaskEntry class
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.books";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_FAVORITE = "favoritebooks";


    public static final class FavoriteBooksEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();

        public static final String TABLE_NAME ="favoritebooks";
        public static final String BOOK_ID="id";
        public static final String BOOK_TITLE="title";
        public static final String BOOK_DESCRIPTION="description";
        public static final String BOOK_IMAGE="smallThumbnail";
        public static final String BOOK_PUBLISHER="publisher";
        public static final String BOOK_PUBLISHER_DATE="publisher_date";
        public static final String BOOK_WEB_READER_LINK="web_reader_link";
        public static final String BOOK_ACS_TOKEN_LINK="acs_token_link";
    }
}
