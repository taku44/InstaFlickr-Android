package com.example.mvince.instagramviewer;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhotosActivity extends ActionBarActivity {
    public static final String API_KEY = "86997f23273f5a518b027e2c8c019b0f";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPopularPhotos();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
        photos = new ArrayList<InstagramPhoto>(); // initialize arraylist
        // Create adapter bind it to the data in arraylist
        aPhotos = new InstagramPhotosAdapter(this, photos);
        // Populate the data into the listview
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        // Set the adapter to the listview (population of items)
        lvPhotos.setAdapter(aPhotos);    //こういう仕組み



        final String mainUrl = "https://api.flickr.com/services/rest/";
        // Create the network client
        final AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("method", "flickr.photos.search");
        params.put("api_key", API_KEY);
        params.put("tags", "dog");         //検索文字列
        params.put("per_page", "30");      //総検索結果(total)のうち、まずこの写真数で区切る
        params.put("page", "1");             //区切ったうちの最初から○番目を指定  historyOffset
        //params.put("max_upload_date", );  //日時を最初の検索時(searchview)に保存、検索のたびにそのまま代入   String(dateUnix)
        params.put("format", "json");
        params.put("nojsoncallback", "1");
        params.put("extras", "url_n,date_upload,owner_name");  //url_nは不要??

        // Trigger the network request
        client.get(mainUrl, params, new JsonHttpResponseHandler() {
            // define success and failure callbacks
            // Handle the successful response (popular photos JSON)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON = null;
                JSONArray commentsJSON = null;
                try {
                    photos.clear();

                    Log.d("photoJSON1", "photoJSON1=" + response);
                    JSONObject jsonn = response.getJSONObject("photos");    //.getJSONArray("")
                    Log.d("photoJSON11", "photoJSON11=" + jsonn);
                    photosJSON = jsonn.getJSONArray("photo");
                    Log.d("photoJSON2", "photoJSON2=" + photosJSON);

                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i); // 1, 2, 3, 4
                        Log.d("photoJSON3", "photoJSON=" + photoJSON);
                        final InstagramPhoto photo = new InstagramPhoto();
//                        photo.profileUrl = photoJSON.getString("profile_picture");
                        photo.username = photoJSON.getString("ownername");
                        photo.createdTime = photoJSON.getString("dateupload");
                        Log.d("time22", "time22=" + photo.createdTime);
                        photo.imageUrl = photoJSON.getString("url_n");
                        photo.imageHeight = photoJSON.getInt("height_n");
                        photo.id = photoJSON.getString("id");



//                        int likesInt = fetchPhotoLikes(photo.id);
//                        photo.likesCount = likesInt;

                        RequestParams params = new RequestParams();
                        params.put("method", "flickr.photos.getFavorites");
                        params.put("api_key", API_KEY);
                        params.put("photo_id", photo.id);         //対象にする画像のid
                        params.put("per_page", "1");             //total数だけがほしいので
                        params.put("format", "json");
                        params.put("nojsoncallback", "1");

                        // Trigger the network request
                        client.get(mainUrl, params, new JsonHttpResponseHandler() {
                            // define success and failure callbacks
                            // Handle the successful response (popular photos JSON)
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                JSONArray photosJSON = null;
                                JSONArray commentsJSON = null;
                                try {
                                    Log.d("likes1", "likes1=" + response);
                                    JSONObject jsonn = response.getJSONObject("photo");
                                    Log.d("likes2", "likes2=" + jsonn);
                                    String likesStr = jsonn.getString("total");
                                    Log.d("likes3", "likes3=" + likesStr);


                                    int likesInt = Integer.parseInt(likesStr);
                                    photo.likesCount = likesInt;

//                    fetchPhotoComments();

                                    //上記が代入終了して...
                                    photos.add(photo);
                                    aPhotos.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    // Fire if things fail, json parsing is invalid
                                    e.printStackTrace();
                                }
                                swipeContainer.setRefreshing(false);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                            }
                        });


                        /*JSONArray commentsJSON = fetchPhotoComments(photo.id);
                        if (commentsJSON.length() > 0) {
                            photo.commentsCount = commentsJSON.length();
                            photo.comment1 = commentsJSON.getJSONObject(commentsJSON.length() - 1).getString("_content");
                            photo.user1 = commentsJSON.getJSONObject(commentsJSON.length() - 1).getString("authorname");
                            if (commentsJSON.length() > 1) {
                                photo.comment2 = commentsJSON.getJSONObject(commentsJSON.length() - 2).getString("_content");
                                photo.user2 = commentsJSON.getJSONObject(commentsJSON.length() - 2).getString("authorname");
                            }
                        } else {
                            photo.commentsCount = 0;
                        }
                        photos.add(photo);
                        aPhotos.notifyDataSetChanged();*/

                        RequestParams params2 = new RequestParams();
                        params2.put("method", "flickr.photos.comments.getList");
                        params2.put("api_key", API_KEY);
                        params2.put("photo_id", photo.id);         //対象にする画像のid
                        params2.put("per_page", "2");             //欲しいコメント数の上限?
                        params2.put("format", "json");
                        params2.put("nojsoncallback", "1");

                        // Trigger the network request
                        client.get(mainUrl, params2, new JsonHttpResponseHandler() {
                            // define success and failure callbacks
                            // Handle the successful response (popular photos JSON)
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                JSONArray commentsJSON = null;
                                try {
                                    Log.d("comments1", "comments1=" + response);
                                    JSONObject jsonn = response.getJSONObject("comments");
                                    Log.d("comments2", "comments2=" + jsonn);
                                    commentsJSON = jsonn.getJSONArray("comment");
                                    Log.d("comments3", "comments3=" + commentsJSON);

                                    photo.commentsCount = commentsJSON.length();
                                    if (commentsJSON.length() > 0) {
                                        photo.comment1 = commentsJSON.getJSONObject(commentsJSON.length() - 1).getString("_content");
                                        photo.user1 = commentsJSON.getJSONObject(commentsJSON.length() - 1).getString("authorname");
                                        if (commentsJSON.length() > 1) {
                                            photo.comment2 = commentsJSON.getJSONObject(commentsJSON.length() - 2).getString("_content");
                                            photo.user2 = commentsJSON.getJSONObject(commentsJSON.length() - 2).getString("authorname");
                                        }
                                    } else {
                                        photo.commentsCount = 0;
                                    }

                                    //上記が代入終了して...
                                    photos.add(photo);
                                    aPhotos.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    // Fire if things fail, json parsing is invalid
                                    e.printStackTrace();
                                }
                                swipeContainer.setRefreshing(false);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                            }
                        });
                    }
                    // Notified the adapter that it should populate new changes into the listview
                    aPhotos.notifyDataSetChanged();

                } catch (JSONException e) {
                    // Fire if things fail, json parsing is invalid
                    e.printStackTrace();
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }

    // CallBackインターフェイスを作成してリファクタリング予定
    private int fetchPhotoLikes(String photoId) {
        String mainUrl = "https://api.flickr.com/services/rest/";
        // Create the network client
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("method", "flickr.photos.getFavorites");
        params.put("api_key", API_KEY);
        params.put("photo_id", photoId);         //対象にする画像のid
        params.put("per_page", "1");             //total数だけがほしいので
        params.put("format", "json");
        params.put("nojsoncallback", "1");

        int likesInt = 0;

        // Trigger the network request
        client.get(mainUrl, params, new JsonHttpResponseHandler() {
            // define success and failure callbacks
            // Handle the successful response (popular photos JSON)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON = null;
                JSONArray commentsJSON = null;
                try {
                    Log.d("likes1", "likes1=" + response);
                    JSONObject jsonn = response.getJSONObject("photo");
                    Log.d("likes2", "likes2=" + jsonn);
                    String likesStr = jsonn.getString("total");
                    Log.d("likes3", "likes3=" + likesStr);

//                    likesInt = Integer.parseInt(likesStr);

                } catch (JSONException e) {
                    // Fire if things fail, json parsing is invalid
                    e.printStackTrace();
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        return likesInt;
    }

    private JSONArray fetchPhotoComments(String photoId) {
        String mainUrl = "https://api.flickr.com/services/rest/";
        // Create the network client
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params2 = new RequestParams();
        params2.put("method", "flickr.photos.comments.getList");
        params2.put("api_key", API_KEY);
        params2.put("photo_id", photoId);         //対象にする画像のid
        params2.put("per_page", "2");             //欲しいコメント数の上限?
        params2.put("format", "json");
        params2.put("nojsoncallback", "1");

        final JSONArray commentsJSON = null;

        // Trigger the network request
        client.get(mainUrl, params2, new JsonHttpResponseHandler() {
            // define success and failure callbacks
            // Handle the successful response (popular photos JSON)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    Log.d("comments1", "comments1=" + response);
                    JSONObject jsonn = response.getJSONObject("comments");
                    Log.d("comments2", "comments2=" + jsonn);
//                    commentsJSON = jsonn.getJSONArray("comment");
                    Log.d("comments3", "comments3=" + commentsJSON);

                } catch (JSONException e) {
                    // Fire if things fail, json parsing is invalid
                    e.printStackTrace();
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        return commentsJSON;
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
