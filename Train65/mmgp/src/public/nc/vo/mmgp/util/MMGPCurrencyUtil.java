package nc.vo.mmgp.util;

import nc.pubitf.uapbd.CurrencyRateUtil;
import nc.pubitf.uapbd.CurrencyRateUtilHelper;
import nc.vo.bd.currinfo.CurrinfoVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * MMGP汇率精度工具类
 * 
 * @see nc.pubitf.empm.pub.EmpmCurrency
 * @author gaotx
 * 
 */
public class MMGPCurrencyUtil {
	/**
	 * 取组织本位币(组织指全局,集团,业务单元)
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	public static String getOrgLocalCurrPK(String pk_org) throws BusinessException {
		return CurrencyRateUtilHelper.getInstance().getLocalCurrtypeByOrgID(pk_org);
	}

	/**
	 * 查询指定日期的汇率
	 * @param pk_org
	 * @param pk_currtype
	 * @param date
	 * @param rateType
	 * @return
	 * @throws BusinessException
	 */
	public static UFDouble getRate(String pk_org, String pk_currtype, UFDate date, int rateType) throws BusinessException {
		String loc = getOrgLocalCurrPK(pk_org);
		return getRate(pk_org, pk_currtype, loc, date, rateType);
	}

	/**
	 * 查询指定日期的汇率
	 * @param pk_org
	 * @param src_currtype
	 * @param pk_currtype
	 * @param date
	 * @param rateType
	 * @return
	 * @throws BusinessException
	 */
	public static UFDouble getRate(String pk_org, String src_currtype, String pk_currtype, UFDate date, int rateType) throws BusinessException {
		UFDouble rate = null;
		nc.pubitf.uapbd.CurrencyRateUtil exec = getExcutor(pk_org);
		if (src_currtype.equals(pk_currtype)) {
			rate = new UFDouble(1.00d);
		} else {
			rate = exec.getRate(src_currtype, pk_currtype, date, rateType);
		}
		return rate;
	}

	public static CurrencyRateUtil getExcutor(String pk_org) {
		return CurrencyRateUtil.getInstanceByOrg(pk_org);
	}

	/**
	 * <br>
	 * 功能说明：
	 * <p>
	 * 获取汇率精度
	 * </p>
	 * 
	 * @param pk_corp
	 * @param pk_currtype
	 * @param oppCurrtype
	 * @return
	 */
	public static int getRateDigit(String pk_corp, String pk_currtype, String oppCurrtype) throws BusinessException {

		int digit = 0;
		if (pk_currtype != null && pk_currtype.equals(oppCurrtype))
			return 0;

		CurrencyRateUtil rateutil = getExcutor(pk_corp);
		if (rateutil == null) {
			digit = 5; // throw new BusinessException("没有设置汇率！");
		} else {
			CurrinfoVO infovo = rateutil.getCurrinfoVO(pk_currtype, oppCurrtype);
			if (infovo == null) {
				digit = 5; // throw new BusinessException("没有设置汇率！");
			} else {
				Integer idigit = infovo.getRatedigit();
				if (idigit == null)
					digit = 5; // throw new BusinessException("没有设置汇率精度！");
				else
					digit = idigit.intValue();
			}
		}
		return digit;
	}

}
