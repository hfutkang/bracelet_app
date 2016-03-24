package sctek.cn.ysbracelet.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.FamiliesListViewAdapter;
import sctek.cn.ysbracelet.uiwidget.HorizontalListView;
import sctek.cn.ysbracelet.uiwidget.RadarScanView;
import sctek.cn.ysbracelet.uiwidget.RandomTextView;

public class FenceActivity extends AppCompatActivity {

    private final static String TAG = AlarmEditorActivity.class.getSimpleName();

    private View actionBarV;
    private TextView titleTv;
    private ImageButton backIb;
    private ImageButton actionIb;

    private RadarScanView fenceRsv;
    private RandomTextView memberRtv;

    private HorizontalListView familiesLv;

    private Switch fenceSw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fence);
        initViewElement();
    }

    private void initViewElement() {

        actionBarV = findViewById(R.id.action_bar_rl);
        titleTv = (TextView)findViewById(R.id.title_tv);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);
        actionIb = (ImageButton)findViewById(R.id.action_ib);

        actionBarV.setBackgroundColor(getResources().getColor(R.color.chartreuse));
        titleTv.setText(R.string.fence_title);
        backIb.setOnClickListener(onViewClickedListener);
        actionIb.setVisibility(View.GONE);

        fenceSw = (Switch)findViewById(R.id.fence_sw);
        fenceSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    fenceRsv.startScan();
                else
                    fenceRsv.stopScan();
            }
        });

        fenceRsv = (RadarScanView)findViewById(R.id.fence_rsv);
        memberRtv = (RandomTextView)findViewById(R.id.member_rtv);
        fenceRsv.stopScan();

        familiesLv = (HorizontalListView)findViewById(R.id.families_hlv);
        FamiliesListViewAdapter adapter = new FamiliesListViewAdapter(this, false);
        familiesLv.setAdapter(adapter);

    }

    private View.OnClickListener onViewClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nav_back_ib:
                    break;
            }
        }
    };
}
