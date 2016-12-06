package nc.impl.mmgp.bd.refcheck.check;

import java.util.List;

import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.bd.BDCommonEvent;
import nc.impl.mmgp.bd.refcheck.CheckRefedUtil;
import nc.impl.mmgp.bd.refcheck.FileRefedInfo;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Sep 22, 2013
 * @author wangweir
 */
public class DefaultPkValueRefCheckerForAddOrUpdate implements IRefChecker {

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
        String checkPKValue = oldValue.getPrimaryKey();
        String checkOrgValue = (String) oldValue.getAttributeValue(getPkOrg());

        for (FileRefedInfo info : refedBaseInfos) {
            // 判断该模块是否被安装
            if (!CheckRefedUtil.isModuleInstalled(info.getModulecode())) {
                continue;
            }

            List<String> displayList =
                    CheckRefedUtil
                        .getDisNoEditFileds(event.getSourceID(), info.getnoEditFileds(), oldValue, newValue);
            if (displayList.size() == 0) {
                continue;
            }

            boolean isRefedByBill = CheckRefedUtil.isRefedByBill(info, checkOrgValue, checkPKValue);
            if (isRefedByBill && event instanceof BDCommonEvent) {
                String errInfo = info.getErrInfo();
                String errorMsg = NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0", "0mmgp001-0101")/*%s时，%s不可以被修改*/;
                ExceptionUtils.wrappBusinessException(String.format(errorMsg, errInfo, displayList.toString()));
            }
        }

    }

    protected String getPkOrg() {
        return MMGlobalConst.PK_ORG;
    }

}
