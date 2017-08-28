package com.example.android.books;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.books.database.FavoriteBooksContract;
import com.example.android.books.model.Books;
import com.squareup.picasso.Picasso;

import static java.security.AccessController.getContext;

public class DetailActivity extends AppCompatActivity {
    private int index=-1;
    private ImageView bookImage;
    private TextView bookTitle;
    private TextView publisherDate;
    private TextView publisher;
    private TextView description;
    private Books book;
    private FloatingActionButton fab;
    private boolean isFav=false;
    private Toast mToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        setLayout();
        isFavorite();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFav){
                    isFav=true;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(FavoriteBooksContract.FavoriteBooksEntry.BOOK_ID, book.getId());
                    contentValues.put(FavoriteBooksContract.FavoriteBooksEntry.BOOK_TITLE, book.getTitle());
                    contentValues.put(FavoriteBooksContract.FavoriteBooksEntry.BOOK_DESCRIPTION, book.getDescription());
                    contentValues.put(FavoriteBooksContract.FavoriteBooksEntry.BOOK_IMAGE, book.getSmallThumbnail());
                    contentValues.put(FavoriteBooksContract.FavoriteBooksEntry.BOOK_PUBLISHER, book.getPublisher());
                    contentValues.put(FavoriteBooksContract.FavoriteBooksEntry.BOOK_PUBLISHER_DATE, book.getPublishedDate());
                    contentValues.put(FavoriteBooksContract.FavoriteBooksEntry.BOOK_WEB_READER_LINK, book.getWebReaderLink());
                    contentValues.put(FavoriteBooksContract.FavoriteBooksEntry.BOOK_ACS_TOKEN_LINK, book.getAcsTokenLink());

                    Uri insertUri = getContentResolver().insert(FavoriteBooksContract.FavoriteBooksEntry.CONTENT_URI,
                            contentValues);
                    long insertedId= ContentUris.parseId(insertUri);
                    if(insertedId>0) {
                        if(mToast !=null)
                            mToast.cancel();
                        mToast = Toast.makeText(getApplication(), getString(R.string.add_book), Toast.LENGTH_LONG);
                        mToast.show();
                    }
                } else {
                    isFav = false;
                    int rowDeleted = getContentResolver().delete(
                            FavoriteBooksContract.FavoriteBooksEntry.CONTENT_URI,
                            FavoriteBooksContract.FavoriteBooksEntry.BOOK_ID+" = ?" ,
                            new String[] {book.getId().toString()}
                    );
                    if(rowDeleted >0) {
                        if(mToast !=null)
                            mToast.cancel();
                        mToast = Toast.makeText(getApplicationContext(), getString(R.string.remove_book), Toast.LENGTH_LONG);
                        mToast.show();
                    }
                }
            }
        });
    }

    private void setLayout() {
        index = this.getIntent().getExtras().getInt("position");
        this.setTitle(MainActivity.booksList.get(index).getTitle());
        book  = MainActivity.booksList.get(index);

        bookImage = findViewById(R.id.iv_book);
        bookTitle = findViewById(R.id.tv_book_title);
        publisherDate = findViewById(R.id.tv_book_date);
        publisher = findViewById(R.id.tv_book_publisher);
        description = findViewById(R.id.tv_book_description);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        Picasso.with(this)
                .load(book.getSmallThumbnail())
                .into(bookImage);
        bookTitle.setText(book.getTitle());
        publisher.setText(book.getPublisher());
        publisherDate.setText(book.getPublishedDate());
        description.setText(book.getDescription());
    }

    private void isFavorite() {
        Cursor cursor = this.getContentResolver().query(
                FavoriteBooksContract.FavoriteBooksEntry.CONTENT_URI,
                new String[]{FavoriteBooksContract.FavoriteBooksEntry.BOOK_ID},
                FavoriteBooksContract.FavoriteBooksEntry.BOOK_ID + " = ? ",
                new String[]{book.getId().toString()},
                null
        );
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return;
        }
        // if Database contains book
        isFav = true;
        cursor.close();
    }
}
