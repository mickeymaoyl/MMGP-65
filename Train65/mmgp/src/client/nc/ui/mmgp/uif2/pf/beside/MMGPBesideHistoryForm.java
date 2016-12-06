package nc.ui.mmgp.uif2.pf.beside;

import javax.swing.SwingWorker;

import nc.ui.pf.workflownote.beside.BesideHistoryForm;
import nc.ui.uif2.components.ITabbedPaneAwareComponent;
import nc.ui.uif2.components.ITabbedPaneAwareComponentListener;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since �������� Oct 11, 2013
 * @author wangweir
 */
public class MMGPBesideHistoryForm extends BesideHistoryForm implements ITabbedPaneAwareComponentListener {

    private static final long serialVersionUID = 1L;

    private ITabbedPaneAwareComponent showUpComonet;

    class FormSwingWorker extends SwingWorker<String, String> {
        private MMGPBesideHistoryForm panel;

        public MMGPBesideHistoryForm getPanel() {
            return panel;
        }

        public void setPanel(MMGPBesideHistoryForm panel) {
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

    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        if (isVisible) {
            onSelectedDateChangedDirect();
        }
    }

    // ģ��ѡ�����ݱ仯����������״̬�仯ʱ������������Ӧ����
    public void onSelectedDateChanged() {
        onSelectedDateChangedDirect();
    }

    // ѡ�е������ݱ仯ʱ������������Ӧ����
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
