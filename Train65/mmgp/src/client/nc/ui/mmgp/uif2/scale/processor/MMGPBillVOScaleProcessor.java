package nc.ui.mmgp.uif2.scale.processor;

import java.util.List;

import nc.bs.logging.Logger;
import nc.md.MDBaseQueryFacade;
import nc.md.data.access.DASFacade;
import nc.md.data.access.NCObject;
import nc.md.model.IAttribute;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.IAttributeMeta;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.CarrierRuntimeException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.scale.BillScaleData;
import nc.vo.pubapp.scale.BillScaleProcessor;
import nc.vo.pubapp.scale.DefaultBillScaleData;
import nc.vo.pubapp.scale.ExchangeScaleObject;
import nc.vo.pubapp.scale.FieldInfo;
import nc.vo.pubapp.scale.IBillVOScaleProcessor;
import nc.vo.pubapp.scale.IFieldValueGetter;
import nc.vo.pubapp.scale.PosEnum;
import nc.vo.pubapp.scale.ScaleObject;
import nc.vo.pubapp.scale.ScaleSetter;
import nc.vo.pubapp.scale.ScaleUtils;
import nc.vo.pubapp.scale.VarScaleObject;

/**
 * 单据VO的精度处理器 使用方法： ScaleProcessor scale = new
 * BillVOScallProcessor(pk_group,vos); 设置换算率，数量等信息 scale.setHslCtlInfo(new
 * String[] { "hsl", "nquoteunitrate" }, PosEnum.body, null);
 * scale.setPriceCtlInfo(new String[]{"nsaleprice", "ntaxprice", "npprice",
 * "nquoteprice", "nquotentprice"}, PosEnum.body, null);
 * scale.setCostPriceCtlInfo(new String[]{"nprice", "nplannedprice"},
 * PosEnum.body, null); scale.setNumCtlInfo(new String[] { "nshouldinnum",
 * "nshouldoutnum", "nsignnum", "ntranoutnum", "nretnum", "noutnum", "nleftnum",
 * "ninnum", "neconomicnum"}; 处理精度 Scale.process();
 *
 * @author yangb
 *
 */
public class MMGPBillVOScaleProcessor extends BillScaleProcessor implements
		IBillVOScaleProcessor {

	protected static class BillVoFieldValueGetter implements IFieldValueGetter {

		private CircularlyAccessibleValueObject parentVo;

		private CircularlyAccessibleValueObject childVo;

		public CircularlyAccessibleValueObject getChildVo() {
			return this.childVo;
		}

		private CircularlyAccessibleValueObject getParentVo() {
			return this.parentVo;
		}

		@Override
		public Object getValue(FieldInfo fieldInfo) {
			if (PosEnum.valueOf(fieldInfo.getPos()) == PosEnum.body) {
				return this.getChildVo().getAttributeValue(fieldInfo.getItemkey());
			} else {
				return this.getParentVo().getAttributeValue(fieldInfo.getItemkey());
			}
		}

		public void setChildVo(CircularlyAccessibleValueObject childVo) {
			this.childVo = childVo;
		}

		public void setParentVo(CircularlyAccessibleValueObject parentVo) {
			this.parentVo = parentVo;
		}

	}

	public class BillVOScaleData extends DefaultBillScaleData {

		private AggregatedValueObject[] billvos;

		/**
     *
     */
		public BillVOScaleData(AggregatedValueObject[] bills, String[] fields,
				PosEnum pos, String tabcode, String reffield, PosEnum refpos,
				String reftabcode) {
			super(fields, pos, tabcode, reffield, refpos, reftabcode);
			this.billvos = bills;
		}

		public AggregatedValueObject[] getBillvos() {
			return billvos;
		}

		public void setBillvos(AggregatedValueObject[] billvos) {
			this.billvos = billvos;
		}

		public BillVOScaleData(String[] fields, PosEnum pos, String tabcode,
				String reffield, PosEnum refpos, String reftabcode) {
			super(fields, pos, tabcode, reffield, refpos, reftabcode);
		}

		private boolean check() {
			if ((this.billvos == null) || (this.billvos.length <= 0)
					|| (this.getCols() == null)) {
				return false;
			}
			return true;
		}

		@Override
		public void setDataDigit(ScaleObject sc) {
			if ((sc == null) || (this.getRefCol() != null) || !this.check()) {
				return;
			}
			int digit = sc.getDigit();
			for (AggregatedValueObject bill : this.billvos) {
				if (this.getPos() == PosEnum.body) {
					CircularlyAccessibleValueObject[] vos = getChildrenVOs(bill);
					if ((vos == null) || (vos.length <= 0)) {
						continue;
					}
					for (CircularlyAccessibleValueObject vo : vos) {
						this.setVODigit(vo, digit);
					}
				} else {
					this.setVODigit(bill.getParentVO(), digit);
				}
			}
		}

		public CircularlyAccessibleValueObject[] getChildrenVOs(
				AggregatedValueObject bill) {
			CircularlyAccessibleValueObject[] vos = null;
			if (null == this.getTabCode() || this.getTabCode().trim().isEmpty()) {
				vos = bill.getChildrenVO();
			} else {
				if (bill instanceof IBill) {
					vos = getVOsByClazzFromVO((IBill) bill);
				} else {
					vos = bill.getChildrenVO();
				}
			}
			return vos;
		}

		// @SuppressWarnings({ "unchecked", "deprecation" })
		@SuppressWarnings({ "unchecked" })
		private CircularlyAccessibleValueObject[] getVOsByClazzFromVO(IBill bill) {
			CircularlyAccessibleValueObject[] vos = null;
			try {

				// List<IAssociation> associations =
				// entity.getAssociationsByKind(kind, nCardinalityType);
				IAttributeMeta[] childrens = bill.getMetaData()
						.getChildForeignKeys();
				IVOMeta voMeta = null;
				if (childrens.length == 1) {
					voMeta = childrens[0].getVOMeta();
				} else {
					IBean bean;
					try {
						bean = MDBaseQueryFacade.getInstance()
								.getBeanByFullName(
										bill.getMetaData().getParent()
												.getEntityName());
					} catch (MetaDataException e) {
						throw new CarrierRuntimeException(e);
					}
					IBusinessEntity entity = (IBusinessEntity) bean;
					IAttribute attrbute = entity.getAttributeByName(getTabCode());
					if(attrbute == null) {
						throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0025")/*@res "表头VO元数据没有属性:"*/
								+ getTabCode());
					}

					voMeta = bill.getMetaData().getVOMeta((Class<? extends ISuperVO>) Class.forName(attrbute.getAssociation().getEndBean().getFullClassName()));
//					ISuperVO[] superVOs = bill.getChildren();
//					for (IAttributeMeta am : childrens) {
//						System.out.println(am.getColumn().getName());
//						if (am.getName().equals(getTabCode())) {
//							voMeta = am.getVOMeta();
//							break;
//						}
//					}
				}
				if (voMeta == null) {
					throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0026")/*@res "没有tabcode:"*/
							+ getTabCode());
				}
				ISuperVO[] superVOs = ((IBill) bill).getChildren(voMeta);
				if (superVOs instanceof CircularlyAccessibleValueObject[]) {
					vos = (CircularlyAccessibleValueObject[]) superVOs;
				}
			} catch (Exception e) {
				Logger.error(e.getMessage());
//				String tabCode = this.getTabCode();
				ExceptionUtils.wrappException(e);
//				ExceptionUtils.wrappBusinessException(
//						NCLangRes4VoTransl.getNCLangRes().getStrByID(
//								"pubapp_0", "0pubapp-0290", null,
//								new String[] { tabCode })/*
//														 * create Class object
//														 * error,class name ：{0}
//														 */, e);
			}
			return vos;
		}

		@Override
		public void setDataDigit(VarScaleObject vsc) {
			if ((vsc == null) || (this.getRefCol() == null) || !this.check()) {
				return;
			}
			// 汇率精度
			if (vsc instanceof ExchangeScaleObject) {
				for (AggregatedValueObject bill : this.billvos) {
					if (null != bill) {
						getValueGetter().setParentVo(bill.getParentVO());
						if (this.getPos() == PosEnum.body) {
							CircularlyAccessibleValueObject[] childVOs = getChildrenVOs(bill);
							// CircularlyAccessibleValueObject[] childVOs =
							// bill.getChildrenVO();
							if (null != childVOs) {
								for (CircularlyAccessibleValueObject vo : childVOs) {
									getValueGetter().setChildVo(vo);
									this.setVODigit(vo, vsc.getDigit());
								}
							}
						} else {
							// 如果精度设置字段不是表体，则依赖字段肯定不是表体的
							// 所以不用设置表体VO，不会报错，如果报错，肯定是有其他问题
							this.setVODigit(bill.getParentVO(), vsc.getDigit());
						}
					}
				}
			}
			// 非汇率精度
			else {
				for (AggregatedValueObject bill : this.billvos) {
					if (null != bill) {
						if (this.getPos() == PosEnum.body) {
							CircularlyAccessibleValueObject[] vos = getChildrenVOs(bill);
							// CircularlyAccessibleValueObject[] vos =
							// bill.getChildrenVO();
							if ((vos == null) || (vos.length <= 0)) {
								continue;
							}
							int digit = 3;
							for (CircularlyAccessibleValueObject vo : vos) {
								if (this.getRefPos() == PosEnum.body) {
									// TODO

									Object value = DASFacade.getAttributeValue(
											NCObject.newInstance(vo),
											this.getRefCol());
									digit = vsc.getDigit(value);
									this.setVODigit(vo, digit);

								} else {
									CircularlyAccessibleValueObject parentvo = bill
											.getParentVO();
									if (parentvo != null) {
										// TODO
										Object value = DASFacade
												.getAttributeValue(NCObject
														.newInstance(parentvo),
														this.getRefCol());
										digit = vsc.getDigit(value);
										this.setVODigit(vo, digit);
									}

								}
							}
						} else {
							CircularlyAccessibleValueObject parentvo = bill
									.getParentVO();
							if (parentvo == null) {
								continue;
							}
							// TODO
							Object value = DASFacade.getAttributeValue(
									NCObject.newInstance(parentvo),
									this.getRefCol());
							int digit = vsc.getDigit(value);
							this.setVODigit(parentvo, digit);
						}
					}
				}
			}
		}

		private void setVODigit(CircularlyAccessibleValueObject vo, int digit) {
			if (vo == null) {
				return;
			}
			for (String col : this.getCols()) {
				// 是否为换算率字段
				boolean ishtl = false;
				// 根据表头还是表体去区别判断是否为换算率字段
				if (this.getPos() == PosEnum.body) {
					if (getBodyHslCtlInfo() != null) {
						for (String name : getBodyHslCtlInfo()) {
							if (name.equals(col)) {
								ishtl = true;
								break;
							}
						}
					}
				} else {
					if (getHeadHslCtlInfo() != null) {
						for (String name : getHeadHslCtlInfo()) {
							if (name.equals(col)) {
								ishtl = true;
								break;
							}
						}
					}
				}

				if (ishtl) {
					vo.setAttributeValue(
							col,
							adjustHslScale((String) vo.getAttributeValue(col),
									digit));
				} else {
					vo.setAttributeValue(col, ScaleUtils.adjustScale(
							(UFDouble) vo.getAttributeValue(col), digit));
				}
			}
		}
	}

	private AggregatedValueObject[] bills;

	private BillVoFieldValueGetter valueGetter = new BillVoFieldValueGetter();

	// 表头换算率字段名称数组
	private String[] headHslCtlInfo;

	// 表体换算率字段名称数组
	private String[] bodyHslCtlInfo;

	/**
   *
   */
	public MMGPBillVOScaleProcessor(String pk_group,
			AggregatedValueObject[] bills) {
		// TODO Auto-generated constructor stub
		super(pk_group);
		this.bills = bills;
	}

	@Override
	public void setHslCtlInfo(String[] hslkeys, PosEnum pos, String tabcode) {
		this.setCtlInfo(hslkeys, pos, tabcode, null, null, null, this
				.getScaleObjectFactory().getHslScaleObject());
		if (pos == PosEnum.body) {
			this.setBodyHslCtlInfo(hslkeys);
		} else {
			this.setHeadHslCtlInfo(hslkeys);
		}
	}

	@Override
	public void process(AggregatedValueObject[] vos) {

		List<BillScaleData> list = this.ctlinfo.getDatasWithScale();
		for (BillScaleData data : list) {
			if (data instanceof BillVOScaleData)
				((BillVOScaleData) data).setBillvos(vos);
		}
		ScaleSetter st = new ScaleSetter();
		st.setScaleCtl(this.ctlinfo);
		st.processScale(null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * nc.vo.ic.pub.scale.BillScallProcessor#createScaleDataCtlInfo(java.lang.
	 * String[], nc.vo.ic.pub.scale.ScaleProcessor.BillPos, java.lang.String,
	 * nc.vo.ic.pub.scale.ScaleProcessor.BillPos)
	 */
	@Override
	protected BillScaleData createScaleDataCtlInfo(String[] keys, PosEnum pPos,
			String tabcode, String refkey, PosEnum pRefpos, String reftabcode) {
		// TODO Auto-generated method stub
		PosEnum pos = pPos;
		PosEnum refpos = pRefpos;
		if (pos == null) {
			pos = PosEnum.body;
		}
		if ((refkey != null) && (refpos == null)) {
			refpos = PosEnum.body;
		}
		return new BillVOScaleData(this.bills, keys, pos, tabcode, refkey,
				refpos, reftabcode);
	}

	@Override
	protected IFieldValueGetter getFieldValueGetter() {
		return this.valueGetter;
	}

	public BillVoFieldValueGetter getValueGetter() {
		return this.valueGetter;
	}

	public void setValueGetter(BillVoFieldValueGetter valueGetter) {
		this.valueGetter = valueGetter;
	}

	public void setBodyHslCtlInfo(String[] bodyHslCtlInfo) {
		this.bodyHslCtlInfo = bodyHslCtlInfo;
	}

	public String[] getBodyHslCtlInfo() {
		return this.bodyHslCtlInfo;
	}

	public void setHeadHslCtlInfo(String[] headHslCtlInfo) {
		this.headHslCtlInfo = headHslCtlInfo;
	}

	public String[] getHeadHslCtlInfo() {
		return this.headHslCtlInfo;
	}

	public String adjustHslScale(String value, int scale) {
		if (value == null) {
			return null;
		}
		if (value.indexOf("/") >= 0) {
			String[] vs = value.split("/");

			UFDouble a = ScaleUtils.adjustScale(new UFDouble(vs[0]), scale);
			// 单据模板换算率是去零的，保持一致。
			a.setTrimZero(true);
			UFDouble b = ScaleUtils.adjustScale(new UFDouble(vs[1]), scale);
			b.setTrimZero(true);
			return a.toString() + "/" + b;
		}
		UFDouble a = ScaleUtils.adjustScale(new UFDouble(value), scale);
		return a.toString();
	}
}