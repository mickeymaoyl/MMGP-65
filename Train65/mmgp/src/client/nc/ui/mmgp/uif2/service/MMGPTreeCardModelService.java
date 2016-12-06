package nc.ui.mmgp.uif2.service;

import nc.bs.framework.common.NCLocator;
import nc.bs.mmgp.common.MMGPVoUtils;
import nc.itf.mmgp.uif2.IMMGPCmnTreeService;
import nc.ui.ml.NCLangRes;
import nc.ui.uif2.model.IAppModelService;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.CarrierRuntimeException;
import nc.vo.uif2.LoginContext;

/**
 *
 * <b> 树卡通用前台服务</b>
 * <p>
 * 提供通用的增加、删除、修改功能，并实现了懒加载接口
 * </p>
 * 创建日期:2012-11-23
 *
 * @author wangweiu
 */
public class MMGPTreeCardModelService implements IAppModelService {

	private IMMGPCmnTreeService manageService;
	private LoginContext context;

	public void delete(Object object) throws Exception {
		// MMGPVoUtils.setHeadVOStatus(object, VOStatus.DELETED);
		getManageService().cmnDeleteTreeVO(castSuperVO(object));
	}

	public Object insert(Object object) throws Exception {
		MMGPVoUtils.setHeadVOStatus(object, VOStatus.NEW);
		return getManageService().cmnInsertTreeVO(castSuperVO(object));
	}

	public Object[] queryByDataVisibilitySetting(LoginContext context)
			throws Exception {
		return null;
	}

	protected SuperVO castSuperVO(Object object) {
		if (!(object instanceof SuperVO)) {
			throw new CarrierRuntimeException("object must be SuperVO!");
		}
		return (SuperVO) object;
	}

	public Object update(Object object) throws Exception {
		MMGPVoUtils.setHeadVOStatus(object, VOStatus.UPDATED);
		return getManageService().cmnUpdateTreeVO(castSuperVO(object));
	}

	public IMMGPCmnTreeService getManageService() {

		if (manageService == null) {
			if (MMStringUtil.isNotEmpty(manageServiceItf)) {
				manageService = (IMMGPCmnTreeService) NCLocator.getInstance()
						.lookup(manageServiceItf);
			} else {
				manageService = NCLocator.getInstance().lookup(
						IMMGPCmnTreeService.class);
			}
		}
		return manageService;
	}

	private String manageServiceItf;

	public String getManageServiceItf() {
		return manageServiceItf;
	}

	public void setManageServiceItf(String itfClassName) {
		try {
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(itfClassName);
			if(!IMMGPCmnTreeService.class.isAssignableFrom(clazz)){
				throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0031")/*@res "后台接口必须继承nc.itf.mmgp.uif2.IMMGPCmnTreeService"*/);
			}
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0092", null, new String[]{itfClassName})/*配置的后台接口[{0}]不存在*/);
		}
		this.manageServiceItf = itfClassName;
	}

	public LoginContext getContext() {
		return context;
	}

	public void setContext(LoginContext context) {
		this.context = context;
	}

}