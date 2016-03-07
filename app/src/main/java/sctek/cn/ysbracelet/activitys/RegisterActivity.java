package sctek.cn.ysbracelet.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.Thread.HttpConnectionWorker;
import sctek.cn.ysbracelet.http.XmlNodes;
import sctek.cn.ysbracelet.user.UserManagerUtils;
import sctek.cn.ysbracelet.user.YsUser;
import sctek.cn.ysbracelet.utils.DialogUtils;
import sctek.cn.ysbracelet.utils.YsTextUtils;

public class RegisterActivity extends AppCompatActivity {

    private final static String TAG = RegisterActivity.class.getSimpleName();

    private EditText userNameEt;
    private EditText passwordEt;
    private EditText pwConfirmEt;

    private TextInputLayout userNameTl;
    private TextInputLayout passwordTl;
    private TextInputLayout pwConfirmTl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initElement();
    }

    private void initElement() {
        userNameEt = (EditText)findViewById(R.id.username_et);
        passwordEt = (EditText)findViewById(R.id.password_et);
        pwConfirmEt = (EditText)findViewById(R.id.confirm_password_et);

        userNameTl = (TextInputLayout)findViewById(R.id.username_tl);
        passwordTl = (TextInputLayout)findViewById(R.id.password_tl);
        pwConfirmTl = (TextInputLayout)findViewById(R.id.confirm_password_tl);
    }

    public void onRegisterButtonClicked(View v) {

        String url = null;

        final String name = userNameEt.getText().toString();
        final String password = passwordEt.getText().toString();
        String confirmPw = pwConfirmEt.getText().toString();

        if(!YsTextUtils.isNameValid(name)) {
            String errorMsg = getResources().getText(R.string.name_error_msg).toString();
            userNameTl.setError(errorMsg);
            return;
        }

        if(!YsTextUtils.isPasswordValid(password, confirmPw)) {
            String errorMsg = getResources().getText(R.string.password_error_msg).toString();
            passwordTl.setError(errorMsg);
            return;
        }

        UserManagerUtils.register(name, password, new HttpConnectionWorker.ConnectionWorkListener() {

            @Override
            public void onWorkDone(int resCode) {
                if(resCode == XmlNodes.RESPONSE_CODE_SUCCESS) {
                    YsUser.getInstance().setName(name);
                    YsUser.getInstance().setPassword(password);
                    startActivity(new Intent(RegisterActivity.this, SetUserInfoActivity.class));
                    return;
                }
                if(resCode == XmlNodes.RESPONSE_CODE_OTHER) {
                    String msg = getString(R.string.register_error_exist);
                    userNameTl.setError(msg);
                }
                else if(resCode == XmlNodes.RESPONSE_CODE_FAIL) {
                    DialogUtils.makeToast(RegisterActivity.this, R.string.register_error_fail);
                }

            }

            @Override
            public void onResult(YsData result) {

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

    }
}
