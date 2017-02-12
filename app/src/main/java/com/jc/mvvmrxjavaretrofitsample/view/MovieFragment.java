package com.jc.mvvmrxjavaretrofitsample.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jc.mvvmrxjavaretrofitsample.R;
import com.jc.mvvmrxjavaretrofitsample.databinding.MovieFragmentBinding;
import com.jc.mvvmrxjavaretrofitsample.model.entity.Movie;
import com.jc.mvvmrxjavaretrofitsample.viewModel.MainViewModel;

/**
 * Created by HaohaoChang on 2017/2/11.
 */
public class MovieFragment extends Fragment implements ObserverOnNextListener<Movie>,SwipeRefreshLayout.OnRefreshListener{

    private static String TAG = MovieFragment.class.getSimpleName();
    private MainViewModel viewModel;
    private MovieFragmentBinding movieFragmentBinding;
    private MovieAdapter movieAdapter;

    public static MovieFragment getInstance() {
        return new MovieFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.movie_fragment, container, false);
        movieFragmentBinding = MovieFragmentBinding.bind(contentView);
        initData();
        return contentView;
    }

    private void initData() {
        movieAdapter = new MovieAdapter();
        movieFragmentBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        movieFragmentBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        movieFragmentBinding.recyclerView.setAdapter(movieAdapter);
        movieFragmentBinding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        movieFragmentBinding.swipeRefreshLayout.setOnRefreshListener(this);
        viewModel = new MainViewModel(this);
        movieFragmentBinding.setViewModel(viewModel);

    }

    @Override
    public void onNext(Movie movie) {
        if (movieFragmentBinding.swipeRefreshLayout.isRefreshing()) {
            movieFragmentBinding.swipeRefreshLayout.setRefreshing(false);
        }
        movieAdapter.addItem(movie);
    }

    @Override
    public void onRefresh() {
        movieAdapter.clearItems();
        viewModel.refreshData();
    }
}
