package sctek.cn.ysbracelet.Device;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by kang on 16-2-24.
 */
public class DevicesManager {

    private ArrayList<DeviceInformation> mDevices;

    public DevicesManager() {
        mDevices = new ArrayList<>();
    }

    public boolean isExist(DeviceInformation device) {
        Iterator<DeviceInformation> iterator = mDevices.iterator();
        while (iterator.hasNext()) {
            DeviceInformation temp = iterator.next();
            if(temp.getMac().equals(device.getMac()))
                return true;
        }
        return false;
    }

    public void addDevice(DeviceInformation device) {
        mDevices.add(device);
    }

    public void deleteDevice(DeviceInformation device) {
        Iterator<DeviceInformation> iterator = mDevices.iterator();
        while (iterator.hasNext()) {
            DeviceInformation temp = iterator.next();
            if(temp.getMac().equals(device.getMac()))
                iterator.remove();
        }
    }

    public int getCount() {
        return mDevices.size();
    }

    public DeviceInformation getDevice(int index) {
        return mDevices.get(index);
    }

    public void clear() {
        mDevices.clear();
    }

}
