package sctek.cn.ysbracelet.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.fragments.HomeFragment;
import sctek.cn.ysbracelet.http.XmlNodes;
import sctek.cn.ysbracelet.thread.HttpConnectionWorker;
import sctek.cn.ysbracelet.user.UserManagerUtils;
import sctek.cn.ysbracelet.user.YsUser;
import sctek.cn.ysbracelet.utils.DialogUtils;

public class EditorDeviceInfoActivity extends AppCompatActivity {

    private final static String TAG = EditorDeviceInfoActivity.class.getSimpleName();

    private TextView titleTv;
    private View actionBarV;
    private ImageButton backIb;
    private ImageButton actionIb;

    private EditText nameEt;
    private Spinner sexSp;
    private Spinner ageSp;
    private Spinner heightSp;
    private Spinner weightSP;

    private String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_editor_device_info);
        } catch (Exception e) {
            e.printStackTrace();
        }

        deviceId = getIntent().getStringExtra(HomeFragment.EXTR_DEVICE_ID);

        initElement();

    }

    private void initElement() {

        actionBarV = findViewById(R.id.action_bar_rl);
        titleTv = (TextView)findViewById(R.id.title_tv);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);
        actionIb = (ImageButton)findViewById(R.id.action_ib);

        DeviceInformation device = YsUser.getInstance().getDevice(deviceId);

        titleTv.setText(device.name);
        actionIb.setVisibility(View.GONE);

        nameEt = (EditText)findViewById(R.id.name_et);
        sexSp = (Spinner)findViewById(R.id.sex_sp);
        ageSp = (Spinner)findViewById(R.id.age_sp);
        heightSp = (Spinner)findViewById(R.id.height_sp);
        weightSP = (Spinner)findViewById(R.id.weigth_sp);

        nameEt.setText(device.name);

        if(device.sex.equals("Female")) {
            sexSp.setSelection(0);
        }
        else {
            sexSp.setSelection(1);
        }
        ageSp.setSelection(device.age -1);
        heightSp.setSelection(device.height - 75);
        weightSP.setSelection(device.weight - 10);

    }

    public void onSaveButtonClicked(View v) {

        if(BleUtils.DEBUG) Log.e(TAG, "onSaveButtonClicked");

        String name = nameEt.getText().toString();
        String sex = sexSp.getSelectedItem().toString();
        String ageStr = ageSp.getSelectedItem().toString();
        String heightStr = heightSp.getSelectedItem().toString();
        String weightStr = weightSP.getSelectedItem().toString();

        if(!isValidName(name)) {
            nameEt.requestFocus();
            return;
        }

        int age = getAge(ageStr);
        int height = getHeight(heightStr);
        int weight = getWeight(weightStr);

        if(!infoChanged(name, sex, age, height, weight)) {
            onBackPressed();
            return;
        }

        UserManagerUtils.updateDeviceInfo(YsUser.getInstance().getName(), deviceId, name, sex, age, height, weight
                , new HttpConnectionWorker.ConnectionWorkListener() {
            @Override
            public void onWorkDone(int resCode) {

                if(resCode == XmlNodes.RESPONSE_CODE_FAIL) {
                    DialogUtils.makeToast(EditorDeviceInfoActivity.this, R.string.update_deviceinfo_fail);
                }
                onBackPressed();
            }

            @Override
            public void onResult(YsData result) {

                if(result instanceof DeviceInformation) {
                    result.update(getContentResolver());
                    DeviceInformation device = YsUser.getInstance().getDevice(deviceId);
                    DeviceInformation temp = (DeviceInformation)result;
                    device.name = temp.name;
                    device.sex = temp.sex;
                    device.age = temp.age;
                    device.height = temp.height;
                    device.weight = temp.weight;
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                DialogUtils.makeToast(EditorDeviceInfoActivity.this, R.string.update_deviceinfo_fail);
                onBackPressed();
            }
        });

    }

    private int getAge(String str) {
        String temp[] = str.split(" ");
        return Integer.parseInt(temp[0]);
    }

    private int getHeight(String str) {
        return Integer.parseInt(str.replace("cm", ""));
    }

    private int getWeight(String str) {
        return Integer.parseInt(str.replace("kg", ""));
    }

    private boolean isValidName(String name) {
        if(TextUtils.isEmpty(name))
            return false;
        if (name.length() > 20) {
            DialogUtils.makeToast(this, R.string.name_too_long);
            return false;
        }
        return true;
    }

    private boolean infoChanged(String name, String sex, int age ,int height, int weight) {
        Log.e(TAG, name + " " + sex + " " + age + " " + height + " " + weight);
        DeviceInformation device = YsUser.getInstance().getDevice(deviceId);
        Log.e(TAG, device.name + " " + device.sex + " " + device.age + " " + device.height + " " + device.weight);
        if (!device.name.equals(name) || !device.sex.equals(sex) || age != device.age
                || device.height != height || device.weight != weight)
            return true;
        return false;
    }
}
