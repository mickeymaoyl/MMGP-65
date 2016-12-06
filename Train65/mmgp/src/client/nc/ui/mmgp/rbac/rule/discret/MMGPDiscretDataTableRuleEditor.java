package nc.ui.mmgp.rbac.rule.discret;

import nc.ui.uap.rbac.rule.discret.DefaultBDDiscretTableRuleDataModelService;
import nc.ui.uap.rbac.rule.discret.DiscretDataTableRuleEditor;
import nc.ui.uap.rbac.rule.discret.IDiscretTableRuleDataModelService;
import nc.vo.pub.SuperVO;
/**
 *  TODO
 * @author wangweiu
 *
 */
public class MMGPDiscretDataTableRuleEditor extends DiscretDataTableRuleEditor {
	private IDiscretTableRuleDataModelService<SuperVO> myservice;
	@Override
	public IDiscretTableRuleDataModelService<SuperVO> getService() {
		DefaultBDDiscretTableRuleDataModelService superService =  (DefaultBDDiscretTableRuleDataModelService) super.getService();
		if (myservice == null) {
			myservice = new MMGPBDDiscretTableRuleDataModelService(superService.getPermissionResourceVO());
		}
		return myservice;
	}

}
