package sctek.cn.ysbracelet.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.github.gfranks.fab.menu.FloatingActionButton;
import com.github.gfranks.fab.menu.FloatingActionsMenu;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.FamiliesListViewAdapter;
import sctek.cn.ysbracelet.uiwidget.HorizontalListView;

public class LocationAcitvity extends AppCompatActivity {

    private static final String TAG = LocationAcitvity.class.getSimpleName();

    private View actionBarV;
    private TextView titleTv;
    private ImageButton backIb;
    private ImageButton actionIb;

    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private FloatingActionsMenu mFloatingActionsMenu;
    private FloatingActionButton rtimeLocationFab;
    private FloatingActionButton fstepFab;
    private FloatingActionButton otherFab;
    private FloatingActionButton refreshFab;

    private View datePickerView;
    private ImageButton preDateIb;
    private ImageButton nextDateIb;
    private TextView dateTv;

    private YsDateManager mDateManager;
    private String currentDate;
    private String currentShowDate;

    private View familiesSelectorV;
    private HorizontalListView familiesLv;
    private ImageButton preIb;
    private ImageButton nextIb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mDateManager = new YsDateManager(YsDateManager.DATE_FORMAT_SHOW1);
        currentDate = mDateManager.getCurrentDate();

        initViewElement();
    }

    private void initViewElement() {

        actionBarV = findViewById(R.id.action_bar_rl);
        actionBarV.setBackgroundColor(getResources().getColor(R.color.aqua));
        titleTv = (TextView)findViewById(R.id.title_tv);
        titleTv.setText(R.string.location_title);
        actionIb = (ImageButton)findViewById(R.id.action_ib);
        actionIb.setVisibility(View.GONE);

        backIb = (ImageButton)findViewById(R.id.nav_back_ib);

        mMapView = (MapView)findViewById(R.id.baidu_mv);
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();

        datePickerView = findViewById(R.id.date_picker_rl);
        datePickerView.getBackground().setAlpha(100);
        dateTv = (TextView)findViewById(R.id.date_tv);
        preDateIb = (ImageButton)findViewById(R.id.date_previous_ib);
        nextDateIb = (ImageButton)findViewById(R.id.date_next_ib);
        currentShowDate = mDateManager.showCurrentDate(dateTv);
        datePickerView.setVisibility(View.GONE);

        dateTv.setOnClickListener(onDatePickerButtonClickedListener);
        preDateIb.setOnClickListener(onDatePickerButtonClickedListener);
        nextDateIb.setOnClickListener(onDatePickerButtonClickedListener);

        mFloatingActionsMenu = (FloatingActionsMenu)findViewById(R.id.location_fam);
        rtimeLocationFab = (FloatingActionButton)findViewById(R.id.runtime_fab);
        fstepFab = (FloatingActionButton)findViewById(R.id.trail_fab);
        otherFab = (FloatingActionButton)findViewById(R.id.find_fab);
        refreshFab = (FloatingActionButton)findViewById(R.id.refresh_fab);

        rtimeLocationFab.setOnClickListener(onFlatingButtonClickedListener);
        fstepFab.setOnClickListener(onFlatingButtonClickedListener);
        otherFab.setOnClickListener(onFlatingButtonClickedListener);
        refreshFab.setOnClickListener(onFlatingButtonClickedListener);

        familiesLv = (HorizontalListView)findViewById(R.id.families_hlv);

        familiesLv.setBackgroundColor(Color.TRANSPARENT);
        FamiliesListViewAdapter adapter = new FamiliesListViewAdapter(this, false);
        familiesLv.setAdapter(adapter);
    }

    private View.OnClickListener onDatePickerButtonClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.date_previous_ib:
                    currentShowDate = mDateManager.showPreviousDate(dateTv);
                    mBaiduMap.clear();
                    break;
                case R.id.date_next_ib:
                    if(currentShowDate.equals(currentDate))
                        return;
                    mBaiduMap.clear();
                    currentShowDate = mDateManager.showNextDate(dateTv);
                    break;
                case R.id.date_tv:
                    break;
            }
        }
    };

    private View.OnClickListener onFlatingButtonClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.runtime_fab:
                    titleTv.setText(R.string.location_title);
                    datePickerView.setVisibility(View.GONE);
                    refreshFab.setVisibility(View.VISIBLE);
                    break;
                case R.id.trail_fab:
                    titleTv.setText(R.string.trail_title);
                    refreshFab.setVisibility(View.GONE);
                    datePickerView.setVisibility(View.VISIBLE);
                    break;
                case R.id.find_fab:
                    titleTv.setText(R.string.find_title);
                    datePickerView.setVisibility(View.GONE);
                    refreshFab.setVisibility(View.VISIBLE);
                    break;
                case R.id.refresh_fab:
                    break;
            }
        }
    };
}
