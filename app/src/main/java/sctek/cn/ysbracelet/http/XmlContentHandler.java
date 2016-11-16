package sctek.cn.ysbracelet.http;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;

import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.devicedata.HeartRateData;
import sctek.cn.ysbracelet.devicedata.Message;
import sctek.cn.ysbracelet.devicedata.PositionData;
import sctek.cn.ysbracelet.devicedata.SleepData;
import sctek.cn.ysbracelet.devicedata.SportsData;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.user.YsUser;

public class XmlContentHandler extends DefaultHandler {
	
	private final static String TAG = "XmlContentHandler";

	private String mNodeName;
	private int mSession = -1;
	private XmlParserListener mXmlParserListener;

	private YsData mYsData;

	private int responseCode;

	private StringBuffer sb;

	private Map<String, YsData> mYsDatas;

	public XmlContentHandler(XmlParserListener xmlParserListener) {
		mXmlParserListener = xmlParserListener;
		responseCode = -1;
		sb = new StringBuffer();

		mYsDatas = new HashMap<>(6);
		mYsDatas.put(XmlNodes.NODE_POSITION, new PositionData());
		mYsDatas.put(XmlNodes.NODE_HEARTRATE, new HeartRateData());
		mYsDatas.put(XmlNodes.NODE_SPORT, new SportsData());
		mYsDatas.put(XmlNodes.NODE_SLEEP, new SleepData());
		mYsDatas.put(XmlNodes.NODE_DEVICE, new DeviceInformation());
		mYsDatas.put(XmlNodes.NODE_MSG, new Message());
	}
	
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		Log.e(TAG, "startElement:" + localName);
		mNodeName = localName;

		YsData temp = mYsDatas.get(mNodeName);
		if(temp != null)
			mYsData = temp;
		if(localName.equals(XmlNodes.NODE_USERINFO)) {
			mYsData = YsUser.getInstance();
		}

	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		Log.e(TAG, "endElement:" + localName);

		if(localName.equals(XmlNodes.NODE_POSITION)
				|| localName.equals(XmlNodes.NODE_HEARTRATE)
				|| localName.equals(XmlNodes.NODE_SPORT)
				|| localName.equals(XmlNodes.NODE_SLEEP)
				|| localName.equals(XmlNodes.NODE_DEVICE)
				|| localName.equals(XmlNodes.NODE_USERINFO)
				|| localName.equals(XmlNodes.NODE_MSG)) {
			publishData(mYsData);
			mYsData.clearField();
			mYsData = null;
		}

		if(mYsData != null) {
			mYsData.setField(mNodeName, sb.toString());
		}

		sb.delete(0, sb.length());

	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		String value = new String(ch, start, length);
		Log.e(TAG, "start:" + start + " length:" + length + " characters:" + value);
		if(value.contains("\n")||value.contains("  ")) {
			return;
		}

		sb.append(value);

		if(mNodeName.equals(XmlNodes.NODE_RES))
			responseCode = Integer.parseInt(value);
	}
	
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		publishResult(responseCode);
		super.endDocument();
	}

	private void publishData(YsData result) {
		Log.e(TAG, "publishData");
		if(mXmlParserListener != null && result != null)
			mXmlParserListener.onGetData(result);
	}

	private void publishResult(int resCode) {
		if(mXmlParserListener != null)
			mXmlParserListener.OnParserEnd(resCode);
	}

	public interface XmlParserListener {

		void onGetData(YsData result);

		void OnParserEnd(int resCode);
	}

}
