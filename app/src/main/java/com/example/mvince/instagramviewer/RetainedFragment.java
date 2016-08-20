/**
 * Created by taku on 16/08/21.
 */

package com.example.mvince.instagramviewer;
        import android.app.Activity;
        import android.os.Bundle;
        import android.app.Fragment;
        import android.support.v4.app.FragmentActivity;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentPagerAdapter;
        import android.support.v4.app.FragmentTabHost;
        import android.support.v4.app.ListFragment;
        import android.support.v4.view.ViewPager;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.TabHost;
        import android.widget.TabHost.TabSpec;
        import android.widget.TextView;
        import java.util.ArrayList;

// 大量のデータの復元、ネットワーク接続の再構築、その他負荷のかかる実行が必要になる場合、Fragmentを保持するやり方で再初期化のコストを削減できる
public class RetainedFragment extends Fragment {

    // data object we want to retain
    private ArrayList<InstagramPhoto> data;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment     これが絶対必要　これによりこのFragmentはcreate、destroyされなくなる
        setRetainInstance(true);
    }

    public void setData(ArrayList<InstagramPhoto> data) {
        this.data = data;
    }

    public ArrayList<InstagramPhoto> getData() {
        return data;
    }
}