package nc.ui.mmgp.uif2.utils;

import java.util.Map;

import nc.funcnode.ui.AbstractFunclet;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.vo.mmgp.util.MMMetaUtils;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.uif2.LoginContext;

/**
 *
 * <b> 前台元数据工具类 </b>
 * <p>
 * 主要提供基于LoginContext的元数据查询
 * </p>
 * 创建日期:2012-11-23
 *
 * @author wangweiu
 */
public class MMGPMetaUtils {

	/**
	 * 根据功能注册，返回主实体的类路径
	 *
	 * @param context
	 *            节点的上下文
	 * @return 主实体的类路径
	 */
	public static String getClassFullName(LoginContext context) {
		IBean bean = getIBean(context);
		return bean.getFullClassName();
	}

	public static IBean getIBean(LoginContext context) {
		String mdid = getMetaId(context);
		return MMMetaUtils.getBeanByMetaID(mdid);
	}

	public static String getPKFieldName(LoginContext context) {
		return getNamePathValue(context, MMMetaUtils.ID);
	}

	public static String getParentPKFieldName(LoginContext context) {
		return getNamePathValue(context, MMMetaUtils.PID);
	}

	public static String getCodeFieldName(LoginContext context) {
		return getNamePathValue(context, MMMetaUtils.CODE);
	}

	public static String getBillNoFieldName(LoginContext context) {
		IBean bean = getIBean(context);
		Map<String, String> name_path_map = ((IBusinessEntity) bean)
				.getBizInterfaceMapInfo(IFlowBizItf.class.getName());
		return name_path_map.get("billno");
	}

	protected static String getNamePathValue(LoginContext context, String key) {
		IBean bean = getIBean(context);
		if (bean == null)
			return null;
		return MMMetaUtils.getNamePathValue(bean, key);
	}

	public static String getMetaId(LoginContext context) {
		AbstractFunclet funclet = (AbstractFunclet) context.getEntranceUI();
		String mdid = funclet.getFuncletContext().getFuncRegisterVO().getMdid();
		if (MMStringUtil.isEmpty(mdid)) {
			throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0032")/*@res "没有配置引用的元数据!"*/);
		}
		return mdid;
	}
}