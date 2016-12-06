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
 * ������VOӳ��֧���� <br>
 * ֧�� ��<br>
 * 1. ��������������ӳ��<br>
 * 2. ����������ӵ�ӳ�� <br>
 * 3. ӳ�����֧�ֹ�ʽ����ֵ��ӳ�䣬����֧�ָ�ֵΪĿ�Ľ������͵�ϵͳ���� "DESTTRANSTYPE"
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
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0068")/*@res "���ת���������⣺"*/ + e.getMessage());
		}
		return destVOs;
	}

	/**
	 * ��ȡӳ�����
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
					Logger.error("��֧�ִ��ֵַ�����");
					return;
				}
			}
		}
		ArrayList<String[]> aRules = new ArrayList<String[]>();
		ArrayList<String[]> mRules = new ArrayList<String[]>();
		ArrayList<String> fRules = new ArrayList<String>();
		HashMap<String, String> destToSrcMap = new HashMap<String, String>();
		for (ExchangeRuleVO ruleVO : splitItemVOCon) {
			// 1. trim����
			ruleVO.setDest_attr(ruleVO.getDest_attr().trim());
			ruleVO.setRuleData(ruleVO.getRuleData().trim());
			String[] tags = ruleVO.getDest_attr().split("\\.");
			if (tags != null) {
				if (tags.length == 3 || tags.length == 2) {
					// ���ӳ�����
					if (ruleVO.getRuleType() == RuleTypeEnum.ASSIGN.toInt()) {
						aRules.add(new String[] { ruleVO.getDest_attr(),
								ruleVO.getRuleData() });
					} else if (ruleVO.getRuleType() == RuleTypeEnum.MOVE
							.toInt()) {
						// �����ɹ���־.Ŀǰ��֧������ӳ��
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
										Logger.error("���ӳ������Ѵ���,����"
												+ ruleVO.getDest_attr() + "->"
												+ ruleVO.getRuleData()
												+ " ��ӳ���ϵ "
												+ grandSrcTableOriginal + "->"
												+ grandDestTable + "������");
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
					Logger.error("��֧������ӳ��,����" + ruleVO.getDest_attr()
							+ " ������Ҫ��");
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
			// ���ӳ�����
			if (desSplitPath.length == 2) {
				CircularlyAccessibleValueObject[] tableVOs = null;
				if (destBillVO instanceof IExAggVO) {
					// ���ӱ�
					IExAggVO exDestBillVO = (IExAggVO) destBillVO;
					tableVOs = exDestBillVO.getTableVO(desSplitPath[0]);
				} else if (destChildAttrs.length > 0) {
					// ���ӱ�
					tableVOs = destBillVO.getChildrenVO();
				}
				if (tableVOs != null && tableVOs.length > 0) {
					if (srcSplitPath.length == 1) {
						// TODO ӳ���ӱ�

					} else if (srcSplitPath.length == 2) {
						// ӳ�����
						CircularlyAccessibleValueObject[] srcChildVOs = null;
						if (srcBillVO instanceof IExAggVO) {
							srcChildVOs = ((IExAggVO) srcBillVO)
									.getTableVO(srcSplitPath[0]);
						} else {
							srcChildVOs = srcBillVO.getChildrenVO();
						}
						if (srcChildVOs == null
								|| srcChildVOs.length != tableVOs.length) {
							Logger.error("���ӳ�����ʱ���ӱ�ӳ�����Ŀ��һ��."
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
									Logger.error("Ԫ���ݴ���:����"
											+ destBillOrTranstype + "�޷��ҵ�����Ϊ"
											+ desSplitPath[0] + "���ֶ�");
								} else {
									IBusinessEntity destGrandBE = PfMetadataTools
											.getBusinessEntityOfAttr(destChildBE
													.getAttributeByName(desSplitPath[1]));
									if (destGrandBE == null) {
										Logger.error("Ԫ���ݴ���:����"
												+ destBillOrTranstype + "����Ϊ"
												+ desSplitPath[0] + "���ӱ������ֶ�"
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
			// ���ӳ���ӱ�
			else if (desSplitPath.length == 1) {
				if (srcSplitPath.length == 1) {
					// TODO ӳ���ӱ�

				} else if (srcSplitPath.length == 2) {
					// ӳ�����
					CircularlyAccessibleValueObject[] srcChildVOs = null;
					if (srcBillVO instanceof IExAggVO) {
						srcChildVOs = ((IExAggVO) srcBillVO)
								.getTableVO(srcSplitPath[0]);
					} else {
						srcChildVOs = srcBillVO.getChildrenVO();
					}
					if (srcChildVOs == null || srcChildVOs.length != 1) {
						Logger.error("���ӳ���ӱ�ʱ���ӱ�ӳ�����Ŀ��һ��."
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
							Logger.error("Ԫ���ݴ���:����" + destBillOrTranstype
									+ "�޷��ҵ�����Ϊ" + desSplitPath[0] + "���ֶ�");
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
			Logger.debug(" ִ�и�ֵ��" + sAssigns.get(i)[0] + "->"
					+ sAssigns.get(i)[1]);
			Object valueObj = sAssigns.get(i)[1];
			// �����ϵͳ����
			if (PfBillMappingUtil.isSystemEnvField(sAssigns.get(i)[1])) {
				valueObj = getEnvParamValue(sAssigns.get(i)[1]);
			}

			// ���Ŀ��Path��Ӧ��Ԫ�������ԣ����������������͸��丳ֵ
			IAttribute attr = ncObj.getRelatedBean().getAttributeByPath(
					sAssigns.get(i)[0]);
			if (attr == null) {
				Logger.error("ȡ·����������VO����·�������ã�ʵ�壺"
						+ ncObj.getRelatedBean().getDisplayName()
						+ "###���������·������" + sAssigns.get(i)[0]);
				continue;
			}
			ncObj.setAttributeValue(sAssigns.get(i)[0],
					getValueByIType(valueObj, attr.getDataType()));
		}
	}

	/**
	 * ��Ԫ���ݵ�����ֵת��Ϊָ��������
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

	// ��ǰϵͳ����
	private String m_strDate = new UFDate().toString();
	// ��ǰҵ������
	private UFDate m_strBuziDate = new UFDate(InvocationInfoProxy.getInstance()
			.getBizDateTime());
	// ��ǰ��½����ԱPK
	private String m_strOperator = InvocationInfoProxy.getInstance()
			.getUserId();
	// ��ǰ��½����
	private String m_strGroup = InvocationInfoProxy.getInstance().getGroupId();
	// ��ǰϵͳʱ��
	private String m_strNowTime = new UFDateTime(new Date()).toString();
	// ��ǰҵ������
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
			// TODO ���ﲻ֧��transtype
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
		// key-value Ŀ������--��Դ����
		HashMap<String, String> noSysEnvPathsMap = new HashMap<String, String>();
		HashMap<String, String> sysEnvPathsMap = new HashMap<String, String>();

		for (int i = 0; i < mRules.size(); i++) {
			if (PfBillMappingUtil.isSystemEnvField(mRules.get(i)[1])) {
				// ϵͳ����
				sysEnvPathsMap.put(mRules.get(i)[0], mRules.get(i)[1]);
			} else {
				// ����������
				noSysEnvPathsMap.put(mRules.get(i)[0], mRules.get(i)[1]);
			}
		}

		// ϵͳ����
		Set<String> syskeys = sysEnvPathsMap.keySet();
		for (String name : syskeys) {
			String srcPath = sysEnvPathsMap.get(name);
			// ȡ����Դ���Ե�ֵ
			Object valueObj = getEnvParamValue(srcPath);
			for (NCObject targetNcObj : targetNcObjs) {
				// ȡ��Ŀ�����Ե�����
				IType iType = targetNcObj.getRelatedBean()
						.getAttributeByPath(name).getDataType();
				if (valueObj == null)
					continue;
				doMeta2Meta(targetNcObj, valueObj, iType, name);
			}
		}

		// ��ϵͳ����,ִ��������
		String[] destAttributes = noSysEnvPathsMap.keySet().toArray(
				new String[0]);
		ArrayList<String> srcAttributes = new ArrayList<String>();
		for (int start = 0, end = destAttributes.length; start < end; start++) {
			srcAttributes.add(noSysEnvPathsMap.get(destAttributes[start]));
		}
		// ���������Դ���Ե�ֵ
		List<Object[]> varValueArr = DASFacade.getAttributeValue(
				srcAttributes.toArray(new String[0]), sourceNcObjs);

		for (int start = 0, end = targetNcObjs == null ? 0
				: targetNcObjs.length; start < end; start++) {
			Object[] varValues = varValueArr.get(start);

			for (int j = 0, k = destAttributes.length; j < k; j++) {
				IType iType = targetNcObjs[start].getRelatedBean()
						.getAttributeByPath(destAttributes[j]).getDataType();
				// ��Դ���Ե�ֵ
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
			// �����Դ����ֵΪ���飬����Դ����Ϊ�ӱ�����
			int len = Array.getLength(valueObj);
			Object[] objArray = new Object[len];
			for (int j = 0; j < len; j++) {
				// �������е�ÿһ��Ԫ�ؽ�������ת����ת����Ŀ�����Ե�����
				objArray[j] = getValueByIType(Array.get(valueObj, j), iType);
			}
			if (isCollectionOfPath(name, ncObj.getRelatedBean()))
				// ���Ŀ������Ҳ�Ǽ������ͣ������鸳����
				ncObj.setAttributeValue(name, objArray);
			else
				// ���Ŀ�����Բ��Ǽ������ͣ���ȡ����ĵ�һ��Ԫ�ظ�����ֵ
				ncObj.setAttributeValue(name, Array.get(objArray, 0));

		} else {
			// ��Դ����ֵ�������飬��ֱ�Ӱ�ֵ����Ŀ������
			ncObj.setAttributeValue(name, getValueByIType(valueObj, iType));
		}
	}

	/**
	 * �ж�ĳpath��Ӧ���������� �Ƿ���ڼ�������
	 *
	 * @param strPath
	 *            ����a.b, details.c��
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
	 * ����ִ��
	 *
	 * @throws BusinessException
	 * */
	private void execFormulasMetaToMetaBatch(AggregatedValueObject[] sourceVOs,
			AggregatedValueObject[] targetVOs, ArrayList<String> formulas)
			throws BusinessException {
		if (formulas == null || formulas.size() == 0)
			return;
		// ִ�й�ʽ����
		for (int i = 0; i < formulas.size(); i++) {
			Logger.debug("**ִ�й�ʽ����:" + formulas.get(i));
			// ���ñ��ʽ
			getFormulaParse().setExpress(formulas.get(i));
			// ��ñ���
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
			// XXX:ȡ�ù�ʽֵ
			Object[] valueObjs = getFormulaParse().getValueO();

			realValues = new Object[targetVOs.length];

			for (int i = 0; i < targetVOs.length; i++) {
				// ***��intת��ΪString�Ĺ�ʽ������ȥ������Դ�Ĺ�ʽ��varry.getVarry().length==0,��ʱ���صĽ�����鳤��ֻΪ1
				if (valueObjs.length == 1
						&& (varry.getVarry() == null || varry.getVarry().length == 0)) {
					realValues[i] = getValueByIType(valueObjs[0], iType);
				} else {
					realValues[i] = getValueByIType(valueObjs[i], iType);
				}
			}

			// ����������׳��쳣
			String errStr = getFormulaParse().getError();
			if (!StringUtil.isEmptyWithTrim(errStr))
				throw new PFBusinessException(errStr);

			// XXX��Ŀ��Ԫ�����е����Ը�ֵ ��dingxm�ṩ�������������޸Ĵ˴�
			for (int i = 0, end = targetVOs.length; i < end; i++) {
				NCObject _ncObj = NCObject.newInstance(targetVOs[i]);
				String[] deepPath = destPath.split("\\.");
				// Liangjie : ִ�й�ʽ�����ǲ����㼶�ģ�������Ҫ�ٴ����㼶
				if (deepPath != null && deepPath.length > 0) {
					for (int j = 0; j < deepPath.length - 2; j++) {
						realValues[j] = new Object[] { realValues[j] };
					}
				}
				_ncObj.setAttributeValue(destPath, realValues[i]);
			}
		}
	}

	// ��mazqa��֪ ��Ŀǰ��֧�� ��ʽ�Ĳ��������飬���������Ǽ��ϡ������������ʱ�޷���������
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
		// �����ϵͳ����
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
			// XXX:ȡ�ù�ʽֵ,�����߿���Ϊ��
			Object[] valueObjs = getFormulaParse().getValueO();
			for (int i = 0; i < (valueObjs == null ? 0 : valueObjs.length); i++) {
				valueObjs[i] = getValueByIType(valueObjs[i], iType);
			}
			realValue = valueObjs;
		} else {
			// XXX:ȡ�ù�ʽֵ
			Object valueObj = getFormulaParse().getValueAsObject();
			realValue = getValueByIType(valueObj, iType);
		}

		// ����������׳��쳣
		String errStr = getFormulaParse().getError();
		if (!StringUtil.isEmptyWithTrim(errStr))
			throw new PFBusinessException(errStr);

		// ��Ŀ��Ԫ�����е����Ը�ֵ
		ncObj.setAttributeValue(destPath, realValue);
	}

	private void parseMetaFormulaVarBatch(AggregatedValueObject[] sourceVOs,
			AggregatedValueObject[] targetVOs, VarryVO varry) {
		Logger.debug("**��ʼִ��Ԫ���ݹ�ʽ");

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
				// ϵͳ����
				Object systemValue = getEnvParamValue(var);
				for (int index = 0, end = sourceObjs.length; index < end; index++) {
					values.add(systemValue);
				}
			} else {
				sourceValues = DASFacade.getAttributeValue(
						new String[] { var }, sourceObjs);

				// ��Դȡ�������ԣ���ȡĿ��vo���������
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
						// ����ѭ��
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
		Logger.debug("**��ʼִ��Ԫ���ݹ�ʽ");
		for (int j = 0; j < (varry.getVarry() == null ? 0
				: varry.getVarry().length); j++) {
			String var = varry.getVarry()[j];
			Object value = getVarValueFromMetadata(sourceVO, var);
			// XXX:leijun@2010-2 �������Դ����VOȡ����ĳ������ֵ�����Ŀ�ĵ���VOȡ
			if (value == null)
				value = getVarValueFromMetadata(targetVO, var);

			// ���ñ���ֵ����ʽ������
			getFormulaParse().addVariable(var, value);
			Logger.debug("**����var=" + var + ";��ֵvalue=" + value);
		}
	}

	FormulaParseFather formulaParse = new nc.bs.pub.formulaparse.FormulaParse();

	public FormulaParseFather getFormulaParse() {
		return formulaParse;
	}
}