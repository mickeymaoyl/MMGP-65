package nc.impl.mmgp.smart;

import java.lang.reflect.Array;

import nc.impl.pubapp.pattern.data.vo.VOInsert;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.impl.pubapp.pattern.data.vo.tool.VOConcurrentTool;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pub.smart.BatchSaveAction;
import nc.md.model.MetaDataException;
import nc.md.persist.framework.MDPersistenceService;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ISuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 覆盖batchSave方法，解决底层Bug
 * </p>
 * 
 * @since 创建日期 May 15, 2013
 * @author wangweir
 */
@SuppressWarnings("rawtypes")
public class MMGPBatchSaveAction<E extends ISuperVO> extends BatchSaveAction {

    /**
     * 
     */
    public MMGPBatchSaveAction() {
        super();
    }

    public MMGPBatchSaveAction(IPluginPoint point) {
        super(point);
    }

    @SuppressWarnings("unchecked")
    public BatchOperateVO batchSave(BatchOperateVO batchVO) {
        if (null != batchVO.getDelObjs() && batchVO.getDelObjs().length > 0) {
            E[] delVos = this.convertArrayType(batchVO.getDelObjs());
            delVos = this.getOriginVOs(delVos);
            this.getDeleteProcesser().before(delVos);
            /* Nov 6, 2013 wangweir 档案直接物理删除了,避免无效数据在缓存中 Begin */
            try {
                MDPersistenceService.lookupPersistenceService().deleteBillFromDB(
                    (CircularlyAccessibleValueObject[]) delVos);
            } catch (MetaDataException e) {
                ExceptionUtils.wrappException(e);
            }
            /* Nov 6, 2013 wangweir End */
            this.getDeleteProcesser().after(delVos);
        }
        if (null != batchVO.getUpdObjs() && batchVO.getUpdObjs().length > 0) {
            E[] updVos = this.convertArrayType(batchVO.getUpdObjs());
            E[] originVos = this.getOriginVOs(updVos);
            this.getUpdateProcesser().before(updVos, originVos);
            updVos = new VOUpdate<E>().update(updVos, originVos);
            this.getUpdateProcesser().after(updVos, originVos);
            batchVO.setUpdObjs(updVos);
        }
        if (null != batchVO.getAddObjs() && batchVO.getAddObjs().length > 0) {
            E[] addVos = this.convertArrayType(batchVO.getAddObjs());
            this.getInsertProcesser().before(addVos);
            addVos = new VOInsert<E>().insert(addVos);
            this.getInsertProcesser().after(addVos);
            batchVO.setAddObjs(addVos);
        }
        return batchVO;
    }

    @SuppressWarnings("unchecked")
    protected E[] convertArrayType(Object[] vos) {
        E[] smartVOs = (E[]) Array.newInstance(vos[0].getClass(), vos.length);
        System.arraycopy(vos, 0, smartVOs, 0, vos.length);
        return smartVOs;
    }

    @SuppressWarnings({"unchecked" })
    private E[] getOriginVOs(E[] elements) {
        VOConcurrentTool bo = new VOConcurrentTool();
        bo.lock(elements);

        int size = elements.length;
        String[] ids = new String[size];
        for (int i = 0; i < size; i++) {
            ids[i] = elements[i].getPrimaryKey();
        }
        VOQuery< ? extends ISuperVO> query = new VOQuery(elements[0].getClass());
        E[] originElements = (E[]) query.query(ids);
        bo.checkTS(elements, originElements);

        return originElements;
    }
}
