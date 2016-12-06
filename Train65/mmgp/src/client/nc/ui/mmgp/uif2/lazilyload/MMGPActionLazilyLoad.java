package nc.ui.mmgp.uif2.lazilyload;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor;
import nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad;
import nc.ui.pubapp.uif2app.lazilyload.IChildrenChangeListener;
import nc.ui.pubapp.uif2app.lazilyload.PubAppHacker;
import nc.ui.uif2.NCAction;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

public class MMGPActionLazilyLoad extends ActionLazilyLoad {
	@Override
	public void addChildrenChangeListener(IChildrenChangeListener ls) {
//		this.listener = ls;
		PubAppHacker.setActionLazilyLoadListener(this, ls);

		Interceptor interceptor = new MMGPInterceptor();
		for (NCAction ncAction : this.getActionList()) {
			// 不能直接设置拦截器，有可能将原有的拦截器丢失
			// 需要使用组合拦截器的方式来增加新的拦截器
			CompositeActionInterceptor
					.addInterceptor(interceptor, ncAction);
		}
	}
	public class MMGPInterceptor extends Interceptor {

		@Override
		public boolean beforeDoAction(Action action, ActionEvent e) {
			Object[] objs = getModel().getSelectedOperaDatas();
			if (objs != null && objs.length > 0) {
				if (objs[0] instanceof IBill) {
					return super.beforeDoAction(action, e);
				}
			}
			return true;
		}
		
	}
}
