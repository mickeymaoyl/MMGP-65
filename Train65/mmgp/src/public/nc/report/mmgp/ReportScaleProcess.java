package nc.report.mmgp;

import java.io.Serializable;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import nc.impl.pubapp.env.BSContext;
import nc.itf.iufo.freereport.extend.IBusiFormat;
import nc.itf.iufo.freereport.extend.RowDataUtil;
import nc.pub.smart.data.IRowData;
import nc.vo.pubapp.scale.ExchangeScaleObject;
import nc.vo.pubapp.scale.FieldInfo;
import nc.vo.pubapp.scale.GroupExchangeScaleObject;
import nc.vo.pubapp.scale.OrgExchangeScaleObject;
import nc.vo.pubapp.scale.ReportFieldValueGetter;
import nc.vo.pubapp.scale.ScaleObject;
import nc.vo.pubapp.scale.ScaleObjectFactory;
import nc.vo.pubapp.scale.ScaleObjectFactory.NumScaleObject;
import nc.vo.pubapp.scale.VarScaleObject;

import org.apache.commons.lang.StringUtils;

import com.ufida.report.anareport.model.CountField;
import com.ufida.report.anareport.util.AnaReportFieldUtil;
import com.ufsoft.table.format.IFormat;
import com.ufsoft.table.format.TableConstant;

public class ReportScaleProcess implements IBusiFormat, Serializable {

  public interface IGetReportDataDigit {

    public int getDigit(IRowData rowData);
  }

  public static class ScaleInfo implements IGetReportDataDigit, Serializable {

    private static final long serialVersionUID = -8038727387593902144L;

    private String paramKey;

    private ScaleObject scaleObject;

    public ScaleInfo() {
      super();
    }

    public ScaleInfo(String paramKey, ScaleObject scaleObject) {
      super();
      if (StringUtils.isEmpty(paramKey)) {
        this.paramKey = paramKey;
      }
      else {
        this.paramKey = paramKey.trim();
      }
      this.scaleObject = scaleObject;
    }

    @Override
    public int getDigit(IRowData rowData) {

      int digit = ReportScaleProcess.DEFAULTDIGITS;
      if (this.scaleObject instanceof ExchangeScaleObject) {
        ((ReportFieldValueGetter) ((ExchangeScaleObject) this.scaleObject)
            .getValueGetter()).setRowData(rowData);
        digit = this.scaleObject.getDigit();
      }
      else if (this.scaleObject instanceof VarScaleObject) {
        Object obj = rowData.getData(this.paramKey);
        if (null != obj) {
          digit = ((VarScaleObject) this.scaleObject).getDigit(obj);
        }
        else {
          digit = this.scaleObject.getDigit();
        }
      }
      else {
        digit = this.scaleObject.getDigit();
      }
      return digit;
    }

    public String getParamKey() {
      return this.paramKey;
    }

    public ScaleObject getScaleObject() {
      return this.scaleObject;
    }

    public void setParamKey(String paramKey) {
      this.paramKey = paramKey;
    }

    public void setScaleObject(ScaleObject scaleObject) {
      this.scaleObject = scaleObject;
    }
  }

  public static final int DEFAULTDIGITS = -1;

  private static final long serialVersionUID = 1L;

  // 缓存精度和对应的应用字段信息
  private Map<String, ScaleInfo> keyScaleInfoMap =
      new HashMap<String, ScaleInfo>();

  // 保存列的最大精度
  private Map<String, Integer> maxScaleMap = new HashMap<String, Integer>();

  // 精度对象工厂
  private ScaleObjectFactory scaleFactory = new ScaleObjectFactory(BSContext
      .getInstance().getGroupID());

  // 缓存精度数据
  private Map<String, Integer> scaleMap = new HashMap<String, Integer>();

  @Override
  public int getColumnDigital(String fldName, CountField countField) {
    return TableConstant.UNDEFINED;
  }

  @Override
  public IFormat getColumnFormat(String fldName, CountField countField,
      IFormat oldFormat) {
    return null;
  }

  @Override
  public int getDataDigital(String fldName, CountField countField,
      IRowData rowData) {
    int digit = ReportScaleProcess.DEFAULTDIGITS;
    String fieldNameLowercase = null == fldName ? null : fldName.toLowerCase();
    if (null != countField) {// 合计行或者小计行
      // if (null != countField) {
      String rangeFld = countField.getRangeFld();
      if (null == rangeFld) {// 合计行
        Integer maxDigit = this.maxScaleMap.get(fieldNameLowercase);
        if (null != maxDigit) {// 注册为合计行
          return maxDigit.intValue();
        }
        return ReportScaleProcess.DEFAULTDIGITS;
      }
      else {// 小计行
        Integer maxDigit = this.maxScaleMap.get(fieldNameLowercase);
        if (null != maxDigit) {// 注册为小计行
          return maxDigit.intValue();
        }
        return ReportScaleProcess.DEFAULTDIGITS;
      }
      // }
    }
    else if (null != rowData) {// 明细行
      // 用于支持交叉指标合计字段名称转义，不影响其它字段 增加2行 jinjya 2011-10-08 09:36:53
      String fldDbName = AnaReportFieldUtil.getFieldNameInRowData(rowData, fldName);
      fieldNameLowercase = null == fldDbName ? null : fldDbName.toLowerCase();
      rowData.getData(fldDbName);
      if (!RowDataUtil.hasField(rowData, fldDbName)) {// 如果结果集里没有对应列
        return ReportScaleProcess.DEFAULTDIGITS;
      }
      if (!this.keyScaleInfoMap.containsKey(fieldNameLowercase)) {// 该字段没有注册成需要处理精度的字段
        return TableConstant.UNDEFINED;
      }
      Object value = rowData.getData(fldDbName);
      if (this.isEmpty(value)) {// 该字段所在的单元格的值为空,直接返回默认精度
        return ReportScaleProcess.DEFAULTDIGITS;
      }
      ScaleInfo si = this.keyScaleInfoMap.get(fieldNameLowercase);
      // 先从内存缓存取精度,如果没有就重新查询
      Object valueFromRowData =
          si.getParamKey() != null ? this.getValueFromRowData(si.getParamKey(),
              rowData) : "";

      if (null != si.getParamKey()
          && null == this.getValueFromRowData(si.getParamKey().trim(), rowData)) {// 引用字段所在的单元格没有数据
        if(si.getScaleObject()instanceof NumScaleObject){
         //数量类型比较特殊。存货分类和存货同时存在时没有计量单位时。需要取8位精度
          return 8;
        }else{
          return ReportScaleProcess.DEFAULTDIGITS;
        }
      }
      String subPram =
          valueFromRowData == null ? "" : valueFromRowData.toString();
      Integer tempDigital = null;
      if (si.getScaleObject() instanceof VarScaleObject) {

        tempDigital = this.scaleMap.get(fldDbName + subPram);
      }
      else {
        tempDigital = this.scaleMap.get(fldDbName);
      }
      if (null != tempDigital) {
        this.saveMaxScale(fieldNameLowercase, tempDigital);
        return tempDigital.intValue();
      }
      // 重新查询
      digit = si.getDigit(rowData);
      this.saveMaxScale(fieldNameLowercase, Integer.valueOf(digit));
      if (si.getScaleObject() instanceof VarScaleObject) {// 动态精度
        // 使用(精度对象类名+引用字段的值)作为key缓存精度
        this.scaleMap.put(fldDbName + subPram, Integer.valueOf(digit));
      }
      else {// 静态精度
        // 使用(精度对象类名)作为key缓存精度
        this.scaleMap.put(fldDbName, Integer.valueOf(digit));
      }

    }
    return digit;

  }

  @Override
  public IFormat getDataFormat(String fldName, CountField countField,
      IRowData rowData, IFormat oldFormat) {
    return null;
  }

  @Override
  public int getFormatType(String fldName, CountField countField) {
    return IBusiFormat.BUSIFORMAT_TYPE_ROW;
  }

  /**
   * 设置字段为常量精度
   * 
   * @param fieldKey
   *          需要设精度的字段
   * @param digit
   *          精度位数
   */
  public void setConstantDigits(String[] fieldKey, final int digit) {
    ScaleObject so = new ScaleObject() {

      private static final long serialVersionUID = 1L;

      @Override
      public int getDigit() {
        return digit;
      }

		@Override
		public int getRoundingMode() {
			// TODO 自动生成的方法存根
			return 4;
		}
    };
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(fieldKey, scaleInfo);
  }

  /**
   * 设置换算率精度
   * 
   * @param convertRateStr
   */
  public void setConvertRateDigits(String[] convertRateStr) {
    ScaleObject so = this.scaleFactory.getConvertRateObject();
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(convertRateStr, scaleInfo);
  }

  /**
   * 设置成本价格精度
   * 
   * @param costPriceStr
   */
  public void setCostPriceDigits(String[] costPriceStr) {
    ScaleObject so = this.scaleFactory.getCostPriceScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(costPriceStr, scaleInfo);
  }

  /**
   * 设置全局本位币汇率精度
   * 
   * @param rateKeys
   *          汇率key
   * @orgOrigCurr
   * @orgLocCurr
   */
  public void setGlobalExchangeCtlInfo(FieldInfo rate, FieldInfo orgOrigCurr,
      FieldInfo orgLocCurr) {
    GroupExchangeScaleObject ocrso =
        new GroupExchangeScaleObject(new ReportFieldValueGetter());
    ocrso.setExchange(rate);
    ocrso.setOrgOrigCurr(orgOrigCurr);
    ocrso.setOrgLocCurr(orgLocCurr);
    ScaleInfo scaleInfo = new ScaleInfo(null, ocrso);
    this.registerScaleInfo(new String[] {
      rate.getItemkey()
    }, scaleInfo);
  }

  /**
   * 设置全局本位币精度
   * 
   * @param globalLocMnyStr
   */
  public void setGlobalLocMnyDigits(String[] globalLocMnyStr) {
    ScaleObject so = this.scaleFactory.getGlobalLocMnyScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(globalLocMnyStr, scaleInfo);
  }

  /**
   * 设置集团本位币汇率精度
   * 
   * @param rateKeys
   *          汇率key
   * @orgOrigCurr
   * @orgLocCurr
   */
  public void setGroupExchangeCtlInfo(FieldInfo rate, FieldInfo orgOrigCurr,
      FieldInfo orgLocCurr) {
    GroupExchangeScaleObject ocrso =
        new GroupExchangeScaleObject(new ReportFieldValueGetter());
    ocrso.setExchange(rate);
    ocrso.setOrgOrigCurr(orgOrigCurr);
    ocrso.setOrgLocCurr(orgLocCurr);
    ScaleInfo scaleInfo = new ScaleInfo(null, ocrso);
    this.registerScaleInfo(new String[] {
      rate.getItemkey()
    }, scaleInfo);
  }

  /**
   * 设置集团本位币精度
   * 
   * @param groupLocMnyStr
   */
  public void setGroupLocMnyStrDigits(String[] groupLocMnyStr) {
    ScaleObject so = this.scaleFactory.getGroupLocMnyScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(groupLocMnyStr, scaleInfo);
  }

  /**
   * 设置换算率精度
   * 
   * @param hslStr
   */
  public void setHslDigits(String[] hslStr) {
    ScaleObject so = this.scaleFactory.getHslScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(hslStr, scaleInfo);
  }

  /**
   * 设置币种精度和金额对应关系
   * 
   * @param currencyKey
   * @param moneyKeys
   */
  public void setMnyDigits(String currencyKey, String[] moneyKeys) {
    ScaleObject so = this.scaleFactory.getMnyScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(currencyKey, so);
    this.registerScaleInfo(moneyKeys, scaleInfo);

  }

  /**
   * 根据主单位设置数量精度
   * 
   * @param key
   * @param keys
   */
  public void setNumDigits(String key, String[] keys) {
    ScaleObject so = this.scaleFactory.getReportNumVarScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(key, so);
    this.registerScaleInfo(keys, scaleInfo);

  }

  /**
   * 走报表精度
   * 
   * @param fieldNames
   */
  public void setNumTotalFields(String[] fieldNames) {
    for (int i = 0; null != fieldNames && i < fieldNames.length; i++) {
      if (null != fieldNames[i]) {
        this.maxScaleMap.put(fieldNames[i].toLowerCase(),
            Integer.valueOf(TableConstant.UNDEFINED));
      }
    }
  }

  /**
   * 设置折本汇率精度
   * 
   * @param rateKeys
   *          汇率key
   * @destCurr
   * @org
   * @srcCurr
   */
  public void setOrgExchangeCtlInfo(FieldInfo rate, FieldInfo srcCurr,
      FieldInfo destCurr, FieldInfo org) {
    OrgExchangeScaleObject ocrso =
        new OrgExchangeScaleObject(new ReportFieldValueGetter());
    ocrso.setExchange(rate);
    ocrso.setSrcCurr(srcCurr);
    ocrso.setDestCurr(destCurr);
    ocrso.setOrg(org);
    ScaleInfo scaleInfo = new ScaleInfo(null, ocrso);
    this.registerScaleInfo(new String[] {
      rate.getItemkey()
    }, scaleInfo);
  }

  /**
   * 设置组织本位币精度
   * 
   * @param key
   * @param keys
   */
  public void setOrgLocMnyDigits(String key, String[] keys) {
    ScaleObject so = this.scaleFactory.getOrgLocMnyScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(key, so);
    this.registerScaleInfo(keys, scaleInfo);
  }

  /**
   * 设置销售/采购价格精度
   * 
   * @param priceStr
   */
  public void setPriceDigits(String[] priceStr) {
    ScaleObject so = this.scaleFactory.getPriceScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(priceStr, scaleInfo);
  }

  /**
   * 设置销售折扣精度
   * 
   * @param saleDiscountStr
   */
  public void setSaleDiscountDigits(String[] saleDiscountStr) {
    ScaleObject so = this.scaleFactory.getSaleDiscountObject();
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(saleDiscountStr, scaleInfo);
  }

  /**
   * 设置标准体积精度
   * 
   * @param standardVolumnStr
   */
  public void setStandardVolumnDigits(String[] standardVolumnStr) {
    ScaleObject so = this.scaleFactory.getStandardVolumnScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(standardVolumnStr, scaleInfo);
  }

  /**
   * 设置标准重量精度
   * 
   * @param standWeightStr
   */
  public void setStandWeightDigits(String[] standWeightStr) {
    ScaleObject so = this.scaleFactory.getStandardWeightScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(standWeightStr, scaleInfo);
  }

  /**
   * 设置库存组织成本精度
   * 
   * @param key
   * @param keys
   */
  public void setStockOrgCostMnyDigits(String key, String[] keys) {
    ScaleObject so = this.scaleFactory.getStockOrgCostMnyScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(key, so);
    this.registerScaleInfo(keys, scaleInfo);
  }

  /**
   * 设置税率精度
   * 
   * @param taxRateStr
   */
  public void setTaxRateDigits(String[] taxRateStr) {
    ScaleObject so = this.scaleFactory.getTaxRateScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(taxRateStr, scaleInfo);
  }

  public void setTotalFields(String[] fieldNames) {
    for (int i = 0; null != fieldNames && i < fieldNames.length; i++) {
      if (null != fieldNames[i]) {
        this.maxScaleMap.put(fieldNames[i].toLowerCase(),
            Integer.valueOf(ReportScaleProcess.DEFAULTDIGITS));
      }
    }
  }

  /**
   * 设置件数精度
   * 
   * @param unitKey
   * @param numKeys
   */
  public void setUnitDigits(String unitKey, String[] numKeys) {
    ScaleObject so = this.scaleFactory.getUnitScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(unitKey, so);
    this.registerScaleInfo(numKeys, scaleInfo);
  }

  private Object getValueFromRowData(String fieldName, IRowData rowData) {
    return rowData.getData(fieldName);
  }

  /**
   * 检查参数是否为空。
   * 
   * @return boolean 如果被检查值为null，返回true。
   *         如果value的类型为String，并且value.length()为0，返回true。
   *         如果value的类型为Object[]，并且value.length为0，返回true。
   *         如果value的类型为Collection，并且value.size()为0，返回true。
   *         如果value的类型为Dictionary，并且value.size()为0，返回true。 否则返回false。
   * @param value
   *          被检查值。
   */
  @SuppressWarnings("rawtypes")
  private boolean isEmpty(Object value) {
    if (value == null) {
      return true;
    }
    if (value instanceof String && ((String) value).trim().length() <= 0) {
      return true;
    }
    if (value instanceof Object[] && ((Object[]) value).length <= 0) {
      return true;
    }
    if (value instanceof Collection && ((Collection) value).size() <= 0) {
      return true;
    }
    if (value instanceof Dictionary && ((Dictionary) value).size() <= 0) {
      return true;
    }
    return false;
  }

  @SuppressWarnings("unused")
  private boolean isExist(String[] fields, String fldName) {
    if (fields == null || fldName == null) {
      return false;
    }
    boolean isExist = false;
    for (int i = 0; i < fields.length; i++) {
      if (!fldName.equalsIgnoreCase(fields[i])) {
        continue;
      }
      isExist = true;
      break;
    }

    return isExist;
  }

  private void registerScaleInfo(String[] fields, ScaleInfo scaleInfo) {
    for (String field : fields) {
      this.keyScaleInfoMap.put(field.toLowerCase(), scaleInfo);
    }
  }

  private void saveMaxScale(String fieldNameLowercase, Integer digit) {
    if (null != digit && null != this.maxScaleMap.get(fieldNameLowercase)) {// 注册为合计行
      if (digit.intValue() > this.maxScaleMap.get(fieldNameLowercase)
          .intValue()) {
        // 如果当前行精度大于最大精度,保存当前精度
        this.maxScaleMap.put(fieldNameLowercase, digit);
      }
    }
  }

}
