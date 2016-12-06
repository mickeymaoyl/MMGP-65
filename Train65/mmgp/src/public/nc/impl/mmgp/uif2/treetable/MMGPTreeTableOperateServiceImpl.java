package nc.impl.mmgp.uif2.treetable;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pub.rule.OrgDisabledCheckRule;
import nc.impl.mmgp.uif2.MMGPCmnOperateService;
import nc.impl.mmgp.uif2.rule.TableBodyBDReferenceCheckRule;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.itf.mmgp.uif2.IMMGPTreeTableOperateService;
import nc.itf.pubapp.pub.smart.IBillMaintainService;
import nc.md.data.access.NCObject;
import nc.md.model.IBean;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMMetaUtils;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.util.bizlock.BizlockDataUtil;

/**
 * @Description: TODO
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-5-15上午11:32:15
 * @author: tangxya
 */
public class MMGPTreeTableOperateServiceImpl extends MMGPCmnOperateService implements IMMGPTreeTableOperateService {

    private String pid = "pk_parent";

    private static final String INNCODE = "innercode";

    private AroundProcesser<IBill> process = new AroundProcesser<IBill>(null);;

    public AroundProcesser<IBill> getProcess() {
        return process;
    }

    public void setProcess(AroundProcesser<IBill> process) {
        this.process = process;
    }

    /**
     * 对数据进行PID更新
     * 
     * @param data
     *        保存过的数据
     * @return 更新过PID数据
     * @throws BusinessException
     */
    public <T> T updateVOWithPid(T data) throws BusinessException {
        AggregatedValueObject aggVO = (AggregatedValueObject) data;

        if (MMArrayUtil.isEmpty(aggVO.getChildrenVO())) {
            return data;
        }
        Map<String, String> inncodeToIdMap = new HashMap<String, String>();
        IBean bean = null;
        for (CircularlyAccessibleValueObject body : aggVO.getChildrenVO()) {

            if (bean == null) {
                bean = MMMetaUtils.getBeanByClassFullName(body.getClass().getName());
                pid = MMMetaUtils.getParentPKFieldName(bean);
            }
            NCObject ncObj = NCObject.newInstance(body);
            String pkFieldName = bean.getTable().getPrimaryKeyName();
            Object pkObj = ncObj.getAttributeValue(bean.getAttributeByName(pkFieldName));
            Object inncode = ncObj.getAttributeValue(bean.getAttributeByName(INNCODE));
            // 一个innercode对应一个VO的id
            inncodeToIdMap.put(inncode.toString(), pkObj.toString());

        }
        for (CircularlyAccessibleValueObject body : aggVO.getChildrenVO()) {
            NCObject ncObj = NCObject.newInstance(body);
            Object parentId = ncObj.getAttributeValue(bean.getAttributeByName(pid));
            Object innerCode = ncObj.getAttributeValue(bean.getAttributeByName(INNCODE));
            // 不是第一级节点
            if (innerCode.toString().indexOf(".") != -1) {
                // 已经有parentId的表体行不再赋值，减少需要更新的表体行
                if (parentId == null) {
                    Object id = inncodeToIdMap.get(this.getParentInncode(innerCode.toString()));
                    body.setAttributeValue(pid, id);
                    body.setStatus(VOStatus.UPDATED);
                }
            }
        }
        return data;
    }

    private String getParentInncode(String inncode) {

        String[] s = inncode.split("\\.");
        return inncode.substring(0, inncode.length() - s[s.length - 1].length() - 1);
    }

    /**
     * 保存的内部方法，此处主要是为了记录业务日志。
     * 
     * @param data
     * @return
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    protected <T> T saveDataInner(T data) throws BusinessException {
        Object savedData = null;

        if (data instanceof IBill) {

            /* May 24, 2013 wangweir 由于前台传递的差异VO此处改为补全前台差异VO再持久化 Begin */
            IBill[] fullBills = null;
            if (this.isNewData(data)) {
                BizlockDataUtil.lockDataByBizlock(data);
                fullBills = (IBill[]) MMArrayUtil.toArray(data);
            } else {
                // 加锁 + 检查ts
                BillTransferTool<IBill> transTool = new BillTransferTool<IBill>((IBill[]) MMArrayUtil.toArray(data));
                // 补全前台VO
                fullBills = transTool.getClientFullInfoBill();
                BizlockDataUtil.lockDataByBizlock((Object[]) fullBills);

            }

            // AroundProcesser<IBill> process = new AroundProcesser<IBill>(null);
            /*
             * // 单据是否存在表体数据的规则 process.addBeforeRule(new CheckNotNullRule());
             */
            // 组织停用校验
            process.addBeforeRule(new OrgDisabledCheckRule(MMGlobalConst.PK_ORG, null));
            // 表体引用校验
            process.addBeforeRule(new TableBodyBDReferenceCheckRule());
            /* 增加前校验环绕 end */
            process.before(fullBills);

            saveValidate(fullBills[0]);
            setDefaultData(fullBills[0]);

            fireBeforeSaveEvent(fullBills[0]);

            if (this.isNewData(fullBills[0])) {
                savedData = saveImpl(fullBills[0]);
                savedData = updateVOWithPid(savedData);
                savedData = NCLocator.getInstance().lookup(IBillMaintainService.class).update((IBill) savedData);
            } else {
                savedData = NCLocator.getInstance().lookup(IBillMaintainService.class).update(fullBills[0]);
                savedData = updateVOWithPid(savedData);
                savedData = NCLocator.getInstance().lookup(IBillMaintainService.class).update((IBill) savedData);
            }
            /* 增加后校验环绕 end */
            process.after(fullBills);

        } else {
            lockData(data);
            checkTs(data);
            saveValidate(data);
            setDefaultData(data);
            fireBeforeSaveEvent(data);
            savedData = saveImpl(data);
        }

        fireAfterSaveEvent(savedData);
        // business log
        writeSaveBusiLog(savedData);

        return (T) savedData;
    }
}
