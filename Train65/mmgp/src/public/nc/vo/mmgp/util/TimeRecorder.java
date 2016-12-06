package nc.vo.mmgp.util;

import java.util.Vector;

import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.vo.logging.Debug;
import nc.vo.pub.lang.UFDateTime;

/**
 * ��ʱ�����࣬�������ܵ��� ����˵����һ���򵥵ġ�����࣬����Ϊ���롣 ����̬�����־������̨������̬�������־�ļ�
 */
public class TimeRecorder {

    // ��ʼʱ�̣�����ʱ��
    private long startTime, endTime;

    // nc log
    //
    @SuppressWarnings("rawtypes")
    private Vector vecPhaseName = new Vector();

    @SuppressWarnings("rawtypes")
    private Vector vecPhaseTime = new Vector();

    private String prefix = "";

    /**
     * Timer ������ע�⡣
     */
    public TimeRecorder() {
        super();
        start();
    }

    /**
     * ������ʾǰ׺
     *
     * @param prefix
     *        the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * �õ�����ʱ��
     */
    public long getTime() {
        stop();
        return endTime - startTime;
    }

    /**
     * ��ʾִ��ʱ��
     */
    public void showExecuteTime(String sTaskHint) {
        stop();
        showTime(sTaskHint);
        start();
    }

    /**
     * ��ʾִ��ʱ��
     */
    public void showTime(String sTaskHint) {
        long lTime = getTime();
        UFDateTime curTime = new UFDateTime(System.currentTimeMillis());
        String sTmp = new String("Time=" + curTime.getTime() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0081")/*@res ":->ִ��<"*/ /* -=notranslate=- */
            + sTaskHint + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0082")/*@res ">���ĵ�ʱ��Ϊ��"*/ + (lTime / 60000) + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0083")/*@res "��"*/ /* -=notranslate=- */
            + ((lTime / 1000) % 60) + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0084")/*@res "��"*/ + (lTime % 1000) + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0085")/*@res "����"*/); /* -=notranslate=- */

        outPrint(sTmp);

    }

    /**
     * ���
     *
     * @param tmp
     */
    private void outPrint(String info) {
        // NCdp201007096 ���ɿ����ʱ�������־��Ӧ������м������Ļ��
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
        outPrint(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0086")/*@res "��ʼִ��"*/ + sTaskHint);
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
     * ����м䲽��
     */
    @SuppressWarnings("unchecked")
    public void addExecutePhase(String sTaskHint) {
        stop();
        vecPhaseName.add(sTaskHint);
        vecPhaseTime.add(Long.valueOf(getTime()));
        start();
    }

    /**
     * ��ʾ���в�����Ϣ
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
            outPrint("\n" + sTaskHint + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0087")/*@res "��ʱ�䣺"*/ + lAllTime);
        } else {
            outPrint("\n" + sTaskHint + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0087")/*@res "��ʱ�䣺"*/ + lAllTime);
        }
        for (int i = 0; i < iSize; i++) {
            if (onserver) {
                outPrint(vecPhaseName.get(i)
                    + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0088")/*@res " ����ʱ�䣺"*/
                    + vecPhaseTime.get(i)
                    + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0089")/*@res " ռ��ʱ�䣺"*/
                    + new nc.vo.pub.lang.UFDouble((((Long) vecPhaseTime.get(i)).longValue() * 100.)
                        / ((lAllTime) * 1.), 2)
                    + "%");
            } else {
                outPrint(vecPhaseName.get(i)
                    + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0088")/*@res " ����ʱ�䣺"*/
                    + vecPhaseTime.get(i)
                    + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0089")/*@res " ռ��ʱ�䣺"*/
                    + new nc.vo.pub.lang.UFDouble((((Long) vecPhaseTime.get(i)).longValue() * 100.)
                        / ((lAllTime) * 1.), 2)
                    + "%");
            }
        }
        vecPhaseName = new Vector();
        vecPhaseTime = new Vector();
    }

}