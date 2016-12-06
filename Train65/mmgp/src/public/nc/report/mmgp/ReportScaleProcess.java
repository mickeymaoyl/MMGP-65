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

  // ���澫�ȺͶ�Ӧ��Ӧ���ֶ���Ϣ
  private Map<String, ScaleInfo> keyScaleInfoMap =
      new HashMap<String, ScaleInfo>();

  // �����е���󾫶�
  private Map<String, Integer> maxScaleMap = new HashMap<String, Integer>();

  // ���ȶ��󹤳�
  private ScaleObjectFactory scaleFactory = new ScaleObjectFactory(BSContext
      .getInstance().getGroupID());

  // ���澫������
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
    if (null != countField) {// �ϼ��л���С����
      // if (null != countField) {
      String rangeFld = countField.getRangeFld();
      if (null == rangeFld) {// �ϼ���
        Integer maxDigit = this.maxScaleMap.get(fieldNameLowercase);
        if (null != maxDigit) {// ע��Ϊ�ϼ���
          return maxDigit.intValue();
        }
        return ReportScaleProcess.DEFAULTDIGITS;
      }
      else {// С����
        Integer maxDigit = this.maxScaleMap.get(fieldNameLowercase);
        if (null != maxDigit) {// ע��ΪС����
          return maxDigit.intValue();
        }
        return ReportScaleProcess.DEFAULTDIGITS;
      }
      // }
    }
    else if (null != rowData) {// ��ϸ��
      // ����֧�ֽ���ָ��ϼ��ֶ�����ת�壬��Ӱ�������ֶ� ����2�� jinjya 2011-10-08 09:36:53
      String fldDbName = AnaReportFieldUtil.getFieldNameInRowData(rowData, fldName);
      fieldNameLowercase = null == fldDbName ? null : fldDbName.toLowerCase();
      rowData.getData(fldDbName);
      if (!RowDataUtil.hasField(rowData, fldDbName)) {// ����������û�ж�Ӧ��
        return ReportScaleProcess.DEFAULTDIGITS;
      }
      if (!this.keyScaleInfoMap.containsKey(fieldNameLowercase)) {// ���ֶ�û��ע�����Ҫ�����ȵ��ֶ�
        return TableConstant.UNDEFINED;
      }
      Object value = rowData.getData(fldDbName);
      if (this.isEmpty(value)) {// ���ֶ����ڵĵ�Ԫ���ֵΪ��,ֱ�ӷ���Ĭ�Ͼ���
        return ReportScaleProcess.DEFAULTDIGITS;
      }
      ScaleInfo si = this.keyScaleInfoMap.get(fieldNameLowercase);
      // �ȴ��ڴ滺��ȡ����,���û�о����²�ѯ
      Object valueFromRowData =
          si.getParamKey() != null ? this.getValueFromRowData(si.getParamKey(),
              rowData) : "";

      if (null != si.getParamKey()
          && null == this.getValueFromRowData(si.getParamKey().trim(), rowData)) {// �����ֶ����ڵĵ�Ԫ��û������
        if(si.getScaleObject()instanceof NumScaleObject){
         //�������ͱȽ����⡣�������ʹ��ͬʱ����ʱû�м�����λʱ����Ҫȡ8λ����
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
      // ���²�ѯ
      digit = si.getDigit(rowData);
      this.saveMaxScale(fieldNameLowercase, Integer.valueOf(digit));
      if (si.getScaleObject() instanceof VarScaleObject) {// ��̬����
        // ʹ��(���ȶ�������+�����ֶε�ֵ)��Ϊkey���澫��
        this.scaleMap.put(fldDbName + subPram, Integer.valueOf(digit));
      }
      else {// ��̬����
        // ʹ��(���ȶ�������)��Ϊkey���澫��
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
   * �����ֶ�Ϊ��������
   * 
   * @param fieldKey
   *          ��Ҫ�辫�ȵ��ֶ�
   * @param digit
   *          ����λ��
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
			// TODO �Զ����ɵķ������
			return 4;
		}
    };
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(fieldKey, scaleInfo);
  }

  /**
   * ���û����ʾ���
   * 
   * @param convertRateStr
   */
  public void setConvertRateDigits(String[] convertRateStr) {
    ScaleObject so = this.scaleFactory.getConvertRateObject();
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(convertRateStr, scaleInfo);
  }

  /**
   * ���óɱ��۸񾫶�
   * 
   * @param costPriceStr
   */
  public void setCostPriceDigits(String[] costPriceStr) {
    ScaleObject so = this.scaleFactory.getCostPriceScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(costPriceStr, scaleInfo);
  }

  /**
   * ����ȫ�ֱ�λ�һ��ʾ���
   * 
   * @param rateKeys
   *          ����key
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
   * ����ȫ�ֱ�λ�Ҿ���
   * 
   * @param globalLocMnyStr
   */
  public void setGlobalLocMnyDigits(String[] globalLocMnyStr) {
    ScaleObject so = this.scaleFactory.getGlobalLocMnyScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(globalLocMnyStr, scaleInfo);
  }

  /**
   * ���ü��ű�λ�һ��ʾ���
   * 
   * @param rateKeys
   *          ����key
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
   * ���ü��ű�λ�Ҿ���
   * 
   * @param groupLocMnyStr
   */
  public void setGroupLocMnyStrDigits(String[] groupLocMnyStr) {
    ScaleObject so = this.scaleFactory.getGroupLocMnyScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(groupLocMnyStr, scaleInfo);
  }

  /**
   * ���û����ʾ���
   * 
   * @param hslStr
   */
  public void setHslDigits(String[] hslStr) {
    ScaleObject so = this.scaleFactory.getHslScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(hslStr, scaleInfo);
  }

  /**
   * ���ñ��־��Ⱥͽ���Ӧ��ϵ
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
   * ��������λ������������
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
   * �߱�����
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
   * �����۱����ʾ���
   * 
   * @param rateKeys
   *          ����key
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
   * ������֯��λ�Ҿ���
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
   * ��������/�ɹ��۸񾫶�
   * 
   * @param priceStr
   */
  public void setPriceDigits(String[] priceStr) {
    ScaleObject so = this.scaleFactory.getPriceScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(priceStr, scaleInfo);
  }

  /**
   * ���������ۿ۾���
   * 
   * @param saleDiscountStr
   */
  public void setSaleDiscountDigits(String[] saleDiscountStr) {
    ScaleObject so = this.scaleFactory.getSaleDiscountObject();
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(saleDiscountStr, scaleInfo);
  }

  /**
   * ���ñ�׼�������
   * 
   * @param standardVolumnStr
   */
  public void setStandardVolumnDigits(String[] standardVolumnStr) {
    ScaleObject so = this.scaleFactory.getStandardVolumnScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(standardVolumnStr, scaleInfo);
  }

  /**
   * ���ñ�׼��������
   * 
   * @param standWeightStr
   */
  public void setStandWeightDigits(String[] standWeightStr) {
    ScaleObject so = this.scaleFactory.getStandardWeightScaleObject();
    ScaleInfo scaleInfo = new ScaleInfo(null, so);
    this.registerScaleInfo(standWeightStr, scaleInfo);
  }

  /**
   * ���ÿ����֯�ɱ�����
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
   * ����˰�ʾ���
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
   * ���ü�������
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
   * �������Ƿ�Ϊ�ա�
   * 
   * @return boolean ��������ֵΪnull������true��
   *         ���value������ΪString������value.length()Ϊ0������true��
   *         ���value������ΪObject[]������value.lengthΪ0������true��
   *         ���value������ΪCollection������value.size()Ϊ0������true��
   *         ���value������ΪDictionary������value.size()Ϊ0������true�� ���򷵻�false��
   * @param value
   *          �����ֵ��
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
    if (null != digit && null != this.maxScaleMap.get(fieldNameLowercase)) {// ע��Ϊ�ϼ���
      if (digit.intValue() > this.maxScaleMap.get(fieldNameLowercase)
          .intValue()) {
        // �����ǰ�о��ȴ�����󾫶�,���浱ǰ����
        this.maxScaleMap.put(fieldNameLowercase, digit);
      }
    }
  }

}
