package sctek.cn.ysbracelet.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by kang on 16-2-25.
 */
public class DialogUtils {

    public static void makeToast(Context context,int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

}
