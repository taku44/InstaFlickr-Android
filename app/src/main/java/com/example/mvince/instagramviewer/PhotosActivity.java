package com.example.mvince.instagramviewer;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.util.Log;

import android.app.FragmentManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhotosActivity extends ActionBarActivity implements ApiRequest.GetPhotosListener,ApiRequest.GetFavoritesListener ,ApiRequest.GetFirstCommentsListener{
    public static final String API_KEY = "86997f23273f5a518b027e2c8c019b0f";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;
    public static String queryString;   //staticメソッドや変数は、複数のところから呼ばれた場合は全ておなじ場所（メモリ）が参照されることになるので、ある所で変数の値を変更されたら他に参照しているところ全てに影響が出ることになります。
    public RetainedFragment dataFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);   //これでlayoutのxmlファイルに定義したレイアウトをこの画面に紐づけしている。→ そのレイアウトがここに表示される

        queryString = getIntent().getStringExtra("queryString");      //画面遷移時の値受け

        Log.d("検索文字列","="+queryString);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);   //layoutのxmlファイルに定義したお絵描き部品をRクラスから参照できる。

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new refreshed());      //別クラスを作成してリスナー登録

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //データ復元するかしないか
        FragmentManager fm = getFragmentManager();
        dataFragment = (RetainedFragment) fm.findFragmentByTag("data");
        // create the fragment and data the first time
        if (dataFragment == null) {
            Log.d("復元データがない場合", "。");

            dataFragment = new RetainedFragment();
            fm.beginTransaction().add(dataFragment, "data").commit();
            // load the data from the web
//            dataFragment.setData();

            if(queryString != null){
                fetchPopularPhotos();
            }else{
                //アラート表示
            }
        }else{      //復元データがあるなら
            Log.d("復元処理2", "。");
            photos = dataFragment.getData();
            aPhotos = new InstagramPhotosAdapter(this, photos);   //これはthisではなくgetApplicationContext()を使うべきでは？
            // Populate the data into the listview
            ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);   //ListViewはAdapterViewを継承している
            // Set the adapter to the listview (population of items)
            lvPhotos.setAdapter(aPhotos);    //こういう仕組み

            aPhotos.notifyDataSetChanged();
        }
    }

    class refreshed implements SwipeRefreshLayout.OnRefreshListener{    //publicなクラスにもできるが。。 http://yyyank.blogspot.jp/2013/01/android.html
        @Override
        public void onRefresh() {
            fetchPopularPhotos();
        }
    }

    private void fetchPopularPhotos() {
        photos = new ArrayList<InstagramPhoto>(); // initialize arraylist
        // Create adapter bind it to the data in arraylist
        aPhotos = new InstagramPhotosAdapter(this, photos);   //これはthisではなくgetApplicationContext()を使うべきでは？
        // Populate the data into the listview
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);   //ListViewはAdapterViewを継承している
        // Set the adapter to the listview (population of items)

        lvPhotos.setAdapter(aPhotos);    //こういう仕組み

        ApiRequest.getPhotos(queryString, this);  //インターフェイスはこのクラスを渡す！  インターフェイスによるcompletionパターン

    }

    //以下、インターフェイスを実装(具体的に)   (ApiRequest結果のタイミングで呼ばれる)
    @Override
    public void onGetPhotosSuccess(JSONArray photosJSONArray) {

        for (int i = 0; i < photosJSONArray.length(); i++) {
            try {
                JSONObject photoJSON = photosJSONArray.getJSONObject(i); // 1, 2, 3, 4
                final InstagramPhoto photo = new InstagramPhoto();
//                 【final修飾子の動き】
//                　[クラスに付けた場合]：そのクラスは継承不可となる
//                　[メソッドに付けた場合]：そのメソッドはオーバーライド不可となる
//                　[変数に付けた場合]：その変数は変更不可（定数）となる
//                        photo.profileUrl = photoJSON.getString("profile_picture");
                photo.username = photoJSON.getString("ownername");
                photo.createdTime = photoJSON.getString("dateupload");
                Log.d("time22", "time22=" + photo.createdTime);
                photo.imageUrl = photoJSON.getString("url_n");
                photo.imageHeight = photoJSON.getInt("height_n");
                photo.id = photoJSON.getString("id");

                photos.add(photo);

                ApiRequest.getFavorites(photo.id,i,this);   //

                ApiRequest.getFirstComments(photo.id,i,this);   //

            }catch (JSONException e) {
                // Fire if things fail, json parsing is invalid
                e.printStackTrace();
            }
        }

        aPhotos.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }
    @Override
    public void onGetPhotosFailure() {
        Log.e("Error", "searching photos");
    }



    @Override
    public void onGetFavoritesSuccess(int likesInt, int listCount) {

        InstagramPhoto photo = photos.get(listCount);
        photo.likesCount = likesInt;        //これで新たな参照が入る
//        photos.add(photo);                //これは不要なので注意！！

        aPhotos.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }
    @Override
    public void onGetFavoritesFailure() {
        Log.e("Error", "get favorites");
    }



    @Override
    public void onGetFirstCommentsSuccess(JSONArray commentsJSONArray,int listCount) {

        InstagramPhoto photo = photos.get(listCount);

        if (commentsJSONArray != null && commentsJSONArray.length() > 0) {
            try {
                photo.commentsCount = commentsJSONArray.length();
                photo.comment1 = commentsJSONArray.getJSONObject(commentsJSONArray.length() - 1).getString("_content");
                photo.user1 = commentsJSONArray.getJSONObject(commentsJSONArray.length() - 1).getString("authorname");
                if (commentsJSONArray.length() > 1) {
                    photo.comment2 = commentsJSONArray.getJSONObject(commentsJSONArray.length() - 2).getString("_content");
                    photo.user2 = commentsJSONArray.getJSONObject(commentsJSONArray.length() - 2).getString("authorname");
                }
            } catch (JSONException e) {
                // Fire if things fail, json parsing is invalid
                e.printStackTrace();
            }
        } else {
            photo.commentsCount = 0;
        }

        //上記が代入終了して...
//        photos.add(photo);        //これは不要なので注意！！

        aPhotos.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }
    @Override
    public void onGetFirstCommentsFailure() {
        Log.e("Error", "get first-comments");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {   //左上の⇦の戻るボタンではないので注意
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //このメソッドのデフォルトの実装では、EditText ウィジェット内のテキストまたは ListView のスクロール位置などのアクティビティのビュー階層の状態に関する情報が保存されます。
    @Override   //アクティビティの追加の状態情報を保存するには、 onSaveInstanceState() を実装し、Bundle オブジェクトにキー値のペアを追加する必要があります。
    public void onSaveInstanceState(Bundle saveInstanceState) {
        Log.d("保存処理1", "。");

        //保存する
        saveInstanceState.putString("queryString", queryString);  //Bundleに渡す

        // デフォルトの実装でビュー階層の状態を保存できるよう、onSaveInstanceState() のスーパークラスの実装を常に呼び出す必要があります。
        super.onSaveInstanceState(saveInstanceState);
    }


    public void onRestoreInstanceState(Bundle savedInstanceState) {     //復元対象の保存済みの状態がある場合のみ、呼ばれる(画面を横にした時などで)

        // デフォルトの実装でビュー階層の状態を復元できるよう、onRestoreInstanceState() のスーパークラスの実装を常に呼び出す必要があります。
        super.onRestoreInstanceState(savedInstanceState);
        //復元する
        queryString = savedInstanceState.getString("queryString");   //Bundleから受け取る
        Log.d("復元処理1 ", "queryString =" + queryString);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // store the data in the fragment
        dataFragment.setData(photos);

        Log.d("保存処理2", "。");
    }
}