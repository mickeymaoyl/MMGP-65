package nc.vo.mmgp.pf;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Logger;
import nc.md.data.access.DASFacade;
import nc.md.data.access.NCObject;
import nc.md.model.IAttribute;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.md.model.type.IType;
import nc.md.util.MDUtil;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.change.ChangeVOAdjustContext;
import nc.vo.pf.change.ExchangeRuleVO;
import nc.vo.pf.change.IChangeVOAdjust;
import nc.vo.pf.change.PfBillMappingUtil;
import nc.vo.pf.change.RuleTypeEnum;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.formulaset.FormulaParseFather;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.trade.pub.IExAggVO;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.uap.rbac.constant.INCSystemUserConst;

/**
 * 主子孙VO映射支持类 <br>
 * 支持 ：<br>
 * 1. 主子孙对主子孙的映射<br>
 * 2. 主子孙对主子的映射 <br>
 * 3. 映射规则支持公式、赋值和映射，但不支持赋值为目的交易类型的系统变量 "DESTTRANSTYPE"
 *
 * @author tanglj
 *
 */
public abstract class MMGPGrandExchange implements IChangeVOAdjust {
	@Override
	public AggregatedValueObject[] batchAdjustAfterChange(
			AggregatedValueObject[] srcVOs, AggregatedValueObject[] destVOs,
			ChangeVOAdjustContext adjustContext) throws BusinessException {
		try {
			m_destBilltype = adjustContext.getDestBilltype();
			dealWithGrand(srcVOs, destVOs);
		} catch (Exception e) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0068")/*@res "孙表转化出现问题："*/ + e.getMessage());
		}
		return destVOs;
	}

	/**
	 * 获取映射规则
	 *
	 * @return
	 */
	public abstract Collection<ExchangeRuleVO> getGrandSplitItemVOCon();

	protected void dealWithGrand(AggregatedValueObject[] srcVOs,
			AggregatedValueObject[] destVOs) throws NegativeArraySizeException,
			BusinessException, ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		Collection<ExchangeRuleVO> splitItemVOCon = getGrandSplitItemVOCon();
		if (srcVOs == null || destVOs == null || splitItemVOCon == null
				|| splitItemVOCon.size() == 0)
			return;

		AggregatedValueObject[] chgDestVOs = destVOs;
		if (srcVOs.length != destVOs.length) {
			if (destVOs.length == 1) {
				if (destVOs[0].getChildrenVO().length == srcVOs.length) {
					chgDestVOs = (AggregatedValueObject[]) Array.newInstance(
							Class.forName(destVOs[0].getClass().getName()),
							srcVOs.length);
					for (int i = 0; i < srcVOs.length; i++) {
						chgDestVOs[i] = destVOs[0].getClass().newInstance();
						chgDestVOs[i].setParentVO(destVOs[0].getParentVO());
						CircularlyAccessibleValueObject[] childVOs = (CircularlyAccessibleValueObject[]) Array
								.newInstance(destVOs[0].getChildrenVO()[0]
										.getClass(), 1);
						childVOs[0] = destVOs[0].getChildrenVO()[i];
						chgDestVOs[i].setChildrenVO(childVOs);
					}
				} else {
					Logger.error("不支持此种分单规则");
					return;
				}
			}
		}
		ArrayList<String[]> aRules = new ArrayList<String[]>();
		ArrayList<String[]> mRules = new ArrayList<String[]>();
		ArrayList<String> fRules = new ArrayList<String>();
		HashMap<String, String> destToSrcMap = new HashMap<String, String>();
		for (ExchangeRuleVO ruleVO : splitItemVOCon) {
			// 1. trim数据
			ruleVO.setDest_attr(ruleVO.getDest_attr().trim());
			ruleVO.setRuleData(ruleVO.getRuleData().trim());
			String[] tags = ruleVO.getDest_attr().split("\\.");
			if (tags != null) {
				if (tags.length == 3 || tags.length == 2) {
					// 孙表映射孙表
					if (ruleVO.getRuleType() == RuleTypeEnum.ASSIGN.toInt()) {
						aRules.add(new String[] { ruleVO.getDest_attr(),
								ruleVO.getRuleData() });
					} else if (ruleVO.getRuleType() == RuleTypeEnum.MOVE
							.toInt()) {
						// 解析成功标志.目前仅支持孙表的映射
						boolean bSuccess = false;

						String grandDestTable = tags.length == 3 ? (tags[0]
								+ "." + tags[1]) : tags[0];
						String grandSrcTable = null;
						int srcIndex = ruleVO.getRuleData().lastIndexOf('.');
						if (srcIndex > 0) {
							grandSrcTable = ruleVO.getRuleData().substring(0,
									srcIndex);
						}
						if (destToSrcMap.containsKey(grandDestTable)) {
							if (grandSrcTable == null) {
								bSuccess = true;
							} else {
								String grandSrcTableOriginal = destToSrcMap
										.get(grandDestTable);
								if (grandSrcTableOriginal == null
										|| grandSrcTable
												.startsWith(grandSrcTableOriginal)) {
									destToSrcMap.put(grandDestTable,
											grandSrcTable);
									bSuccess = true;
								} else {
									if (grandSrcTableOriginal
											.startsWith(grandSrcTable)) {
										bSuccess = true;
									} else {
										Logger.error("孙表映射规则已存在,规则："
												+ ruleVO.getDest_attr() + "->"
												+ ruleVO.getRuleData()
												+ " 与映射关系 "
												+ grandSrcTableOriginal + "->"
												+ grandDestTable + "不兼容");
									}
								}
							}
						} else {
							destToSrcMap.put(grandDestTable, grandSrcTable);
							bSuccess = true;
						}
						if (bSuccess) {
							mRules.add(new String[] { ruleVO.getDest_attr(),
									ruleVO.getRuleData() });
						}

					} else if (ruleVO.getRuleType() == RuleTypeEnum.FORMULA
							.toInt()) {
						fRules.add(ruleVO.getDest_attr() + "->"
								+ ruleVO.getRuleData());
					}
				} else {
					Logger.error("仅支持孙表的映射,规则：" + ruleVO.getDest_attr()
							+ " 不符合要求");
				}
			}
		}

		for (int i = 0; i < srcVOs.length; i++) {
			AggregatedValueObject destVO = chgDestVOs[i];
			AggregatedValueObject srcVO = srcVOs[i];
			initDestBillVO(srcVO, destVO, m_destBilltype, destToSrcMap);
			execAssignsForMetadata(destVO, aRules);
		}
		execFieldsMetaToMetaBatch(srcVOs, chgDestVOs, mRules);
		execFormulasMetaToMetaBatch(srcVOs, chgDestVOs, fRules);
	}

	private static void initDestBillVO(AggregatedValueObject srcBillVO,
			AggregatedValueObject destBillVO, String destBillOrTranstype,
			HashMap<String, String> destToSrcMap) throws BusinessException,
			NegativeArraySizeException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		String[] destChildAttrs = PfMetadataTools
				.queryChildAttributes(destBillOrTranstype);
		IBusinessEntity destBE = PfMetadataTools
				.queryMetaOfBilltype(destBillOrTranstype);
		HashMap<CircularlyAccessibleValueObject, CircularlyAccessibleValueObject> dest2srcVOMap = new HashMap<CircularlyAccessibleValueObject, CircularlyAccessibleValueObject>();
		for (Entry<String, String> entry : destToSrcMap.entrySet()) {
			String desPath = entry.getKey();
			String scrPath = entry.getValue();
			String[] desSplitPath = desPath.split("\\.");
			String[] srcSplitPath = scrPath.split("\\.");
			// 孙表映射孙表
			if (desSplitPath.length == 2) {
				CircularlyAccessibleValueObject[] tableVOs = null;
				if (destBillVO instanceof IExAggVO) {
					// 多子表
					IExAggVO exDestBillVO = (IExAggVO) destBillVO;
					tableVOs = exDestBillVO.getTableVO(desSplitPath[0]);
				} else if (destChildAttrs.length > 0) {
					// 单子表
					tableVOs = destBillVO.getChildrenVO();
				}
				if (tableVOs != null && tableVOs.length > 0) {
					if (srcSplitPath.length == 1) {
						// TODO 映射子表

					} else if (srcSplitPath.length == 2) {
						// 映射孙表
						CircularlyAccessibleValueObject[] srcChildVOs = null;
						if (srcBillVO instanceof IExAggVO) {
							srcChildVOs = ((IExAggVO) srcBillVO)
									.getTableVO(srcSplitPath[0]);
						} else {
							srcChildVOs = srcBillVO.getChildrenVO();
						}
						if (srcChildVOs == null
								|| srcChildVOs.length != tableVOs.length) {
							Logger.error("孙表映射孙表时，子表映射的数目不一致."
									+ scrPath
									+ ":"
									+ (srcChildVOs == null ? 0
											: srcChildVOs.length) + "->"
									+ desPath + ":" + tableVOs.length);
						} else {
							for (int i = 0; i < tableVOs.length; i++) {
								CircularlyAccessibleValueObject[] srcGrandVOs = (CircularlyAccessibleValueObject[]) BeanHelper
										.getProperty(srcChildVOs[i],
												srcSplitPath[1]);
								IBusinessEntity destChildBE = PfMetadataTools
										.getBusinessEntityOfAttr(destBE
												.getAttributeByName(desSplitPath[0]));
								if (destChildBE == null) {
									Logger.error("元数据错误:单据"
											+ destBillOrTranstype + "无法找到属性为"
											+ desSplitPath[0] + "的字段");
								} else {
									IBusinessEntity destGrandBE = PfMetadataTools
											.getBusinessEntityOfAttr(destChildBE
													.getAttributeByName(desSplitPath[1]));
									if (destGrandBE == null) {
										Logger.error("元数据错误:单据"
												+ destBillOrTranstype + "属性为"
												+ desSplitPath[0] + "的子表不包含字段"
												+ desSplitPath[1]);
									} else {
										if (srcGrandVOs != null
												&& srcGrandVOs.length > 0) {
											String destGrandVoClzName = destGrandBE
													.getFullClassName();
											CircularlyAccessibleValueObject[] itemVos = (CircularlyAccessibleValueObject[]) Array
													.newInstance(
															Class.forName(destGrandVoClzName),
															srcGrandVOs.length);
											for (int j = 0; j < srcGrandVOs.length; j++) {

												itemVos[j] = (CircularlyAccessibleValueObject) Class
														.forName(
																destGrandVoClzName)
														.newInstance();
												dest2srcVOMap.put(itemVos[j],
														srcGrandVOs[j]);
											}
											BeanHelper.setProperty(tableVOs[i],
													desSplitPath[1], itemVos);
										}
									}
								}
							}
						}
					}
				}
			}
			// 孙表映射子表
			else if (desSplitPath.length == 1) {
				if (srcSplitPath.length == 1) {
					// TODO 映射子表

				} else if (srcSplitPath.length == 2) {
					// 映射孙表
					CircularlyAccessibleValueObject[] srcChildVOs = null;
					if (srcBillVO instanceof IExAggVO) {
						srcChildVOs = ((IExAggVO) srcBillVO)
								.getTableVO(srcSplitPath[0]);
					} else {
						srcChildVOs = srcBillVO.getChildrenVO();
					}
					if (srcChildVOs == null || srcChildVOs.length != 1) {
						Logger.error("孙表映射子表时，子表映射的数目不一致."
								+ scrPath
								+ ":"
								+ (srcChildVOs == null ? 0 : srcChildVOs.length)
								+ "->" + desPath + ":" + 0);
						return;
					} else {
						CircularlyAccessibleValueObject[] srcGrandVOs = (CircularlyAccessibleValueObject[]) BeanHelper
								.getProperty(srcChildVOs[0], srcSplitPath[1]);
						IBusinessEntity destChildBE = PfMetadataTools
								.getBusinessEntityOfAttr(destBE
										.getAttributeByName(desSplitPath[0]));
						if (destChildBE == null) {
							Logger.error("元数据错误:单据" + destBillOrTranstype
									+ "无法找到属性为" + desSplitPath[0] + "的字段");
						} else {
							if (srcGrandVOs != null && srcGrandVOs.length > 0) {
								String destChildVoClzName = destChildBE
										.getFullClassName();
								CircularlyAccessibleValueObject[] itemVos = (CircularlyAccessibleValueObject[]) Array
										.newInstance(Class
												.forName(destChildVoClzName),
												srcGrandVOs.length);
								for (int j = 0; j < srcGrandVOs.length; j++) {

									itemVos[j] = (CircularlyAccessibleValueObject) Class
											.forName(destChildVoClzName)
											.newInstance();
									dest2srcVOMap.put(itemVos[j],
											srcGrandVOs[j]);
								}
								BeanHelper.setProperty(destBillVO,
										desSplitPath[0], itemVos);
								if (destBillVO instanceof IExAggVO) {
									((IExAggVO) destBillVO).setTableVO(
											desSplitPath[0], itemVos);
								} else {
									destBillVO.setChildrenVO(itemVos);
								}
							}
						}
					}
				}
			}
		}
	}

	private void execAssignsForMetadata(AggregatedValueObject targetVO,
			ArrayList<String[]> sAssigns) {
		if (sAssigns == null || sAssigns.size() == 0)
			return;
		NCObject ncObj = NCObject.newInstance(targetVO);
		for (int i = 0; i < sAssigns.size(); i++) {
			Logger.debug(" 执行赋值：" + sAssigns.get(i)[0] + "->"
					+ sAssigns.get(i)[1]);
			Object valueObj = sAssigns.get(i)[1];
			// 如果是系统变量
			if (PfBillMappingUtil.isSystemEnvField(sAssigns.get(i)[1])) {
				valueObj = getEnvParamValue(sAssigns.get(i)[1]);
			}

			// 获得目的Path对应的元数据属性，并根据其数据类型给其赋值
			IAttribute attr = ncObj.getRelatedBean().getAttributeByPath(
					sAssigns.get(i)[0]);
			if (attr == null) {
				Logger.error("取路径错误，请检查VO交换路径的配置，实体："
						+ ncObj.getRelatedBean().getDisplayName()
						+ "###错误的属性路径：：" + sAssigns.get(i)[0]);
				continue;
			}
			ncObj.setAttributeValue(sAssigns.get(i)[0],
					getValueByIType(valueObj, attr.getDataType()));
		}
	}

	/**
	 * 将元数据的属性值转化为指定的类型
	 *
	 * @param valueObj
	 * @param iType
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object getValueByIType(Object valueObj, IType iType) {
		if (valueObj == null)
			return null;
		if (valueObj instanceof Object[]) {
			Object[] valueObjs = (Object[]) valueObj;
			for (int i = 0; i < valueObjs.length; i++) {
				valueObjs[i] = iType.valueOf(valueObjs[i]);
			}
			return valueObjs;
		} else if (valueObj instanceof Collection) {
			Object[] valueObjResult = ((Collection) valueObj)
					.toArray(new Object[0]);
			return getValueByIType(valueObjResult, iType);
		} else {
			return iType.valueOf(valueObj);
		}
	}

	// 当前系统日期
	private String m_strDate = new UFDate().toString();
	// 当前业务日期
	private UFDate m_strBuziDate = new UFDate(InvocationInfoProxy.getInstance()
			.getBizDateTime());
	// 当前登陆操作员PK
	private String m_strOperator = InvocationInfoProxy.getInstance()
			.getUserId();
	// 当前登陆集团
	private String m_strGroup = InvocationInfoProxy.getInstance().getGroupId();
	// 当前系统时间
	private String m_strNowTime = new UFDateTime(new Date()).toString();
	// 当前业务日期
	private UFDate m_strBuziTime = new UFDate(InvocationInfoProxy.getInstance()
			.getBizDateTime());
	private String m_destBilltype;
	private String m_destTranstype;

	private Object getEnvParamValue(String srcField) {
		Object valueObj = null;
		if (srcField.equals(PfBillMappingUtil.ENV_NOWDATE)) {
			valueObj = m_strDate;
		} else if (srcField.equals(PfBillMappingUtil.ENV_BUZIDATE)) {
			valueObj = m_strBuziDate;
		} else if (srcField.equals(PfBillMappingUtil.ENV_LOGINUSER)) {
			valueObj = m_strOperator;
		} else if (srcField.equals(PfBillMappingUtil.ENV_LOGINGROUP)) {
			valueObj = m_strGroup;
		} else if (srcField.equals(PfBillMappingUtil.ENV_NOWTIME)) {
			valueObj = m_strNowTime;
		} else if (srcField.equals(PfBillMappingUtil.ENV_BUZITIME)) {
			valueObj = m_strBuziTime;
		} else if (srcField.equals(PfBillMappingUtil.ENV_NCSYSUSER)) {
			valueObj = INCSystemUserConst.NC_USER_PK;
		} else if (srcField.equals(PfBillMappingUtil.ENV_DEST_BILLTYPE)) {
			valueObj = m_destBilltype;
		} else if (srcField.equals(PfBillMappingUtil.ENV_DEST_TRANSTYPE)) {
			// TODO 这里不支持transtype
			valueObj = m_destTranstype;
		}
		return valueObj;
	}

	private void execFieldsMetaToMetaBatch(AggregatedValueObject[] sourceVOs,
			AggregatedValueObject[] targetVOs, ArrayList<String[]> mRules) {
		if (mRules == null || mRules.size() == 0)
			return;
		NCObject[] targetNcObjs = new NCObject[targetVOs.length];
		NCObject[] sourceNcObjs = new NCObject[sourceVOs.length];

		for (int i = 0, end = targetVOs.length; i < end; i++) {
			targetNcObjs[i] = NCObject.newInstance(targetVOs[i]);
		}

		for (int i = 0, end = sourceVOs.length; i < end; i++) {
			sourceNcObjs[i] = NCObject.newInstance(sourceVOs[i]);
		}

//		Object value = null;
		// key-value 目的属性--来源属性
		HashMap<String, String> noSysEnvPathsMap = new HashMap<String, String>();
		HashMap<String, String> sysEnvPathsMap = new HashMap<String, String>();

		for (int i = 0; i < mRules.size(); i++) {
			if (PfBillMappingUtil.isSystemEnvField(mRules.get(i)[1])) {
				// 系统变量
				sysEnvPathsMap.put(mRules.get(i)[0], mRules.get(i)[1]);
			} else {
				// 用于批处理
				noSysEnvPathsMap.put(mRules.get(i)[0], mRules.get(i)[1]);
			}
		}

		// 系统变量
		Set<String> syskeys = sysEnvPathsMap.keySet();
		for (String name : syskeys) {
			String srcPath = sysEnvPathsMap.get(name);
			// 取得来源属性的值
			Object valueObj = getEnvParamValue(srcPath);
			for (NCObject targetNcObj : targetNcObjs) {
				// 取得目的属性的类型
				IType iType = targetNcObj.getRelatedBean()
						.getAttributeByPath(name).getDataType();
				if (valueObj == null)
					continue;
				doMeta2Meta(targetNcObj, valueObj, iType, name);
			}
		}

		// 非系统变量,执行批处理
		String[] destAttributes = noSysEnvPathsMap.keySet().toArray(
				new String[0]);
		ArrayList<String> srcAttributes = new ArrayList<String>();
		for (int start = 0, end = destAttributes.length; start < end; start++) {
			srcAttributes.add(noSysEnvPathsMap.get(destAttributes[start]));
		}
		// 批量获得来源属性的值
		List<Object[]> varValueArr = DASFacade.getAttributeValue(
				srcAttributes.toArray(new String[0]), sourceNcObjs);

		for (int start = 0, end = targetNcObjs == null ? 0
				: targetNcObjs.length; start < end; start++) {
			Object[] varValues = varValueArr.get(start);

			for (int j = 0, k = destAttributes.length; j < k; j++) {
				IType iType = targetNcObjs[start].getRelatedBean()
						.getAttributeByPath(destAttributes[j]).getDataType();
				// 来源属性的值
				Object valueObj = varValues[j];
				if (valueObj == null)
					continue;
				String[] desPaths = destAttributes[j].split("\\.");
				if (desPaths.length == 2) {
					if (valueObj instanceof Object[]) {
						valueObj = ((Object[]) valueObj)[0];
					}
					if (valueObj == null)
						continue;
				}
				doMeta2Meta(targetNcObjs[start], valueObj, iType,
						destAttributes[j]);
			}
		}
	}

	private void doMeta2Meta(NCObject ncObj, Object valueObj, IType iType,
			String name) {
		// ncObj.setAttributeValue(name,
		// ((Object[]) (((Object[]) valueObj)[0]))[0]);
		if (PfMetadataTools.isObjectArray(valueObj)) {
			// 如果来源属性值为数组，即来源属性为子表属性
			int len = Array.getLength(valueObj);
			Object[] objArray = new Object[len];
			for (int j = 0; j < len; j++) {
				// 对数组中的每一个元素进行类型转换，转换成目的属性的类型
				objArray[j] = getValueByIType(Array.get(valueObj, j), iType);
			}
			if (isCollectionOfPath(name, ncObj.getRelatedBean()))
				// 如果目的属性也是集合类型，则将数组赋给它
				ncObj.setAttributeValue(name, objArray);
			else
				// 如果目的属性不是集合类型，则取数组的第一个元素给它赋值
				ncObj.setAttributeValue(name, Array.get(objArray, 0));

		} else {
			// 来源属性值不是数组，则直接把值赋给目的属性
			ncObj.setAttributeValue(name, getValueByIType(valueObj, iType));
		}
	}

	/**
	 * 判断某path对应的属性链中 是否存在集合类型
	 *
	 * @param strPath
	 *            比如a.b, details.c等
	 * @param bean
	 * @return
	 */
	private boolean isCollectionOfPath(String strPath, IBean bean) {
		IAttribute attr = null;
		int npos = strPath.indexOf('.');
		if (npos < 0) {
			attr = bean.getAttributeByName(strPath);
			return MDUtil.isCollectionType(attr.getDataType());
		} else {
			String[] paths = strPath.split("\\.");
			StringBuffer newPath = new StringBuffer();
			for (int j = 0; j < paths.length; j++) {
				newPath.append(paths[j]);
				attr = bean.getAttributeByPath(newPath.toString());
				if (MDUtil.isCollectionType(attr.getDataType()))
					return true;
				newPath.append(".");
			}
		}
		return false;
	}

	/**
	 * 批量执行
	 *
	 * @throws BusinessException
	 * */
	private void execFormulasMetaToMetaBatch(AggregatedValueObject[] sourceVOs,
			AggregatedValueObject[] targetVOs, ArrayList<String> formulas)
			throws BusinessException {
		if (formulas == null || formulas.size() == 0)
			return;
		// 执行公式交换
		for (int i = 0; i < formulas.size(); i++) {
			Logger.debug("**执行公式交换:" + formulas.get(i));
			// 设置表达式
			getFormulaParse().setExpress(formulas.get(i));
			// 获得变量
			VarryVO varry = getFormulaParse().getVarry();
			// leijun 2006-3-6 add this break
			if (varry == null)
				continue;

			execFormulaMetaToMeta2Batch(sourceVOs, targetVOs, varry);
		}

	}

	private void execFormulaMetaToMeta2Batch(AggregatedValueObject[] sourceVOs,
			AggregatedValueObject[] targetVOs, VarryVO varry)
			throws BusinessException {

		String destPath = varry.getFormulaName();
		NCObject ncObj = NCObject.newInstance(targetVOs[0]);

		boolean canBatch = canChangeBatch(sourceVOs, targetVOs, varry);

		if ((!canBatch)
				|| (isCollectionOfPath(destPath, ncObj.getRelatedBean()) && isCollectionOfSrc(varry))) {
			for (int i = 0; i < sourceVOs.length; i++) {
				execFormulaMetaToMeta2(sourceVOs[i], targetVOs[i], varry);
			}
		} else {

			parseMetaFormulaVarBatch(sourceVOs, targetVOs, varry);

			IType iType = ncObj.getRelatedBean().getAttributeByPath(destPath)
					.getDataType();

			Object[] realValues = null;
			// XXX:取得公式值
			Object[] valueObjs = getFormulaParse().getValueO();

			realValues = new Object[targetVOs.length];

			for (int i = 0; i < targetVOs.length; i++) {
				// ***从int转换为String的公式，或者去多于资源的公式，varry.getVarry().length==0,此时返回的结果数组长度只为1
				if (valueObjs.length == 1
						&& (varry.getVarry() == null || varry.getVarry().length == 0)) {
					realValues[i] = getValueByIType(valueObjs[0], iType);
				} else {
					realValues[i] = getValueByIType(valueObjs[i], iType);
				}
			}

			// 如果出错，则抛出异常
			String errStr = getFormulaParse().getError();
			if (!StringUtil.isEmptyWithTrim(errStr))
				throw new PFBusinessException(errStr);

			// XXX给目的元数据中的属性赋值 等dingxm提供批量方法，再修改此处
			for (int i = 0, end = targetVOs.length; i < end; i++) {
				NCObject _ncObj = NCObject.newInstance(targetVOs[i]);
				String[] deepPath = destPath.split("\\.");
				// Liangjie : 执行公式返回是不带层级的，这里需要再创建层级
				if (deepPath != null && deepPath.length > 0) {
					for (int j = 0; j < deepPath.length - 2; j++) {
						realValues[j] = new Object[] { realValues[j] };
					}
				}
				_ncObj.setAttributeValue(destPath, realValues[i]);
			}
		}
	}

	// 从mazqa得知 ，目前不支持 公式的参数是数组，并且类型是集合。这种情况下暂时无法批量处理
	private boolean canChangeBatch(AggregatedValueObject[] sourceVOs,
			AggregatedValueObject[] targetVOs, VarryVO varry) {

		for (int j = 0; j < (varry.getVarry() == null ? 0
				: varry.getVarry().length); j++) {
			String var = varry.getVarry()[j];

			for (int start = 0, end = sourceVOs.length; start < end; start++) {
				Object value = getVarValueFromMetadata(sourceVOs[start], var);
				if (value == null) {
					value = getVarValueFromMetadata(targetVOs[start], var);
				}
				if (value instanceof Object[]) {
					if ((Object[]) value != null
							&& ((Object[]) value).length > 1) {
						return false;
					}
				}
			}

		}
		return true;
	}

	private Object getVarValueFromMetadata(AggregatedValueObject sourceVO,
			String srcPath) {
		Object valueObj = null;
		// 如果是系统变量
		if (PfBillMappingUtil.isSystemEnvField(srcPath))
			valueObj = getEnvParamValue(srcPath);
		else {
			NCObject ncObj = NCObject.newInstance(sourceVO);
			valueObj = ncObj.getAttributeValue(srcPath);
			if (valueObj != null && !valueObj.equals("")) {
				if (valueObj instanceof String)
					valueObj = valueObj.toString();
			}
		}
		return valueObj;
	}

	private void execFormulaMetaToMeta2(AggregatedValueObject sourceVO,
			AggregatedValueObject targetVO, VarryVO varry)
			throws BusinessException {
		parseMetaFormulaVar(sourceVO, targetVO, varry);

		String destPath = varry.getFormulaName();
		NCObject ncObj = NCObject.newInstance(targetVO);
		IType iType = ncObj.getRelatedBean().getAttributeByPath(destPath)
				.getDataType();
		Object realValue = null;
		if (isCollectionOfPath(destPath, ncObj.getRelatedBean())
				&& isCollectionOfSrc(varry)) {
			// XXX:取得公式值,返回者可能为空
			Object[] valueObjs = getFormulaParse().getValueO();
			for (int i = 0; i < (valueObjs == null ? 0 : valueObjs.length); i++) {
				valueObjs[i] = getValueByIType(valueObjs[i], iType);
			}
			realValue = valueObjs;
		} else {
			// XXX:取得公式值
			Object valueObj = getFormulaParse().getValueAsObject();
			realValue = getValueByIType(valueObj, iType);
		}

		// 如果出错，则抛出异常
		String errStr = getFormulaParse().getError();
		if (!StringUtil.isEmptyWithTrim(errStr))
			throw new PFBusinessException(errStr);

		// 给目的元数据中的属性赋值
		ncObj.setAttributeValue(destPath, realValue);
	}

	private void parseMetaFormulaVarBatch(AggregatedValueObject[] sourceVOs,
			AggregatedValueObject[] targetVOs, VarryVO varry) {
		Logger.debug("**开始执行元数据公式");

		NCObject[] sourceObjs = new NCObject[sourceVOs.length];
		NCObject[] targetObjs = new NCObject[targetVOs.length];
		for (int index = 0, end = sourceObjs.length; index < end; index++) {
			sourceObjs[index] = NCObject.newInstance(sourceVOs[index]);
		}

		for (int j = 0; j < (varry.getVarry() == null ? 0
				: varry.getVarry().length); j++) {
			String var = varry.getVarry()[j];
			ArrayList<Object> values = new ArrayList<Object>();
			List<Object[]> sourceValues = null;

			if (PfBillMappingUtil.isSystemEnvField(var)) {
				// 系统变量
				Object systemValue = getEnvParamValue(var);
				for (int index = 0, end = sourceObjs.length; index < end; index++) {
					values.add(systemValue);
				}
			} else {
				sourceValues = DASFacade.getAttributeValue(
						new String[] { var }, sourceObjs);

				// 来源取不到属性，则取目的vo的相关属性
				if (sourceValues == null || sourceValues.size() == 0) {
					for (int index = 0, end = targetObjs.length; index < end; index++) {
						targetObjs[index] = NCObject
								.newInstance(targetVOs[index]);
					}
					sourceValues = DASFacade.getAttributeValue(
							new String[] { var }, targetObjs);
				}

				for (int index = 0, end = sourceObjs.length; index < end; index++) {
					if (sourceValues == null)
						// 跳出循环
						break;

					Object[] temp_sourceValue = sourceValues.get(index);
					Object sourceValue = null;
					if (temp_sourceValue != null
							&& temp_sourceValue.length != 0) {
						sourceValue = temp_sourceValue[0];
					}
					if (sourceValue instanceof Object[]) {
						Object temp = null;
						if (sourceValue != null
								&& ((Object[]) sourceValue).length == 1) {
							temp = ((Object[]) sourceValue)[0];
						}
						values.add(temp);
					} else {
						values.add(sourceValue);
					}
				}
			}

			getFormulaParse().addVariable(var, values);

		}
	}

	private boolean isCollectionOfSrc(VarryVO varry) {
		if (varry.getVarry() != null && varry.getVarry().length > 0) {
			for (String var : varry.getVarry()) {
				Object tmpObj = getFormulaParse().getJepParser().getVariables()
						.get(var);
				if (tmpObj instanceof List)
					return true;
			}
		}
		return false;
	}

	private void parseMetaFormulaVar(AggregatedValueObject sourceVO,
			AggregatedValueObject targetVO, VarryVO varry) {
		Logger.debug("**开始执行元数据公式");
		for (int j = 0; j < (varry.getVarry() == null ? 0
				: varry.getVarry().length); j++) {
			String var = varry.getVarry()[j];
			Object value = getVarValueFromMetadata(sourceVO, var);
			// XXX:leijun@2010-2 如果从来源单据VO取不到某个变量值，则从目的单据VO取
			if (value == null)
				value = getVarValueFromMetadata(targetVO, var);

			// 设置变量值到公式解析器
			getFormulaParse().addVariable(var, value);
			Logger.debug("**变量var=" + var + ";赋值value=" + value);
		}
	}

	FormulaParseFather formulaParse = new nc.bs.pub.formulaparse.FormulaParse();

	public FormulaParseFather getFormulaParse() {
		return formulaParse;
	}
}