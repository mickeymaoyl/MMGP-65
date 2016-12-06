package nc.impl.mmgp.uif2.rule;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uap.bd.refcheck.IReferenceCheck;
import nc.md.model.IBean;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMMetaUtils;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 
 * <b> 树表表体引用校验rule </b>
 * <p>
 * 用于树表表体校验...李思远
 * </p>
 * 
 * @since: 创建日期:Jun 24, 2014
 * @author:gaotx
 */
public class TableBodyBDReferenceCheckRule implements IRule {

	private String vbcode = "vbcode"; // 初始默认 分类编码字段

	private IReferenceCheck getService() {
		return NCLocator.getInstance().lookup(IReferenceCheck.class);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void process(Object[] vos) {

		if (MMArrayUtil.isEmpty(vos)) {
			return;
		}

		for (Object vo : vos) {
			AggregatedValueObject aggvo = (AggregatedValueObject) vo;
			try {
				String msg = null;
				IBean bean = null;
				for (CircularlyAccessibleValueObject body : aggvo
						.getChildrenVO()) {
					if (bean == null) {
						bean = MMMetaUtils.getBeanByClassFullName(body
								.getClass().getName());
						vbcode = MMMetaUtils.getCodeFieldName(bean);
					}
					if (body.getStatus() == VOStatus.DELETED) {
						SuperVO bodyVO = (SuperVO) body;
						/*
						 * uap这里也是循环查询,感觉没有太好的办法呀
						 */
						Boolean referenced = getService().isReferenced(
								bodyVO.getTableName(), bodyVO.getPrimaryKey(),
								new String[] { bodyVO.getTableName() });
						if (referenced) {// 如果有引用
							if (msg == null) {
								msg = NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0", "0mmgp001-0106", null, new String[]{(String)bodyVO.getAttributeValue(vbcode)})/*分类编码为[{0}]*/;
							} else {
								msg += "[" + bodyVO.getAttributeValue(vbcode)
										+ "]";
							}
						}
					}
				}
				if (msg != null) {
					msg += NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0", "0mmgp001-0107")/*的表体行数据已被引用，不能删除！*/;
					throw new BusinessException(msg);
				}
			} catch (BusinessException e) {
				ExceptionUtils.wrappException(e);
			}
		}

	}

}
