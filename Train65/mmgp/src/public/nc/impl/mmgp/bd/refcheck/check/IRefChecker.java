package nc.impl.mmgp.bd.refcheck.check;

import java.util.List;

import nc.bs.businessevent.IBusinessEvent;
import nc.impl.mmgp.bd.refcheck.FileRefedInfo;
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
public interface IRefChecker {

    public void check(IBusinessEvent event,
                      List<FileRefedInfo> refedBaseInfos,
                      SuperVO oldValue,
                      SuperVO newValue) throws BusinessException;
}
