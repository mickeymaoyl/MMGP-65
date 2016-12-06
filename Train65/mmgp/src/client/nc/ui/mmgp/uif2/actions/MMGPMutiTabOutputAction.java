package nc.ui.mmgp.uif2.actions;

import nc.ui.pubapp.uif2app.actions.OutputAction;

/**
 * <b> ���ӱ��ҳǩ��� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since: ��������:Jul 23, 2014
 * @author:liwsh
 */
public class MMGPMutiTabOutputAction extends OutputAction {

    private static final long serialVersionUID = 5794941849834679320L;

    /**
     * ��ӡģ��nodekey���ɹ���
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
