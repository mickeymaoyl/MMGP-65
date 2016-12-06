package nc.vo.mmgp.util;

import nc.ui.ml.NCLangRes;

public class MMLangUtil {
	public static String getStrByID(String productCode, String resId) {
		return NCLangRes.getInstance().getStrByID(productCode, resId);
	}
	
}
