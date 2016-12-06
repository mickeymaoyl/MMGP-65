package nc.vo.mmgp.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * TimeRecorder的封装，一个线程一个TimeRecorder，采用静态获取方式，更易用
 * </p>
 * 创建日期:2011-5-27
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
     * 获取一个TimeRecorder,一个线程一个TimeRecorder
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
