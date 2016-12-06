package nc.ui.mmgp.uif2.meta;

import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.vo.bd.meta.BDObjectTreeCreateStrategy;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.uif2.LoginContext;

/**
 * 扩展基本的tree策略.<br>
 * <p>
 * 增加了按照功能注册的元数据自动设置classname的功能。 简化配置。<br>
 * 增加根据元数据名称设置根名称
 * 
 * @author wangweiu
 * 
 */
public class MMGPBDTreeCreateStrategy extends BDObjectTreeCreateStrategy {
	private LoginContext context;

	public LoginContext getContext() {
		return context;
	}

	public void setContext(LoginContext context) {
		this.context = context;
	}

	private Object mmgpDataObject;

	public Object getMmgpDataObject() {
		return mmgpDataObject;
	}

	public void setMmgpDataObject(Object mmgpDataObject) {
		this.mmgpDataObject = mmgpDataObject;
	}

	public String getClassName() {
		if (MMStringUtil.isEmpty(super.getClassName())) {
			setClassName(MMGPMetaUtils.getClassFullName(context));
		}
		return super.getClassName();
	}

	@Override
	public String getRootName() {
		String rootName = super.getRootName();
		if (MMStringUtil.isEmpty(rootName)) {
			setRootName(MMGPMetaUtils.getIBean(context).getDisplayName());
		}
		return super.getRootName();
	}

}
