/**
 * Created by taku on 16/08/17.
 */

package com.example.mvince.instagramviewer;

//import android.util.Log;
//import java.lang.reflect.Type;
//import java.util.List;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.Header;

public class ApiRequest {

    public static final String API_KEY = "86997f23273f5a518b027e2c8c019b0f";
    final static String mainUrl="https://api.flickr.com/services/rest/";

    public interface GetCommentsListener {
        void onGetCommentsSuccess(JSONArray commentsJSONArray);  //実はこれは自動でpublic abstractである。（インターフェイスのメソッドは全てそうなる）
        void onGetCommentsFailure();
    }

    public static void getAllComments(String id,
                                      final GetCommentsListener listener) {

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params2 = new RequestParams();
        params2.put("method", "flickr.photos.comments.getList");
        params2.put("api_key", API_KEY);
        params2.put("photo_id", id);         //対象にする画像のid
        params2.put("per_page", "100");             //欲しいコメント数の上限?
        params2.put("format", "json");
        params2.put("nojsoncallback", "1");

        client.get(mainUrl, params2, new
                        JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                handleResponse(response, listener);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable
                                    throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);

                                if (listener != null) {
                                    listener.onGetCommentsFailure();
                                }
                            }
                        }
        );
    }

    //ポリモーフィズム(オーバーロード)
    private static void handleResponse(JSONObject response, GetCommentsListener listener) {
            if (response != null && response.length() > 0 && !response.equals("null")) {
                /*Gson gson = new Gson();                    //Gson使う？？
                Type type = new TypeToken<List<Comment>>() {}.getType();
                List<Comment> comments = gson.fromJson(response, type);
                comments = decryptNames(comments);*/

                JSONArray commentsJSONArray = null;
                try {
                    JSONObject jsonn = response.getJSONObject("comments");
                    commentsJSONArray = jsonn.getJSONArray("comment");

                } catch (JSONException e) {
                    // Fire if things fail, json parsing is invalid
                    e.printStackTrace();

                    if (listener != null) {
                        listener.onGetCommentsFailure();
                    }
                }

                if(listener != null) {
                    listener.onGetCommentsSuccess(commentsJSONArray);
                }
            } else {
                if (listener != null) {
                    listener.onGetCommentsFailure();
                }
            }
    };





    public interface GetPhotosListener {
        void onGetPhotosSuccess(JSONArray PhotosJSONArray);
        void onGetPhotosFailure();
    }

    public static void getPhotos(String queryString,
                                      final GetPhotosListener listener) {

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params2 = new RequestParams();
        params2.put("method", "flickr.photos.search");
        params2.put("api_key", API_KEY);
        params2.put("tags", queryString);         //検索文字列
        params2.put("per_page", "30");       //総検索結果(total)のうち、まずこの写真数で区切る
        params2.put("page", "1");             //区切ったうちの最初から○番目を指定  historyOffset
        //params2.put("max_upload_date", );  //日時を最初の検索時(searchview)に保存、検索のたびにそのまま代入   String(dateUnix)
        params2.put("format", "json");
        params2.put("nojsoncallback", "1");
        params2.put("extras", "url_n,date_upload,owner_name");  //url_nは不要??

        client.get(mainUrl, params2, new
                        JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                handleResponse(response, listener);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable
                                    throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);

                                if (listener != null) {
                                    listener.onGetPhotosFailure();
                                }
                            }
                        }
        );
    }

    //ポリモーフィズム
    private static void handleResponse(JSONObject response, GetPhotosListener listener) {
        if (response != null && response.length() > 0 && !response.equals("null")) {
            JSONArray photosJSONArray = null;
            try {
                JSONObject jsonn = response.getJSONObject("photos");
                photosJSONArray = jsonn.getJSONArray("photo");

            } catch (JSONException e) {
                // Fire if things fail, json parsing is invalid
                e.printStackTrace();

                if (listener != null) {
                    listener.onGetPhotosFailure();
                }
            }

            if(listener != null) {
                listener.onGetPhotosSuccess(photosJSONArray);
            }
        } else {
            if (listener != null) {
                listener.onGetPhotosFailure();
            }
        }
    };





    public interface GetFavoritesListener {
        void onGetFavoritesSuccess(int likesInt, int listCount);
        void onGetFavoritesFailure();
    }

    public static void getFavorites(String photoId, final int listCount,
                                 final GetFavoritesListener listener) {

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params2 = new RequestParams();
        params2.put("method", "flickr.photos.getFavorites");
        params2.put("api_key", API_KEY);
        params2.put("photo_id", photoId);         //対象にする画像のid
        params2.put("per_page", "1");             //total数だけがほしいので
        params2.put("format", "json");
        params2.put("nojsoncallback", "1");

        client.get(mainUrl, params2, new
                        JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                handleResponse(response,listCount,listener);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable
                                    throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);

                                if (listener != null) {
                                    listener.onGetFavoritesFailure();
                                }
                            }
                        }
        );
    }

    //ポリモーフィズム
    private static void handleResponse(JSONObject response, int listCount ,GetFavoritesListener listener) {
        if (response != null && response.length() > 0 && !response.equals("null")) {
            int likesInt = 0;
            try {
                JSONObject jsonn = response.getJSONObject("photo");
                String likesStr = jsonn.getString("total");
                likesInt = Integer.parseInt(likesStr);

            } catch (JSONException e) {
                // Fire if things fail, json parsing is invalid
                e.printStackTrace();

                if (listener != null) {
                    listener.onGetFavoritesFailure();
                }
            }

            if(listener != null) {
                listener.onGetFavoritesSuccess(likesInt,listCount);
            }
        } else {
            if (listener != null) {
                listener.onGetFavoritesFailure();
            }
        }
    };




    public interface GetFirstCommentsListener {
        void onGetFirstCommentsSuccess(JSONArray commentsJSONArray,int listCount);
        void onGetFirstCommentsFailure();
    }

    public static void getFirstComments(String photoId, final int listCount,
                                 final GetFirstCommentsListener listener) {

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params2 = new RequestParams();
        params2.put("method", "flickr.photos.comments.getList");
        params2.put("api_key", API_KEY);
        params2.put("photo_id", photoId);         //対象にする画像のid
        params2.put("per_page", "2");             //欲しいコメント数の上限?(最初の2つまでのコメントが欲しい)
        params2.put("format", "json");
        params2.put("nojsoncallback", "1");

        client.get(mainUrl, params2, new
                        JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                handleResponse(response, listCount, listener);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable
                                    throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);

                                if (listener != null) {
                                    listener.onGetFirstCommentsFailure();
                                }
                            }
                        }
        );
    }

    //ポリモーフィズム
    private static void handleResponse(JSONObject response, int listCount, GetFirstCommentsListener listener) {
        if (response != null && response.length() > 0 && !response.equals("null")) {
            JSONArray commentsJSONArray = null;
            try {

                Log.d("レスポンス", "x=" + response);

                JSONObject jsonn = response.getJSONObject("comments");
                commentsJSONArray = jsonn.getJSONArray("comment");

            } catch (JSONException e) {     //コメントが0件の場合
                e.printStackTrace();

                if (listener != null) {
                    listener.onGetFirstCommentsSuccess(null, listCount);
                }
            }

            if(listener != null) {
                listener.onGetFirstCommentsSuccess(commentsJSONArray,listCount);
            }
        } else {
            if (listener != null) {
                listener.onGetFirstCommentsFailure();
            }
        }
    };
}
