package nc.vo.mmgp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.pf.IPfRetCheckInfo;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

/**
 * <b> 单据状态过滤工具类 </b>
 * <p>
 * 通过单据状态过滤单据
 * 
 * @since 创建日期 Jun 28, 2013
 * @author dongyshd
 */
public class MMGPBillStatusFilterUtil {

    /**
     * 过滤审批通过的单据
     * 
     * @param vos
     * @return
     */
    public static <E extends AbstractBill> List<E> filterApprovePassingBills(E[] vos) {
        return filterApproveBillsByType(vos, IPfRetCheckInfo.PASSING);
    }

    /**
     * 过滤审批不通过的单据
     * 
     * @param vos
     * @return
     */
    public static <E extends AbstractBill> List<E> filterApproveNopassBills(E[] vos) {
        return filterApproveBillsByType(vos, IPfRetCheckInfo.NOPASS);
    }

    /**
     * 过滤自由态的单据
     * 
     * @param vos
     * @return
     */
    public static <E extends AbstractBill> List<E> filterApproveNostateBills(E[] vos) {
        return filterApproveBillsByType(vos, IPfRetCheckInfo.NOSTATE);
    }

    /**
     * 过滤进行中的单据
     * 
     * @param vos
     * @return
     */
    public static <E extends AbstractBill> List<E> filterApproveGoingonBills(E[] vos) {
        return filterApproveBillsByType(vos, IPfRetCheckInfo.GOINGON);
    }

    /**
     * 过滤提交的单据
     * 
     * @param vos
     * @return
     */
    public static <E extends AbstractBill> List<E> filterApproveCommitBills(E[] vos) {
        return filterApproveBillsByType(vos, IPfRetCheckInfo.COMMIT);
    }

    /**
     * 根据审批状态获得单据
     * 
     * @param vos
     * @param status
     * @return
     */
    public static <E extends AbstractBill> List<E> filterApproveBillsByType(E[] vos,
                                                                            int status) {
        if (MMArrayUtil.isEmpty(vos)) {
            return null;
        }
        List<E> retList = new ArrayList<E>();
        for (E vo : vos) {
            String billStatusFlagColumn = getBillStatusFlagColumn(vo.getParent());
            if (MMStringUtil.isEmpty(billStatusFlagColumn)) {
                continue;
            }

            Object statusFlag = vo.getParent().getAttributeValue(billStatusFlagColumn);
            if (statusFlag == null) {
                continue;
            }
            if ((Integer) statusFlag == status) {
                retList.add(vo);
            }
        }
        return retList;
    }

    /**
     * 获取单据状态字段
     * 
     * @param parent
     *        单据表头
     * @return 单据状态字段名，如果未映射，返回MMGlobalConst.FSTATUSFLAG
     */
    private static String getBillStatusFlagColumn(ISuperVO parent) {
        Map<String, String> statusFlageMap = null;
        String entityName = parent.getMetaData().getEntityName();
        IBusinessEntity entity = null;
        try {
            entity = MDBaseQueryFacade.getInstance().getBusinessEntityByFullName(entityName);
            if (null != entity) {
                statusFlageMap = entity.getBizInterfaceMapInfo(IFlowBizItf.class.getName());
            }
        } catch (MetaDataException e) {
            ExceptionUtils.wrappException(e);
        }

        if (MMMapUtil.isEmpty(statusFlageMap)) {
            return MMGlobalConst.FSTATUSFLAG;
        }
        String statusFlag = statusFlageMap.get(IFlowBizItf.ATTRIBUTE_APPROVESTATUS);
        return statusFlag == null ? MMGlobalConst.FSTATUSFLAG : statusFlag;
    }
}
