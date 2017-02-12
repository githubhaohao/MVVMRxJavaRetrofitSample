package com.jc.mvvmrxjavaretrofitsample.viewModel;

import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import com.jc.mvvmrxjavaretrofitsample.model.data.RetrofitHelper;
import com.jc.mvvmrxjavaretrofitsample.model.entity.Movie;
import com.jc.mvvmrxjavaretrofitsample.view.ObserverOnNextListener;

import java.util.List;

import rx.Subscriber;

/**
 * Created by HaohaoChang on 2017/2/11.
 */
public class MainViewModel {
    public ObservableField<Integer> contentViewVisibility;
    public ObservableField<Integer> progressBarVisibility;
    public ObservableField<Integer> errorInfoLayoutVisibility;
    public ObservableField<String> exception;
    private ObserverOnNextListener<Movie> onNextListener;
    private Subscriber<Movie> subscriber;

    public MainViewModel(ObserverOnNextListener<Movie> onNextListener) {
        this.onNextListener = onNextListener;
        initData();
        getMovies();
    }

    private void getMovies() {
        subscriber = new Subscriber<Movie>() {
            @Override
            public void onCompleted() {
                Log.d("[MainViewModel]", "onCompleted");
                hideAll();
                contentViewVisibility.set(View.VISIBLE);
            }

            @Override
            public void onError(Throwable e) {
                hideAll();
                errorInfoLayoutVisibility.set(View.VISIBLE);
                exception.set(e.getMessage());
            }

            @Override
            public void onNext(Movie movie) {
                onNextListener.onNext(movie);
            }
        };
        RetrofitHelper.getInstance().getMovies(subscriber, 0, 20);
    }

    public void refreshData() {
        getMovies();
    }

    private void initData() {
        contentViewVisibility = new ObservableField<>();
        progressBarVisibility = new ObservableField<>();
        errorInfoLayoutVisibility = new ObservableField<>();
        exception = new ObservableField<>();
        contentViewVisibility.set(View.GONE);
        errorInfoLayoutVisibility.set(View.GONE);
        progressBarVisibility.set(View.VISIBLE);
    }

    private void hideAll(){
        contentViewVisibility.set(View.GONE);
        errorInfoLayoutVisibility.set(View.GONE);
        progressBarVisibility.set(View.GONE);
    }
}
