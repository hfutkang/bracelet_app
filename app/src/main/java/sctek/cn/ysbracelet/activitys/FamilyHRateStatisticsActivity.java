package sctek.cn.ysbracelet.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.FamilyHRateStatisticsDayVpAdapter;

public class FamilyHRateStatisticsActivity extends FamilyDataStatisticsBaseActivity {

    private final static String TAG = FamilyHRateStatisticsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initDataElement() {
        super.initDataElement();
        statisticsMode = STATISTICS_MODE_DAY;
        adapter = new FamilyHRateStatisticsDayVpAdapter(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_family_hrate_statistics);
    }

    @Override
    protected PagerAdapter constructAdapter(int mode) {
        return null;
    }

    protected void initViewElement() {
        super.initViewElement();

        actionBarV.setBackgroundColor(Color.RED);
        titleTv.setText(R.string.family_hrate_title);
        actionIb.setVisibility(View.GONE);

        dayIv.setOnClickListener(onImageViewClickedListener);
        dayIv.setSelected(true);
    }
}
