package com.example.mvince.instagramviewer;

import android.support.v7.app.ActionBarActivity;
//import android.support.v7.app.ActionBar.
//import android.support.v7.app.ActionBar.Tab;
//import android.support.v7.app.ActionBar.TabListener;
//import android.app.ActionBar;
//import android.app.ActionBar.Tab;
//import android.app.ActionBar.TabListener;
//import android.support.v4.app.FragmentTransaction;

import android.widget.TabHost;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

/*
import com.example.mvince.instagramviewer.R;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;*/

import android.content.Context;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
//import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;


public class MainActivity extends ActionBarActivity implements TabHost.OnTabChangeListener{

    private TabHost mTabHost;
    private String mLastTabId;  // Last selected tabId

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;       //これはviewGroupの一種

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);


        // FragmentPagerAdapterを継承したクラスのアダプターを独自に作成
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager());

        // ViewPagerにSectionPagerAdapterをセット
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);




        // Action barのモードをタブモードに切り替え
//        final ActionBar actionBar = getActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        /* Tab1 設定 */
        TabSpec tab1 = mTabHost.newTabSpec("tab1");
        tab1.setIndicator("TAB1");
        tab1.setContent(new DummyTabFactory(this));
        mTabHost.addTab(tab1);

        // Tab2 設定
        TabSpec tab2 = mTabHost.newTabSpec("tab2");
        tab2.setIndicator("TAB2");
        tab2.setContent(new DummyTabFactory(this));
        mTabHost.addTab(tab2);

        // Tab3 設定
        TabSpec tab3 = mTabHost.newTabSpec("tab3");
        tab3.setIndicator("TAB3");
        tab3.setContent(new DummyTabFactory(this));
        mTabHost.addTab(tab3);

        // タブ変更時イベントハンドラ
        mTabHost.setOnTabChangedListener(this);
        // 初期タブ選択
        onTabChanged("tab1");


/**
 * スワイプしたときにもActionbarのタブ（NavigationItem）を常に表示させる処理
 */
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
//                actionBar.setSelectedNavigationItem(position);

//                タブの位置を変更



            }
        });

        /*// getCountで指定されたタブの数をフェッチ。
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Actionbarにタブを追加。
            // getPageTitleでタブのタイトルを表示
            actionBar.addTab(actionBar.newTab()
                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }*/
    }

    //以下、TabListenerインターフェイスの実装

//    タブの選択が変わったときに呼び出される
    @Override
    public void onTabChanged(String tabId) {
        Log.d("TAB_FRAGMENT_LOG","tabId:" + tabId);

        //ここで表示するフラグメントを決定する　　これでSectionsPagerAdapter.getItemが呼び出される！(一行ずつのlistVIewと似てる)
        mViewPager.setCurrentItem(Integer.parseInt(tabId));       //tab.getPosition()

        /*if(mLastTabId != tabId){
            FragmentTransaction fragmentTransaction
                    = getSupportFragmentManager().beginTransaction();
            if("tab1" == tabId){                        //Stringの同値判断はequalsを使うべきでは??
                fragmentTransaction
                        .replace(R.id.realtabcontent, new Tab1Fragment());
            }else if("tab2" == tabId){
                fragmentTransaction
                        .replace(R.id.realtabcontent, new Tab2Fragment());
            }else if("tab3" == tabId){
                fragmentTransaction
                        .replace(R.id.realtabcontent, new Tab3Fragment());
            }
            mLastTabId = tabId;
            fragmentTransaction.commit();
        }*/
    }


    /*
     * android:id/tabcontent のダミーコンテンツ
     */
    private static class DummyTabFactory implements TabContentFactory {

        /* Context */
        private final Context mContext;

        DummyTabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            return v;
        }
    }


/**
 * タブを選択した時の処理
 */
//    @Override
//    public void onTabSelected (Tab tab, FragmentTransaction fragmentTransaction){
//         ここで表示するフラグメントを決定する　　         これでSectionsPagerAdapter.getItemが呼び出される！(一行ずつのlistVIewと似てる)
//        mViewPager.setCurrentItem(tab.getPosition());
//    }

/**
 * タブの選択が外れた場合の処理
 */
//    @Override
//    public void onTabUnselected(Tab tab, FragmentTransaction ft) {

//    }

/**
 * タブが2度目以降に選択された場合の処理
 */
//    @Override
//        public void onTabReselected (ActionBar.Tab tab, FragmentTransaction fragmentTransaction){
//     }


    /**
     * ViewPagerの動作を作成するアダプター
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * 各ページに設定するFragmentを指定
         * @params タブの位置。左から0,1,2..となる
         */
        @Override
        public Fragment getItem(int position) {

            ProfileFragment fragment = new ProfileFragment();

            /*if (position == 0){
                ProfileFragment fragment = new ProfileFragment();
                return fragment;
            } else if (position == 1){
//                FragmentTest fragment = new FragmentTest();
                ProfileFragment fragment = new ProfileFragment();
                return fragment;
            } else if (position == 2){
            //                FragmentTest fragment = new FragmentTest();
                ProfileFragment fragment = new ProfileFragment();
                return fragment;
            }*/
            return fragment;
        }

        /**
         * タブの数を決定
         */
        @Override
        public int getCount() {
            return 3;
        }

        /**
         * タブのタイトルを決定
         */
        @Override
        public CharSequence getPageTitle(int position) {    //pagerTitleStripにページの情報を渡すメソッド
            switch (position) {
                case 0:
                    return getString(R.string.profile).toUpperCase();
                case 1:
                    return getString(R.string.search).toUpperCase();
                case 2:
                    return getString(R.string.notification).toUpperCase();
            }
            return null;
        }
    }
}






//以下、sdk標準の場合

/*package com.example.mvince.instagramviewer;


import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    *//**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     *//*
    private SectionsPagerAdapter mSectionsPagerAdapter;

    *//**
     * The {@link ViewPager} that will host the section contents.
     *//*
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);     //この辺りの参考：http://qiita.com/Yuki_Yamada/items/6d8b38effeb38ed96d78
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    *//**
     * A placeholder fragment containing a simple view.
     *//*
    public static class PlaceholderFragment extends Fragment {
        *//**
         * The fragment argument representing the section number for this
         * fragment.
         *//*
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        *//**
         * Returns a new instance of this fragment for the given section
         * number.
         *//*
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);  //バンドルによるkey-valueストア保存みたいなもの http://qiita.com/kojionilk/items/138eea19dadb14997136
            fragment.setArguments(args);   //フラグメントに引数渡すパターン
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    *//**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     *//*
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);       //staticなためインスタンス不要。
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}*/


