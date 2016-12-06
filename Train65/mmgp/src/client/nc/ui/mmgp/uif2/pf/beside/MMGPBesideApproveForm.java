package nc.ui.mmgp.uif2.pf.beside;

import nc.ui.pf.workflownote.beside.BesideApproveForm;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.components.ITabbedPaneAwareComponent;
import nc.ui.uif2.components.ITabbedPaneAwareComponentListener;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 重写fillBesideContext(),解决流量问题
 * </p>
 * 
 * @since 创建日期 Oct 11, 2013
 * @author wangweir
 */
public class MMGPBesideApproveForm extends BesideApproveForm implements ITabbedPaneAwareComponentListener {
    private static final long serialVersionUID = 1L;

    private ITabbedPaneAwareComponent showUpComonet;

    @Override
    public void handleEvent(AppEvent event) {
        super.handleEvent(event);
    }

    public void onSelectedDateChanged() {
        if (this.getShowUpComonet() != null && !this.getShowUpComonet().isComponentVisible()) {
            return;
        }
        super.onSelectedDateChanged();
    }

    /**
     * @return the showUpComonet
     */
    public ITabbedPaneAwareComponent getShowUpComonet() {
        return showUpComonet;
    }

    /**
     * @param showUpComonet
     *        the showUpComonet to set
     */
    public void setShowUpComonet(ITabbedPaneAwareComponent showUpComonet) {
        this.showUpComonet = showUpComonet;
        this.showUpComonet.addTabbedPaneAwareComponentListener(this);
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.components.ITabbedPaneAwareComponentListener#componentHidden()
     */
    @Override
    public void componentHidden() {

    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.components.ITabbedPaneAwareComponentListener#componentShowed()
     */
    @Override
    public void componentShowed() {
        super.onSelectedDateChanged();
    }

}
