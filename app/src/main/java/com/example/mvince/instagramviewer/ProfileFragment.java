/**
 * Created by taku on 16/08/20.
 */

package com.example.mvince.instagramviewer;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.graphics.Color;
//import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

//import android.util.Log;


public class ProfileFragment extends ListFragment {
    /**
     * ListFragmentにどのようなアイテムを入れるかを実装
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 実際のリストアイテム代入
        String[] category = {"ユーザー名:MyName", "メールアドレス:yahoo.co.jp", "年齢:20", "職業:SE"};
        ArrayAdapter<String> adapter = (new ArrayAdapter<String>(getActivity(), R.layout.row, category){
            /**
             * GetViewをオーバーライドして背景色を交互に変える
             */
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                if (position == 0){
                    view.setBackgroundColor(Color.YELLOW);
                }else{
                    //view.setBackgroundColor();
                }

                return view;        //一行ずつViewを渡す
            }
        });

        setListAdapter(adapter);

        ListView lv = getListView();

        /**
         * リストの項目をクリックしたときの処理（今回は違うActivityにタッチした場所ごとの値を渡して呼び出します）
         * @params position：タッチした場所（一番上は0）
         */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                /*Intent intent = new Intent(getActivity(), SubActivity.class);
                // Intentに値をセット
                intent.putExtra("id", position);
                startActivity(intent);*/


                //プロフィール設定変更ダイアログを表示？
                Log.d("lvタップされた","id="+id);


            }
        });
    }

}