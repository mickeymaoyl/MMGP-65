package nc.impl.mmgp.bd.refcheck.check;

import java.util.List;
import java.util.Set;

import nc.bs.businessevent.IBusinessEvent;
import nc.impl.mmgp.bd.refcheck.CheckRefedUtil;
import nc.impl.mmgp.bd.refcheck.FileRefedInfo;
import nc.vo.bd.material.MaterialVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.model.tool.VOTool;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Sep 22, 2013
 * @author wangweir
 */
public class MaterialVOAddOrUpdateRefChecker implements IRefChecker {

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
        String checkOrgValue = (String) oldValue.getAttributeValue(MaterialVO.PK_ORG);
        String checkGroupValue = (String) oldValue.getAttributeValue(MaterialVO.PK_GROUP);
        String checkPKValue = oldValue.getPrimaryKey();

        VOTool votool = new VOTool();
        Set<String> editedSet = votool.getDifferentField(oldValue, newValue);

        CheckRefedUtil.chkMultiVersionRefed(
            event,
            refedBaseInfos,
            editedSet,
            checkOrgValue,
            checkGroupValue,
            checkPKValue);
    }

}
