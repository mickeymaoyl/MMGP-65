package nc.impl.mmgp.uif2.validator;

/**
 * 
 */

import java.util.Collection;

import nc.md.model.IBean;
import nc.vo.bd.config.BDUniqueruleVO;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.util.AbstractUniqueRuleValidate;
import nc.vo.util.UniqueConditionUtil;

/**
 * Ψһ�Թ���У���࣬�����Զ��巶Χ
 * 
 */
public class MMGPUniqueRuleValidateWithCustomerFilter extends
		AbstractUniqueRuleValidate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8741919621562526161L;

	private String[] filterFields;

	public MMGPUniqueRuleValidateWithCustomerFilter(String... filters) {
		this(null, filters);

	}

	public MMGPUniqueRuleValidateWithCustomerFilter(IBean bean,
			String... filters) {
		this(bean, false, filters);
	}

	public MMGPUniqueRuleValidateWithCustomerFilter(IBean bean,
			boolean checknullFlag, String... filters) {
		super(bean, checknullFlag);
		filterFields = filters;
	}

	@Override
	protected Collection<BDUniqueruleVO> getUniqueRules(IBean bean,
			SuperVO[] vos) throws BusinessException {
		return getService().getBDUniqueruleVOById(bean.getID());
	}

	/**
	 * ��Ӧ����д������������Ǹı�ԭ���ж�pk�Ƿ���ȵ��߼���ʵ�֣�
	 * 
	 * @see String 
	 *      nc.impl.bd.config.uniquerule.BDUniqueruleQryServiceImpl#getFilterWhere
	 *      (IBean , SuperVO ) <br>
	 *      ���Ǹ��඼��Ū��һ���ˣ����øģ����Ծ���д���ʵ�ֱȽϷ���
	 */
	@Override
	protected String getUniqueScopeWhere(SuperVO vo, IBean bean)
			throws BusinessException {

		String filterWhereCondition = " 1 = 1 ";
		if (MMArrayUtil.isNotEmpty(filterFields)) {
			for (String filterField : filterFields) {
				Object filterValue = vo.getAttributeValue(filterField);
				String filterWhere = getFilter(filterField, filterValue);
				filterWhereCondition = filterWhereCondition + " or "
						+ filterWhere;
			}
		}
		String baseUniqueCondition = UniqueConditionUtil
				.getUniqueConditionByBean(vo, bean, null);
		return MMStringUtil.isEmpty(baseUniqueCondition) ? filterWhereCondition
				: baseUniqueCondition + " and (" + filterWhereCondition + ")";
	}

	protected String getFilter(String filterField, Object filterValue) {
		// ����Ϊnull
		if (filterValue == null) {
			throw new IllegalArgumentException("field " + filterField
					+ " can't be null");
		}
		String scropewhere;
		if (filterValue instanceof UFDouble || filterValue instanceof Number) {
			scropewhere = filterField + " <> " + filterValue;
		} else {
			scropewhere = filterField + " <> '" + filterValue.toString() + "'";
		}
		return scropewhere;
	}

}
