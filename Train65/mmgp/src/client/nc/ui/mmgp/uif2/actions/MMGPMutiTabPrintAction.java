package nc.ui.mmgp.uif2.actions;

import nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction;

/**
 * <b> ���ӱ��ҳǩ��ӡ </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since: ��������:Jul 23, 2014
 * @author:liwsh
 */
public class MMGPMutiTabPrintAction extends MetaDataBasedPrintAction {

    private static final long serialVersionUID = 8895904750826851917L;

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
