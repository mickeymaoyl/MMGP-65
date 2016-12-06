package nc.ui.mmgp.uif2.actions.pflow;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import nc.bs.uif2.validation.ValidationException;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.uap.rbac.core.dataperm.DataPermissionFacade;
import nc.ui.mmgp.uif2.model.MMGPMainGrandModel;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction;
import nc.ui.pubapp.uif2app.components.grand.CardGrandPanelComposite;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.uif2.UIState;
import nc.ui.uif2.components.ITabbedPaneAwareComponent;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMMapUtil;
import nc.vo.mmgp.util.grand.MMGPGrandClientBillCombinServer;
import nc.vo.mmgp.util.grand.MMGPGrandClientBillToServer;
import nc.vo.mmgp.util.grand.MMGPGrandPseudoColUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.pub.Constructor;
import nc.vo.pubapp.pflow.PFReturnObject;
import nc.vo.uif2.LoginContext;

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
public class MMGPGrandScriptPFlowAction extends ScriptPFlowAction {

    private MMGPMainGrandModel mainGrandModel;

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction#getFlowData()
     */
    @Override
    protected AbstractBill[] getFlowData() {

        AbstractBill[] data = null;
        // 新增(转单新增)
        if (UIState.ADD == this.model.getUiState()) {
            Object value = this.editor.getValue();
            validateValue();
            MMGPGrandPseudoColUtil.setPseudoColInfo(value);
            data = new AbstractBill[]{(AbstractBill) value };

            data = (AbstractBill[]) this.processBefore(this.getRealArray(data));
            this.setFullOldVOs(data);
            this.extractTrantype(data);
            this.setNewVOStatus(data);
        }
        // 编辑
        else if (AppUiState.EDIT == this.model.getAppUiState()) {
            Object value = this.editor.getValue();
            validateValue();
            MMGPGrandPseudoColUtil.setPseudoColInfo(value);
            data = new AbstractBill[]{(AbstractBill) value };

            data = (AbstractBill[]) this.processBefore(this.getRealArray(data));
            this.setFullOldVOs(data);
            this.extractTrantype(data);
            data = this.produceLightVO(data);
        } else {
            Object[] tempData = null;
            // 因为存在没有卡片界面的节点，只有列表下编辑，所以不能强制注入editor
            // if (((ITabbedPaneAwareComponent)editor).isComponentVisible()) {
            if (this.editor != null && ((ITabbedPaneAwareComponent) this.editor).isComponentVisible()) {
                // 如果卡片界面显示，则只能处理卡片显示的单个单据
                tempData = new Object[]{this.mainGrandModel.getSelectedData() };
            } else {
                tempData = this.mainGrandModel.getSelectedOperaDatas();
            }

            // 当只支持单选的情况下会发生tempData为空，直接取当前选择数据
            if (tempData == null || tempData.length == 0) {
                tempData = new Object[]{this.mainGrandModel.getSelectedData() };
            }

            data = (AbstractBill[]) this.processBefore(this.getRealArray(tempData));
            this.setFullOldVOs(data);
            this.extractTrantype(data);
            data = this.produceLightVO(data);
        }
        return data;
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction#checkDataPermission()
     */
    @Override
    protected boolean checkDataPermission() {
        if (StringUtil.isEmptyWithTrim(getOperateCode())
            && StringUtil.isEmptyWithTrim(getMdOperateCode())
            || StringUtil.isEmptyWithTrim(getResourceCode())) return true;
        LoginContext context = getModel().getContext();
        String userId = context.getPk_loginUser();
        String pkgroup = context.getPk_group();
        Object data = getMainGrandModel().getSelectedData();
        boolean hasp = true;
        if (!StringUtil.isEmptyWithTrim(getMdOperateCode())) {
            hasp =
                    DataPermissionFacade.isUserHasPermissionByMetaDataOperation(
                        userId,
                        getResourceCode(),
                        getMdOperateCode(),
                        pkgroup,
                        data);
        } else {
            hasp =
                    DataPermissionFacade.isUserHasPermission(
                        userId,
                        getResourceCode(),
                        getOperateCode(),
                        pkgroup,
                        data);
        }
        return hasp;
    }

    /**
     * @throws ValidationException
     */
    protected void validateValue() {
        try {
            if (this.editor instanceof BillForm && !((BillForm) this.editor).validateValue()) {
                throw new ValidationException();
            } else if (this.editor instanceof CardGrandPanelComposite) {
                this.validateValueForGrand((CardGrandPanelComposite) this.editor);
            }
        } catch (ValidationException e) {
            ExceptionUtils.wrappException(e);
        }
    }

    /**
     * @param editor
     * @throws ValidationException
     */
    protected void validateValueForGrand(CardGrandPanelComposite editor) throws ValidationException {
        BillForm mainBillForm = (BillForm) editor.getMainPanel();
        Map<String, Object> grandBillForms = editor.getMaingrandrelationship().getBodyTabTOGrandCardComposite();
        if (!mainBillForm.validateValue()) {
            throw new ValidationException();
        }

        if (MMMapUtil.isEmpty(grandBillForms)) {
            return;
        }
        for (Object grandBillForm : grandBillForms.values()) {
            if (!((BillForm) grandBillForm).validateValue()) {
                throw new ValidationException();
            }
        }
    }

    protected void extractTrantype(AbstractBill[] data) {
        if (MMArrayUtil.isEmpty(data)) {
            return;
        }
        String ttKey = this.getIFlowBizItfMapKey(data[0], IFlowBizItf.ATTRIBUTE_TRANSTYPE);
        if (StringUtil.isEmptyWithTrim(ttKey)) {
            return;
        }
        String tt = (String) data[0].getParentVO().getAttributeValue(ttKey);
        // 交易类型编码允许录入中文，如果控制也是uap做。目前财务那里预制脚本有问题，
        // 环境稳定后，要去掉中文过滤 liuzwc，郭宝华
        if (StringUtil.isContainChinese(tt)) {
            return;
        }
        this.getFlowContext().setTrantype(tt);
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction#getFullBill(java.lang.Object)
     */
    @Override
    protected Object getFullBill(Object vo) {
        UIState state = this.getModel().getUiState();
        if (UIState.ADD == state) {
            return vo;
        }
        if (UIState.EDIT == state) {
            return this.editor.getValue();
        }
        if (!(vo instanceof AggregatedValueObject)) {
            return vo;
        }
        try {
            String pk = ((AggregatedValueObject) vo).getParentVO().getPrimaryKey();
            List< ? > datas = this.getMainGrandModel().getData();
            for (int i = 0; i < datas.size(); ++i) {
                Object data = datas.get(i);
                if (data instanceof AggregatedValueObject) {
                    String datapk = ((AggregatedValueObject) data).getParentVO().getPrimaryKey();
                    if (datapk.equals(pk)) {
                        return data;
                    }
                }
            }
        } catch (BusinessException e) {
            // 日志异常
            ExceptionUtils.wrappException(e);
        }
        return vo;
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction#processReturnObj(java.lang.Object[])
     */
    @Override
    protected void processReturnObj(Object[] pretObj) throws Exception {
        Object[] retObj = pretObj;
        if (retObj == null || retObj.length == 0) {
            if (PfUtilClient.isSuccess()) {
                this.model.setAppUiState(AppUiState.NOT_EDIT);
            }
            showMsginfo(retObj);
            return;
        }
        if (pretObj instanceof PFReturnObject[]) {
            retObj = ((PFReturnObject) pretObj[0]).getBills();
        }

        if (PfUtilClient.isSuccess() && retObj[0] instanceof AggregatedValueObject) {
            // 新增(转单新增)
            if (UIState.ADD == this.model.getAppUiState().getUiState()) {
                new MMGPGrandClientBillCombinServer<IBill>()
                    .combine((IBill[]) this.getFullOldVOs(), (IBill[]) retObj);
                this.getMainGrandModel().directlyAdd(this.getFullOldVOs()[0]);
                this.model.setAppUiState(AppUiState.NOT_EDIT);
            } else {
                // 有可能在中途取消导致返回的数据长度跟原始的长度不一致，所以要截取
                IBill[] updatedVos = this.getFullOldVOs();
                if (retObj.length < this.getFullOldVOs().length) {
                    updatedVos = (IBill[]) Array.newInstance(retObj[0].getClass(), retObj.length);
                    System.arraycopy(this.getFullOldVOs(), 0, updatedVos, 0, retObj.length);
                }
                if (this.isLightBillUsed()) {
                    new MMGPGrandClientBillCombinServer<IBill>().combine(updatedVos, (IBill[]) retObj);
                    this.getMainGrandModel().directlyUpdate(updatedVos);
                } else {
                    this.getMainGrandModel().directlyUpdate(retObj);
                }
                this.model.setAppUiState(AppUiState.NOT_EDIT);

            }

        }
        showMsginfo(retObj);
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction#produceLightVO(nc.vo.pubapp.pattern.model.entity.bill.
     * AbstractBill[])
     */
    @Override
    protected AbstractBill[] produceLightVO(AbstractBill[] newVO) {
        if (!this.isLightBillUsed()) {
            return newVO;
        }
        MMGPGrandClientBillToServer<IBill> tool = new MMGPGrandClientBillToServer<IBill>();
        AbstractBill[] oldVO = newVO;

        IBill[] lightVOs;
        if (AppUiState.EDIT == this.model.getAppUiState()) {
            oldVO = new AbstractBill[]{(AbstractBill) this.getMainGrandModel().getSelectedData() };
            lightVOs = tool.construct(oldVO, newVO);
        } else {
            lightVOs = tool.construct(oldVO);
        }
        // 差异后补充流程字段
        this.fillInfoAfterLight((AbstractBill[]) lightVOs);
        return (AbstractBill[]) lightVOs;
    }

    protected Object[] getRealArray(Object[] vos) {
        if (null == vos || 0 == vos.length) {
            return vos;
        }
        Object[] nvos = Constructor.declareArray(vos[0].getClass(), vos.length);
        System.arraycopy(vos, 0, nvos, 0, vos.length);
        return nvos;
    }

    /**
     * @return the mainGrandModel
     */
    public MMGPMainGrandModel getMainGrandModel() {
        return mainGrandModel;
    }

    /**
     * @param mainGrandModel
     *        the mainGrandModel to set
     */
    public void setMainGrandModel(MMGPMainGrandModel mainGrandModel) {
        this.mainGrandModel = mainGrandModel;
    }
}
