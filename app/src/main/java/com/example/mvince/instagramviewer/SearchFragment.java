/**
 * Created by taku on 16/08/20.
 */

package com.example.mvince.instagramviewer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;

import android.support.v4.view.MenuItemCompat;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Button;
import android.text.SpannableStringBuilder;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import android.view.LayoutInflater;

public class SearchFragment extends Fragment{

    EditText editText1;
    Button okButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  //FragmentがView階層に関連付けられる時に呼ばれる。
//        Fragmentで表示するViewを戻り値として返す、これがないと何も表示されない
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
//        　ListView にAdapter を セットするなどの時は、Viewが作成された後でなければならないのでこのメソッドで行う。
//        　今までActivityのonCreate()で行なっていた処理はこのメソッドに記述しておくと良い。
        super.onActivityCreated(savedInstanceState);    //Fragmentが関連付いているActivityのonCreate()が呼ばれた直後に呼び出される。

        okButton = (Button)getActivity().findViewById(R.id.okButton);
        editText1 = (EditText)getActivity().findViewById(R.id.editText1);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intentでPhotosActivityに遷移、クエリ文字列を渡す
                if (editText1.length() != 0) {
                    SpannableStringBuilder sb = (SpannableStringBuilder)editText1.getText();
                    String queryString = sb.toString();
                    Intent intent = new Intent(getActivity().getApplicationContext(), PhotosActivity.class);    //getApplicationContext()を使用   http://qiita.com/yu_eguchi/items/65311af1c9fc0bff0cb0
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  //デフォルトのオプション。  起動されたActivityは既にスタックに存在したとしても新たにインスタンスが生成され実行されます
                    intent.putExtra("queryString", queryString);         //画面遷移時の値渡し
                    getActivity().getApplicationContext().startActivity(intent);
                }
            }
        });

        /*editText1.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    // ここに処理
                }
            }
        });*/
    }
}