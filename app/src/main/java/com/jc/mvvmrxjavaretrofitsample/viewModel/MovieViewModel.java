package com.jc.mvvmrxjavaretrofitsample.viewModel;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jc.mvvmrxjavaretrofitsample.R;
import com.jc.mvvmrxjavaretrofitsample.model.entity.Movie;

/**
 * Created by HaohaoChang on 2017/2/11.
 */
public class MovieViewModel extends BaseObservable {
    private Movie movie;

    public MovieViewModel(Movie movie) {
        this.movie = movie;
    }

    public String getCoverUrl() {
        return movie.getImages().getSmall();
    }

    public String getTitle() {
        return movie.getTitle();
    }

    public float getRating() {
        return movie.getRating().getAverage();
    }

    public String getRatingText(){
        return String.valueOf(movie.getRating().getAverage());
    }

    public String getYear() {
        return movie.getYear();
    }

    public String getMovieType() {
        StringBuilder builder = new StringBuilder();
        for (String s : movie.getGenres()) {
            builder.append(s + " ");
        }
        return builder.toString();
    }

    public String getImageUrl() {
        return movie.getImages().getSmall();
    }

    @BindingAdapter({"app:imageUrl"})
    public static void loadImage(ImageView imageView,String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.cover)
                .error(R.drawable.cover)
                .into(imageView);

    }

}
