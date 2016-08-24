/**
 * Created by taku on 16/08/21.
 */

package com.example.mvince.instagramviewer;

        import android.content.DialogInterface.OnCancelListener;
        import android.os.AsyncTask; import android.util.Log;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;

public class DialogAsyncTask extends AsyncTask<String, Integer, Long> implements OnCancelListener {
    final String TAG = "DialogAsyncTask";
    ProgressDialog dialog;
    Context context;

    public DialogAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {     //最初に UI スレッド上で呼び出されます
        Log.d(TAG, "onPreExecute");
        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait");
        dialog.setMessage("Loading data...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(true);   //ユーザーによるキャンセル処理
        dialog.setOnCancelListener(this);
        dialog.setMax(100);
        dialog.setProgress(0);
        dialog.show();
    }

    @Override
    protected Long doInBackground(String... params) {      //ワーカースレッド上で実行される (AsyncTask を extends するときのひとつめのパラメータの型が渡される)
        Log.d(TAG, "doInBackground - " + params[0]);   //asynctask.execute("Param1")で渡した"Param1"
        try {
            for (int i = 0; i < 10; i++) {
                if (isCancelled()) {   //以下のonCancel→this.cancel(true);が呼ばれるとこれはtrueになる
                    Log.d(TAG, "Cancelled!");
                    break;
                }
                Thread.sleep(500);
                publishProgress((i + 1) * 10);  //進捗を更新  これにより以下のonProgressUpdateが呼ばれる
            }
        } catch (InterruptedException e) {
            Log.d(TAG, "InterruptedException in doInBackground");
        }
        return 123L;   //doInBackgroundが終わると以下のonPostExecuteが呼ばれる
    }

    @Override
    protected void onProgressUpdate(Integer... values) { //UI スレッド上で呼ばれます。引数の型は AsyncTask の extends 時のふたつめのパラメータの型。
        Log.d(TAG, "onProgressUpdate - " + values[0]);
        if(dialog != null) {
            dialog.setProgress(values[0]);
        }
    }

    @Override
    protected void onCancelled() {  //キャンセルした状態で doInBackground を抜けると onPostExecute ではなく、onCancelled が呼ばれる
        Log.d(TAG, "onCancelled");
        if(dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onPostExecute(Long result) {   //AsyncTask を extends するときの三つめのパラメータの型
        Log.d(TAG, "onPostExecute - " + result);
        if(dialog != null) {  //doInBackground途中で画面を横向きにされた場合を考慮??
            dialog.dismiss();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {  //(Backボタンを押すなどして)AsyncTask の cancel メソッドを呼ぶ。
        Log.d(TAG, "Dialog onCancell... calling cancel(true)");
        this.cancel(true);     //isCancelled が true になる。
    }
}