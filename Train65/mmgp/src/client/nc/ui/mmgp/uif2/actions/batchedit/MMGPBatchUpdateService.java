package nc.ui.mmgp.uif2.actions.batchedit;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.itf.mmgp.uif2.IMMGPGrandOperateService;
import nc.itf.mmgp.uif2.IMMGPSmartService;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.transfer.bill.ClientBillToServer;

/**
 * <b> 批改Service,目前只支持管理型单据（主子）单据，单表体类型的暂时不支持 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 2013-7-30
 * @author tangxya
 */
public class MMGPBatchUpdateService implements IMMGPBatchService {
    protected Map<String, Object> attr_valueMap;

    @Override
    public Object[] batchUpdateData(Map<String, Object> attr_valueMap,
                                    MMGPBatchUpdateContext updateContext) throws BusinessException {
        this.attr_valueMap = attr_valueMap;

        Object[] bills = updateContext.getDatas();
        if (MMArrayUtil.isEmpty(bills)) {
            return bills;
        }
        beforeSetNewValue(attr_valueMap);
        // 保存至数据库中，复制原来的vo进行修改
        if (updateContext.isPersistent()) {
            Object[] clonebills = cloneBillVOs(bills);
            bills = clonebills;
        }
        // 设置批改的数据至VO
        setNewValue(bills, updateContext.getChildClass(), updateContext.getupdatePos());
        // 不保存至数据库，直接VO数据
        if (!updateContext.isPersistent()) {
            return bills;
        }

        beforeDBupdate(updateContext.getDatas(), bills);

        // 数据库更新
        Object[] returnvos = batchUpdateVOs(updateContext.getDatas(), bills);
        returnvos = afterDBupdate(bills, returnvos);

        return returnvos;
    }

    protected Object[] afterDBupdate(Object[] bills,
                                     Object[] returnvos) throws BusinessException {
        return returnvos;
    }

    protected void beforeDBupdate(Object[] oldVos,
                                  Object[] bills) throws BusinessException {

    }

    public void afterUIRefresh(Map<String, Object> attr_valueMap,
                               MMGPBatchUpdateContext updateContext) {

    }

    protected Object[] batchUpdateVOs(Object[] oldVos,
                                      Object[] bills) throws BusinessException {
        Object[] returnvos = null;
        if (isBill(bills[0])) {
            IBill[] lightvos = constructligthsVOs(objToBillArray(oldVos), objToBillArray(bills));
            returnvos = getBillOperateService().update(lightvos);
        } else {

            BatchOperateVO batchVO = new BatchOperateVO();
            batchVO.setUpdObjs(objToSuperVOArray(bills));
            BatchOperateVO returnvo = getVOSmartService().batchSave(batchVO);
            returnvos = returnvo.getUpdObjs();
        }
        return returnvos;
    }

    private IBill[] objToBillArray(Object[] objvos) {
        IBill[] bill = new IBill[objvos.length];
        System.arraycopy(objvos, 0, bill, 0, objvos.length);
        return bill;
    }

    private SuperVO[] objToSuperVOArray(Object[] objvos) {
        SuperVO[] bill = new SuperVO[objvos.length];
        System.arraycopy(objvos, 0, bill, 0, objvos.length);
        return bill;
    }

    protected IBill[] constructligthsVOs(IBill[] oldvos,
                                         IBill[] updatevos) {
        ClientBillToServer<IBill> tool = new ClientBillToServer<IBill>();
        IBill[] lightVOs = tool.construct(oldvos, updatevos);
        return lightVOs;
    }

    private Object[] cloneBillVOs(Object[] bills) {
        ArrayList<Object> list = new ArrayList<Object>();
        if (isBill(bills[0])) {
            for (Object obj : bills) {
                IBill bill = (IBill) obj;
                list.add(bill.clone());
            }
        } else {
            for (Object obj : bills) {
                SuperVO vo = (SuperVO) obj;
                list.add(vo.clone());
            }
        }
        return list.toArray(new Object[0]);

    }

    private boolean isBill(Object objvo) {
        return objvo instanceof IBill;
    }

    protected void beforeSetNewValue(Map<String, Object> attr_valueMap) throws BusinessException {

    }

    protected void setNewValue(Object[] oldvos,
                               Class< ? extends ISuperVO> childClass,
                               int pos) {
        Set<String> attrs = this.attr_valueMap.keySet();

        for (String attrName : attrs) {
            for (Object voObj : oldvos) {
                if (pos == MMGPBatchUpdateContext.BATCH_UPDATE_HEAD || pos == MMGPBatchUpdateContext.BATCH_UPDATE_All) {
                    setHeadValue(voObj, attrName, attr_valueMap.get(attrName));
                }
                if (pos == MMGPBatchUpdateContext.BATCH_UPDATE_BODY || pos == MMGPBatchUpdateContext.BATCH_UPDATE_All) {
                    setBodyValue(voObj, attrName, attr_valueMap.get(attrName), childClass);
                }
            }
        }
    }

    protected void setHeadValue(Object voObj,
                                String field,
                                Object value) {
        SuperVO head = null;
        if (isBill(voObj)) {
            IBill bill = (IBill) voObj;
            head = (SuperVO) bill.getParent();
        } else {
            head = (SuperVO) voObj;
        }
        head.setAttributeValue(field, value);
    }

    protected void setBodyValue(Object voObj,
                                String field,
                                Object value,
                                Class< ? extends ISuperVO> childClass) {
        if (isBill(voObj)) {
            IBill bill = (IBill) voObj;
            if (childClass == null) {
                childClass = getUpdateChildrenCalss(bill);
            }
            ISuperVO[] bodys = bill.getChildren(childClass);
            for (ISuperVO body : bodys) {
                body.setAttributeValue(field, value);
            }
        } else {
            SuperVO body = (SuperVO) voObj;
            body.setAttributeValue(field, value);
        }

    }

    protected Class< ? extends ISuperVO> getUpdateChildrenCalss(IBill bill) {
        IVOMeta[] voMeta = bill.getMetaData().getChildren();
        Class< ? extends ISuperVO> childClass = bill.getMetaData().getVOClass(voMeta[0]);
        return childClass;
    }

    protected IMMGPGrandOperateService getBillOperateService() {
        IMMGPGrandOperateService service = NCLocator.getInstance().lookup(IMMGPGrandOperateService.class);
        return service;
    }

    protected IMMGPSmartService getVOSmartService() {
        IMMGPSmartService smatrservice = NCLocator.getInstance().lookup(IMMGPSmartService.class);
        return smatrservice;
    }

}
