package nc.ui.mmgp.rbac.rule.discret;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.core.util.ObjectCreator;
import nc.bs.logging.Logger;
import nc.itf.bd.config.mode.IBDMode;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.uap.rbac.rule.discret.DefaultBDDiscretTableRuleDataModelService;
import nc.vo.bd.config.BDModeSelectedVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.uap.rbac.permissionresource.PermissionResourceVO;
import nc.vo.uif2.LoginContext;
import nc.vo.util.BDModeManager;

public class MMGPBDDiscretTableRuleDataModelService extends DefaultBDDiscretTableRuleDataModelService {

    public MMGPBDDiscretTableRuleDataModelService(PermissionResourceVO resVO) {
        super(resVO);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public SuperVO[] queryAllObjectToBeSelect(LoginContext context) {
        IMDPersistenceQueryService mdPersistenceQueryService =
                NCLocator.getInstance().lookup(IMDPersistenceQueryService.class);
        String classname = getBean().getFullClassName();
        SuperVO[] s = null;
        try {
            // 默认查当前集团下的数据 王伟修改 按管控模式过滤
            // String cond = "pk_group = '" + context.getPk_group() + "'";
            // String cond = VisibleUtil.getRefVisibleCondition(
            // context.getPk_group(), (String)null, getBean().getID());
            BDModeSelectedVO vo = BDModeManager.getInstance().getBDModeSelectedVOByMDClassID(getBean().getID());
            Integer visiblescope = vo != null ? vo.getVisiblescope() : IBDMode.SCOPE_GLOBE;
            String cond = " 1= 1 ";
            if (!isGlobal(visiblescope)) {
                cond = "pk_group = '" + context.getPk_group() + "'";
            }
            Class clazz = ObjectCreator.newInstance(classname).getClass();
            Collection c = mdPersistenceQueryService.queryBillOfVOByCond(clazz, cond, true, true);
            if (c == null) {
                return null;
            }
            List<SuperVO> result = new ArrayList<SuperVO>();
            Iterator it = c.iterator();
            while (it.hasNext()) {
                Object value = it.next();
                if (value instanceof SuperVO) {
                    result.add((SuperVO) value);
                } else if (value instanceof AggregatedValueObject) {
                    result.add((SuperVO) ((AggregatedValueObject) value).getParentVO());
                } else {
                    throw new IllegalArgumentException(value.getClass().getName());
                }
            }
            s = result.toArray(new SuperVO[0]);
        } catch (Exception e) {
            Logger.debug(e.getMessage());
            MessageDialog.showErrorDlg(
                null,
                nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("RBAC", "0RBAC0035")/* @res "错误" */,
                nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("RBAC", "0RBAC0037")/* @res "查询数据出错" */);
        }
        return s;
    }

    protected boolean isGlobal(Integer visible) {
        switch (visible) {
            case IBDMode.SCOPE_GLOBE:
            case IBDMode.SCOPE_GLOBE_ORG:
            case IBDMode.SCOPE_GLOBE_GROUP:
            case IBDMode.SCOPE_GLOBE_GROUP_ORG:
                return true;
            default:
                return false;
        }
    }

}
