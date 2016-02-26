package sctek.cn.ysbracelet.http;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    private String mParams;

    public YsHttpConnection(String urlString, String method, String params) {

        mParams = params;

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
            mHttpURLConnection.setRequestMethod(method);
        } catch (IOException e1) {
            e1.printStackTrace();
            mHttpURLConnection = null;
            return;
        }

        mHttpURLConnection.setDoInput(true);
        mHttpURLConnection.setDoOutput(true);

        mHttpURLConnection.setConnectTimeout(DEFAULT_HTTP_CONNECT_TIMEOUT);
        mHttpURLConnection.setReadTimeout(DEFAULT_HTTP_READ_TIMEOUT);
        mHttpURLConnection.setRequestProperty("Accept", "text/plain");
        mHttpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        mHttpURLConnection.setRequestProperty("Charset", "UTF-8");

        if(mParams != null) {
            mHttpURLConnection.setRequestProperty("Content-Length", mParams.getBytes().length + "");
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
            if (mParams != null) {
                OutputStream outputStream = mHttpURLConnection.getOutputStream();
                outputStream.write(mParams.getBytes());
                outputStream.flush();
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
