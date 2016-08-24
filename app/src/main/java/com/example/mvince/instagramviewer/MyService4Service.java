/**
 * Created by taku on 16/08/24.
 */

package com.example.mvince.instagramviewer;

        import java.util.Calendar;
        import java.util.GregorianCalendar;
        import java.util.TimeZone;

        import android.app.Service;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Handler;
        import android.os.IBinder;
        import android.os.Message;
        import android.util.Log;

public class MyService4Service extends Service {
    public static final String actionName = "MYSERVICE4_ACTION";
    MyHandler mHandler = null;
    private static Context contex;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MyService4Service", "onStartCommand");

        if (mHandler == null)
        {
            mHandler = new MyHandler();
            mHandler.sendEmptyMessage(1);       //実行させる場合は、どこからか、これを呼ぶ (識別子を指定する)
        }

        contex = getBaseContext();

        return START_NOT_STICKY;//サービスの強制終了後、再起動しない
    }

    public void onDestroy()
    {
        if (mHandler != null)
        {
            mHandler.removeMessages(1);  //(識別子を指定する)
            mHandler = null;
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    // handlerクラス を使用して、一定時間毎にイベントを発生させる
    static class MyHandler extends Handler
    {
        public void handleMessage(Message msg)
        {
            GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
            String stringTime = "現在時刻 " + gc.get(Calendar.HOUR_OF_DAY) + ":" + gc.get(Calendar.MINUTE) + ":" + gc.get(Calendar.SECOND);

            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra("message", stringTime);
            broadcastIntent.setAction(actionName);    //intentにアクションを設定する
            contex.sendBroadcast(broadcastIntent);    //intentをブロードキャストする

            sendEmptyMessageDelayed(1, 1000);   //第一引数はメッセージの識別子として使う事を目的とした、ユーザが勝手に定義して使う事のできる値。
            //↑ 第二引数がインターバル時間(ミリ秒)

            //messageオブジェクトでobjectを渡すことも可能。 参考 http://ichitcltk.hustle.ne.jp/gudon2/index.php?pageType=file&id=Android010_Handler
        }
    }
}