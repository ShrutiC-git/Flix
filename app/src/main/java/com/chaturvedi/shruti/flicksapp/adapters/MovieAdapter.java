package com.chaturvedi.shruti.flicksapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chaturvedi.shruti.flicksapp.DetailsActivity;
import com.chaturvedi.shruti.flicksapp.R;
import com.chaturvedi.shruti.flicksapp.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Movie> movies;

    public static final int POPULAR = 0;
    public static final int NOT_POPULAR = 1;

    public static final int RADIUS = 30;
    public static final int MARGIN = 0;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getItemViewType(int position) {
        double popularity = movies.get(position).getPopularity();
        if (popularity > 1000) {
            return POPULAR;
        } else {
            return NOT_POPULAR;
        }
    }

    //It will create a layout from the XML for list we have defined and will return the layout to use later
    //Primary use is pass the viewholder to the onBindViewHolder method
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View movieView = LayoutInflater.from(context).inflate(R.layout.movielistlayout, parent, false);
        //return new ViewHolder(movieView);

        RecyclerView.ViewHolder customViewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case POPULAR:
                View view1 = inflater.inflate(R.layout.popularmovielayout, parent, false);
                customViewHolder = new ViewHolderPopular(view1);
                break;

            case NOT_POPULAR:
                View view2 = inflater.inflate(R.layout.movielistlayout, parent, false);
                customViewHolder = new ViewHolder(view2);
                break;

            default:
                View view3 = inflater.inflate(R.layout.movielistlayout, parent, false);
                customViewHolder = new ViewHolder(view3);
                break;
        }
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        switch (holder.getItemViewType()) {
            case NOT_POPULAR:
                ViewHolder holder1 = (ViewHolder) holder;
                bind(holder1, movie);
                break;

            case POPULAR:
                ViewHolderPopular holder2 = (ViewHolderPopular) holder;
                bindPopular(holder2, movie);
                break;
        }
    }

    private void bindPopular(final ViewHolderPopular holder, final Movie movie) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Glide.with(context).load(movie.getBackdrop()).placeholder(R.drawable.placeholder).into(holder.getIvPopular());
            //Glide.with(context).load(movie.getBackdrop()).placeholder(R.drawable.placeholder).transform(new RoundedCornersTransformation(RADIUS, MARGIN)).into(holder.getIvPopular());
        } else {
            Glide.with(context).load(movie.getPosterPath()).placeholder(R.drawable.placeholder).into(holder.getIvPopular());
            //Glide.with(context).load(movie.getPosterPath()).placeholder(R.drawable.placeholder).transform(new RoundedCornersTransformation(RADIUS, MARGIN)).into(holder.getIvPopular());
        }
        holder.getPopularLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("movie", Parcels.wrap(movie));
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context, (View) holder.getIvPopular(), "profile");
                context.startActivity(intent);
            }
        });
    }

    private void bind(ViewHolder holder, final Movie movie) {
        holder.getTvTitle().setText(movie.getTitle());
        holder.getTvOverview().setText(movie.getOverview());
        String imageUrl;

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageUrl = movie.getBackdrop();
        } else {
            imageUrl = movie.getPosterPath();
        }

        Glide.with(context).load(imageUrl).placeholder(R.drawable.placeholder).into(holder.getIvPoster());

        holder.getLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("movie", Parcels.wrap(movie));
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvOverview;
        ImageView ivPoster;
        RelativeLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            layout = itemView.findViewById(R.id.layout);
        }

        public TextView getTvTitle() {
            return tvTitle;
        }

        public TextView getTvOverview() {
            return tvOverview;
        }

        public ImageView getIvPoster() {
            return ivPoster;
        }

        public RelativeLayout getLayout() {
            return layout;
        }
    }

    public class ViewHolderPopular extends RecyclerView.ViewHolder {

        private ImageView ivPopular;

        private ConstraintLayout popularLayout;

        public ViewHolderPopular(@NonNull View itemView) {
            super(itemView);
            ivPopular = itemView.findViewById(R.id.ivPopular);
            popularLayout = itemView.findViewById(R.id.popularLayout);
        }

        public ImageView getIvPopular() {
            return ivPopular;
        }

        public ConstraintLayout getPopularLayout() {
            return popularLayout;
        }
    }
}
