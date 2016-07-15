package sctek.cn.ysbracelet.activitys;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.fragments.HomeFragment;

public abstract class PersonalDataStatisticsBaseActivity extends AppCompatActivity {

    private final static String TAG = PersonalDataStatisticsBaseActivity.class.getSimpleName();

    protected final static int STATISTICS_MODE_DAY = 1;
    protected final static int STATISTICS_MODE_WEEK = 2;
    protected final static int STATISTICS_MODE_MONTH = 3;

    protected int statisticsMode;

    protected ViewPager mViewPager;

    protected YsDateManager dateManager;

    protected PagerAdapter adapter;

    protected String deviceId;

    protected View actionBarV;
    protected TextView titleTv;
    protected ImageButton backIb;
    protected ImageButton actionIb;

    protected ImageView dayIv;
    protected ImageView weekIv;
    protected ImageView monthIv;

    protected TextView timeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView();
        initDataElement();
        initViewElement();
    }

    protected abstract void setContentView();

    protected abstract PagerAdapter constructAdapter(int mode);

    protected void initDataElement() {
        deviceId = getIntent().getStringExtra(HomeFragment.EXTR_DEVICE_ID);
    }

    protected void initViewElement() {

        actionBarV = findViewById(R.id.action_bar_rl);
        titleTv = (TextView)findViewById(R.id.title_tv);
        actionIb = (ImageButton)findViewById(R.id.action_ib);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);
        timeTv = (TextView)findViewById(R.id.time_tv);

        backIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mViewPager = (ViewPager)findViewById(R.id.chart_vp);
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                timeTv.setText(getTimeStr(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        dayIv = (ImageView)findViewById(R.id.day_iv);
        weekIv = (ImageView)findViewById(R.id.week_iv);
        monthIv = (ImageView)findViewById(R.id.month_iv);

        timeTv.setText(getTimeStr(0));

    }

    protected String getTimeStr(int position) {
        if(statisticsMode == STATISTICS_MODE_DAY) {
            return dateManager.getDayDateBy(-position);
        }
        else if(statisticsMode == STATISTICS_MODE_WEEK) {
            String start = dateManager.getFirstDayOfWeekBy(-position);
            String end = dateManager.getLastDayOfWeekBy(-position);
            return start + "/" + end;
        }
        else if(statisticsMode == STATISTICS_MODE_MONTH) {
           return dateManager.getMonthBy(-position);
        }
        return "";
    }

    protected View.OnClickListener onImageViewClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.week_iv:
                    if(statisticsMode != STATISTICS_MODE_WEEK) {
                        statisticsMode = STATISTICS_MODE_WEEK;
                        adapter = constructAdapter(statisticsMode);
                        mViewPager.setAdapter(adapter);
                        weekIv.setSelected(true);
                        monthIv.setSelected(false);
                        timeTv.setText(getTimeStr(0));
                    }
                    break;
                case R.id.month_iv:
                    if(statisticsMode != STATISTICS_MODE_MONTH) {
                        statisticsMode = STATISTICS_MODE_MONTH;
                        adapter = constructAdapter(statisticsMode);
                        mViewPager.setAdapter(adapter);
                        monthIv.setSelected(true);
                        weekIv.setSelected(false);
                        timeTv.setText(getTimeStr(0));
                    }
                    break;
            }
        }
    };

}
