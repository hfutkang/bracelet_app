package sctek.cn.ysbracelet.http;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kang on 16-1-15.
 */
public class YsHttpConnection {

    private HttpURLConnection mHttpURLConnection;

    private static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5 * 1000; // milliseconds

    private static final int DEFAULT_HTTP_READ_TIMEOUT = 5 * 1000; // milliseconds

    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    public static String IMAGE_CONTENT_TYPE = "multipart/form-data";   //内容类型
    public static String  BOUNDARY =  "sctek";  //边界标识   随机生成

    private byte[] mParamsBytes;

    public YsHttpConnection(String urlString, String method, String params) {

        try {
            mParamsBytes = params == null ? null : params.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String encodeUrl = Uri.encode(urlString, ALLOWED_URI_CHARS);
        URL url;
        try {
            url = new URL(encodeUrl);
        }catch (MalformedURLException e) {
            e.printStackTrace();
            mHttpURLConnection = null;
            return;
        }

        try {
            mHttpURLConnection = (HttpURLConnection) url.openConnection();

            mHttpURLConnection.setDoInput(true);
            if(METHOD_POST.equals(method)) {
                mHttpURLConnection.setDoOutput(true);
                mHttpURLConnection.setRequestMethod(METHOD_POST);
            }

        } catch (IOException e1) {
            e1.printStackTrace();
            mHttpURLConnection = null;
            return;
        }

        mHttpURLConnection.setConnectTimeout(DEFAULT_HTTP_CONNECT_TIMEOUT);
        mHttpURLConnection.setReadTimeout(DEFAULT_HTTP_READ_TIMEOUT);
        mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
        mHttpURLConnection.setRequestProperty("Accept", "text/xml");
        mHttpURLConnection.setRequestProperty("Content-Type", IMAGE_CONTENT_TYPE + "; boundary=" + BOUNDARY);
        mHttpURLConnection.setRequestProperty("Charset", "UTF-8");

        if(mParamsBytes != null) {
            mHttpURLConnection.setRequestProperty("Content-Length", mParamsBytes.length + "");
        }

    }

//    public YsHttpConnection(String urlString, String get) {
//
//        String encodeUrl = Uri.encode(urlString, ALLOWED_URI_CHARS);
//        URL url;
//        try {
//            url = new URL(encodeUrl);
//        }catch (MalformedURLException e) {
//            e.printStackTrace();
//            mHttpURLConnection = null;
//            return;
//        }
//
//        try {
//            mHttpURLConnection = (HttpURLConnection) url.openConnection();
//            mHttpURLConnection.setRequestMethod(get);
//        } catch (IOException e1) {
//            e1.printStackTrace();
//            mHttpURLConnection = null;
//            return;
//        }
//
//        mHttpURLConnection.setConnectTimeout(DEFAULT_HTTP_CONNECT_TIMEOUT);
//        mHttpURLConnection.setReadTimeout(DEFAULT_HTTP_READ_TIMEOUT);
//        mHttpURLConnection.setRequestProperty("Accept", "text/plain");
//        mHttpURLConnection.setRequestProperty("Charset", "UTF-8");
//        mHttpURLConnection.setDoInput(true);
//
//    }

    public InputStream doRequest() throws  IOException {
        try {
            mHttpURLConnection.connect();
            if (mParamsBytes != null) {
                OutputStream outputStream = mHttpURLConnection.getOutputStream();
                outputStream.write(mParamsBytes);
                outputStream.flush();
                outputStream.close();
            }

            if (mHttpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return mHttpURLConnection.getInputStream();
            } else {
                throw new IOException("Connection error " + mHttpURLConnection.getResponseCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
