package sctek.cn.ysbracelet.devicedata;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * Created by kang on 16-2-22.
 */
public interface YsData {
    void setField(String field, String value);
    Uri insert(ContentResolver cr);
    int update(ContentResolver cr);
    int delete(ContentResolver cr);
}
