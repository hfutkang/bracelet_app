package sctek.cn.ysbracelet.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.FamilySleepStatisticsMonthVpAdapter;
import sctek.cn.ysbracelet.adapters.FamilySleepStatisticsWeekVpAdapter;

public class FamilySleepStatisticsActivity extends FamilyDataStatisticsBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initDataElement() {
        super.initDataElement();
        statisticsMode = STATISTICS_MODE_WEEK;
        adapter = new FamilySleepStatisticsWeekVpAdapter(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_family_sleep_statistics);
    }

    @Override
    protected PagerAdapter constructAdapter(int mode) {
        if(mode == STATISTICS_MODE_WEEK) {
            return new FamilySleepStatisticsWeekVpAdapter(this);
        }
        else if(mode == STATISTICS_MODE_MONTH) {
            return new FamilySleepStatisticsMonthVpAdapter(this);
        }
        return null;
    }

    protected void initViewElement() {
        super.initViewElement();

        actionBarV.setBackgroundColor(Color.BLUE);
        titleTv.setText(R.string.family_sleep_title);
        actionIb.setVisibility(View.GONE);

        weekIv.setSelected(true);
        monthIv.setSelected(false);

        weekIv.setOnClickListener(onImageViewClickedListener);
        monthIv.setOnClickListener(onImageViewClickedListener);
    }
}
