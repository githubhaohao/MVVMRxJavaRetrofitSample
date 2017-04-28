![Cover](https://github.com/githubhaohao/ImageRoom/blob/master/Images/country-1295915__340.png?raw=true)

[![Scrutinizer Build](https://img.shields.io/scrutinizer/build/g/filp/whoops.svg)]() [![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)]()

在 GitHub 上看了几个关于 MVVM 设计架构的例子，发现他们并没有做到 View 层与 Model 层逻辑的完全分离，此作为对 MVVM 的总结。

## 效果预览

![result](https://github.com/githubhaohao/MVVMRxJavaRetrofitSample/blob/master/image/sample.gif?raw=true)

[Demo 下载](https://github.com/githubhaohao/MVVMRxJavaRetrofitSample/blob/master/demo.apk)

## 准备知识
### MVC
![mvc](https://github.com/githubhaohao/ImageRoom/blob/master/Images/mvvm/mvc.PNG?raw=true)

- **视图（View）**：用户界面。
- **控制器（Controller）**：业务逻辑
- **模型（Model）**：数据保存

---
1. View 传送指令到 Controller
2. Controller 完成业务逻辑后，要求 Model 改变状态
3. Model 将新的数据发送到 View，使用户得到反馈

**缺陷**:View 和 Model 是相互可知，耦合性大，像 Activity 或者 Fragment 既是 Controller 层，又是 View 层，造成工程的可扩展性可维护性非常差。

### MVP
![mvp](https://github.com/githubhaohao/ImageRoom/blob/master/Images/mvvm/mvp.png?raw=true)

在 MVP 设计架构中，Controller 变成了 Presenter。

1. 各层之间的通信，都是双向的。
2. View 与 Model 不直接发生联系，都通过 Presenter 进行间接通信。
3. Model 层与 Presenter 层，Presenter 层与 View 层之间通过接口建立联系。

采用 MVP 设计架构，Activity 与 Fragment 只位于 View 层。

**MVP 的缺陷在于**:由于我们使用了接口的方式去连接 View 层和  Presenter 层，这样就导致了一个问题，当你的页面逻辑很复杂的时候，你的接口会有很多，如果你的 app 中有很多个这样复杂的页面，维护接口的成本就会变的非常的大。

### MVVM
![MVVM](https://github.com/githubhaohao/ImageRoom/blob/master/Images/mvvm/mvvp.PNG?raw=true)

MVVM 模式将 Presenter 改名为 ViewModel，基本上与 MVP 模式完全一致。
**区别在于**: View 层与 ViewModel 层通过`DataBinding`相互绑定，View的变动，自动反映在 ViewModel，反之亦然。

### [RxJava](https://github.com/ReactiveX/RxJava )

RxJava 在 GitHub 主页上的自我介绍是 "a library for composing asynchronous and event-based programs using observable sequences for the Java VM"（一个在 Java VM 上使用可观测的序列来组成异步的、基于事件的程序的库）。

RxJava 本质上是一个异步操作库，是一个能让你用极其简洁的逻辑去处理繁琐复杂任务的异步事件库。

简而言之，RxJava 可以用几个关键字概括：**简洁**，**队列化**，**异步**。

### [Retrofit](https://github.com/square/retrofit)

![retrofit](https://github.com/githubhaohao/ImageRoom/blob/master/Images/mvvm/android-libs-retrofit-1-638.jpg?raw=true)

一个 Android 和 Java 上 HTTP 库（利用注解和 okhttp 来实现和服务器的数据交互）。

[**Retrofit 官方文档:http://square.github.io/retrofit/**](http://square.github.io/retrofit/)

### [DataBinding](https://developer.android.com/topic/libraries/data-binding/index.html)

![data-binding](https://github.com/githubhaohao/ImageRoom/blob/master/Images/mvvm/data_binding.png?raw=true)

在今年的 Google IO 2015 中，Google 在 support-v7 中新增了 Data Binding，使用 Data Binding 可以直接在布局的 xml 中绑定布局与数据，从而简化代码，Android Data Binding 是Android 的 MVVM 框架。因为 Data Binding 是包含在 support-v7 包里面的，所以可以向下兼容到最低 Android 2.1 (API level 7+).

## 实践

嫌代码不够高亮？请移步博客[http://haohaochang.cn](http://haohaochang.cn/2017/02/12/MVVM%EF%BC%8CRxJava%E5%92%8CRetrofit%E7%9A%84%E4%B8%80%E6%AC%A1%E5%AE%9E%E8%B7%B5/)

直接上代码。

### 依赖的第三方类库

```gradle
    compile 'io.reactivex:rxjava:1.1.0'
    compile 'io.reactivex:rxandroid:1.1.0'
    compile 'com.squareup.retrofit2:retrofit:2.0.0-beta4'
    compile 'com.squareup.retrofit2:converter-gson:2.0.0-beta4'
    compile 'com.squareup.retrofit2:adapter-rxjava:2.0.0-beta4'
    compile 'com.github.bumptech.glide:glide:3.7.0'
```

### API

`https://api.douban.com/v2/movie/top250?start=0&count=20`

### 引入DataBinding

```gradle
android {
    ......

    dataBinding {
        enabled = true
    }
}

```

### 工程目录结构

![目录](https://github.com/githubhaohao/ImageRoom/blob/master/Images/mvvm/%E7%9B%AE%E5%BD%95.png?raw=true)

### MVVM 之 View

**MainActivity.java**

```java
getFragmentManager().beginTransaction().add(R.id.movie_fragment, MovieFragment.getInstance()).commit();

```

**MovieFragment.java**

```java
public class MovieFragment extends Fragment implements CompletedListener,SwipeRefreshLayout.OnRefreshListener{

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
        viewModel = new MainViewModel(movieAdapter,this);
        movieFragmentBinding.setViewModel(viewModel);

    }

    @Override
    public void onRefresh() {
        movieAdapter.clearItems();
        viewModel.refreshData();
    }

    @Override
    public void onCompleted() {
        if (movieFragmentBinding.swipeRefreshLayout.isRefreshing()) {
            movieFragmentBinding.swipeRefreshLayout.setRefreshing(false);
        }
    }
}

```

**activity_main.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    tools:context=".view.MainActivity">

    <!-- ... -->

    <FrameLayout
        android:layout_marginTop="?attr/actionBarSize"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/movie_fragment"/>

    <!-- ... -->

</android.support.design.widget.CoordinatorLayout>

```

**movie_fragment.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="viewModel"
            type="com.jc.mvvmrxjavaretrofitsample.viewModel.MainViewModel"/>
    </data>
    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <android.support.v4.widget.SwipeRefreshLayout
            android:visibility="@{viewModel.contentViewVisibility}"
            android:id="@+id/swipe_refresh_layout"
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <android.support.v7.widget.RecyclerView
                android:id="@+id/recycler_view"
                android:background="#ddd"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:padding="8dp">
            </android.support.v7.widget.RecyclerView>

        </android.support.v4.widget.SwipeRefreshLayout>

        <ProgressBar
            style="?android:attr/progressBarStyleLarge"
            android:id="@+id/progress_bar"
            android:visibility="@{viewModel.progressBarVisibility}"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"/>

        <LinearLayout
            android:layout_width="match_parent"
            android:id="@+id/error_info_layout"
            android:visibility="@{viewModel.errorInfoLayoutVisibility}"
            android:orientation="vertical"
            android:layout_height="match_parent">
            <TextView
                android:layout_gravity="center"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@{viewModel.exception}"/>
        </LinearLayout>
    </RelativeLayout>
</layout>

```

**movie_item.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/tools">
    <data>
        <variable
            name="viewModel"
            type="com.jc.mvvmrxjavaretrofitsample.viewModel.MovieViewModel"/>
    </data>
    <android.support.v7.widget.CardView
        xmlns:card_view="http://schemas.android.com/apk/res-auto"
        android:id="@+id/card_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        card_view:cardCornerRadius="4dp"
        card_view:cardBackgroundColor="@color/background"
        card_view:cardUseCompatPadding="true">
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal">
            <ImageView
                android:layout_margin="8dp"
                android:layout_width="60dp"
                android:layout_height="100dp"
                android:src="@drawable/cover"
                app:imageUrl="@{viewModel.imageUrl}"
                android:id="@+id/cover"/>
            <LinearLayout
                android:layout_width="wrap_content"
                android:layout_height="match_parent"
                android:layout_margin="8dp"
                android:orientation="vertical">
                <TextView
                    android:textColor="@android:color/black"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="@{viewModel.title}"
                    android:textSize="12sp"/>
                <LinearLayout
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="4dp"
                    android:orientation="horizontal">
                    <android.support.v7.widget.AppCompatRatingBar
                        android:id="@+id/ratingBar"
                        style="?android:attr/ratingBarStyleSmall"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_gravity="center_vertical"
                        android:isIndicator="true"
                        android:max="10"
                        android:numStars="5"
                        android:rating="@{viewModel.rating}" />

                    <TextView
                        android:id="@+id/rating_text"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_gravity="center_vertical"
                        android:layout_marginLeft="6dp"
                        android:text="@{viewModel.ratingText}"
                        android:textColor="?android:attr/textColorSecondary"
                        android:textSize="10sp" />

                </LinearLayout>
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textColor="?android:attr/textColorSecondary"
                    android:textSize="10sp"
                    android:text="@{viewModel.movieType}"
                    android:id="@+id/movie_type_text"
                    android:layout_marginTop="6dp"
                    />
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textColor="?android:attr/textColorSecondary"
                    android:textSize="10sp"
                    android:text="@{viewModel.year}"
                    android:id="@+id/year_text"
                    android:layout_marginTop="6dp"
                    />
            </LinearLayout>

        </LinearLayout>

    </android.support.v7.widget.CardView>
</layout>
```

**MovieAdapter.java**

```java
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.BindingHolder> {
    private List<Movie> movies;

    public MovieAdapter() {
        movies = new ArrayList<>();
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MovieItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.movie_item, parent, false);
        return new BindingHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        MovieViewModel movieViewModel = new MovieViewModel(movies.get(position));
        holder.itemBinding.setViewModel(movieViewModel);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void addItem(Movie movie) {
        movies.add(movie);
        notifyItemInserted(movies.size() - 1);
    }

    public void clearItems() {
        movies.clear();
        notifyDataSetChanged();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private MovieItemBinding itemBinding;

        public BindingHolder(MovieItemBinding itemBinding) {
            super(itemBinding.cardView);
            this.itemBinding = itemBinding;
        }
    }
}
```

回调接口** CompletedListener.java**

```java
public interface CompletedListener {
    void onCompleted();
}
```
### MVVM 之 ViewModel

**MainViewModel.java**

```java
public class MainViewModel {
    public ObservableField<Integer> contentViewVisibility;
    public ObservableField<Integer> progressBarVisibility;
    public ObservableField<Integer> errorInfoLayoutVisibility;
    public ObservableField<String> exception;
    private Subscriber<Movie> subscriber;
    private MovieAdapter movieAdapter;
    private CompletedListener completedListener;

    public MainViewModel(MovieAdapter movieAdapter,CompletedListener completedListener) {
        this.movieAdapter = movieAdapter;
        this.completedListener = completedListener;
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
                completedListener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                hideAll();
                errorInfoLayoutVisibility.set(View.VISIBLE);
                exception.set(e.getMessage());
            }

            @Override
            public void onNext(Movie movie) {
                movieAdapter.addItem(movie);
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

```

**MovieViewModel.java**

```java
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

```

### MVVM 之 Model

**DouBanMovieService.java**

```java
public interface DouBanMovieService {
    String BASE_URL = "https://api.douban.com/v2/movie/";

    @GET("top250")
    Observable<Response<List<Movie>>> getMovies(@Query("start") int start, @Query("count") int count);
}
```
**RetrofitHelper.java**
```java
public class RetrofitHelper {
    private static final int DEFAULT_TIMEOUT = 10;
    private Retrofit retrofit;
    private DouBanMovieService movieService;
    OkHttpClient.Builder builder;

    /**
     * 获取RetrofitHelper对象的单例
     * */
    private static class Singleton {
        private static final RetrofitHelper INSTANCE = new RetrofitHelper();
    }

    public static RetrofitHelper getInstance() {
        return Singleton.INSTANCE;
    }

    public RetrofitHelper() {
        builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(DouBanMovieService.BASE_URL)
                .build();
        movieService = retrofit.create(DouBanMovieService.class);
    }

    public void getMovies(Subscriber<Movie> subscriber, int start, int count) {
        movieService.getMovies(start, count)
                .map(new Func1<Response<List<Movie>>, List<Movie>>() {
                    @Override
                    public List<Movie> call(Response<List<Movie>> listResponse) {
                        return listResponse.getSubjects();
                    }
                })
                .flatMap(new Func1<List<Movie>, Observable<Movie>>() {
                    @Override
                    public Observable<Movie> call(List<Movie> movies) {
                        return Observable.from(movies);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
```

还有 entity 类，这里就不贴出来了。



