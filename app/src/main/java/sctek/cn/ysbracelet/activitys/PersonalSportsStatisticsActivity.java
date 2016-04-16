package sctek.cn.ysbracelet.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.PersonalSportsStatisticsMonthAdapter;
import sctek.cn.ysbracelet.adapters.PersonalSportsStatisticsWeekAdapter;

public class PersonalSportsStatisticsActivity extends PersonalDataStatisticsBaseActivity {

    private final static String TAG = PersonalSportsStatisticsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_personal_sports_statistics);
    }

    @Override
    protected PagerAdapter constructAdapter(int mode) {
        if(mode == STATISTICS_MODE_WEEK) {
            return new PersonalSportsStatisticsWeekAdapter(this, deviceId);
        }
        else if(mode == STATISTICS_MODE_MONTH) {
            return new PersonalSportsStatisticsMonthAdapter(this, deviceId);
        }
        return null;
    }

    @Override
    protected void initDataElement() {
        super.initDataElement();
        statisticsMode = STATISTICS_MODE_WEEK;
        dateManager = new YsDateManager(YsDateManager.DATE_FORMAT_SECOND);
        adapter = new PersonalSportsStatisticsWeekAdapter(this, deviceId);
    }

    protected void initViewElement() {
        super.initViewElement();

        actionBarV.setBackgroundColor(Color.YELLOW);
        titleTv.setText(R.string.sports_title);
        actionIb.setVisibility(View.GONE);

        weekIv.setOnClickListener(onImageViewClickedListener);
        monthIv.setOnClickListener(onImageViewClickedListener);
        weekIv.setSelected(true);
        monthIv.setSelected(false);

    }

}
