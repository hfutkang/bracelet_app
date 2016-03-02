package sctek.cn.ysbracelet.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.user.YsUser;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(YsUser.getInstance().isLogined()) {
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                }
                else {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                }
            }
        }, 2000);
    }
}
