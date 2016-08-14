package com.example.mvince.instagramviewer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CommentsActivity extends ActionBarActivity {
    public static final String API_KEY = "86997f23273f5a518b027e2c8c019b0f";
    private ArrayList<Comment> comments;
    private CommentsAdapter aComments;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        id = getIntent().getStringExtra("id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        fetchComments();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comments, menu);
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

    private void fetchComments() {
        comments = new ArrayList<Comment>(); // initialize arraylist
        // Create adapter bind it to the data in arraylist
        aComments = new CommentsAdapter(this, comments);
        // Populate the data into the listview
        ListView lvComments = (ListView) findViewById(R.id.lvComments);
        // Set the adapter to the listview (population of items)
        lvComments.setAdapter(aComments);
        // https://api.instagram.com/v1/media/<id>/comments?client_id=<clientid>
        // Setup comments url endpoint
        final String mainUrl = "https://api.flickr.com/services/rest/";
        // Create the network client
        AsyncHttpClient client = new AsyncHttpClient();


        RequestParams params2 = new RequestParams();
        params2.put("method", "flickr.photos.comments.getList");
        params2.put("api_key", API_KEY);
        params2.put("photo_id", id);         //対象にする画像のid
        params2.put("per_page", "100");             //欲しいコメント数の上限?
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
                    comments.clear();
                    Log.d("comments1", "comments1=" + response);
                    JSONObject jsonn = response.getJSONObject("comments");
                    Log.d("comments2", "comments2=" + jsonn);
                    commentsJSON = jsonn.getJSONArray("comment");
                    Log.d("comments3", "comments3=" + commentsJSON);

                    for (int i = commentsJSON.length() - 1; i >= 0; i--) {
                        JSONObject commentJSON = commentsJSON.getJSONObject(i);
                        Log.d("comments4", "comments4=" + commentJSON);
                        Comment comment = new Comment();
//                        comment.profileUrl = commentJSON.getJSONObject("from").getString("profile_picture");
                        comment.username = commentJSON.getString("authorname");
                        comment.text = commentJSON.getString("_content");
                        comment.createdTime = commentJSON.getString("datecreate");
                        comments.add(comment);
                    }

                    aComments.notifyDataSetChanged();

                } catch (JSONException e) {
                    // Fire if things fail, json parsing is invalid
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
