package nc.ui.mmgp.uif2.actions.batchedit;

import java.awt.Container;
import java.util.Map;

import javax.swing.SwingWorker;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIProgressDialog;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 *
 * @since �������� 2013-6-26
 * @author tangxya
 */
public class MMGPBatchUpdateExcutor {
    private Container dlg;

    private Exception exception = null;

    private MMGPBatchUpdateContext batchcontext;

    public MMGPBatchUpdateExcutor(Container dlg,
                                  MMGPBatchUpdateContext batchcontext) {
        this.dlg = dlg;
        this.batchcontext = batchcontext;
    }

    public void doBatchUpdate(final Map<String, Object> attr_valueMap) throws Exception {

        // ִ����ɲ���
        final UIProgressDialog progressDlg =
                new UIProgressDialog(this.dlg, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC001-0000085")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
                    "common",
                    "UC001-0000142")
                /* @res "���ڽ��к�̨����, ���Ե�..." */);

        SwingWorker<Object[], Object> worker = new SwingWorker<Object[], Object>() {
            @Override
            protected void done() {
                progressDlg.closeProgressBar();
                Object[] result = null;
                try {
                    result = get();
                } catch (Exception e) {
                    // �쳣��doInBackground()�в��񣬴˴�ֻ���񲢷��쳣��������ʾ������
                    if (exception == null) {
                        exception = e;
                    }
                }

                if (result != null) {
                    batchcontext.setDatas(result);
                    // ˢ�½���
                    refreshCacheData(result);

                    // ����ˢ�º�Ĵ���
                    IMMGPBatchService service = batchcontext.getBatchUpdateService();
                    service.afterUIRefresh(attr_valueMap, batchcontext);

                    MessageDialog.showHintDlg(dlg, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC001-0000085")/*@res "����"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0012")/*@res "�����ɹ���"*/);
                } else {
                    // ����ˢ�º�Ĵ���
                    IMMGPBatchService service = batchcontext.getBatchUpdateService();
                    service.afterUIRefresh(attr_valueMap, batchcontext);
                }
            }

            @Override
            protected Object[] doInBackground() throws Exception {
                try {
                    IMMGPBatchService service = batchcontext.getBatchUpdateService();
                    Object[] result = service.batchUpdateData(attr_valueMap, batchcontext);

                    return result;
                } catch (Exception ex) {
                    exception = ex;
                    return null;
                }
            }
        };
        worker.execute();

        if (!worker.isDone()) {
            progressDlg.showProgressBar();
        }

        if (exception != null) {
            throw exception;
        }

    }

    protected void refreshCacheData(Object[] result) {
        batchcontext.getModel().directlyUpdate(result);
    }

}