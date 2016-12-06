package nc.vo.mmgp.util;

import nc.bs.logging.Logger;
import nc.ui.format.NCFormater;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.IAttributeMeta;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.JavaType;
import nc.vo.pub.format.exception.FormatException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

/**
 * 保存的字符的长度和元数据该字段所定义的长度进行比较
 * 
 * @author wangweir
 * 
 */
public class MMGPVOFieldLengthChecker {

  private static final String DOUBLE_DECIMAL_SIGN = ".";

  private static final String MAX_BIT_VALUE = "9";

  private static final String MINUS_SIGN = "-";

  private static final int REF_PK_LENGTH = 20;

  private MMGPVOFieldLengthChecker() {
    super();
  }

  /**
   * 检查主子表VO各个字段的长度
   * 
   * @param bill
   */
  public static void checkVOFieldsLength(IBill bill) {
    if (bill == null) {
      return;
    }

    IBillMeta billMeta = bill.getMetaData();
    ISuperVO headVO = bill.getParent();
    MMGPVOFieldLengthChecker.checkHeadFieldLength(headVO, billMeta.getParent());
    MMGPVOFieldLengthChecker.checkBodyFieldLength(bill, billMeta.getChildren());
  }

  /**
   * 检查主子表VO各个字段的长度
   * 
   * @param bills
   */
  public static void checkVOFieldsLength(IBill[] bills) {
    if (bills == null) {
      return;
    }
    for (IBill bill : bills) {
      MMGPVOFieldLengthChecker.checkVOFieldsLength(bill);
    }
  }

  /**
   * 检查单表VO各个字段的长度
   * 
   * @param childrenVO
   */
  public static void checkVOFieldsLength(ISuperVO[] childrenVO) {
    if (childrenVO == null || childrenVO.length == 0) {
      return;
    }
    IVOMeta childMeta = childrenVO[0].getMetaData();
    IAttributeMeta[] attrMetas =
        childMeta.getStatisticInfo().getPerisistentAttributes();
    for (ISuperVO childVO : childrenVO) {
      MMGPVOFieldLengthChecker.checkAttributeLength(childVO, attrMetas);
    }
  }

  private static void checkAttributeLength(ISuperVO vo,
      IAttributeMeta[] attrMetas) {
    if (vo == null) {
      return;
    }
    for (IAttributeMeta attrMeta : attrMetas) {
      if (attrMeta.getColumn() == null) {
        continue;
      }
      Object value = vo.getAttributeValue(attrMeta.getName());
      if (value == null) {
        continue; 
      }
      if (attrMeta.getJavaType() == JavaType.Integer
          || attrMeta.getJavaType() == JavaType.UFFlag) {
        continue;
      }
      int attrMaxLength = MMGPVOFieldLengthChecker.getAttributeLength(attrMeta);
      if (value.toString().length() > attrMaxLength) {
    	String label=attrMeta.getColumn().getLabel();
        ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0282", null, new String[]{label,String.valueOf(attrMaxLength)})/*【{0}】超出最大长度：{1}，请重新输入*/);
      }
      if (attrMeta.getJavaType() == JavaType.UFDouble
          || attrMeta.getJavaType() == JavaType.BigDecimal) {
        MMGPVOFieldLengthChecker.checkDoubleValue(attrMeta,
            new UFDouble(value.toString()), MMGPVOFieldLengthChecker
                .getAttributeLength(attrMeta), attrMeta.getColumn()
                .getPrecision());
      }
    }
  }

  private static void checkBodyFieldLength(IBill bill, IVOMeta[] childrenMeta) {
    if (childrenMeta == null) {
      return;
    }
    for (IVOMeta childMeta : childrenMeta) {
      IAttributeMeta[] attrMetas =
          childMeta.getStatisticInfo().getPerisistentAttributes();
      ISuperVO[] childrenVO = bill.getChildren(childMeta);
      if (childrenVO == null) {
        continue;
      }
      for (ISuperVO childVO : childrenVO) {
        MMGPVOFieldLengthChecker.checkAttributeLength(childVO, attrMetas);
      }
    }
  }

  /**
   * 检查浮点类型的数值是否在最大值和最小值范围之外
   * 
   * @param attrMeta
   * @param value
   * @param length
   *          总长度
   * @param precision
   *          精度
   */
  private static void checkDoubleValue(IAttributeMeta attrMeta, UFDouble value,
      int length, int precision) {
    if (precision > length - 1) {
    	String label=attrMeta.getColumn().getLabel();
      throw new IllegalArgumentException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0283", null, new String[]{label})/*【{0}】元数据长度定义不正确！*/);
    }
    StringBuffer maxValue = new StringBuffer();
    StringBuffer minValue = new StringBuffer(MMGPVOFieldLengthChecker.MINUS_SIGN);
    for (int i = 0; i < length - precision; i++) {
      maxValue.append(MMGPVOFieldLengthChecker.MAX_BIT_VALUE);
      if (i != 0) {
        minValue.append(MMGPVOFieldLengthChecker.MAX_BIT_VALUE);
      }
    }
    if (precision > 0) {
      maxValue.append(MMGPVOFieldLengthChecker.DOUBLE_DECIMAL_SIGN);
      minValue.append(MMGPVOFieldLengthChecker.DOUBLE_DECIMAL_SIGN);
    }
    for (int i = 0; i < precision; i++) {
      maxValue.append(MMGPVOFieldLengthChecker.MAX_BIT_VALUE);
      minValue.append(MMGPVOFieldLengthChecker.MAX_BIT_VALUE);
    }
    if (value.compareTo(new UFDouble(maxValue.toString())) > 0) {
    	String label=attrMeta.getColumn().getLabel();
    	String formateValue = String.valueOf(maxValue);
    	try {
    		formateValue = NCFormater.formatNumber(maxValue).getValue();
		} catch (FormatException e) {
		}
      ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0284", null, new String[]{label,formateValue})/*【{0}】大于最大值{1}，请重新输入*/);
    }
    if (value.compareTo(new UFDouble(minValue.toString())) < 0) {
    	String label=attrMeta.getColumn().getLabel();
    	
    	String formateValue = String.valueOf(minValue);
    	try {
    		formateValue = NCFormater.formatNumber(minValue).getValue();
		} catch (FormatException e) {
			Logger.error(e.getMessage(), e);
		}
    	
      ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0285", null, new String[]{label,formateValue})/*【{0}】小于最大值{1}，请重新输入*/);
    }
  }

  private static void checkHeadFieldLength(ISuperVO headVO, IVOMeta headMeta) {
    IAttributeMeta[] headAttrMetas =
        headMeta.getStatisticInfo().getPerisistentAttributes();
    if (headAttrMetas == null) {
      return;
    }
    MMGPVOFieldLengthChecker.checkAttributeLength(headVO, headAttrMetas);
  }

  private static int getAttributeLength(IAttributeMeta attrMeta) {
    if (attrMeta.getColumn().getLength() <= 0) {
      if (attrMeta.getReferenceDoc() != null) {
        return MMGPVOFieldLengthChecker.REF_PK_LENGTH;
      }
    }
    return attrMeta.getColumn().getLength();
  }
}
