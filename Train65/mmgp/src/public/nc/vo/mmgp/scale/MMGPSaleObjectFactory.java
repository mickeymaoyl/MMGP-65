package nc.vo.mmgp.scale;

import java.util.HashMap;
import java.util.Map;

import nc.pubitf.uapbd.MeasdocUtil;
import nc.ui.bd.ref.busi.DefaultRoleRefModel;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.scale.ScaleObject;
import nc.vo.pubapp.scale.ScaleObjectFactory;

/**
 * @Description: TODO
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-11-6下午7:59:34
 * @author: tangxya
 */
public class MMGPSaleObjectFactory extends ScaleObjectFactory {

	private Map<String, ScaleObject> paramap = new HashMap<String, ScaleObject>();
	
	private String pk_group;

	/**
	 * @param pk_group
	 */
	public MMGPSaleObjectFactory(String pk_group) {
		super(pk_group);
		this.pk_group=pk_group;
	}

	public ScaleObject getParamVarScaleObject(String param) {
		ScaleObject ret = this.getScaleObject(param);
		if (ret == null) {
			ret = new ParamScaleObject(param,pk_group);
		}
		return ret;
	}
	
	
	public ScaleObject getParamMeasdocVarScaleObject(String param) {
		ScaleObject ret = this.getScaleObject(param);
		if (ret == null) {
			ret = new ParamMeasDocScaleObject(param,pk_group);
		}
		return ret;
	}
	

	private ScaleObject getScaleObject(String key) {
		return this.paramap.get(key);
	}

	public static class ParamScaleObject implements ScaleObject {
		private int defaultdigit = 2;
		private int defaultRoundingMode = 4;
		public ParamScaleObject(String param,String pk_group) {
			initDigit(param,pk_group);
		}

		private void initDigit(String param,String pk_group) {
			// 先由参数查询计量单位pk 暂时省略第一步
			// 查询精度
			if (param == null) {
				return;
			}
			Integer digit = null;
			try {
				digit=nc.pubitf.para.SysInitQuery.getParaInt(pk_group, param);
			} catch (Exception e) {
				ExceptionUtils.wrappException(e);
			}
			if (digit == null ) {
				return;
			}
			defaultdigit = digit;
		}

		@Override
		public int getDigit() {
			return defaultdigit;
		}

		@Override
		public int getRoundingMode() {
			// TODO 自动生成的方法存根
			return defaultRoundingMode;
		}

	}
	
	
	public static class ParamMeasDocScaleObject implements ScaleObject {
		private int defaultdigit = 2;
		private int defaultRoundingMode = 4;
		public ParamMeasDocScaleObject(String param,String pk_group) {
			initDigit(param,pk_group);
		}

		private void initDigit(String param,String pk_group) {
			// 先由参数查询计量单位pk 暂时省略第一步
			// 查询精度
			if (param == null) {
				return;
			}
			Integer[] digits = null;
			try {
				digits = MeasdocUtil.getInstance().getPrecisionByPks(
						new String[] { (String) param });
			} catch (Exception e) {
				ExceptionUtils.wrappException(e);
			}
			if (digits == null || digits.length <= 0 || digits[0] == null) {
				return;
			}
			defaultdigit = digits[0].intValue();
		}

		@Override
		public int getDigit() {
			return defaultdigit;
		}

		@Override
		public int getRoundingMode() {
			// TODO 自动生成的方法存根
			return defaultRoundingMode;
		}

	}

}
