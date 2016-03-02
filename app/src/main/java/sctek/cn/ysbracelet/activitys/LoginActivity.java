package sctek.cn.ysbracelet.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.braceletdata.YsData;
import sctek.cn.ysbracelet.http.HttpConnectionWorker;
import sctek.cn.ysbracelet.http.XmlNodes;
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

    private TextView findPassordTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initElement();
    }

    private void initElement() {

        nameEt = (EditText)findViewById(R.id.username_et);
        passwordEt = (EditText)findViewById(R.id.password_et);

        nameTl = (TextInputLayout)findViewById(R.id.username_tl);
        passwordTl = (TextInputLayout)findViewById(R.id.password_tl);

        findPassordTv = (TextView)findViewById(R.id.find_password_tv);

        findPassordTv.setText(Html.fromHtml(getString(R.string.forget_password_str)));
        findPassordTv.setMovementMethod(LinkMovementMethod.getInstance());

    }

    public void onLoginButtonClicked(View v) {

        String name = nameEt.getText().toString();
        String password = passwordEt.getText().toString();

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

        UserManagerUtils.login(name, password, new HttpConnectionWorker.ConnectionWorkeListener() {
            @Override
            public void onWorkeDone(int resCode) {
                if(resCode == XmlNodes.RESPONSE_CODE_SUCCESS) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                else if(resCode == XmlNodes.RESPONSE_CODE_OTHER) {
                    DialogUtils.makeToast(LoginActivity.this, R.string.login_error_password);
                }
            }

            @Override
            public void onResult(YsData result) {
                if(result instanceof YsUser) {
                    ((YsUser)result).updateUserInfo(LoginActivity.this);
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

    }

    public void onRegisterButtonClicked(View v) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

}
