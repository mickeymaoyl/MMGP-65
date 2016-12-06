package nc.impl.mmgp.bd.refcheck.check;

import java.util.List;

import nc.bs.businessevent.CheckEvent;
import nc.bs.businessevent.IBusinessEvent;
import nc.impl.mmgp.bd.refcheck.CheckRefedUtil;
import nc.impl.mmgp.bd.refcheck.FileRefedInfo;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.stock.MaterialStockVO;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since �������� Sep 22, 2013
 * @author wangweir
 */
public class MarBatchUpdateChecker implements IRefChecker {

    /*
     * (non-Javadoc)
     * @see nc.impl.mmgp.bd.refcheck.check.IRefChecker#check(nc.bs.businessevent.IBusinessEvent, java.util.List,
     * nc.vo.pub.SuperVO, nc.vo.pub.SuperVO)
     */
    @Override
    public void check(IBusinessEvent event,
                      List<FileRefedInfo> refedBaseInfos,
                      SuperVO oldValue,
                      SuperVO newValue) throws BusinessException {
        CheckEvent checkevent = (CheckEvent) event;
        Object[] vos = checkevent.getInputVOs();
        if (MMArrayUtil.isEmpty(vos)) {
            return;
        }

        // ���ϻ�����Ϣ
        if (vos[0] instanceof MaterialVO || vos[0] instanceof MaterialStockVO) {
            // ����ע�����汾��Ϣ, ����ֻ��ע��ǰ�汾������ǰ�汾�ĵ���δ�����ã������汾�����ã����Կ��޸�
            // ��ע�����汾��Ϣ, ����ֻҪ��һ���汾�����ã��õ����Ͳ��ܱ��޸�
            CheckRefedUtil.chkMulVsRefedForBatchUpdBef(event, refedBaseInfos);
        }

    }

}
