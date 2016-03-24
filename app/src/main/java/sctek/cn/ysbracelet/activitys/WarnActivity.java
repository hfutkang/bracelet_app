package sctek.cn.ysbracelet.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.AlarmsLvAdapter;

public class WarnActivity extends AppCompatActivity {

    private final static String TAG = WarnActivity.class.getSimpleName();

    private View clikableV;

    private View actionBarV;
    private TextView titleTv;
    private ImageButton backIb;
    private ImageButton actionIb;

    private Switch callReminderSw;
    private Switch alarmSw;

    private ListView alarmsLv;
    private ImageButton addIb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn);
        clikableV = findViewById(R.id.clickable_v);

        initViewElement();
    }

    private void initViewElement() {

        clikableV = findViewById(R.id.clickable_v);

        actionBarV = findViewById(R.id.action_bar_rl);
        titleTv = (TextView)findViewById(R.id.title_tv);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);
        actionIb = (ImageButton)findViewById(R.id.action_ib);

        actionBarV.setBackgroundColor(getResources().getColor(R.color.slateblue));
        titleTv.setText(R.string.warning_title);
        backIb.setOnClickListener(onViewClickedListener);
        actionIb.setVisibility(View.GONE);

        callReminderSw = (Switch)findViewById(R.id.call_reminder_sw);
        alarmSw = (Switch)findViewById(R.id.alarm_sw);

        callReminderSw.setOnCheckedChangeListener(onCheckedChangeListener);
        alarmSw.setOnCheckedChangeListener(onCheckedChangeListener);

        alarmsLv = (ListView)findViewById(R.id.alarms_lv);
        addIb = (ImageButton)findViewById(R.id.add_alarm_ib);

        addIb.setOnClickListener(onViewClickedListener);
        AlarmsLvAdapter adapter = new AlarmsLvAdapter(this, null);
        alarmsLv.setAdapter(adapter);
        alarmsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(WarnActivity.this, AlarmEditorActivity.class));
            }
        });
    }

    private View.OnClickListener onViewClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nav_back_ib:
                    break;
                case R.id.alarms_rl:
                    break;
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.call_reminder_sw:
                    break;
                case R.id.alarm_sw:
                    break;
            }
        }
    };


}
