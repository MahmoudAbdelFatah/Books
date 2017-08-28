package com.example.android.books;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.content.Loader;
import android.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.books.adapters.BooksAdapter;
import com.example.android.books.database.FavoriteBooksContract;
import com.example.android.books.model.Books;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> , SharedPreferences.OnSharedPreferenceChangeListener
         {
    private RecyclerView recyclerView;
    private BooksAdapter booksAdapter;
    private SharedPreferences sharedPref;
    public static List<Books> booksList;
    private Toast mToast;
    private AdView mAdView;
    private static final int LOADER_ID = 1;
    private String orderType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequestBuilder = new AdRequest.Builder().build();
        mAdView.loadAd(adRequestBuilder);

        booksList = new ArrayList<>();
        recyclerView = findViewById(R.id.rv_books);
        recyclerView.setLayoutManager(new GridLayoutManager(this,numberOfColumns()));
        booksAdapter = new BooksAdapter(this , booksList);
        recyclerView.setAdapter(booksAdapter);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
        orderType = sharedPref.getString("books", "");
        Log.v("sharedPref", orderType);

        if (savedInstanceState != null) {
            booksList = (List<Books>) savedInstanceState.getSerializable("books");
            booksAdapter = new BooksAdapter(this , booksList);
            recyclerView.setAdapter(booksAdapter);
        }
        else
            updateOrderType();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void downloadFromInternet(){
        String URL = getString(R.string.BASE_URL) + "&" + getString(R.string.PRINT_TYPE) + "&" +
                getString(R.string.KEY);
        Ion.with(this)
                .load(URL)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        booksList.clear();
                        String temp="";
                        Books book ;
                        List<String> authors= new ArrayList<>();
                        if (e == null) {
                            JsonArray jsonArray = result.get("items").getAsJsonArray();
                            for (int i = 0; i < jsonArray.size(); i++) {
                                book = new Books();
                                authors.clear();
                                book.setId(jsonArray.get(i).getAsJsonObject().get("id").toString());
                                JsonObject volumeInfo = jsonArray.get(i).getAsJsonObject().get("volumeInfo").getAsJsonObject();
                                book.setTitle(volumeInfo.get("title").getAsString());
                                JsonArray autherArray = volumeInfo.get("authors").getAsJsonArray();
                                for(int j=0; j<autherArray.size(); j++) {
                                    authors.add(autherArray.get(j).getAsString());
                                }
                                book.setAuthors(authors);

                                if (volumeInfo.has("description")) {
                                    temp = volumeInfo.get("description").toString();
                                    book.setDescription(temp.substring(1, temp.length()-1));

                                } else
                                    book.setDescription("No Description Available!");
                                temp = volumeInfo.get("publishedDate").toString();
                                book.setPublishedDate(temp.substring(1 , temp.length()-1));
                                if(volumeInfo.has("publisher")) {
                                    temp = volumeInfo.get("publisher").toString();
                                    book.setPublisher(temp.substring(1 , temp.length()-1));
                                } else
                                    book.setPublisher("unknown!");

                                JsonObject accessInfo = jsonArray.get(i).getAsJsonObject().get("accessInfo").getAsJsonObject();
                                if(accessInfo.has("webReaderLink"))
                                    book.setWebReaderLink(accessInfo.get("webReaderLink").toString());
                                else
                                    book.setWebReaderLink("");
                                if(accessInfo.get("pdf").getAsJsonObject().has("acsTokenLink"))
                                    book.setAcsTokenLink(accessInfo.get("pdf").getAsJsonObject().get("acsTokenLink").toString());
                                else
                                    book.setAcsTokenLink("");

                                book.setSmallThumbnail(volumeInfo.get("imageLinks").getAsJsonObject().get("smallThumbnail").getAsString()
                                        .substring(0, volumeInfo.get("imageLinks").getAsJsonObject().get("smallThumbnail").getAsString().length() - 1));
                                booksList.add(book);
                            }
                        }
                        Log.i("test", "" + booksList.size());
                        booksAdapter.notifyDataSetChanged();
                    }
                });
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2  ;
        return nColumns;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String aboutAppData = null;
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.about_appId) {
            try {
                aboutAppData = new AboutAppAsyncTask().execute(getResources().getString(R.string.about_app_API)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(getApplicationContext(), AboutApp.class);
            intent.putExtra("data", aboutAppData);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateOrderType(){
        try{
            switch (orderType){
                case "all_books":
                    if(isNetworkAvailable(this))
                        downloadFromInternet();
                    else{
                        if(mToast !=null)
                            mToast.cancel();
                        mToast =Toast.makeText(this,R.string.no_connection, Toast.LENGTH_LONG);
                        mToast.show();
                    }
                    break;
                case "favorite_books":
                    getLoaderManager().initLoader(LOADER_ID, null, this);
                    if(!isNetworkAvailable(this)) {
                        if(mToast !=null)
                            mToast.cancel();
                        mToast =Toast.makeText(this,R.string.no_connection, Toast.LENGTH_LONG);
                        mToast.show();
                    }
                    booksAdapter.notifyDataSetChanged();
                    break;
            }
        }catch (Exception e){
            FirebaseCrash.log(getString(R.string.no_connection));
        }
    }


    private void getFavoriteBooksFromDb(Cursor cursor) {
        booksList.clear();
        if(cursor != null && cursor.getCount()>0) {
            Books book;
            if (cursor.moveToFirst()) {
                do {
                    book = new Books();
                    //Read row by row
                    book.setId(cursor.getString(0));
                    book.setTitle(cursor.getString(1));
                    book.setDescription(cursor.getString(2));
                    book.setSmallThumbnail(cursor.getString(3));
                    book.setPublisher(cursor.getString(4));
                    book.setPublishedDate(cursor.getString(5));
                    book.setWebReaderLink(cursor.getString(6));
                    book.setAcsTokenLink(cursor.getString(7));
                    booksList.add(book);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        booksAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (sharedPreferences.getString(s, "").equals("all_books")) {
            downloadFromInternet();
        } else if (sharedPreferences.getString(s, "").equals("favorite_books")) {
            getLoaderManager().initLoader(LOADER_ID, null, this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return new CursorLoader(
                    this,
                    FavoriteBooksContract.FavoriteBooksEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        getFavoriteBooksFromDb(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("books", (Serializable) booksList);
    }
}
