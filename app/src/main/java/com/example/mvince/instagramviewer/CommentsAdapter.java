package com.example.mvince.instagramviewer;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mvince on 1/26/15.
 */
public class CommentsAdapter extends ArrayAdapter<Comment> {
    public CommentsAdapter(Context context, List<Comment> comments) {
        super(context, android.R.layout.simple_list_item_1, comments);
    }

    class ViewHolder{   //ViewHolderパターン (Custom Viewを使うパターンもある  http://qiita.com/mofumofu3n/items/28f8be64d39b20e69552)    //このクラスはstaticにするべき？？
        TextView tvComment;
        TextView tvCommentTime;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Take the data source at position (e.g. 0)
        // Get the data item
        Comment comment = getItem(position);
        final ViewHolder holder;

        // Check if we are using a recycled view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);

            holder = new ViewHolder();
//        ImageView imgProfile = (ImageView) convertView.findViewById(R.id.imgCommentProfile);
            holder.tvComment = (TextView) convertView.findViewById(R.id.tvComment);
            holder.tvCommentTime = (TextView) convertView.findViewById(R.id.tvCommentTime);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }


        holder.tvComment.setText(Html.fromHtml("<font color='#3f729b'><b>" + comment.username + "</b></font> " + comment.text));
        holder.tvCommentTime.setText(comment.getRelativeTime());

        // Reset the images from the recycled view
//        imgProfile.setImageResource(0);

        // Ask for the photo to be added to the imageview based on the photo url
        // Background: Send a network request to the url, download the image bytes, convert into bitmap, insert bitmap into the imageview
//        Picasso.with(getContext()).load(comment.profileUrl).into(imgProfile);

        // Return the view for that data item
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        // disables selection
        return false;
    }
}
