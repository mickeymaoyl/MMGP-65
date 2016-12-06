package nc.ui.mmgp.uif2;

import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.ui.pubapp.uif2app.ToftPanelAdaptorEx;
import nc.vo.logging.Debug;

/**
 * ͨ�õ�ClientUI��ֻ�Ƿ�װ��һ�㣬Ŀǰû�����⴦�����ں�����չ
 */
public class MMGPToftPanelAdaptor extends ToftPanelAdaptorEx {
    /**
	 *
	 */
    private static final long serialVersionUID = 2416711038537902520L;

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.uif2app.ToftPanelAdaptorEx#createFactory()
     */
    @Override
    protected void createFactory() {
        String info = this.createInfo();
        if (RuntimeEnv.getInstance().isDevelopMode()) {
            Debug.setDebuggable(true);
            Debug.debug(info);
        }
        Logger.info(info);
        super.createFactory();
    }

    /**
     * ��ӡ�ڵ���Ϣ���������
     *
     * @return
     */
    private String createInfo() {
        StringBuilder info = new StringBuilder();
        info.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0036")/*@res "�򿪽ڵ㣺"*/).append(this.getFuncCode());
        info.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0037")/*@res "\t�����ļ�:"*/).append(this.getBeanConfigFilePath());
        return info.toString();
    }
}