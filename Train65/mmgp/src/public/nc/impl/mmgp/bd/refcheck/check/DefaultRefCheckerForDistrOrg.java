package nc.impl.mmgp.bd.refcheck.check;

import java.util.List;

import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IEventType;
import nc.bs.businessevent.bd.BDCommonEvent;
import nc.impl.mmgp.bd.refcheck.CheckRefedUtil;
import nc.impl.mmgp.bd.refcheck.FileRefedInfo;
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
public class DefaultRefCheckerForDistrOrg implements IRefChecker {

    /**
     *
     */
    private static final String PK_MATERIAL = "pk_material";

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
        String checkPKValue = (String) newValue.getAttributeValue(getPkMaterial());
        String checkOrgValue = (String) newValue.getAttributeValue(getPkOrg());
        for (FileRefedInfo info : refedBaseInfos) {
            // 判断该模块是否被安装
            if (!CheckRefedUtil.isModuleInstalled(info.getModulecode())) {
                break;
            }
            // 若已修改的字段包含：该单据中引用后不能修改的字段, 查看该单据是否引用该档案
            boolean isRefedByBill = CheckRefedUtil.isRefedByBill(info, checkOrgValue, checkPKValue);

            if (isRefedByBill && event instanceof BDCommonEvent) {
                String errInfo = info.getErrInfo();
                if (event.getEventType().equals(IEventType.TYPE_DELETE_AFTER)) {
                    ExceptionUtils.wrappBusinessException(errInfo + ", " + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0058")/*@res "不可删除!"*/);
                } else {
                    ExceptionUtils.wrappBusinessException(errInfo + ", " + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0059")/*@res "不可取消分配!"*/);
                }
            }
        }
    }

    protected String getPkOrg() {
        return MMGlobalConst.PK_ORG;
    }

    protected String getPkMaterial() {
        return PK_MATERIAL;
    }

}