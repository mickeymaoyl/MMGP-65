package nc.vo.mmgp.util;

import java.util.Vector;

import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.vo.logging.Debug;
import nc.vo.pub.lang.UFDateTime;

/**
 * 计时工具类，用于性能调试 类型说明：一个简单的“秒表”类，精度为毫秒。 开发态输出日志至控制台，生成态输出至日志文件
 */
public class TimeRecorder {

    // 开始时刻，结束时刻
    private long startTime, endTime;

    // nc log
    //
    @SuppressWarnings("rawtypes")
    private Vector vecPhaseName = new Vector();

    @SuppressWarnings("rawtypes")
    private Vector vecPhaseTime = new Vector();

    private String prefix = "";

    /**
     * Timer 构造子注解。
     */
    public TimeRecorder() {
        super();
        start();
    }

    /**
     * 设置显示前缀
     *
     * @param prefix
     *        the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 得到运行时间
     */
    public long getTime() {
        stop();
        return endTime - startTime;
    }

    /**
     * 显示执行时间
     */
    public void showExecuteTime(String sTaskHint) {
        stop();
        showTime(sTaskHint);
        start();
    }

    /**
     * 显示执行时间
     */
    public void showTime(String sTaskHint) {
        long lTime = getTime();
        UFDateTime curTime = new UFDateTime(System.currentTimeMillis());
        String sTmp = new String("Time=" + curTime.getTime() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0081")/*@res ":->执行<"*/ /* -=notranslate=- */
            + sTaskHint + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0082")/*@res ">消耗的时间为："*/ + (lTime / 60000) + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0083")/*@res "分"*/ /* -=notranslate=- */
            + ((lTime / 1000) % 60) + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0084")/*@res "秒"*/ + (lTime % 1000) + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0085")/*@res "毫秒"*/); /* -=notranslate=- */

        outPrint(sTmp);

    }

    /**
     * 输出
     *
     * @param tmp
     */
    private void outPrint(String info) {
        // NCdp201007096 生成库操作时，相关日志不应输出到中间件的屏幕上
        info = prefix + info;
        if (RuntimeEnv.getInstance().isDevelopMode()) {
            Debug.setDebuggable(true);
            Debug.debug(info);
        }
        Logger.info(info);
    }

    /**
     *
     */
    public void start() {
        startTime = System.currentTimeMillis();
    }

    /**
     *
     */
    public void start(String sTaskHint) {
        start();
        outPrint(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0086")/*@res "开始执行"*/ + sTaskHint);
    }

    /**
     */
    public void stop() {
        endTime = System.currentTimeMillis();
    }

    /**
     *
     */
    public void stopAndShow(String sTaskHint) {
        stop();
        showTime(sTaskHint);
    }

    /**
     * 添加中间步骤
     */
    @SuppressWarnings("unchecked")
    public void addExecutePhase(String sTaskHint) {
        stop();
        vecPhaseName.add(sTaskHint);
        vecPhaseTime.add(Long.valueOf(getTime()));
        start();
    }

    /**
     * 显示所有步骤信息
     */
    @SuppressWarnings("rawtypes")
    public void showAllExecutePhase(String sTaskHint) {
        stop();
        int iSize = vecPhaseTime.size();
        if (iSize == 0) {
            return;
        }
        long lAllTime = 0;
        for (int i = 0; i < iSize; i++) {
            lAllTime += ((Long) vecPhaseTime.get(i)).longValue();
        }
        boolean onserver = false;
        if (System.getProperties().containsKey("WWW_HOME")) {
            onserver = true;
            outPrint("\n" + sTaskHint + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0087")/*@res "总时间："*/ + lAllTime);
        } else {
            outPrint("\n" + sTaskHint + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0087")/*@res "总时间："*/ + lAllTime);
        }
        for (int i = 0; i < iSize; i++) {
            if (onserver) {
                outPrint(vecPhaseName.get(i)
                    + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0088")/*@res " 消耗时间："*/
                    + vecPhaseTime.get(i)
                    + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0089")/*@res " 占总时间："*/
                    + new nc.vo.pub.lang.UFDouble((((Long) vecPhaseTime.get(i)).longValue() * 100.)
                        / ((lAllTime) * 1.), 2)
                    + "%");
            } else {
                outPrint(vecPhaseName.get(i)
                    + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0088")/*@res " 消耗时间："*/
                    + vecPhaseTime.get(i)
                    + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0089")/*@res " 占总时间："*/
                    + new nc.vo.pub.lang.UFDouble((((Long) vecPhaseTime.get(i)).longValue() * 100.)
                        / ((lAllTime) * 1.), 2)
                    + "%");
            }
        }
        vecPhaseName = new Vector();
        vecPhaseTime = new Vector();
    }

}