package sctek.cn.ysbracelet.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.Thread.HttpConnectionWorker;
import sctek.cn.ysbracelet.http.XmlNodes;
import sctek.cn.ysbracelet.user.UserManagerUtils;
import sctek.cn.ysbracelet.user.YsUser;
import sctek.cn.ysbracelet.utils.DialogUtils;

public class SetUserInfoActivity extends AppCompatActivity {

    private final static String TAG = SetUserInfoActivity.class.getSimpleName();

    private Spinner sexSp;
    private Spinner ageSp;
    private Spinner heightSp;
    private Spinner weightSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_set_userinfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initElement();

    }

    private void initElement() {

        sexSp = (Spinner)findViewById(R.id.sex_sp);
        ageSp = (Spinner)findViewById(R.id.age_sp);
        heightSp = (Spinner)findViewById(R.id.height_sp);
        weightSP = (Spinner)findViewById(R.id.weigth_sp);

    }

    public void onSaveButtonClicked(View v) {

        if(BleUtils.DEBUG) Log.e(TAG, "onSaveButtonClicked");

        String sex = sexSp.getSelectedItem().toString();
        String ageStr = ageSp.getSelectedItem().toString();
        String heightStr = heightSp.getSelectedItem().toString();
        String weightStr = weightSP.getSelectedItem().toString();

        int age = getAge(ageStr);
        int height = getHeight(heightStr);
        int weight = getWeight(weightStr);

        UserManagerUtils.updateUserInfo(YsUser.getInstance().getName(), sex, age, height, weight
                , new HttpConnectionWorker.ConnectionWorkListener() {
            @Override
            public void onWorkDone(int resCode) {

                if(resCode == XmlNodes.RESPONSE_CODE_FAIL) {
                    DialogUtils.makeToast(SetUserInfoActivity.this, R.string.update_userinfo_fail);
                }

                startActivity(new Intent(SetUserInfoActivity.this, MainActivity.class));
                finish();

            }

            @Override
            public void onResult(YsData result) {

                if(result instanceof YsUser) {
                    ((YsUser)result).updateUserInfo(SetUserInfoActivity.this);
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                DialogUtils.makeToast(SetUserInfoActivity.this, R.string.update_userinfo_fail);
                startActivity(new Intent(SetUserInfoActivity.this, MainActivity.class));
                finish();
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
}
