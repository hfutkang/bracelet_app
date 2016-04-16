package sctek.cn.ysbracelet.utils;

import android.content.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sctek.cn.ysbracelet.R;

/**
 * Created by kang on 16-2-23.
 */
public class YsTextUtils {

    public static double METRIC_RUNNING_FACTOR = 1.02784823;
    public static double METRIC_WALKING_FACTOR = 0.708;
    public static double HEIGHT_STEPLENGHT_RATIO = 0.45;

    public static boolean isNameValid(String name) {
        if(name == null || name.equals("") || name.length() > 20)
            return  false;
        return  true;
    }

    public static boolean isEmail(String email) {
        if (null==email) return false;
//        Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isPasswordValid(String password, String confirm) {
        if(password == null || password.length() < 6 || password.length() > 20
                || !password.equals(confirm)) return false;
        return true;
    }

    public static boolean isPasswordValid(String password) {
        if(password == null || password.length() < 6 || password.length() > 20)
            return false;
        return true;
    }

    public static boolean isPhoneNumber(String str) {

        if(str == null) return false;

        Pattern patter = Pattern.compile("0?(13|14|15|18)[0-9]{9}");
        Matcher matcher = patter.matcher(str);
        return matcher.matches();
    }

    public static double calculateCalories(int run, int walk, int weight, int height) {
        double stepLength = height*HEIGHT_STEPLENGHT_RATIO/100000;//单位是千米

        return run*stepLength*weight*METRIC_RUNNING_FACTOR
                + walk*stepLength*weight*METRIC_WALKING_FACTOR;
    }

    public static String paserHourForMinute(Context context, int mins) {
        int h = mins/60;
        int m = mins%60;

        String hour = (h != 0)? h + context.getResources().getString(R.string.hour)
                :"";
        String minute = (m != 0)?m + context.getResources().getString(R.string.minute)
                :"";

        return hour + minute;
    }
}
