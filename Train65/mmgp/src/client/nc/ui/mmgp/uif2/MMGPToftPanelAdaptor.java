package nc.ui.mmgp.uif2;

import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.ui.pubapp.uif2app.ToftPanelAdaptorEx;
import nc.vo.logging.Debug;

/**
 * 通用的ClientUI，只是封装了一层，目前没做特殊处理，便于后期扩展
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
     * 打印节点信息，方便调试
     *
     * @return
     */
    private String createInfo() {
        StringBuilder info = new StringBuilder();
        info.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0036")/*@res "打开节点："*/).append(this.getFuncCode());
        info.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0037")/*@res "\t配置文件:"*/).append(this.getBeanConfigFilePath());
        return info.toString();
    }
}