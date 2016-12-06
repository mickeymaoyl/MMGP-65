package nc.ui.mmgp.uif2.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import nc.bs.uif2.IActionCode;
import nc.funcnode.ui.action.INCAction;
import nc.ui.mmgp.flexgant.view.MMGPGantChart;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.print.PrintCil;
import nc.ui.pub.query.tools.ImageIconAccessor;
import nc.ui.pubapp.uif2app.model.BatchBillTableModel;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.model.HierachicalDataAppModel;
import nc.ui.uif2.CheckDataPermissionUtil;
import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.ActionInitializer;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.trade.checkrule.VOChecker;

/**
 * 
 * <b> ����ͼ��ӡ���� </b>
 * <p>
 *     ��ϸ��������
 * </p>
 * @since: 
 * ��������:2014-11-25
 * @author:liujhh
 */
public class MMGPGantChartPrintAction extends NCAction{
    private static final long serialVersionUID = -325497726307386810L;
    private IDataSplit dataSplit;
    private MMGPGantChart gantchart;
    private boolean preview = true;
    private String actioncode;
    private String actionname;
    protected AbstractUIAppModel model;
    
    // ����ʵ������Ȩ����Ҫ
    private String mdOperateCode = null; // Ԫ���ݲ�������
    private String operateCode = null; // ��Դ����������룬��������ע����һ������ע�룬�򲻽�������Ȩ�޿��ơ�
    private String resourceCode = null; // ҵ��ʵ����Դ����

    public interface IDataSplit {
        Object[] splitData(Object[] datas);
    }

    @Override
    public void doAction(ActionEvent e) throws Exception {  
        // ����ӡʱ�Ƿ�Ϊѵ����
        if(!isPreview() && !checkPrecondition()){
            return;
        }
        checkDataPermission(getDatas());// Ȩ��У��
        getGantchart().print(isPreview());
    }
    
    public boolean checkPrecondition() {
        // �Ƿ�ѵ����
        if (PrintCil.isTraining()){
            MessageDialog.showErrorDlg(model.getContext().getEntranceUI(),null,nc.ui.ml.NCLangRes.getInstance().getStrByID("10100108","UPP10100108-000918")/*@res "����ǰʹ�õ���ѵ����,�޷���ӡ���!"*/);
            return false;
        }      
        return true;
    }

    public IDataSplit getDataSplit() {
        return dataSplit;
    }

    public void setDataSplit(IDataSplit dataSplit) {
        this.dataSplit = dataSplit;
    }

    public String getActioncode() {
        return actioncode;
    }

    public void setActioncode(String actioncode) {
        this.actioncode = actioncode;
    }

    public MMGPGantChart getGantchart() {
        return gantchart;
    }

    public void setGantchart(MMGPGantChart gantchart) {
        this.gantchart = gantchart;
    }

    public boolean isPreview() {
        return preview;
    }

    public String getActionname() {
        return actionname;
    }

    public void setActionname(String actionname) {
        this.actionname = actionname;
    }

    public AbstractUIAppModel getModel() {
        return model;
    }

    public void setModel(AbstractUIAppModel model) {
        this.model = model;
        this.model.addAppEventListener(this);
    }
    
    protected Object[] getUnDataPermissionData(Object obj) {
        return CheckDataPermissionUtil.getUnDataPermissionData(operateCode,
                mdOperateCode, resourceCode, model.getContext(), obj);
    }
    
    /** ����Ȩ��У�� **/
    protected void checkDataPermission(Object obj) throws Exception {
        Object[] objs = getUnDataPermissionData(obj);
        if (objs != null && objs.length > 0) {
            throw new BusinessException(
                    IShowMsgConstant.getDataPermissionInfo());
        }
    }

    public void setPreview(boolean preview) {
        // �����ļ���Ϊ�˰�ťע������ʱ��������ע��actioncode��actionname���ԣ���ע��preview����
        if (preview) {
            ActionInitializer.initializeAction(this, IActionCode.PREVIEW);
            this.putValue(Action.SMALL_ICON,
                    ImageIconAccessor.getIcon("toolbar/icon/preview.gif"));

        } else {
            ActionInitializer.initializeAction(this, IActionCode.PRINT);
            this.putValue(Action.SMALL_ICON,
                    ImageIconAccessor.getIcon("toolbar/icon/print.gif"));
        }
        if (!StringUtil.isEmpty(getActioncode())) {
            this.putValue(INCAction.CODE, getActioncode());
        }
        if (!StringUtil.isEmpty(getActionname())) {
            this.putValue(INCAction.NAME, getActionname());
        }
        this.preview = preview;
    }

    @Override
    protected boolean isActionEnable() {
        return this.model.getUiState() == UIState.NOT_EDIT
                && this.model.getSelectedData() != null;
    }

    @SuppressWarnings("unchecked")
    public Object[] getDatas() {
        Object[] datas = null;
        if (this.getModel() instanceof BillManageModel) {
            datas = ((BillManageModel) this.getModel()).getData().toArray(
                    new Object[0]);
            if (VOChecker.isEmpty(datas)) {
                datas = new Object[] { this.getModel().getSelectedData() };
            }
        } else if (this.getModel() instanceof HierachicalDataAppModel) {
            datas = ((HierachicalDataAppModel) this.getModel()).getAllDatas();
        } else {
            datas = ((BatchBillTableModel) this.getModel()).getRows().toArray(
                    new Object[0]);
        }
        if (this.getDataSplit() != null) {
            datas = this.getDataSplit().splitData(datas);
        }
        return datas;
    }   

    public String getMdOperateCode() {
        return mdOperateCode;
    }

    public String getOperateCode() {
        return operateCode;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setMdOperateCode(String mdOperateCode) {
        this.mdOperateCode = mdOperateCode;
    }

    public void setOperateCode(String operateCode) {
        this.operateCode = operateCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }
}
