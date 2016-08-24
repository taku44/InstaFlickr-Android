/**
 * Created by taku on 16/08/24.
 */

package com.example.mvince.instagramviewer;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.TextView;

public class MyService4Receiver extends BroadcastReceiver {
    MainActivity2 activity;// onCreate の　receiver.activity = this;

    @Override
    public void onReceive(Context context, Intent intent) {
        // メッセージを取ってくる

        Log.i("MyService4Receiver", "onReceive");

        Bundle bundle = intent.getExtras();
        String message = bundle.getString("message");
        // そいつを表示する
        if (activity != null)
        {
            TextView tv = (TextView) activity.findViewById(R.id.timeTextView);
            tv.setText(message);
        }
    }
}