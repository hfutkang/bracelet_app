package sctek.cn.ysbracelet.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.fragments.HomeFragment;

/**
 * Created by kang on 16-4-6.
 */
public abstract class PersonalLatestDataBaseActivity extends AppCompatActivity {

    private final static String TAG = PersonalLatestDataBaseActivity.class.getSimpleName();

    public final static String PREFERENCE_NAME = "sc.sctek.ysbracelet.preference";

    protected View actionBarV;
    protected TextView titleTv;
    protected ImageButton backIb;
    protected ImageButton actionIb;

    protected TextView timeTv;

    protected String deviceId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView();

        initDataElement();

        initViewElement();

        loadLatestRecord();

    }

    protected void initViewElement(){

        actionBarV = findViewById(R.id.action_bar_rl);
        titleTv = (TextView)findViewById(R.id.title_tv);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);
        actionIb = (ImageButton)findViewById(R.id.action_ib);

        timeTv = (TextView)findViewById(R.id.time_tv);
    }

    private void initDataElement() {
        deviceId = getIntent().getStringExtra(HomeFragment.EXTR_DEVICE_ID);
    }

    protected abstract void setContentView();

    protected abstract void loadLatestRecord();
}
