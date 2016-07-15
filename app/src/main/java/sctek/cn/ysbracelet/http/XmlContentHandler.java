package sctek.cn.ysbracelet.http;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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

	public XmlContentHandler(XmlParserListener xmlParserListener) {
		mXmlParserListener = xmlParserListener;
		responseCode = -1;
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

		if(localName.equals(XmlNodes.NODE_POSITION)) {
			mYsData = new PositionData();
		}
		else if(localName.equals(XmlNodes.NODE_HEARTRATE)) {
			mYsData = new HeartRateData();
		}
		else if(localName.equals(XmlNodes.NODE_SPORT)) {
			mYsData = new SportsData();
		}
		else if(localName.equals(XmlNodes.NODE_LOGIN)) {
//			mYsData = YsUser.getInstance();
		}
		else if(localName.equals(XmlNodes.NODE_REGISTER)) {
		}
		else if(localName.equals(XmlNodes.NODE_SLEEP)) {
			mYsData = new SleepData();
		}
		else if(localName.equals(XmlNodes.NODE_ADD_DEVICE)) {

		}
		else if(localName.equals(XmlNodes.NODE_DELETE_DEVICE)) {

		}
		else if(localName.equals(XmlNodes.NODE_DEVICE)) {
			mYsData = new DeviceInformation();
		}
		else if(localName.equals(XmlNodes.NODE_USERINFO)) {
			mYsData = YsUser.getInstance();
		}
		else if(localName.equals(XmlNodes.NODE_MSG)) {
			Log.e(TAG, "node message xxxxxxxxxxxxxxxxx");
			mYsData = new Message();
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
				|| localName.equals(XmlNodes.NODE_REGISTER)
				|| localName.equals(XmlNodes.NODE_SLEEP)
				|| localName.equals(XmlNodes.NODE_DEVICE)
				|| localName.equals(XmlNodes.NODE_USERINFO)
				|| localName.equals(XmlNodes.NODE_MSG))
			publishData(mYsData);


	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		String value = new String(ch, start, length);
		Log.e(TAG, "characters:" + value);
		if(value.contains("\n")||value.contains("  ")) {
			return;
		}
		if(mYsData != null)
			mYsData.setField(mNodeName, value);

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
