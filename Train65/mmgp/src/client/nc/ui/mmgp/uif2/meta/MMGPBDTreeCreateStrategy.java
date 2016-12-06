package nc.ui.mmgp.uif2.meta;

import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.vo.bd.meta.BDObjectTreeCreateStrategy;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.uif2.LoginContext;

/**
 * ��չ������tree����.<br>
 * <p>
 * �����˰��չ���ע���Ԫ�����Զ�����classname�Ĺ��ܡ� �����á�<br>
 * ���Ӹ���Ԫ�����������ø�����
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
