package sctek.cn.ysbracelet.activitys;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.fragments.FamilyHRateFragment;
import sctek.cn.ysbracelet.fragments.FamilySleepFragment;
import sctek.cn.ysbracelet.fragments.FamilySportsFragment;
import sctek.cn.ysbracelet.fragments.HomeFragment;
import sctek.cn.ysbracelet.fragments.MessageFragment;
import sctek.cn.ysbracelet.fragments.OnFragmentInteractionListener;
import sctek.cn.ysbracelet.fragments.SettingsFragment;
import sctek.cn.ysbracelet.fragments.StatisticsFragment;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, OnFragmentInteractionListener {

    private final static String TAG = MainActivity.class.getSimpleName();

    private TextView titleTv;
    private View actionBarV;
    private ImageButton backIb;
    private ImageButton actionIb;

    private RadioButton nullRb;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        initNavigationView();

        initContentView();

    }

    private void initContentView() {
        try {
            showFragment(HomeFragment.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initNavigationView() {

        actionBarV = findViewById(R.id.action_bar_rl);
        titleTv = (TextView)findViewById(R.id.title_tv);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);
        actionIb = (ImageButton)findViewById(R.id.action_ib);

        titleTv.setText(R.string.label_home);
        backIb.setOnClickListener(this);
        actionIb.setOnClickListener(this);
        actionIb.setImageResource(R.drawable.statistics);
        actionIb.setVisibility(View.GONE);

        TextView sportsItem = (TextView) findViewById(R.id.statistics_fimaly_sports);
        TextView hrateItem = (TextView) findViewById(R.id.statistics_fimaly_hrate);
        TextView sleepItem = (TextView) findViewById(R.id.statistics_fimaly_sleep);
        View sideNavigationView = findViewById(R.id.side_navigation_ll);

        sideNavigationView.getBackground().setAlpha(150);

        sportsItem.setOnClickListener(this);
        hrateItem.setOnClickListener(this);
        sleepItem.setOnClickListener(this);

        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.bottom_navigation_rg);
        nullRb = (RadioButton)findViewById(R.id.null_rb);
        radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            Log.e(TAG, "onCheckedChanged:" + checkedId + " " + R.id.home_rb + " " + R.id.messages_rb);
            actionIb.setVisibility(View.GONE);
            try {
                switch (checkedId) {
                    case R.id.home_rb:
                        titleTv.setText(R.string.label_home);
                        actionBarV.setBackgroundColor(getResources().getColor(R.color.orange));
                        showFragment(HomeFragment.class);
                        break;
                    case R.id.messages_rb:
                        titleTv.setText(R.string.label_messages);
                        actionBarV.setBackgroundColor(getResources().getColor(R.color.orange));
                        showFragment(MessageFragment.class);
                        break;
                    case R.id.settings_rb:
                        titleTv.setText(R.string.label_settings);
                        actionBarV.setBackgroundColor(getResources().getColor(R.color.orange));
                        showFragment(SettingsFragment.class);
                        break;
                    case R.id.statistics_rb:
                        titleTv.setText(R.string.statistics_title);
                        actionBarV.setBackgroundColor(getResources().getColor(R.color.orange));
                        showFragment(StatisticsFragment.class);
                        break;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        nullRb.setChecked(true);

        if(id == R.id.action_ib) {
            startActivity(intent);
        }

        try {
            if (id == R.id.statistics_fimaly_sports) {
                actionIb.setVisibility(View.VISIBLE);
                titleTv.setText(R.string.family_sports_title);
                actionBarV.setBackgroundColor(Color.YELLOW);
                showFragment(FamilySportsFragment.class);
                intent = new Intent(this, FamilySportsStatisticsActivity.class);
            } else if (id == R.id.statistics_fimaly_hrate) {
                actionIb.setVisibility(View.VISIBLE);
                titleTv.setText(R.string.heart_rate_title);
                actionBarV.setBackgroundColor(Color.RED);
                showFragment(FamilyHRateFragment.class);
                intent = new Intent(this, FamilyHRateStatisticsActivity.class);

            } else if (id == R.id.statistics_fimaly_sleep) {
                actionIb.setVisibility(View.VISIBLE);
                titleTv.setText(R.string.sleep_title);
                actionBarV.setBackgroundColor(Color.BLUE);
                showFragment(FamilySleepFragment.class);
                intent = new Intent(this, FamilySleepStatisticsActivity.class);
            } else if (id == R.id.nav_back_ib) {
                drawer.openDrawer(GravityCompat.START);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFragment(Class fragment) throws IllegalAccessException ,InstantiationException{
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fg= fragmentManager.findFragmentByTag(fragment.getSimpleName());
        if(fg == null)
            fg = (Fragment) fragment.newInstance();
        transaction.replace(R.id.fragment_fl, fg, fragment.getSimpleName()).commit();
    }

}
