package com.example.mvince.instagramviewer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CommentsActivity extends ActionBarActivity implements    //このクラスがインターフェイスを実装
        ApiRequest.GetCommentsListener {
    private ArrayList<Comment> comments;
    private CommentsAdapter aComments;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        id = getIntent().getStringExtra("id");      //画面遷移時の値受け
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

        ApiRequest.getAllComments(id,this);  //idは対象画像のid、インターフェイスはこのクラスを渡す！  インターフェイスによるcompletionパターン
    }

    //以下、インターフェイスを実装(具体的に)   (ApiRequest結果のタイミングで呼ばれる)
    @Override
    public void onGetCommentsSuccess(JSONArray commentsJSONArray) {

        for (int i = commentsJSONArray.length() - 1; i >= 0; i--) {
            try {
                JSONObject commentJSON = commentsJSONArray.getJSONObject(i);
                Comment comment = new Comment();
//                        comment.profileUrl = commentJSON.getJSONObject("from").getString("profile_picture");
                comment.username = commentJSON.getString("authorname");
                comment.text = commentJSON.getString("_content");
                comment.createdTime = commentJSON.getString("datecreate");
                comments.add(comment);
            }catch (JSONException e) {
                // Fire if things fail, json parsing is invalid
                e.printStackTrace();
            }
        }

        aComments.notifyDataSetChanged();
    }

    @Override
    public void onGetCommentsFailure() {
        Log.e("Error", "downloading all comments");
    }
}
