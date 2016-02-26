package sctek.cn.ysbracelet.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kang on 16-2-23.
 */
public class TextUtils {

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
}
