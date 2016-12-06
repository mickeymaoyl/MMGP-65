package nc.ui.mmgp.uif2.view;

import nc.ui.mmgp.uif2.view.grand.MMGPMainGrandAssist;
import nc.ui.pubapp.uif2app.components.grand.CardGrandPanelComposite;
import nc.ui.pubapp.uif2app.components.grand.util.MainGrandAssist;
import nc.ui.uif2.editor.IEditor;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 May 22, 2013
 * @author wangweir
 */
@SuppressWarnings("serial")
public class MMGPCardGrandPanelComposite extends CardGrandPanelComposite implements IEditor {

    private MainGrandAssist mainGrandAssist4MMGP;

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.uif2app.components.grand.CardGrandPanelComposite#setValue(java.lang.Object)
     */
    @Override
    public void setValue(Object object) {

        try {
            super.setValue(object);
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.uif2app.components.grand.GrandPanelComposite#getMainGrandAssist()
     */
    @Override
    public MainGrandAssist getMainGrandAssist() {
        /* Sep 27, 2013 wangweir 替换原来的MainGrandAssist，解决孙表数据获取后，对界面的操作影响获取值的Bug Begin */
        // return super.getMainGrandAssist();
        return this.getMainGrandAssist4MMGP();
    }

    /**
     * @return the mainGrandAssist4MMGP
     */
    public MainGrandAssist getMainGrandAssist4MMGP() {
        if (this.mainGrandAssist4MMGP == null) {
            this.setMainGrandAssist4MMGP(new MMGPMainGrandAssist());
        }
        return mainGrandAssist4MMGP;
    }

    /**
     * @param mainGrandAssist4MMGP
     *        the mainGrandAssist4MMGP to set
     */
    public void setMainGrandAssist4MMGP(MainGrandAssist mainGrandAssist4MMGP) {
        this.mainGrandAssist4MMGP = mainGrandAssist4MMGP;
    }
}
