package nc.ui.mmgp.uif2.service;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.pubapp.pub.smart.SmartBatchAppModelService;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.uif2.LoginContext;

/**
 * 
 * <b> 但表体通用前台服务 </b>
 * <p>
 * 增删改查
 * </p>
 * 创建日期:2012-11-23
 * 
 * @author wangweiu
 */
public class MMGPSmartBatchAppModelService extends SmartBatchAppModelService {

	private LoginContext context;

	public LoginContext getContext() {
		return context;
	}

	@Override
	public BatchOperateVO batchSave(BatchOperateVO batchVO) throws Exception {
		Object[] insertVOs = batchVO.getAddObjs();
		if (insertVOs != null) {
			for (Object obj : insertVOs) {
				if (obj instanceof CircularlyAccessibleValueObject) {
					CircularlyAccessibleValueObject vo = (CircularlyAccessibleValueObject) obj;
					if (MMStringUtil.isObjectStrEmpty(vo
							.getAttributeValue("pk_org"))) {
						vo.setAttributeValue("pk_org", context.getPk_org());
					}

					if (MMStringUtil.isObjectStrEmpty(vo
							.getAttributeValue("pk_group"))) {
						vo.setAttributeValue("pk_group", context.getPk_group());
					}
					if (MMStringUtil
							.isObjectStrEmpty(vo
									.getAttributeValue(IBaseServiceConst.ENABLESTATE_FIELD))) {
						vo.setAttributeValue(
								IBaseServiceConst.ENABLESTATE_FIELD,
								IPubEnumConst.ENABLESTATE_ENABLE);
					}
				}
			}
		}
		return super.batchSave(batchVO);
	}

	@Override
	public Object[] queryByWhereSql(String whereSql) throws Exception {
		if (!MMStringUtil.isEmpty(whereSql)) {
			whereSql = " and " + whereSql;
		}
		return super.queryByWhereSql(whereSql);
	}

	public void setContext(LoginContext context) {
		this.context = context;
		setVoClass(MMGPMetaUtils.getClassFullName(context));
	}

}
