package nc.ui.mmgp.uif2.scale;

import nc.pubitf.uapbd.CurrencyRateUtil;
import nc.vo.bd.currinfo.CurrinfoVO;
import nc.vo.mmgp.util.MMGPCurrencyUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.scale.ExchangeScaleObject;
import nc.vo.pubapp.scale.IEditStatus;
import nc.vo.pubapp.scale.IFieldValueGetter;
import nc.vo.pubapp.scale.ISetValueListener;
import nc.vo.pubapp.scale.OrgExchangeScaleObject;
import nc.vo.pubapp.util.ScaleUitls;

import org.apache.commons.lang.StringUtils;

/**
 * 组织折本汇率精度对象
 * 
 * @author wangfan3
 * 
 */
public class MMGPOrgExchangeScaleObject extends OrgExchangeScaleObject
		implements ExchangeScaleObject, ISetValueListener, IEditStatus {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4521772080140491849L;

	public MMGPOrgExchangeScaleObject(IFieldValueGetter valueGetter) {
		super(valueGetter);
	}

	@Override
	public int getDigit() {
		try {
			String pkorg = "";
			String pkSrcCurr = "";
			String pkDestCurr = "";
			// TODO 从vo取数据
			if (null != this.getVos()
					&& (this.getEditStatus() == IEditStatus.FORM_VO || this
							.getEditStatus() == IEditStatus.FORM_VO_ONCE)) {// 优先从vo取所需的参数
				pkorg = ScaleUitls.getValueFromVO(this.getVos(), getRow(),
						this.getOrg());
				pkSrcCurr = ScaleUitls.getValueFromVO(this.getVos(), getRow(),
						this.getSrcCurr());
				if (this.getDestCurr() != null) {
					pkDestCurr = ScaleUitls.getValueFromVO(this.getVos(),
							getRow(), this.getDestCurr());
				} else {
					pkDestCurr = MMGPCurrencyUtil.getOrgLocalCurrPK(pkorg);
				}
			} else {
				pkorg = (String) this.getValueGetter().getValue(this.getOrg());
				pkSrcCurr = (String) this.getValueGetter().getValue(
						this.getSrcCurr());
				if (this.getDestCurr() != null) {
					pkDestCurr = (String) this.getValueGetter().getValue(
							this.getDestCurr());
				} else {
					pkDestCurr = MMGPCurrencyUtil.getOrgLocalCurrPK(pkorg);
				}

			}
			if (StringUtils.isBlank(pkorg) || StringUtils.isBlank(pkSrcCurr)
					|| StringUtils.isBlank(pkDestCurr)) {
				return 8;
			}
			if (pkSrcCurr.equals(pkDestCurr)) {
				return 0;
			}
			CurrinfoVO ru = CurrencyRateUtil.getInstanceByOrg(pkorg)
					.getCurrinfoVO(pkSrcCurr, pkDestCurr);
			return ru == null ? 8 : ru.getRatedigit().intValue();
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		// 不会运行到这里
		return -1;
	}

}
