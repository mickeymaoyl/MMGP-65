package nc.vo.mmgp.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * TimeRecorder�ķ�װ��һ���߳�һ��TimeRecorder�����þ�̬��ȡ��ʽ��������
 * </p>
 * ��������:2011-5-27
 * 
 * @author:wangweir
 */
public class TimeRecordUtil {

    private static ThreadLocal<Map<String, TimeRecorder>> timeRecoder = new ThreadLocal<Map<String, TimeRecorder>>() {
        @Override
        protected Map<String, TimeRecorder> initialValue() {
            return new HashMap<String, TimeRecorder>();
        }
    };

    /**
     * ��ȡһ��TimeRecorder,һ���߳�һ��TimeRecorder
     * 
     * @return TimeRecorder TimeRecorder
     */
    public static TimeRecorder getTimeRecorder(String uniquePrefix) {
        Map<String, TimeRecorder> timeRecorderMap = timeRecoder.get();

        TimeRecorder aTimeRecorder = timeRecorderMap.get(uniquePrefix);
        if (aTimeRecorder == null) {
            aTimeRecorder = new TimeRecorder();
            aTimeRecorder.setPrefix(uniquePrefix);
            timeRecorderMap.put(uniquePrefix, aTimeRecorder);
        }

        return aTimeRecorder;
    }
}
