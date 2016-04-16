package sctek.cn.ysbracelet.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.PersonalHRateStatisticsDayAdapter;

public class PersonalHRateStatisticsActivity extends PersonalDataStatisticsBaseActivity {

    private final static String TAG = PersonalHRateStatisticsActivity.class.getSimpleName();

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
        setContentView(R.layout.activity_personal_hrate_statistics);
    }

    @Override
    protected PagerAdapter constructAdapter(int mode) {
        return null;
    }

    @Override
    protected void initDataElement() {
        super.initDataElement();
        statisticsMode = STATISTICS_MODE_DAY;
        dateManager = new YsDateManager(YsDateManager.DATE_FORMAT_DAY);
        adapter = new PersonalHRateStatisticsDayAdapter(this, deviceId);
    }

    protected void initViewElement() {
        super.initViewElement();

        actionBarV.setBackgroundColor(Color.RED);
        titleTv.setText(R.string.heart_rate_title);
        actionIb.setVisibility(View.GONE);

        dayIv.setSelected(true);
    }

}
