package com.agmcs.countdown;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;


import com.gc.materialdesign.widgets.Dialog;

import java.util.ArrayList;
import java.util.List;

import github.chenupt.dragtoplayout.DragTopLayout;


public class MainActivity extends ActionBarActivity {
    private ViewPager viewPager;
    private List<Fragment> list = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        if(toolbar != null){
            setSupportActionBar(toolbar);
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.add_event:{
                        sendBroadcast(new Intent("com.agmcs.countdown.add_event"));
                        DragTopLayout dtl = (DragTopLayout)findViewById(R.id.dtl);
                        viewPager.setCurrentItem(0);
                        dtl.toggleTopView();
                        ListView lv = (ListView)findViewById(R.id.listview);
                        lv.setSelection(0);
                        Log.d("hihihi", "yes");
                        break;
                    }
                    case R.id.more:{
                        Dialog dialog = new Dialog(MainActivity.this,"about","欢迎反馈:skkg@qq.com,\n查看悬浮的影子?");
                        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.wandoujia.com/apps/com.agmcs.FloatingShadow"));
                                startActivity(i);
                            }
                        });
                        dialog.show();
                    }
                }
                return true;
            }
        });

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        list.add(new EventListFragment());
        list.add(new DateCalmFrag());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),list);
        viewPager.setAdapter(adapter);
//        FragmentManager fm = getSupportFragmentManager();
//        Fragment fragment = fm.findFragmentById(R.id.content);
//        if (fragment == null) {
//            fragment = new EventListFragment();
//            fm.beginTransaction()
//                    .add(R.id.content, fragment)
//                    .commit();
//        }
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter{
        private List<Fragment> list;

        public ViewPagerAdapter(FragmentManager fm,List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
