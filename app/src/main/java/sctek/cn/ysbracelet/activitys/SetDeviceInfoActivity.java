package sctek.cn.ysbracelet.activitys;

import android.accounts.Account;
import android.content.ContentResolver;
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
import sctek.cn.ysbracelet.sqlite.LocalDataContract;
import sctek.cn.ysbracelet.sync.SyncAdapter;
import sctek.cn.ysbracelet.thread.HttpConnectionWorker;
import sctek.cn.ysbracelet.user.UserManagerUtils;
import sctek.cn.ysbracelet.user.YsUser;
import sctek.cn.ysbracelet.utils.DialogUtils;

public class SetDeviceInfoActivity extends AppCompatActivity {

    private final static String TAG = SetDeviceInfoActivity.class.getSimpleName();

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
    private String mac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_set_device_info);
        } catch (Exception e) {
            e.printStackTrace();
        }

        deviceId = getIntent().getStringExtra(HomeFragment.EXTR_DEVICE_ID);
        mac = getIntent().getStringExtra(HomeFragment.EXTR_DEVICE_MAC);

        initElement();

    }

    private void initElement() {

        actionBarV = findViewById(R.id.action_bar_rl);
        titleTv = (TextView)findViewById(R.id.title_tv);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);
        actionIb = (ImageButton)findViewById(R.id.action_ib);

        backIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        titleTv.setText(R.string.label_device_info);
        actionIb.setVisibility(View.GONE);

        nameEt = (EditText)findViewById(R.id.name_et);
        sexSp = (Spinner)findViewById(R.id.sex_sp);
        ageSp = (Spinner)findViewById(R.id.age_sp);
        heightSp = (Spinner)findViewById(R.id.height_sp);
        weightSP = (Spinner)findViewById(R.id.weigth_sp);

    }

    public void onSaveButtonClicked(View v) {

        if(BleUtils.DEBUG) Log.e(TAG, "onSaveButtonClicked");

        final String name = nameEt.getText().toString();
        final String sex = sexSp.getSelectedItem().toString();
        String ageStr = ageSp.getSelectedItem().toString();
        String heightStr = heightSp.getSelectedItem().toString();
        String weightStr = weightSP.getSelectedItem().toString();

        if(!isValidName(name)) {
            nameEt.requestFocus();
            return;
        }

        final int age = getAge(ageStr);
        final int height = getHeight(heightStr);
        final int weight = getWeight(weightStr);

        Log.e(TAG, name + " " + sex + " " + age + " " + height + " " + weight);

        UserManagerUtils.addDevice(YsUser.getInstance().getName(), deviceId, mac, name, sex, age, height, weight
                , new HttpConnectionWorker.ConnectionWorkListener() {
            @Override
            public void onWorkDone(int resCode) {

                if(resCode == XmlNodes.RESPONSE_CODE_FAIL) {
                    DialogUtils.makeToast(SetDeviceInfoActivity.this, R.string.add_device_fail);
                    setResult(HomeFragment.RESULT_CODE_ADD_FAIL);
                }
                else if(resCode == XmlNodes.RESPONSE_CODE_OTHER) {
                    DialogUtils.makeToast(SetDeviceInfoActivity.this, R.string.new_device_exist);
                    setResult(HomeFragment.RESULT_CODE_ADD_FAIL);
                }
                else if(resCode == XmlNodes.RESPONSE_CODE_SUCCESS){

                    DeviceInformation device = new DeviceInformation(deviceId, mac, name);
                    device.sex = sex;
                    device.age = age;
                    device.height = height;
                    device.weight = weight;
                    device.insert(getContentResolver());

                    YsUser.getInstance().addDevice(device);

                    setResult(HomeFragment.RESULT_CODE_ADD_OK);
                    Account account = YsUser.getInstance().getAccount();

                    ContentResolver.setSyncAutomatically(account, LocalDataContract.AUTHORITY, true);
                    ContentResolver.setIsSyncable(account, LocalDataContract.AUTHORITY, 1);

                    Bundle bundle = new Bundle();
                    bundle.putInt(SyncAdapter.SYNC_EXTR_MODE, SyncAdapter.SYNC_TYPE_MANUAL_SINGLE);
                    bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                    bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                    bundle.putString(SyncAdapter.SYNC_EXTR_DEVICE, deviceId);
                    ContentResolver.requestSync(account, LocalDataContract.AUTHORITY, bundle);

                }

                finish();

            }

            @Override
            public void onResult(YsData result) {

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                DialogUtils.makeToast(SetDeviceInfoActivity.this, R.string.add_device_fail);
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
}
