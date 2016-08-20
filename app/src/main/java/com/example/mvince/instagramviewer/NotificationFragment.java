/**
 * Created by taku on 16/08/20.
 */
package com.example.mvince.instagramviewer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class NotificationFragment extends ListFragment {
    /**
     * ListFragmentにどのようなアイテムを入れるかを実装
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 実際のリストアイテム代入
        String[] category = {"通知1:天気", "通知２:地震", "通知3:アイテム", "通知4:"};
        ArrayAdapter<String> adapter = (new ArrayAdapter<String>(getActivity(), R.layout.row, category){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                view.setBackgroundColor(Color.GRAY);
                return view;    //一行ずつViewを渡す
            }
        });

        setListAdapter(adapter);   //listViewにはアダプタが必要。

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


                //個々の通知の既読をオンにする?
                Log.d("lvタップされた", "id=" + id);


            }
        });
    }

}