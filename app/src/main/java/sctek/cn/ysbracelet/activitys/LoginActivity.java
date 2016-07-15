package sctek.cn.ysbracelet.activitys;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.igexin.sdk.PushManager;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.http.XmlNodes;
import sctek.cn.ysbracelet.thread.HttpConnectionWorker;
import sctek.cn.ysbracelet.user.UserManagerUtils;
import sctek.cn.ysbracelet.user.YsUser;
import sctek.cn.ysbracelet.utils.DialogUtils;
import sctek.cn.ysbracelet.utils.YsTextUtils;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText nameEt;
    private EditText passwordEt;

    private TextInputLayout nameTl;
    private TextInputLayout passwordTl;

    private TextView findPasswordTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_login);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initElement();
    }

    private void initElement() {

        nameEt = (EditText)findViewById(R.id.username_et);
        passwordEt = (EditText)findViewById(R.id.password_et);

        nameTl = (TextInputLayout)findViewById(R.id.username_tl);
        passwordTl = (TextInputLayout)findViewById(R.id.password_tl);

        findPasswordTv = (TextView)findViewById(R.id.find_password_tv);

        findPasswordTv.setText(Html.fromHtml(getString(R.string.forget_password_str)));
        findPasswordTv.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG);
        findPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, FindPasswordActivity.class));
            }
        });
//        findPassordTv.setMovementMethod(LinkMovementMethod.getInstance());

    }

    public void onLoginButtonClicked(View v) {

        final String name = nameEt.getText().toString();
        final String password = passwordEt.getText().toString();

        if(!YsTextUtils.isPhoneNumber(name)) {
            String msg = getString(R.string.name_error_msg);
            nameTl.setError(msg);
            return;
        }
        if(!YsTextUtils.isPasswordValid(password)) {
            String msg = getString(R.string.password_error_msg);
            passwordTl.setError(msg);
            return;
        }

        UserManagerUtils.login(name, password, new HttpConnectionWorker.ConnectionWorkListener() {
            @Override
            public void onWorkDone(int resCode) {
                if(resCode == XmlNodes.RESPONSE_CODE_SUCCESS) {

                    YsUser.getInstance().setName(name);
                    YsUser.getInstance().setPassword(password);
                    YsUser.getInstance().insert(getContentResolver());

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    UserManagerUtils.createSyncAccount(getApplicationContext());//登录成功，开始同步服务器数据
                    PushManager.getInstance().initialize(getApplicationContext());
                    boolean result = PushManager.getInstance().bindAlias(getApplicationContext(), name);
                    Log.e(TAG, "bind alias result:" + result);
                    finish();
                }
                else if(resCode == XmlNodes.RESPONSE_CODE_OTHER) {
                    DialogUtils.makeToast(LoginActivity.this, R.string.login_error_password);
                }
                else if(resCode == XmlNodes.RESPONSE_CODE_FAIL) {
                    DialogUtils.makeToast(LoginActivity.this, R.string.login_not_exist);
                }
            }

            @Override
            public void onResult(YsData result) {

                /*
                * 登录成功后返回登录用户监护的设备，
                * 下面将他们插入到本地数据库。
                * */
                result.insert(getContentResolver());

                if(result instanceof DeviceInformation) {
                    YsUser.getInstance().addDevice((DeviceInformation)result);
                }
            }

            @Override
            public void onError(Exception e) {
                DialogUtils.makeToast(LoginActivity.this, R.string.login_fail);
                e.printStackTrace();
            }
        });
    }

    public void onRegisterButtonClicked(View v) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

}
