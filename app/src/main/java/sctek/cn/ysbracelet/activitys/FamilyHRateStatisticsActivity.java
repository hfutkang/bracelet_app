package sctek.cn.ysbracelet.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.FamilyHRateStatisticsLvAdapter;

public class FamilyHRateStatisticsActivity extends AppCompatActivity {

    private ListView recordsLv;
    private TextView emptyTv;

    private View actionBarV;
    private TextView titleTv;
    private ImageButton backIb;
    private ImageButton actionIb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_hrate_statistics);
        initViewElement();
    }

    private void initViewElement() {

        actionBarV = findViewById(R.id.action_bar_rl);
        titleTv = (TextView)findViewById(R.id.title_tv);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);
        actionIb = (ImageButton)findViewById(R.id.action_ib);

        actionBarV.setBackgroundColor(Color.RED);
        titleTv.setText(R.string.family_sleep_title);
        actionIb.setVisibility(View.GONE);

        recordsLv = (ListView)findViewById(R.id.data_lv);
        FamilyHRateStatisticsLvAdapter adapter = new FamilyHRateStatisticsLvAdapter(this, null, 180, 1);
        recordsLv.setAdapter(adapter);
        recordsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FamilyHRateStatisticsLvAdapter.ViewHolder viewHolder = (FamilyHRateStatisticsLvAdapter.ViewHolder)view.getTag();
                if(viewHolder.lineChart.getVisibility() == View.GONE) {
                    viewHolder.lineChart.setVisibility(View.VISIBLE);
                }
                else {
                    viewHolder.lineChart.setVisibility(View.GONE);
                }
            }
        });

        emptyTv = (TextView)findViewById(R.id.empty_tv);
    }
}
