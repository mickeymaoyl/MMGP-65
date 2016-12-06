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
 * <b> �����������У��rule </b>
 * <p>
 * �����������У��...��˼Զ
 * </p>
 * 
 * @since: ��������:Jun 24, 2014
 * @author:gaotx
 */
public class TableBodyBDReferenceCheckRule implements IRule {

	private String vbcode = "vbcode"; // ��ʼĬ�� ��������ֶ�

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
						 * uap����Ҳ��ѭ����ѯ,�о�û��̫�õİ취ѽ
						 */
						Boolean referenced = getService().isReferenced(
								bodyVO.getTableName(), bodyVO.getPrimaryKey(),
								new String[] { bodyVO.getTableName() });
						if (referenced) {// ���������
							if (msg == null) {
								msg = NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0", "0mmgp001-0106", null, new String[]{(String)bodyVO.getAttributeValue(vbcode)})/*�������Ϊ[{0}]*/;
							} else {
								msg += "[" + bodyVO.getAttributeValue(vbcode)
										+ "]";
							}
						}
					}
				}
				if (msg != null) {
					msg += NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0", "0mmgp001-0107")/*�ı����������ѱ����ã�����ɾ����*/;
					throw new BusinessException(msg);
				}
			} catch (BusinessException e) {
				ExceptionUtils.wrappException(e);
			}
		}

	}

}
