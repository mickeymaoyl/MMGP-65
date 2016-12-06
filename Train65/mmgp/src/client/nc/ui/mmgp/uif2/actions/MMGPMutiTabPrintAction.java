package nc.ui.mmgp.uif2.actions;

import nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction;

/**
 * <b> 主子表多页签打印 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since: 创建日期:Jul 23, 2014
 * @author:liwsh
 */
public class MMGPMutiTabPrintAction extends MetaDataBasedPrintAction {

    private static final long serialVersionUID = 8895904750826851917L;

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
