package nc.impl.mmgp.bd.refcheck.check;

import java.util.List;

import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.bd.BDCommonEvent;
import nc.bs.framework.common.NCLocator;
import nc.impl.mmgp.bd.refcheck.CheckRefedUtil;
import nc.impl.mmgp.bd.refcheck.FileRefedInfo;
import nc.itf.bd.material.baseinfo.IMaterialBaseInfoQueryService;
import nc.vo.bd.material.MaterialVO;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * // 辅助属性结构( 物料版本被引用后，其中物料版本中 "辅助属性结构" 所关联的 "辅助属性结构" 档案不可修改)
 * </p>
 *
 * @since 创建日期 Sep 22, 2013
 * @author wangweir
 */
public class MarAsstFrameVOChecher implements IRefChecker {

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

        String checkOrgValue = (String) oldValue.getAttributeValue(getPkOrg());
        String pk_marAssFrame = oldValue.getPrimaryKey();
        SqlBuilder condition = new SqlBuilder();
        condition.append(MaterialVO.PK_MARASSTFRAME, pk_marAssFrame);
        String[] pk_materialids = this.getMarterialService().queryMaterialPksByCondition(null, condition.toString());

        if (MMArrayUtil.isEmpty(pk_materialids)) {
            return;
        }

        for (String materlid : pk_materialids) {
            for (FileRefedInfo info : refedBaseInfos) {
                // 判断该模块是否被安装
                if (!CheckRefedUtil.isModuleInstalled(info.getModulecode())) {
                    break;
                }
                boolean isEdited = false;
                for (String noEdit : info.getnoEditFileds()) {
                    if (noEdit.toLowerCase().equals(MaterialVO.PK_MARASSTFRAME.toLowerCase())) {
                        isEdited = true;
                        break;
                    }
                }
                if (!isEdited) {
                    return;
                }
                // 档案是否被单据引用
                boolean isRefedByBill = CheckRefedUtil.isRefedByBill(info, checkOrgValue, materlid);
                if (isRefedByBill && event instanceof BDCommonEvent) {
                    ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0060")/*@res "存在参照该档案的物料版本已被单据引用, 该档案不可修改!"*/);
                }
            }

        }

    }

    private IMaterialBaseInfoQueryService getMarterialService() {
        return NCLocator.getInstance().lookup(IMaterialBaseInfoQueryService.class);
    }

    protected String getPkOrg() {
        return MMGlobalConst.PK_ORG;
    }

}