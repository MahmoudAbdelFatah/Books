package com.example.android.books.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.books.DetailActivity;
import com.example.android.books.R;
import com.example.android.books.model.Books;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mahmoud on 8/23/2017.
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder>  {
    private List<Books> booksList;
    private Context mContext;

    public BooksAdapter(Context context , List<Books>booksList) {
        mContext = context;
        this.booksList = booksList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.rv_book_items, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        final Books book = booksList.get(position);

        //Download image using picasso library
        Picasso.with(mContext).load(book.getSmallThumbnail())
                .into(viewHolder.imageView);
        viewHolder.tvTitle.setText(book.getTitle());
        viewHolder.tvYear.setText(book.getPublishedDate());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("position", position);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) mContext,viewHolder.imageView , "book_image");
                    mContext.startActivity(intent, options.toBundle());

                } else {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("position", position);
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView tvTitle;
        public TextView tvYear;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.iv_book);
            tvTitle =  itemView.findViewById(R.id.tv_book_title);
            tvYear =  itemView.findViewById(R.id.tv_publisher_date);
            cardView = itemView.findViewById(R.id.cardView1);
        }
    }
}
