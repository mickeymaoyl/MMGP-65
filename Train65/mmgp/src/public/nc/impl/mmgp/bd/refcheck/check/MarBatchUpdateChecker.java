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
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Sep 22, 2013
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

        // 物料基本信息
        if (vos[0] instanceof MaterialVO || vos[0] instanceof MaterialStockVO) {
            // 不关注其他版本信息, 即：只关注当前版本，若当前版本的档案未被引用，其他版本被引用，则仍可修改
            // 关注其他版本信息, 即：只要有一个版本被引用，该档案就不能被修改
            CheckRefedUtil.chkMulVsRefedForBatchUpdBef(event, refedBaseInfos);
        }

    }

}
