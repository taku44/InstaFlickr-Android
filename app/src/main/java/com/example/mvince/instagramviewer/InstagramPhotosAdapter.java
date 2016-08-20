package com.example.mvince.instagramviewer;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mvince on 1/25/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {    //sdk標準のアダプタを継承して独自定義したアダプタ

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos) {
        super(context, android.R.layout.simple_list_item_1, photos);
    }

    class ViewHolder{   //ViewHolderパターン (Custom Viewを使うパターンもある  http://qiita.com/mofumofu3n/items/28f8be64d39b20e69552)
        ImageView imgPhoto;
        TextView tvUsername;
        TextView tvTime;
        TextView tvLikes;
        TextView tvCaption;
        TextView tvViewAllComments;
        TextView tvComment1;
        TextView tvComment2;
    }

    //おそらくnotifyDataSetChangedで呼ばれる？Adapterで表示させる画面は一行一行表示させるUIを指定します。 その際に、一行一行読み込まれる際のコールバックがこのgetView()。次に表示するViewを戻り値として返す。
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {     //convertViewとは「一行のUIを生成する」という重い作業のために、次の新しい行に対して再利用されるインスタンス(画面上から表示しきれなくなったViewの事。そのため、初回生成時にはnullとなる)のこと。 parentとは、追加対象のViewGroupの事。
        // Take the data source at position (e.g. 0)
        // Get the data item 　　　 これで今回のアイテム1つを確保
        InstagramPhoto photo = getItem(position);
        final ViewHolder holder;

        // Check if we are using a recycled view (再利用可能なlayoutがあったらそれを使う。なかったら新しく作成する) もしこの記述がなく、毎回LayoutInflaterで作成していたらカクカクで重いアプリになってしまいます
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);  //第三引数が第二引数で指定したViewをroot要素とするかどうか。falseなら、単純に第一引数で指定したレイアウトのRootViewがそのままRootになる

            holder = new ViewHolder();
//        ImageView imgProfile = (ImageView) convertView.findViewById(R.id.imgProfile);
            holder.imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
            holder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);            //「何度も何度もfindViewByIdをするのはコスト的に問題がある」と言う事で、ViewHolderパターンと呼ばれるものを使うことがほとんどです
            holder.tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
            holder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            holder.tvViewAllComments = (TextView) convertView.findViewById(R.id.tvViewAllComments);
            holder.tvComment1 = (TextView) convertView.findViewById(R.id.tvComment1);
            holder.tvComment2 = (TextView) convertView.findViewById(R.id.tvComment2);

            convertView.setTag(holder);  //単一のobjectを紐付け可能
        }else{
            holder = (ViewHolder) convertView.getTag();  //紐付けられていた単一のobjectを取り出す(これには既に各viewが紐付けられている)(従って、findViewByIdに掛かるコストを消せる)
        }

        // Populate the subviews (textfield, imageview) with the correct data
        holder.tvUsername.setText(photo.username);
        holder.tvTime.setText(photo.getRelativeTime());
        holder.tvLikes.setText(String.format("%d likes", photo.likesCount));
        if (photo.caption != null) {
            holder.tvCaption.setText(Html.fromHtml("<font color='#3f729b'><b>" + photo.username + "</b></font> " + photo.caption));
            holder.tvCaption.setVisibility(View.VISIBLE);
        } else {
            holder.tvCaption.setVisibility(View.GONE);
        }

        if (photo.commentsCount > 0) {
            holder.tvViewAllComments.setText(String.format("view all %d comments", photo.commentsCount));
            // set click handler for view all comments
            holder.tvViewAllComments.setOnClickListener(new View.OnClickListener() {     //インナークラスでイベントリスナーを登録
                @Override
                public void onClick(View v) {           //タップされたらこれが実行される
                    Intent intent = new Intent(getContext(), CommentsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //起動されるAcitivityより前のスタックのAcitvityをクリアして起動させる http://blog.choilabo.com/20150216/505
                    InstagramPhoto ip = getItem(position);
                    intent.putExtra("id", ip.id);         //画面遷移時の値渡し   Intent のインスタンスは内部的に Bundle を持っており、intent.putExtra() は実際はその内部の Bundle に対し値を設定しているだけである
                    getContext().startActivity(intent);
                }
            });
            holder.tvViewAllComments.setVisibility(View.VISIBLE);
        } else {
            holder.tvViewAllComments.setVisibility(View.GONE);
        }

        // Set last 2 comments
        if (photo.comment1 != null) {
            holder.tvComment1.setText(Html.fromHtml("<font color='#3f729b'><b>" + photo.user1 + "</b></font> " + photo.comment1));   //これで元のconvertViewのデータの書き換えが実行される
            holder.tvComment1.setVisibility(View.VISIBLE);
        } else {
            holder.tvComment1.setVisibility(View.GONE);
        }

        if (photo.comment2 != null) {
            holder.tvComment2.setText(Html.fromHtml("<font color='#3f729b'><b>" + photo.user2 + "</b></font> " + photo.comment2));
            holder.tvComment2.setVisibility(View.VISIBLE);
        } else {
            holder.tvComment2.setVisibility(View.GONE);
        }

        // use device width for photo height
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        holder.imgPhoto.getLayoutParams().height = displayMetrics.widthPixels;

        // Reset the images from the recycled view
//        imgProfile.setImageResource(0);
        holder.imgPhoto.setImageResource(android.R.color.transparent);    //setImageResource(0);      ここでconvertViewの以前のimageViewを消す  http://stackoverflow.com/questions/2859212/how-to-clear-an-imageview-in-android

        // Ask for the photo to be added to the imageview based on the photo url
        // Background: Send a network request to the url, download the image bytes, convert into bitmap, insert bitmap into the imageview
//        Picasso.with(getContext()).load(photo.profileUrl).into(imgProfile);
        //読み込み、キャッシュ、表示の全てやってくれる  (読み込みについてはAsyncTaskクラスを用いてもやれる、ようは画像のデコードや通信処理などの重い事を別スレッドで非同期に処理している)  http://tomoyukim.hatenablog.com/entry/2014/04/24/092125
        Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.drawable.instagram_glyph_on_white).into(holder.imgPhoto);
        // Return the view for that data item
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        // disables selection
        return false;
    }
}

