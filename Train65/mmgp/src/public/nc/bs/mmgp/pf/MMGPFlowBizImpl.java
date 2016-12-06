package nc.bs.mmgp.pf;

import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.data.access.NCObject;
import nc.md.model.IAttribute;
import nc.md.model.type.IType;
import nc.uap.pf.metadata.FlowBizImpl;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ����װ�������������UFDate���ͣ����Դ˴���������
 * </p>
 * 
 * @since �������� May 28, 2013
 * @author wangweir
 */
public class MMGPFlowBizImpl extends FlowBizImpl {

    /**
     * @param ncobject
     */
    public MMGPFlowBizImpl(NCObject ncobject) {
        super(ncobject);
    }

    @Override
    public void setApproveDate(UFDateTime approveDate) {
        // ����װ�������������UFDate���ͣ����Դ˴���������
        if (this.isApproveDataUFDateType()) {
            UFDate date = null;
            if (null != approveDate) {
                date = approveDate.getDate();
            }
            this.setAttributeValue(IFlowBizItf.ATTRIBUTE_APPROVEDATE, date);
        } else {
            super.setApproveDate(approveDate);
        }
    }

    /**
     * 
     */
    private boolean isApproveDataUFDateType() {
        if (this.getDataMap() == null) {
            return false;
        }
        String strAttr = this.getDataMap().get(IFlowBizItf.ATTRIBUTE_APPROVEDATE);
        if (StringUtil.isEmptyWithTrim(strAttr)) {
            return false;
        }
        IAttribute attr = ncobject.getRelatedBean().getAttributeByPath(strAttr);
        if (attr == null || attr.getDataType() == null) {
            return false;
        }
        return attr.getDataType().getTypeType() == IType.TYPE_UFDate;
    }
}
