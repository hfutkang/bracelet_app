package sctek.cn.ysbracelet.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.http.XmlNodes;
import sctek.cn.ysbracelet.thread.HttpConnectionWorker;
import sctek.cn.ysbracelet.user.UserManagerUtils;
import sctek.cn.ysbracelet.utils.DialogUtils;
import sctek.cn.ysbracelet.utils.YsTextUtils;

public class RegisterActivity extends AppCompatActivity {

    private final static String TAG = RegisterActivity.class.getSimpleName();

    private EditText userNameEt;
    private EditText passwordEt;
    private EditText pwConfirmEt;
    private EditText emlaiEt;

    private TextInputLayout userNameTl;
    private TextInputLayout passwordTl;
    private TextInputLayout pwConfirmTl;
    private TextInputLayout emailTl;

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
        emlaiEt = (EditText)findViewById(R.id.email_et);

        userNameTl = (TextInputLayout)findViewById(R.id.username_tl);
        passwordTl = (TextInputLayout)findViewById(R.id.password_tl);
        pwConfirmTl = (TextInputLayout)findViewById(R.id.confirm_password_tl);
        emailTl = (TextInputLayout)findViewById(R.id.email_tl);

        userNameEt.requestFocus();
    }

    public void onRegisterButtonClicked(View v) {

        final String name = userNameEt.getText().toString();
        final String password = passwordEt.getText().toString();
        String confirmPw = pwConfirmEt.getText().toString();
        String email = emlaiEt.getText().toString();

        if(!YsTextUtils.isPhoneNumber(name)) {
            String errorMsg = getResources().getText(R.string.name_error_msg).toString();
            userNameTl.setError(errorMsg);
            userNameEt.requestFocus();
            return;
        }

        if(!YsTextUtils.isPasswordValid(password)) {
            String errorMsg = getResources().getText(R.string.password_error_msg).toString();
            passwordTl.setError(errorMsg);
            passwordEt.requestFocus();
            return;
        }

        if(!password.equals(confirmPw)) {
            String errorMsg = getResources().getText(R.string.confirm_pw_error_msg).toString();
            pwConfirmTl.setError(errorMsg);
            pwConfirmEt.requestFocus();
            return;
        }

        if(!YsTextUtils.isEmail(email)) {
            String errorMsg = getResources().getText(R.string.email_error_msg).toString();
            emailTl.setError(errorMsg);
            emlaiEt.requestFocus();
            return;
        }

        UserManagerUtils.register(name, password, email, "", new HttpConnectionWorker.ConnectionWorkListener() {

            @Override
            public void onWorkDone(int resCode) {
                if(resCode == XmlNodes.RESPONSE_CODE_SUCCESS) {
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                    return;
                }
                if(resCode == XmlNodes.RESPONSE_CODE_OTHER) {
                    String msg = getString(R.string.register_error_exist);
                    userNameTl.setError(msg);
                    userNameEt.requestFocus();
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
