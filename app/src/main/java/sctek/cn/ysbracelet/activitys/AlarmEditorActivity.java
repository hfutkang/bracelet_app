package sctek.cn.ysbracelet.activitys;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.devicedata.YsTimer;

public class AlarmEditorActivity extends AppCompatActivity {

    private final static String TAG = AlarmEditorActivity.class.getSimpleName();

    private View actionBarV;
    private TextView titleTv;
    private ImageButton backIb;
    private ImageButton actionIb;

    private TimePicker mTimePicker;

    private View modeV;
    private TextView modeTv;
    private int modeIndex;

    private View nameV;
    private TextView nameTv;

    private YsTimer mTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_editor);
        initViewElement();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initViewElement() {

        Intent intent = getIntent();

        modeIndex = intent.getIntExtra("mode", 0);

        actionBarV = findViewById(R.id.action_bar_rl);
        titleTv = (TextView)findViewById(R.id.title_tv);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);
        actionIb = (ImageButton)findViewById(R.id.action_ib);

        actionBarV.setBackgroundColor(getResources().getColor(R.color.slateblue));
        titleTv.setText(R.string.alarm_editor_title);
        backIb.setOnClickListener(onViewClickedListener);
        actionIb.setOnClickListener(onViewClickedListener);

        mTimePicker = (TimePicker)findViewById(R.id.alarm_tp);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setCurrentHour(intent.getIntExtra("hour", 12));
        mTimePicker.setCurrentMinute(intent.getIntExtra("min", 0));

        modeV = findViewById(R.id.alarm_mode_rl);
        modeTv = (TextView)findViewById(R.id.alarm_mode_tv);
        String mode = getResources()
                .getStringArray(R.array.timer_mode)[modeIndex];
        modeTv.setText(mode);

        nameV = findViewById(R.id.alarm_name_rl);
        nameTv = (TextView)findViewById(R.id.alarm_label_tv);
        String res = intent.getStringExtra("des");
        Log.e(TAG, res + " xxxxxx");
        if(res == null)
            res = "";
        nameTv.setText(res);

        modeV.setOnClickListener(onViewClickedListener);
        nameV.setOnClickListener(onViewClickedListener);

    }

    private View.OnClickListener onViewClickedListener = new View.OnClickListener() {
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nav_back_ib:
                    onBackPressed();
                    break;
                case R.id.alarm_mode_rl:
                    showModePickeDialog();
                    break;
                case R.id.alarm_name_rl:
                    showNameEditorDialog();
                    break;
                case R.id.action_ib:
                    Intent intent = new Intent();
                    intent.putExtra("hour", mTimePicker.getCurrentHour());
                    intent.putExtra("min", mTimePicker.getCurrentMinute());
                    intent.putExtra("mode", modeIndex);
                    intent.putExtra("des", nameTv.getText().toString());
                    setResult(RESULT_OK, intent);
                    onBackPressed();
                    break;
            }
        }
    };

    private void showModePickeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.shock_mode);
        builder.setSingleChoiceItems(R.array.timer_mode, modeIndex,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        modeIndex = which;
                    }
                });
        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mode = getResources()
                        .getStringArray(R.array.timer_mode)[modeIndex];
                modeTv.setText(mode);
            }
        });
        builder.show();
    }

    private void showNameEditorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.timer_name_editor_view, null);
        final EditText nameEt = (EditText)view.findViewById(R.id.name_et);
        nameEt.setText(nameTv.getText().toString());
        builder.setView(view);
        builder.setTitle(R.string.timer_name);
        builder.setNeutralButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEt.getText().toString();
                nameTv.setText(name);
            }
        });
        builder.show();
    }
}
