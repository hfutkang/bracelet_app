package sctek.cn.ysbracelet.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.PersonalHistoryHRateLvAdapter;

public class PersonalHistoryHRateActivity extends AppCompatActivity {

    private final static String TAG = PersonalHistoryHRateActivity.class.getSimpleName();

    private ListView recordsLv;
    private TextView emptyTv;

    private View actionBarV;
    private TextView titleTv;
    private ImageButton backIb;
    private ImageButton actionIb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_history_hrate);

        initViewElement();
    }

    private void initViewElement() {

        actionBarV = findViewById(R.id.action_bar_rl);
        titleTv = (TextView)findViewById(R.id.title_tv);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);
        actionIb = (ImageButton)findViewById(R.id.action_ib);

        actionBarV.setBackgroundColor(Color.RED);
        titleTv.setText(R.string.heart_rate_title);
        actionIb.setVisibility(View.GONE);

        recordsLv = (ListView)findViewById(R.id.history_hrate_lv);
        PersonalHistoryHRateLvAdapter adapter = new PersonalHistoryHRateLvAdapter(this, null);
        recordsLv.setAdapter(adapter);

        emptyTv = (TextView)findViewById(R.id.empty_tv);
        emptyTv.setVisibility(View.GONE);
    }
}
