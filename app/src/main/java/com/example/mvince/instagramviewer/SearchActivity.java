/**
 * Created by taku on 16/08/16.
 */
package com.example.mvince.instagramviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;

import android.support.v4.view.MenuItemCompat;
import android.view.MenuItem;

public class SearchActivity extends ActionBarActivity {

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {   //Activityクラスで定義されている、「onCreateOptionsMenu」をオーバーライドすることによって、メニューを作成します

        // Set Menu
        getMenuInflater().inflate(R.menu.menu_search, menu);  //menu_search.xmlを読み込む

        MenuItem menuItem = menu.findItem(R.id.menu_searchView);  //そのうち特定のアイテムを指定

        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);   //そのアイテム内で定義してあるactionViewを取得

        // whether display Magnifying Glass Icon at first
        searchView.setIconifiedByDefault(true);
        // whether display Submit Button
        searchView.setSubmitButtonEnabled(false);

        // メニューアイテムを追加します
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {   //匿名のインナークラス
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null) {
                    //intentでPhotosActivityに遷移、クエリ文字列を渡す
                    Intent intent = new Intent(getApplicationContext(), PhotosActivity.class);    //getApplicationContext()を使用   http://qiita.com/yu_eguchi/items/65311af1c9fc0bff0cb0
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  //デフォルトのオプション。  起動されたActivityは既にスタックに存在したとしても新たにインスタンスが生成され実行されます
                    intent.putExtra("queryString", query);         //画面遷移時の値渡し
                    getApplicationContext().startActivity(intent);
                }
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                suggestion_search(newText);
                return false;
            }
        });
//        menu.add(Menu.NONE, , Menu.NONE, "Menu1");
//        menu.add(Menu.NONE, MENU_ID_MENU2, Menu.NONE, "Menu2");

        return super.onCreateOptionsMenu(menu);
    }

    /*// オプションメニューが表示される度に呼び出されます
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    // オプションメニューアイテムが選択された時に呼び出されます
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }*/
}
