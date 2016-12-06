package nc.ui.mmgp.uif2.actions;

import nc.ui.pubapp.uif2app.actions.OutputAction;

/**
 * <b> 主子表多页签输出 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since: 创建日期:Jul 23, 2014
 * @author:liwsh
 */
public class MMGPMutiTabOutputAction extends OutputAction {

    private static final long serialVersionUID = 5794941849834679320L;

    /**
     * 打印模板nodekey生成工厂
     */
    private MMGPPrintNodeKeyFactory nodekeyFactory;

    @Override
    public String getNodeKey() {

        return nodekeyFactory.getNodeKey();

    }

    public MMGPPrintNodeKeyFactory getNodekeyFactory() {
        return nodekeyFactory;
    }

    public void setNodekeyFactory(MMGPPrintNodeKeyFactory nodekeyFactory) {
        this.nodekeyFactory = nodekeyFactory;
    }

}
