package com.jc.mvvmrxjavaretrofitsample.model.data;

import com.jc.mvvmrxjavaretrofitsample.model.entity.Movie;
import com.jc.mvvmrxjavaretrofitsample.model.entity.Response;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by HaohaoChang on 2017/2/11.
 */
public interface DouBanMovieService {
    String BASE_URL = "https://api.douban.com/v2/movie/";

    @GET("top250")
    Observable<Response<List<Movie>>> getMovies(@Query("start") int start, @Query("count") int count);
}
