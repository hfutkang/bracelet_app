package sctek.cn.ysbracelet.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import sctek.cn.ysbracelet.R;

public class AlarmEditorActivity extends AppCompatActivity {

    private final static String TAG = AlarmEditorActivity.class.getSimpleName();

    private View actionBarV;
    private TextView titleTv;
    private ImageButton backIb;
    private ImageButton actionIb;

    private TimePicker mTimePicker;

    private View modeV;
    private TextView modeTv;

    private View nameV;
    private TextView nameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_editor);
        initViewElement();
    }

    private void initViewElement() {
        actionBarV = findViewById(R.id.action_bar_rl);
        titleTv = (TextView)findViewById(R.id.title_tv);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);
        actionIb = (ImageButton)findViewById(R.id.action_ib);

        actionBarV.setBackgroundColor(getResources().getColor(R.color.slateblue));
        titleTv.setText(R.string.alarm_editor_title);
        backIb.setOnClickListener(onViewClickedListener);

        mTimePicker = (TimePicker)findViewById(R.id.alarm_tp);
        mTimePicker.setIs24HourView(true);

        modeV = findViewById(R.id.alarm_mode_rl);
        modeTv = (TextView)findViewById(R.id.alarm_mode_tv);

        nameV = findViewById(R.id.alarm_name_rl);
        nameTv = (TextView)findViewById(R.id.alarm_name_tv);

    }

    private View.OnClickListener onViewClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nav_back_ib:
                    break;
                case R.id.alarm_mode_rl:
                    break;
                case R.id.alarm_name_rl:
                    break;
                case R.id.action_ib:
                    break;
            }
        }
    };
}
