package sctek.cn.ysbracelet.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.http.XmlNodes;
import sctek.cn.ysbracelet.thread.HttpConnectionWorker;
import sctek.cn.ysbracelet.user.UserManagerUtils;
import sctek.cn.ysbracelet.utils.DialogUtils;
import sctek.cn.ysbracelet.utils.YsTextUtils;

public class FindPasswordActivity extends AppCompatActivity {

    private final static String TAG = FindPasswordActivity.class.getSimpleName();

    private TextInputLayout nameTl;
    private TextInputLayout passwordTl;
    private TextInputLayout confirmTl;
    private TextInputLayout vCodeTl;

    private EditText userNameEt;
    private EditText passwordEt;
    private EditText confirmEt;
    private EditText vCodeEt;
    private Button getCodeBt;
    private Button changePwBt;

    private final static int REENABLE_GET_VERIFY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_find_password);

        initViewElement();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeMessages(REENABLE_GET_VERIFY_CODE);
    }

    private void initViewElement() {

        nameTl = (TextInputLayout)findViewById(R.id.username_tl);
        passwordTl = (TextInputLayout)findViewById(R.id.password_tl);
        confirmTl = (TextInputLayout)findViewById(R.id.confirm_password_tl);
        vCodeTl = (TextInputLayout)findViewById(R.id.vcode_tl);

        userNameEt = (EditText)findViewById(R.id.username_et);
        passwordEt = (EditText)findViewById(R.id.password_et);
        confirmEt = (EditText)findViewById(R.id.confirm_password_et);
        vCodeEt = (EditText)findViewById(R.id.verify_code_et);
        getCodeBt = (Button)findViewById(R.id.get_verify_code_bt);
        changePwBt = (Button)findViewById(R.id.chang_pw_bt);

        userNameEt.requestFocus();
    }

    public void onGetVerifyCodeButtonClicked(View v) {
        String name = userNameEt.getText().toString();
        if(!YsTextUtils.isPhoneNumber(name)) {
            String msg = getString(R.string.name_error_msg);
            nameTl.setError(msg);
            return;
        }

        UserManagerUtils.getVerifyCode(name, new HttpConnectionWorker.ConnectionWorkListener() {
            @Override
            public void onWorkDone(int resCode) {
                if(resCode == XmlNodes.RESPONSE_CODE_OTHER) {
                    String errorMsg = getResources().getText(R.string.user_not_exist).toString();
                    nameTl.setError(errorMsg);
                    getCodeBt.setText(R.string.get_verify_code);
                    getCodeBt.setEnabled(true);
                    mHandler.removeMessages(REENABLE_GET_VERIFY_CODE);
                }
                else if(resCode == XmlNodes.RESPONSE_CODE_SUCCESS) {
                    getCodeBt.setText(R.string.try_again_in60s);
                }
            }

            @Override
            public void onResult(YsData result) {
                getCodeBt.setText(R.string.try_again_in60s);
            }

            @Override
            public void onError(Exception e) {
            }
        });
        getCodeBt.setEnabled(false);
        mHandler.sendEmptyMessageDelayed(REENABLE_GET_VERIFY_CODE, 60*1000);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REENABLE_GET_VERIFY_CODE:
                    getCodeBt.setText(R.string.get_verify_code);
                    getCodeBt.setEnabled(true);
                    break;
            }
        }
    };

    public void onNewPasswordButtonClicked(View view) {
        String name = userNameEt.getText().toString();
        String password = passwordEt.getText().toString();
        String confirm = confirmEt.getText().toString();
        String vCode = vCodeEt.getText().toString();

        if(!YsTextUtils.isPhoneNumber(name)) {
            String errorMsg = getResources().getText(R.string.name_error_msg).toString();
            nameTl.setError(errorMsg);
            userNameEt.requestFocus();
            return;
        }

        if(!YsTextUtils.isPasswordValid(password)) {
            String errorMsg = getResources().getText(R.string.password_error_msg).toString();
            passwordTl.setError(errorMsg);
            passwordEt.requestFocus();
            return;
        }

        if(!confirm.equals(password)) {
            String errorMsg = getResources().getText(R.string.confirm_pw_error_msg).toString();
            confirmTl.setError(errorMsg);
            confirmTl.requestFocus();
            return;
        }

        if(!YsTextUtils.isValidCode(vCode)) {
            String errorMsg = getResources().getText(R.string.invalid_vcode_msg).toString();
            vCodeTl.setError(errorMsg);
            vCodeEt.requestFocus();
        }

        UserManagerUtils.changePassword(name, password, vCode, new HttpConnectionWorker.ConnectionWorkListener() {
            @Override
            public void onWorkDone(int resCode) {
                if(resCode == XmlNodes.RESPONSE_CODE_SUCCESS) {
                    DialogUtils.makeToast(FindPasswordActivity.this, R.string.change_password_success);
                    onBackPressed();
                }
                else if(resCode == XmlNodes.RESPONSE_CODE_OTHER) {
                    DialogUtils.makeToast(FindPasswordActivity.this, R.string.invalid_vcode_msg);
                    changePwBt.setEnabled(true);
                }
                else if(resCode == XmlNodes.RESPONSE_CODE_FAIL) {
                    DialogUtils.makeToast(FindPasswordActivity.this, R.string.change_password_fail);
                    changePwBt.setEnabled(true);
                }
            }

            @Override
            public void onResult(YsData result) {

            }

            @Override
            public void onError(Exception e) {
                changePwBt.setEnabled(true);
                DialogUtils.makeToast(FindPasswordActivity.this, R.string.change_password_fail);
            }
        });
        changePwBt.setEnabled(false);
    }

}
