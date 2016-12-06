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
 *               ��ϸ��������
 *               </p>
 * @data:2014-5-15����11:32:15
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
     * �����ݽ���PID����
     * 
     * @param data
     *        �����������
     * @return ���¹�PID����
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
            // һ��innercode��Ӧһ��VO��id
            inncodeToIdMap.put(inncode.toString(), pkObj.toString());

        }
        for (CircularlyAccessibleValueObject body : aggVO.getChildrenVO()) {
            NCObject ncObj = NCObject.newInstance(body);
            Object parentId = ncObj.getAttributeValue(bean.getAttributeByName(pid));
            Object innerCode = ncObj.getAttributeValue(bean.getAttributeByName(INNCODE));
            // ���ǵ�һ���ڵ�
            if (innerCode.toString().indexOf(".") != -1) {
                // �Ѿ���parentId�ı����в��ٸ�ֵ��������Ҫ���µı�����
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
     * ������ڲ��������˴���Ҫ��Ϊ�˼�¼ҵ����־��
     * 
     * @param data
     * @return
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    protected <T> T saveDataInner(T data) throws BusinessException {
        Object savedData = null;

        if (data instanceof IBill) {

            /* May 24, 2013 wangweir ����ǰ̨���ݵĲ���VO�˴���Ϊ��ȫǰ̨����VO�ٳ־û� Begin */
            IBill[] fullBills = null;
            if (this.isNewData(data)) {
                BizlockDataUtil.lockDataByBizlock(data);
                fullBills = (IBill[]) MMArrayUtil.toArray(data);
            } else {
                // ���� + ���ts
                BillTransferTool<IBill> transTool = new BillTransferTool<IBill>((IBill[]) MMArrayUtil.toArray(data));
                // ��ȫǰ̨VO
                fullBills = transTool.getClientFullInfoBill();
                BizlockDataUtil.lockDataByBizlock((Object[]) fullBills);

            }

            // AroundProcesser<IBill> process = new AroundProcesser<IBill>(null);
            /*
             * // �����Ƿ���ڱ������ݵĹ��� process.addBeforeRule(new CheckNotNullRule());
             */
            // ��֯ͣ��У��
            process.addBeforeRule(new OrgDisabledCheckRule(MMGlobalConst.PK_ORG, null));
            // ��������У��
            process.addBeforeRule(new TableBodyBDReferenceCheckRule());
            /* ����ǰУ�黷�� end */
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
            /* ���Ӻ�У�黷�� end */
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
