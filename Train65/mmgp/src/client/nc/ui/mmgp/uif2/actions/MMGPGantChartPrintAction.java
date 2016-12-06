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
 * <b> 甘特图打印功能 </b>
 * <p>
 *     详细描述功能
 * </p>
 * @since: 
 * 创建日期:2014-11-25
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
    
    // 以下实现数据权限需要
    private String mdOperateCode = null; // 元数据操作编码
    private String operateCode = null; // 资源对象操作编码，以上两者注入其一，都不注入，则不进行数据权限控制。
    private String resourceCode = null; // 业务实体资源编码

    public interface IDataSplit {
        Object[] splitData(Object[] datas);
    }

    @Override
    public void doAction(ActionEvent e) throws Exception {  
        // 检查打印时是否为训练盘
        if(!isPreview() && !checkPrecondition()){
            return;
        }
        checkDataPermission(getDatas());// 权限校验
        getGantchart().print(isPreview());
    }
    
    public boolean checkPrecondition() {
        // 是否训练盘
        if (PrintCil.isTraining()){
            MessageDialog.showErrorDlg(model.getContext().getEntranceUI(),null,nc.ui.ml.NCLangRes.getInstance().getStrByID("10100108","UPP10100108-000918")/*@res "您当前使用的是训练盘,无法打印输出!"*/);
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
    
    /** 数据权限校验 **/
    protected void checkDataPermission(Object obj) throws Exception {
        Object[] objs = getUnDataPermissionData(obj);
        if (objs != null && objs.length > 0) {
            throw new BusinessException(
                    IShowMsgConstant.getDataPermissionInfo());
        }
    }

    public void setPreview(boolean preview) {
        // 配置文件里为此按钮注入属性时，必须先注入actioncode、actionname属性，再注入preview属性
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
