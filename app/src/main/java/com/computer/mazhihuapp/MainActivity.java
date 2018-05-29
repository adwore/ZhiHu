package com.computer.mazhihuapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.computer.mazhihuapp.Fragment.MainFragment;
import com.computer.mazhihuapp.Fragment.MenuFragment;
import com.computer.mazhihuapp.Fragment.NewFragment;
import com.computer.mazhihuapp.activity.DownloadActivity;
import com.computer.mazhihuapp.db.CacheDbHelper;
import com.guomob.banner.GuomobAdView;
import com.guomob.banner.OnBannerAdListener;
import com.guomob.screen.GuomobInScreenAd;
import com.guomob.screen.OnInScreenAdListener;

public class MainActivity extends AppCompatActivity {

    private FrameLayout fl_content;
    private MenuFragment menu_fragment;
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Toolbar mToolbar;
    private boolean isLight;
    private SharedPreferences sp;
    private long firstTime;
    private String curId;
    private CacheDbHelper dbHelper;
    GuomobAdView m_adView;
    RelativeLayout m_Relative;
    GuomobInScreenAd m_InScreenAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        dbHelper = new CacheDbHelper(this, 1);
        isLight = sp.getBoolean("isLight", true);
        initView();
        loadLatest();
        guomeng();
        InScreenAd();
    }




    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
        setSupportActionBar(mToolbar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                replaceFragment();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        fl_content = (FrameLayout) findViewById(R.id.fl_content);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        final ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    public void loadLatest() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
                .replace(R.id.fl_content, new MainFragment(), "latest")
                .commit();
        curId = "latest";
    }

    public void setCurId(String id) {
        curId = id;
    }

    public void replaceFragment() {
        if (curId.equals("latest")) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_in_from_right).replace(R.id.fl_content, new MainFragment(), "latest").commit();
        } else {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setTitle(sp.getBoolean("isLight", true) ? "夜间模式" : "日间模式");
        return true;

    }

    public void closeMenu() {
        mDrawerLayout.closeDrawers();
    }

    public void setSwipeRefreshEnable(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }

    public void setToolbarTitle(String text) {
        mToolbar.setTitle(text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cation_mode) {
            isLight = !isLight;
            item.setTitle(isLight ? "夜间模式" : "日间模式");
            mToolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
            if (curId.equals("latest")) {
                ((MainFragment) getSupportFragmentManager().findFragmentByTag("latest")).updateTheme();
            } else {
                ((NewFragment) getSupportFragmentManager().findFragmentByTag("news")).updateTheme();
            }
            ((MenuFragment) getSupportFragmentManager().findFragmentById(R.id.menu_fragment)).updateTheme();
            sp.edit().putBoolean("isLight", isLight).apply();
        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, DownloadActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    public boolean isLight() {
        return isLight;
    }

    public CacheDbHelper getCachDbHelper() {
        return dbHelper;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
        } else {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Snackbar sb = Snackbar.make(fl_content, "再按一次提出", Snackbar.LENGTH_LONG);
                sb.getView().setBackgroundColor(getResources().getColor(isLight ? android.R.color.holo_blue_dark : android.R.color.black));
                sb.show();
                firstTime = secondTime;
            } else {
                finish();
            }

        }
    }

    private void guomeng(){

        m_adView = new GuomobAdView(this);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        m_adView.setLayoutParams(lp2);

        m_Relative = (RelativeLayout) findViewById(R.id.banner);
        m_Relative.addView(m_adView);
        m_adView.setOnBannerAdListener(new OnBannerAdListener() {

            //无网络连接
            public void onNetWorkError() {
                Log.e("GuomobLog", "onNetWorkError");
            }

            //加载广告成功
            public void onLoadAdOk() {
                Log.e("GuomobLog", "onLoadAdOk");
            }

            //加载广告失败  arg0：失败原因
            public void onLoadAdError(String arg0) {
                Log.e("GuomobLog", "onLoadAdError" + arg0);
            }
        });

    }

    private void InScreenAd(){
        m_InScreenAd = new GuomobInScreenAd(this,true);
        m_InScreenAd.LoadInScreenAd(true);
        m_InScreenAd.setOnInScreenAdListener(new OnInScreenAdListener() {
            //无网络连接
            public void onNetWorkError() {
                // TODO Auto-generated method stub
                Log.e("GuomobLog", "onNetWorkError");
            }

            //加载广告成功
            public void onLoadAdOk() {
                // TODO Auto-generated method stub
                Log.e("GuomobLog", "onLoadAdOk");
            }

            //加载广告失败 arg0：失败原因
            public void onLoadAdError(String arg0) {
                // TODO Auto-generated method stub
                Log.e("GuomobLog", "onLoadAdError:" + arg0);
            }

            //用户关闭广告
            public void onClose() {
                // TODO Auto-generated method stub
                Log.e("GuomobLog", "onClose");
            }
        });
        m_InScreenAd.ShowInScreenAd();


    }
}
