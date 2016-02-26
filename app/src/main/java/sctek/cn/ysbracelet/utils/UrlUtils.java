package sctek.cn.ysbracelet.utils;

/**
 * Created by kang on 16-2-23.
 */
public class UrlUtils {

    public static final String URL_BASE = "http://192.168.5.253:9090/YouSong/";

    public static String compositeRegisterUrl(String userName, String password) {

        return URL_BASE
                + "cmd=register"
                + "&ts=" + getCurrentMillis()
                + "&uname=" + userName
                + "&pw=" + password;
    }

    public static String compositeLoginUrl(String userName, String password) {

        return URL_BASE
                + "cmd=login"
                + "&ts=" + getCurrentMillis()
                + "&uname=" + userName
                + "&pw=" + password;
    }

    public static String compositeUpdateUserInfoUrl(String name, String sex, int age, int height, int weight){
        return URL_BASE
                + "cmd=updateInfo"
                + "&ts=" + getCurrentMillis()
                + "&uname=" + name
                + "&sex=" + sex
                + "&age=" + age
                + "&height=" +height
                + "&weight=" + weight;
    }

    public static String compositeGetUserInfoUrl(String name) {
        return URL_BASE
                + "cmd=getUserInfo"
                + "&ts=" + getCurrentMillis()
                + "&uname=" + name;
    }

    public static String compositeAddDeviceUrl(String uName, String id, String dName){
        return URL_BASE
                + "cmd=addDevice"
                + "&ts=" + getCurrentMillis()
                + "&uname=" + uName
                + "&id=" + id
                + "&dname=" + dName;
    }

    public static String compositeDeleteDeviceUrl(String uName, String id) {
        return URL_BASE
                + "cmd=delDevice"
                + "&ts=" + getCurrentMillis()
                + "&id=" + id;
    }

    public static String compositeGetDevicesUrl(String uName) {
        return URL_BASE
                + "cmd=getDevices"
                + "&uname=" + uName;
    }

    public static String getCurrentMillis() {return "" + System.currentTimeMillis();}
}
