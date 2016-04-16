package sctek.cn.ysbracelet.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.FamilySportsStatisticsMonthVpAdapter;
import sctek.cn.ysbracelet.adapters.FamilySportsStatisticsWeekVpAdapter;

public class FamilySportsStatisticsActivity extends FamilyDataStatisticsBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initDataElement() {
        super.initDataElement();
        statisticsMode = STATISTICS_MODE_WEEK;
        adapter = new FamilySportsStatisticsWeekVpAdapter(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_family_sports_statistics);
    }

    @Override
    protected PagerAdapter constructAdapter(int mode) {
        if(mode == STATISTICS_MODE_WEEK) {
            return new FamilySportsStatisticsWeekVpAdapter(this);
        }
        else if(mode == STATISTICS_MODE_MONTH) {
            return new FamilySportsStatisticsMonthVpAdapter(this);
        }
        return null;
    }

    protected void initViewElement() {
        super.initViewElement();
        actionBarV.setBackgroundColor(Color.YELLOW);
        titleTv.setText(R.string.family_sports_title);
        actionIb.setVisibility(View.GONE);

        weekIv.setSelected(true);
        monthIv.setSelected(false);

        weekIv.setOnClickListener(onImageViewClickedListener);
        monthIv.setOnClickListener(onImageViewClickedListener);
    }
}
