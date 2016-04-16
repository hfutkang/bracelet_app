package sctek.cn.ysbracelet.thread;

import android.os.Handler;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.http.XmlContentHandler;
import sctek.cn.ysbracelet.http.YsHttpConnection;

/**
 * Created by kang on 16-1-15.
 */
public class HttpConnectionWorker extends Thread implements XmlContentHandler.XmlParserListener {

    private static final String TAG = HttpConnectionWorker.class.getSimpleName();
    private YsHttpConnection mConnection;
    private ConnectionWorkListener mListener;
    private Handler mHandler;

    public HttpConnectionWorker(YsHttpConnection connection) {
        mConnection = connection;
    }

    public HttpConnectionWorker(YsHttpConnection connection, ConnectionWorkListener listener) {
        mConnection = connection;
        mListener = listener;
        mHandler = new Handler();
    }

    @Override
    public void run() {
        super.run();
        if(BleUtils.DEBUG) Log.e(TAG, "run");
        try {
//            InputStream inputStream = mConnection.doRequest();
            InputStream inputStream = new FileInputStream(testXmlPath);
            decodeInputStream(inputStream);
            inputStream.close();
        }catch (final Exception e) {
            e.printStackTrace();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onError(e);
                }
            });
        }
    }

    private void decodeInputStream(InputStream in) throws ParserConfigurationException
            , SAXException, IOException{
        if(BleUtils.DEBUG) Log.e(TAG, "decodeInputStream");

        XMLReader xmlReader= SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        XmlContentHandler xmlContentHandler = new XmlContentHandler(this);
        xmlReader.setContentHandler(xmlContentHandler);
        InputSource is = new InputSource(in);
        xmlReader.parse(is);

    }

    public void setListener(ConnectionWorkListener listener) {
        mListener = listener;
    }

    @Override
    public void onGetData(YsData result) {
        final YsData data = result;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onResult(data);
            }
        });
    }

    @Override
    public void OnParserEnd(final int resCode) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onWorkDone(resCode);
            }
        });
    }

    public interface ConnectionWorkListener {
        public void onWorkDone(int resCode);
        public void onResult(YsData result);
        public void onError(Exception e);
    }

    private String testXmlPath;
    public HttpConnectionWorker setTestXmlFile(String path) {
        testXmlPath = path;
        return this;
    }


}
