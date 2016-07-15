package sctek.cn.ysbracelet.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by kang on 16-2-23.
 */
public class UrlUtils {

    public static final String TAG = "UrlUtils";

    public static final String DEBUG_URL_PREFIX = "http://192.168.5.253:81/";
    public static final String SC_URL_PREFIX = "http://www.sctek.cn:9090/";

    public static final String URL_BASE = DEBUG_URL_PREFIX + "app.php?";

    public static final String LOGIN_BASE = DEBUG_URL_PREFIX + "login.php?";

    public static final String REGISTER_BASE = DEBUG_URL_PREFIX + "register.php?";

    public static final String UPLOAD_IMAGE_URL = DEBUG_URL_PREFIX + "uploadimage.php";

    public static final String IMAGE_PATH_BASE = DEBUG_URL_PREFIX + "images/";

    public static String  BOUNDARY =  "sctek";  //边界标识   随机生成
    public static String PREFIX = "--" , LINE_END = System.getProperty("line.separator");

    public static String compositeRegisterUrl(String phoneNum, String password, String email, String vCode) {

        return REGISTER_BASE
                + "uname=" + phoneNum
                + "&pw=" + password
                + "&email=" + email
                + "&code=" + vCode;
    }

    public static String compositeLoginUrl(String userName, String password) {

        return LOGIN_BASE
                + "uname=" + userName
                + "&pw=" + password;
    }

    public static String compositeUpdateUserInfoUrl(String name, String sex, int age, int height, int weight){
        return URL_BASE
                + "cmd=updateInfo"
                + "&uname=" + name
                + "&sex=" + sex
                + "&age=" + age
                + "&height=" +height
                + "&weight=" + weight;
    }

    public static String compositeGetUserInfoUrl(String name) {
        return URL_BASE
                + "cmd=getUserInfo"
                + "&uname=" + name;
    }

    public static String compositeAddDeviceUrl(String uName, String id, String mac, String dName, String sex
                            , int age, int height, int weight){
        return URL_BASE
                + "cmd=addDevice"
                + "&uname=" + uName
                + "&id=" + id
                + "&mac=" + mac
                + "&name=" + dName
                + "&sex=" + sex
                + "&age=" + age
                + "&height=" + height
                + "&weight=" + weight;
    }

    public static String compositeUpdateDeviceUrl(String uName, String id, String dName, String sex
            , int age, int height, int weight){
        return URL_BASE
                + "cmd=updateDevice"
                + "&uname=" + uName
                + "&id=" + id
                + "&name=" + dName
                + "&sex=" + sex
                + "&age=" + age
                + "&height=" + height
                + "&weight=" + weight;
    }

    public static String compositeDeleteDeviceUrl(String uName, String id) {
        return URL_BASE
                + "cmd=deleteDevice"
                + "&uname=" + uName
                + "&id=" + id;
    }

    public static String compositeGetDevicesUrl(String uName) {
        return URL_BASE
                + "cmd=getDevices"
                + "&uname=" + uName;
    }

    public static String compositeGetLatestLocationForUserUrl(String userName) {
        return URL_BASE
                + "cmd=getPositionForUser"
                + "&uname=" + userName;
    }

    public static String compositeTurnRuntimeLocationOnUrl(String deviceId) {
        return URL_BASE
                + "cmd=locationOn"
                + "&ts=" + getCurrentMillis()
                + "&uname=" + deviceId;
    }

    public static String compositeGetLatestLocationForDeviceUrl(String deviceId) {
        return URL_BASE
                + "cmd=getPositionForDevice"
                + "&ts=" + getCurrentMillis()
                + "&user=" + deviceId;
    }

    public static String compositeGetFootStepsUrl(String deviceId, String start, String end) {
        return URL_BASE
                + "cmd=getPosition"
                + "&id=" + deviceId
                + "&start=" + start
                + "&end=" + end;
    }

    public static String compositeGetHRateRecordsUrl(String deviceId, String start, String end) {
        return URL_BASE
                + "cmd=getHR"
                + "&id=" + deviceId
                + "&start=" + start
                + "&end=" + end;
    }

    public static String compositeGetSportsRecordsUrl(String deviceId, String start, String end) {
        return URL_BASE
                + "cmd=getSport"
                + "&id=" + deviceId
                + "&start=" + start
                + "&end=" + end;
    }

    public static String compositeUserSyncUrl(String userName, String startTime) {
        return URL_BASE
                + "cmd=syncAll"
                + "&uname=" + userName
                + "&start=" + startTime;
    }

    public static String compositeUserSyncUrl(String deviceId) {
        return URL_BASE
                + "cmd=syncSingle"
                + "&id=" + deviceId;
    }

    public static String compositeMeasureHRateUrl(String sn) {
        return URL_BASE
                + "cmd=measureHRate"
                + "&id=" + sn;
    }

    public static String compositeGetVerifyCodeUrl(String name) {
        return URL_BASE
                + "cmd=getVerifyCode"
                + "&uname=" + name;
    }

    public static String compositeChangePasswordUrl(String name, String pw, String code) {
        return URL_BASE
                + "cmd=changePassword"
                + "&uname=" + name
                + "&pw=" + pw
                + "&code=" + code;
    }

    public static String compositeUploadImageParams(String uname, String deviceId, Bitmap bitmap) {

        StringBuffer sb = new StringBuffer(1000);

        Log.e(TAG, uname + " " + deviceId);
        sb.append(PREFIX + BOUNDARY + LINE_END);
        sb.append("Content-Disposition: form-data; name=\"uname\"" + LINE_END + LINE_END);
        sb.append(uname + LINE_END);


        sb.append(PREFIX + BOUNDARY + LINE_END);
        sb.append("Content-Disposition: form-data; name=\"deviceId\"" + LINE_END + LINE_END);
        sb.append(deviceId + LINE_END);

        sb.append(PREFIX + BOUNDARY + LINE_END);

        sb.append("Content-Disposition: form-data; name=\"image\"; filename=\"imageCropTemp.png\"" + LINE_END);
        sb.append("Content-Type: image/png;" + LINE_END + LINE_END);

        try {
            String filePath = Environment.getExternalStorageDirectory().getPath() + "/imageCropTemp.png";
            FileInputStream fis = new FileInputStream(new File(filePath));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int n = 0;
            while ((n = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, n);
            }

//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            sb.append(new String(bos.toByteArray(), "ISO-8859-1"));
            fis.close();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sb.append(LINE_END);
        sb.append(PREFIX + BOUNDARY + PREFIX + LINE_END);
        return sb.toString();
    }

    public static String getCurrentMillis() {return "" + System.currentTimeMillis();}
}
