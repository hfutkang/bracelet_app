package sctek.cn.ysbracelet.http;

import android.os.Handler;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.braceletdata.YsData;

/**
 * Created by kang on 16-1-15.
 */
public class HttpConnectionWorker extends Thread implements XmlContentHandler.XmlParserListener {

    private static final String TAG = HttpConnectionWorker.class.getSimpleName();
    private YsHttpConnection mConnection;
    private ConnectionWorkeListener mListener;
    private Handler mHandler;

    public HttpConnectionWorker(YsHttpConnection connection) {
        mConnection = connection;
    }

    public HttpConnectionWorker(YsHttpConnection connection, ConnectionWorkeListener listener) {
        mConnection = connection;
        mListener = listener;
        mHandler = new Handler();
    }

    @Override
    public void run() {
        super.run();
        if(BleUtils.DEBUG) Log.e(TAG, "run");
        try {
            InputStream inputStream = mConnection.doRequest();
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

    public void setListener(ConnectionWorkeListener listener) {
        mListener = listener;
    }

    @Override
    public void onGetData(final YsData result) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onResult(result);
            }
        });
    }

    @Override
    public void OnParserEnd(final int resCode) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onWorkeDone(resCode);
            }
        });
    }

    public interface ConnectionWorkeListener {
        public void onWorkeDone(int resCode);
        public void onResult(YsData result);
        public void onError(Exception e);
    }


}
