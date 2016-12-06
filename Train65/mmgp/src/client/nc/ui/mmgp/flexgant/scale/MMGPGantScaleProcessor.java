package nc.ui.mmgp.flexgant.scale;

import nc.ui.mmgp.flexgant.listener.ITreeTableModelDecimalListener;
import nc.ui.mmgp.flexgant.model.MMGPGantChartModel;
import nc.ui.mmgp.flexgant.treetable.MMGPGantItem;
import nc.ui.pubapp.gantt.model.AppGanttChartNode;
import nc.ui.pubapp.scale.BizDecimalParaVO;
import nc.vo.mmgp.scale.MMGPSaleObjectFactory;
import nc.vo.pubapp.scale.DefaultTableScaleData;
import nc.vo.pubapp.scale.ExchangeScaleObject;
import nc.vo.pubapp.scale.ScaleObject;
import nc.vo.pubapp.scale.ScaleSetter;
import nc.vo.pubapp.scale.TableScaleCtl;
import nc.vo.pubapp.scale.TableScaleData;
import nc.vo.pubapp.scale.VarScaleObject;

/**
 *               <p>
 *               ��ϸ��������
 *               </p>
 * @data:2014-5-29����4:13:24
 * @author: tangxya
 */
public class MMGPGantScaleProcessor {

	protected TableScaleCtl<TableScaleData> ctlinfo = new TableScaleCtl<TableScaleData>();

	private String pk_group;

	private MMGPSaleObjectFactory sofc;

	public MMGPGantChartModel chartModel;

	/**
	   * 
	   */
	public MMGPGantScaleProcessor(String pk_group, MMGPGantChartModel chartModel) {
		this.pk_group = pk_group;
		this.chartModel = chartModel;
	}

	public MMGPGantScaleProcessor(MMGPSaleObjectFactory f) {
		this.sofc = f;
	}

	public static class GantTableScaleData extends DefaultTableScaleData {

		protected MMGPGantChartModel chartModel;

		/**
     * 
     */
		public GantTableScaleData(MMGPGantChartModel chartModel,
				String[] fields, String reffield) {
			super(fields, reffield);
			this.chartModel = chartModel;
		}

		@Override
		public void setDataDigit(ScaleObject src) {
			if ((src == null) || (chartModel == null)
					|| (this.getCols() == null)) {
				return;
			}
			MMGPGantItem bi = null;
			int digit = src.getDigit();
			for (String col : this.getCols()) {
				bi = chartModel.getItemByKey(col);
				if (bi == null) {
					continue;
				}
				bi.setDecimalDigits(digit);
			}
		}

		@Override
		public void setDataDigit(VarScaleObject src) {
			/*
			 * if (src == null || this.chartModel == null || this.getCols() ==
			 * null || this.getRefCol() == null) { return; }
			 */
		}

	}

	public void process() {
		ScaleSetter st = new ScaleSetter();
		st.setScaleCtl(this.ctlinfo);
		st.processScale(null);

	}

	public MMGPSaleObjectFactory getScaleObjectFactory() {
		if (this.sofc == null) {
			this.sofc = new MMGPSaleObjectFactory(this.pk_group);
		}
		return this.sofc;
	}

	public void setNumCtlInfo(String[] numkeys, String unitkey) {
		this.setCtlInfo(numkeys, unitkey, this.getScaleObjectFactory()
				.getNumVarScaleObject());
	}

	/**
	 * 
	 * @Description:ҵ������ڵ� ���þ��Ȳ���
	 * @param: @param numkeys ���������ƾ��ȵ��ֶ�
	 * @param: @param paramkey��ҵ����� ���õľ������ ��BD305��
	 * @return:void
	 * @author: tangxya
	 * @data:2014-11-6����7:51:24
	 * 
	 */
	public void setParamCtlInfo(String[] paramkeys, String paramkey) {
		this.setCtlInfo(paramkeys, null, this.getScaleObjectFactory()
				.getParamVarScaleObject(paramkey));
	}
	
	/**
	 * @Description:ҵ������ڵ� ���õļ�����λ����ʱֱ�Ӵ�������λPK
	 * @param: @param paramkeys:�������ƾ��ȵ��ֶ�
	 * @param: @param paramkey:ҵ����� ���õľ������ ��BD305��
	 * @return:void
	 * @author: tangxya
	 * @data:2014-11-11����2:00:36
	 *
	 */
	public void setParamMeasdocCtlInfo(String[] paramkeys, String paramkey) {
		this.setCtlInfo(paramkeys, null, this.getScaleObjectFactory()
				.getParamMeasdocVarScaleObject(paramkey));
	}

	/*
	 * protected abstract TableScaleData createScaleDataCtlInfo(String[] keys,
	 * String refkey);
	 */

	protected void setCtlInfo(String[] keys, final String refkey,
			ScaleObject so) {
		if (keys == null || keys.length <= 0 || so == null) {
			return;
		}
		// ֻ����Ҫ���ò����Ĳ���Ҫ�Ӽ���
		if (so instanceof VarScaleObject) {
			if (so instanceof ExchangeScaleObject) {
				final ExchangeScaleObject ocrso = (ExchangeScaleObject) so;

				this.chartModel
						.addDecimalListener(new ITreeTableModelDecimalListener() {
							@Override
							public int getDecimalFromSource(
									AppGanttChartNode node, Object okValue) {
								return ocrso.getDigit();
							}

							@Override
							public String getSource() {
								return refkey;
							}

							@Override
							public boolean isTarget(MMGPGantItem item) {
								return item.getKey().equals(
										ocrso.getExchange().getItemkey());
							}

							@Override
							public String[] getTarget() {
								return null;
							}
						});
			} else {
				BizDecimalParaVO paraVO = new BizDecimalParaVO();
				paraVO.setVso((VarScaleObject) so);
				paraVO.setSrc(refkey);
				paraVO.setTargets(keys);
				new MMGPGantDecimaListener(
						paraVO, this.chartModel);
			}
			final VarScaleObject fso = (VarScaleObject)so;
			final String[] fkeys = keys;
			this.chartModel
					.addDecimalListener(new ITreeTableModelDecimalListener() {
						@Override
						public int getDecimalFromSource(AppGanttChartNode node,
								Object okValue) {
							return fso.getDigit(okValue);
						}

						@Override
						public String getSource() {
							return refkey;
						}

						@Override
						public boolean isTarget(MMGPGantItem item) {
							return false;
						}

						@Override
						public String[] getTarget() {
							return fkeys;
						}
					});
		}		
		// �����ȶ����뾫�����ݹ�ϵ���뵽��������
		this.addCtlInfo(keys, refkey, so);
	}

	protected void addCtlInfo(String[] keys, final String refkey, ScaleObject so) {
		this.ctlinfo.addCtlInfo(createScaleDataCtlInfo(keys, refkey), so);
	}

	protected GantTableScaleData createScaleDataCtlInfo(String[] keys,
			String refkey) {
		return new GantTableScaleData(this.chartModel, keys, refkey);
	}
}
