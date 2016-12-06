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
 * 唯一性规则校验类，根据自定义范围
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
	 * 不应该重写这个方法，而是改变原来判断pk是否相等的逻辑来实现，
	 * 
	 * @see String 
	 *      nc.impl.bd.config.uniquerule.BDUniqueruleQryServiceImpl#getFilterWhere
	 *      (IBean , SuperVO ) <br>
	 *      但是父类都给弄到一起了，不好改，所以就重写这里，实现比较方便
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
		// 不能为null
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
