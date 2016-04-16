package sctek.cn.ysbracelet.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.PersonalSleepStatisticsMonthAdapter;
import sctek.cn.ysbracelet.adapters.PersonalSleepStatisticsWeekAdapter;

public class PersonalSleepStatisticsActivity extends PersonalDataStatisticsBaseActivity {

    private final static String TAG = PersonalSleepStatisticsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initDataElement() {
        super.initDataElement();
        statisticsMode = STATISTICS_MODE_WEEK;
        dateManager = new YsDateManager(YsDateManager.DATE_FORMAT_SECOND);
        adapter = new PersonalSleepStatisticsWeekAdapter(this, deviceId);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_personal_sleep_statistics);
    }

    @Override
    protected PagerAdapter constructAdapter(int mode) {
        if(mode == STATISTICS_MODE_WEEK) {
            return new PersonalSleepStatisticsWeekAdapter(this, deviceId);
        }
        else if(mode == STATISTICS_MODE_MONTH) {
            return new PersonalSleepStatisticsMonthAdapter(this, deviceId);
        }
        return null;
    }

    protected void initViewElement() {
        super.initViewElement();

        actionBarV.setBackgroundColor(Color.BLUE);
        titleTv.setText(R.string.sleep_title);
        actionIb.setVisibility(View.GONE);

        weekIv.setOnClickListener(onImageViewClickedListener);
        monthIv.setOnClickListener(onImageViewClickedListener);
        weekIv.setSelected(true);
        monthIv.setSelected(false);

    }

}
