package nc.ui.mmgp.uif2.userdefitem;

import java.util.List;

import nc.md.model.IBean;
import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.uif2.userdefitem.QueryParam;
import nc.ui.uif2.userdefitem.UserDefItemContainer;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.uif2.LoginContext;

public class MMGPUserDefItemContainer extends UserDefItemContainer {

	@Override
	public void setContext(LoginContext context) {
		super.setContext(context);
		initDefaultMdFullName();
	}

	@Override
	public void setParams(List<QueryParam> params) {
		super.setParams(params);
		initDefaultMdFullName();
	}

	protected void initDefaultMdFullName() {
		if (getContext() == null || MMCollectionUtil.isEmpty(getParams())) {
			return;
		}
		IBean bean = MMGPMetaUtils.getIBean(getContext());
		for (QueryParam query : getParams()) {
			if (MMStringUtil.isEmpty(query.getMdfullname())) {
				query.setMdfullname(bean.getFullName());
			}
		}
	}

}
