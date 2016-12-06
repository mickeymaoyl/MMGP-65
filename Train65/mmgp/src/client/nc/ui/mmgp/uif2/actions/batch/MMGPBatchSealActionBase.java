package nc.ui.mmgp.uif2.actions.batch;

import java.awt.event.ActionEvent;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.bs.uif2.BusinessExceptionAdapter;
import nc.bs.uif2.validation.IBatchValidationService;
import nc.bs.uif2.validation.ValidationException;
import nc.ui.pub.beans.UIDialog;
import nc.ui.uif2.CheckDataPermissionUtil;
import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.model.BatchBillTableModel;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

public abstract class MMGPBatchSealActionBase extends NCAction {

    private static final long serialVersionUID = 283994281412077261L;

    private BatchBillTableModel model;

    // private BatchBillTable editor;

    // У�������Ҫ�ڴ˴�ע�䣬��Ҫ��ģ����ע�䣬���򽫿��ܵ������ⷢ����
    private IBatchValidationService validationService = null;

    // ����ʵ������Ȩ����Ҫ
    private String mdOperateCode = null; // Ԫ���ݲ�������

    private String operateCode = null; // ��Դ����������룬��������ע����һ������ע�룬�򲻽�������Ȩ�޿��ơ�

    private String resourceCode = null; // ҵ��ʵ����Դ����

    /**
     * ѡ�������ȫΪͣ��״̬
     */
    protected static final int ALL_DISABLE = 1;

    /**
     * ѡ�������ȫΪ����״̬
     */
    protected static final int ALL_ENABLE = 2;

    /**
     * ѡ������ݰ������ú�ͣ��״̬
     */
    protected static final int ENABLE_DISABLE = 3;

    public MMGPBatchSealActionBase() {
        super();
    }

    @Override
    public void doAction(ActionEvent e) throws Exception {
        checkPermission();

        validate();

        doOperate();
    }

    protected void doOperate() throws Exception {

        if (UIDialog.ID_YES == showConfirmDialog()) {
            Integer[] selectRows;
            Object[] selectDatas = null;
            if (getModel().isInMultiSelectmode()) {
                selectRows = getModel().getSelectedOperaRows();
                selectDatas = getModel().getSelectedOperaDatas();
            } else {
                selectRows = new Integer[]{getModel().getSelectedIndex() };
                selectDatas = new Object[]{getModel().getSelectedData() };
            }

            for (Object obj : selectDatas) {
                setEnableState(obj);
            }
            for (int i = 0; i < selectRows.length; i++) {
                getModel().updateLine(selectRows[i], selectDatas[i]);
            }
            getModel().save();
        }
    }

    protected abstract int showConfirmDialog();

    protected abstract void setEnableState(Object obj) throws Exception;

    protected void validate() {
        validate(this.getCheckData());
    }

    protected void checkPermission() throws Exception {
        if (needCheckPermission()) {
            return;
        }
        Object[] objs = getUnDataPermissionData();
        if (objs != null && objs.length > 0) {
            throw new BusinessException(IShowMsgConstant.getDataPermissionInfo());
        }
    }

    /**
     * @return
     */
    protected boolean needCheckPermission() {
        return (MMStringUtil.isEmptyWithTrim(getOperateCode()) && MMStringUtil.isEmptyWithTrim(getMdOperateCode()))
            || MMStringUtil.isEmptyWithTrim(getResourceCode());
    }

    protected Object[] getUnDataPermissionData() {
        Object obj = getCheckData();
        return CheckDataPermissionUtil.getUnDataPermissionData(
            operateCode,
            mdOperateCode,
            resourceCode,
            model.getContext(),
            obj);
    }

    protected Object getCheckData() {
        return this.getModel().getSelectedOperaDatas();
    }

    public BatchBillTableModel getModel() {
        return model;
    }

    public void setModel(BatchBillTableModel model) {
        this.model = model;
        model.addAppEventListener(this);
    }

    public boolean isCurrentDataEnable() {
        // ���ص�ǰѡ�����ݵ��Ƿ����ñ��
        CircularlyAccessibleValueObject vo = (CircularlyAccessibleValueObject) getModel().getSelectedData();
        return vo.getAttributeValue(IBaseServiceConst.ENABLESTATE_FIELD) == null
            || (IPubEnumConst.ENABLESTATE_ENABLE == (Integer) vo
                .getAttributeValue(IBaseServiceConst.ENABLESTATE_FIELD));
    }

    // public BatchBillTable getEditor() {
    // return editor;
    // }
    //
    // public void setEditor(BatchBillTable editor) {
    // this.editor = editor;
    // }

    public IBatchValidationService getValidationService() {
        return validationService;
    }

    public void setValidationService(IBatchValidationService validationService) {
        this.validationService = validationService;
    }

    /**
     * ����У��
     */
    protected void validate(Object value) {
        if (validationService != null) {
            try {
                validationService.validate(value);
            } catch (ValidationException e) {
                throw new BusinessExceptionAdapter(e);
            }
        }

    }

    public String getMdOperateCode() {
        return mdOperateCode;
    }

    public void setMdOperateCode(String mdOperateCode) {
        this.mdOperateCode = mdOperateCode;
    }

    public String getOperateCode() {
        return operateCode;
    }

    public void setOperateCode(String operateCode) {
        this.operateCode = operateCode;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    /**
     * ��ȡ��ǰѡ�����ݵ�״̬
     * 
     * @return
     */
    public int getSelectedDataStatus() {
        // ���ص�ǰѡ�����ݵ����ñ�ʶ.���ȫΪ���÷���True
        boolean enableFlag = false;
        boolean disableFlag = false;

        // ���ɾ��ʱ�׳��쳣��Index: 0, Size: 0��
        if (!this.isSelectDataLegal()) {
            return MMGPBatchSealActionBase.ENABLE_DISABLE;
        }

        Object[] vos = this.getModel().getSelectedOperaDatas();

        // ���û��ѡ�����ݣ���ʾ
        if (vos == null || vos != null && vos.length == 0) {
            return -1;
        }

        // ��ѡ�������״̬���з���
        for (Object vo : vos) {
            if (this.isEnableValue((CircularlyAccessibleValueObject) vo)) {
                enableFlag = true;
            } else {
                disableFlag = true;
            }

            if (enableFlag && disableFlag) {
                return MMGPBatchSealActionBase.ENABLE_DISABLE;
            }
        }

        if (enableFlag) {
            return MMGPBatchSealActionBase.ALL_ENABLE;
        }

        return MMGPBatchSealActionBase.ALL_DISABLE;
    }

    /**
     * ���ɾ��ʱ�׳��쳣��Index: 0, Size: 0��
     * 
     * @return true if the select data is legal
     */
    private boolean isSelectDataLegal() {
        Integer[] rows = this.getModel().getSelectedOperaRows();
        int rowCount = this.getModel().getRowCount();
        for (Integer row : rows) {
            if (row >= rowCount) {
                return false;
            }
        }
        return true;
    }

    /**
     * �ж�VO�����Ƿ�����
     * 
     * @param vo
     *        VO����
     * @return VO�����Ƿ�����
     */
    protected Boolean isEnableValue(CircularlyAccessibleValueObject vo) {
        Object enableStatus = vo.getAttributeValue(IBaseServiceConst.ENABLESTATE_FIELD);
        return enableStatus != null && ((Integer) enableStatus).equals(IPubEnumConst.ENABLESTATE_ENABLE);
    }
}
