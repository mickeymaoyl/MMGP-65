package nc.ui.mmgp.uif2.pf.beside;

import javax.swing.SwingWorker;

import nc.ui.pf.workflownote.beside.BesideHintMessageForm;
import nc.ui.uif2.components.ITabbedPaneAwareComponent;
import nc.ui.uif2.components.ITabbedPaneAwareComponentListener;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Oct 11, 2013
 * @author wangweir
 */
public class MMGPBesideHintMessageForm extends BesideHintMessageForm implements ITabbedPaneAwareComponentListener {

    private static final long serialVersionUID = 1L;

    private ITabbedPaneAwareComponent showUpComonet;

    class FormSwingWorker extends SwingWorker<String, String> {
        private MMGPBesideHintMessageForm panel;

        public MMGPBesideHintMessageForm getPanel() {
            return panel;
        }

        public void setPanel(MMGPBesideHintMessageForm panel) {
            this.panel = panel;
        }

        @Override
        protected String doInBackground() throws Exception {
            panel.onSelectedDateChangedDirect();
            return "success";
        }
    }

    public void onSelectedDateChangedDirect() {
        if ((this.getShowUpComonet() != null && !this.getShowUpComonet().isComponentVisible())) {
            return;
        }
        super.onSelectedDateChanged();
    }

    public void onSelectedDateChanged() {
        onSelectedDateChangedDirect();
    }

    public void onSelectionChanged() {
        onSelectedDateChangedDirect();
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
        this.onSelectedDateChanged();
    }

}
