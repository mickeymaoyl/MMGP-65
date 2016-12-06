package nc.ui.mmgp.uif2.mediator.num;

import java.util.Date;

import nc.pubitf.uapbd.CurrencyRateUtilHelper;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.calculator.data.IDataSetForCal;
import nc.vo.pubapp.calculator.data.IRelationForItems;
import nc.vo.pubapp.pattern.data.ValueUtils;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 卡片界面表头主辅计量数据获取类
 * </p>
 * 
 * @since 创建日期 Jun 14, 2013
 * @author wangweir
 */
public class MMGPBillCardPanelHeadDataSet implements IDataSetForCal {

    public static final String PK_GROUP = "pk_group";

    private BillCardPanel cardPanel;

    private IRelationForItems item;

    public MMGPBillCardPanelHeadDataSet(BillCardPanel cardPanel,
                                        IRelationForItems item) {
        this.item = item;
        this.cardPanel = cardPanel;
    }

    @Override
    public Object getAttributeValue(String key) {
        return getHeadItemValue(key);
    }

    @Override
    public UFDate getBillDate() {
        String key = this.item.getBillDate();
        UFDate date = this.getDate(key);
        if (date == null) {
            date = new UFDate(new Date());
        }
        return date;
    }

    @Override
    public String getCastunitid() {
        String value = this.getString(this.item.getCastunitidKey());
        return value;
    }

    @Override
    public String getCcurrencyid() {
        String value = this.getString(this.item.getCcurrencyidKey());
        if (value == null) {
            String pk_org = this.getPk_org();
            value = CurrencyRateUtilHelper.getInstance().getLocalCurrtypeByOrgID(pk_org);
        }
        return value;
    }

    @Override
    public String getCorigcurrencyid() {
        String value = this.getString(this.item.getCorigcurrencyidKey());
        return value;
    }

    @Override
    public String getCqtcurrencyid() {
        String value = this.getString(this.item.getCqtcurrencyidKey());
        return value;
    }

    @Override
    public String getCqtunitid() {
        String value = this.getString(this.item.getCqtunitidKey());
        return value;
    }

    @Override
    public String getCunitid() {
        String value = this.getString(this.item.getCunitidKey());
        return value;
    }

    @Override
    public int getFtaxtypeflag() {
        int value = this.getInt(this.item.getFtaxtypeflagKey());
        return value;
    }

    @Override
    public UFDouble getNaskqtorigprice() {
        UFDouble value = this.getUFDoubleValue(this.item.getNaskqtorigpriceKey());
        return value;
    }

    @Override
    public UFDouble getNaskqtorigtaxprc() {
        UFDouble value = this.getUFDoubleValue(this.item.getNaskqtorigtaxprcKey());
        return value;
    }

    @Override
    public UFDouble getNaskqtprice() {
        UFDouble value = this.getUFDoubleValue(this.item.getNaskqtpriceKey());
        return value;
    }

    @Override
    public UFDouble getNaskqttaxprice() {
        UFDouble value = this.getUFDoubleValue(this.item.getNaskqttaxpriceKey());
        return value;
    }

    @Override
    public UFDouble getNassistnum() {
        UFDouble value = this.getUFDoubleValue(this.item.getNassistnumKey());
        return value;
    }

    @Override
    public String getNchangerate() {
        String value = this.getString(this.item.getNchangerateKey());
        return value;
    }

    @Override
    public UFDouble getNcostmny() {
        UFDouble value = this.getUFDoubleValue(this.item.getNcostmnyKey());
        return value;
    }

    @Override
    public UFDouble getNcostprice() {
        UFDouble value = this.getUFDoubleValue(this.item.getNcostpriceKey());
        return value;
    }

    @Override
    public UFDouble getNdiscount() {
        UFDouble value = this.getUFDoubleValue(this.item.getNdiscountKey());
        return value;
    }

    @Override
    public UFDouble getNdiscountrate() {
        UFDouble value = this.getUFDoubleValue(this.item.getNdiscountrateKey());
        return value;
    }

    @Override
    public UFDouble getNexchangerate() {
        return this.getUFDoubleValue(this.item.getNexchangerateKey());
    }

    @Override
    public UFDouble getNglobalexchgrate() {
        UFDouble value = this.getUFDoubleValue(this.item.getNglobalexchgrateKey());
        return value;
    }

    @Override
    public UFDouble getNglobalmny() {
        UFDouble value = this.getUFDoubleValue(this.item.getNglobalmnyKey());
        return value;
    }

    @Override
    public UFDouble getNglobaltaxmny() {
        UFDouble value = this.getUFDoubleValue(this.item.getNglobaltaxmnyKey());
        return value;
    }

    @Override
    public UFDouble getNgroupexchgrate() {
        UFDouble value = this.getUFDoubleValue(this.item.getNgroupexchgrateKey());
        return value;
    }

    @Override
    public UFDouble getNgroupmny() {
        UFDouble value = this.getUFDoubleValue(this.item.getNgroupmnyKey());
        return value;
    }

    @Override
    public UFDouble getNgrouptaxmny() {
        UFDouble value = this.getUFDoubleValue(this.item.getNgrouptaxmnyKey());
        return value;
    }

    @Override
    public UFDouble getNitemdiscountrate() {
        UFDouble value = this.getUFDoubleValue(this.item.getNitemdiscountrateKey());
        return value;
    }

    @Override
    public UFDouble getNmny() {
        UFDouble value = this.getUFDoubleValue(this.item.getNmnyKey());
        return value;
    }

    @Override
    public UFDouble getNnetprice() {
        UFDouble value = this.getUFDoubleValue(this.item.getNnetpriceKey());
        return value;
    }

    @Override
    public UFDouble getNnum() {
        UFDouble value = this.getUFDoubleValue(this.item.getNnumKey());
        return value;
    }

    @Override
    public UFDouble getNorigdiscount() {
        UFDouble value = this.getUFDoubleValue(this.item.getNorigdiscountKey());
        return value;
    }

    @Override
    public UFDouble getNorigmny() {
        UFDouble value = this.getUFDoubleValue(this.item.getNorigmnyKey());
        return value;
    }

    @Override
    public UFDouble getNorignetprice() {
        UFDouble value = this.getUFDoubleValue(this.item.getNorignetpriceKey());
        return value;
    }

    @Override
    public UFDouble getNorigprice() {
        UFDouble value = this.getUFDoubleValue(this.item.getNorigpriceKey());
        return value;
    }

    @Override
    public UFDouble getNorigtax() {
        UFDouble value = this.getUFDoubleValue(this.item.getNorigtaxKey());
        return value;
    }

    @Override
    public UFDouble getNorigtaxmny() {
        UFDouble value = this.getUFDoubleValue(this.item.getNorigtaxmnyKey());
        return value;
    }

    @Override
    public UFDouble getNorigtaxnetprice() {
        UFDouble value = this.getUFDoubleValue(this.item.getNorigtaxnetpriceKey());
        return value;
    }

    @Override
    public UFDouble getNorigtaxprice() {
        UFDouble value = this.getUFDoubleValue(this.item.getNorigtaxpriceKey());
        return value;
    }

    @Override
    public UFDouble getNprice() {
        UFDouble value = this.getUFDoubleValue(this.item.getNpriceKey());
        return value;
    }

    @Override
    public UFDouble getNqtnetprice() {
        UFDouble value = this.getUFDoubleValue(this.item.getNqtnetpriceKey());
        return value;
    }

    @Override
    public UFDouble getNqtorignetprice() {
        UFDouble value = this.getUFDoubleValue(this.item.getNqtorignetpriceKey());
        return value;
    }

    @Override
    public UFDouble getNqtorigprice() {
        UFDouble value = this.getUFDoubleValue(this.item.getNqtorigpriceKey());
        return value;
    }

    @Override
    public UFDouble getNqtorigtaxnetprc() {
        UFDouble value = this.getUFDoubleValue(this.item.getNqtorigtaxnetprcKey());
        return value;
    }

    @Override
    public UFDouble getNqtorigtaxprice() {
        UFDouble value = this.getUFDoubleValue(this.item.getNqtorigtaxpriceKey());
        return value;
    }

    @Override
    public UFDouble getNqtprice() {
        UFDouble value = this.getUFDoubleValue(this.item.getNqtpriceKey());
        return value;
    }

    @Override
    public UFDouble getNqttaxnetprice() {
        UFDouble value = this.getUFDoubleValue(this.item.getNqttaxnetpriceKey());
        return value;
    }

    @Override
    public UFDouble getNqttaxprice() {
        UFDouble value = this.getUFDoubleValue(this.item.getNqttaxpriceKey());
        return value;
    }

    @Override
    public UFDouble getNqtunitnum() {
        UFDouble value = this.getUFDoubleValue(this.item.getNqtunitnumKey());
        return value;
    }

    @Override
    public String getNqtunitrate() {
        String value = this.getString(this.item.getNqtunitrateKey());
        return value;
    }

    @Override
    public UFDouble getNtax() {
        UFDouble value = this.getUFDoubleValue(this.item.getNtaxKey());
        return value;
    }

    @Override
    public UFDouble getNtaxmny() {
        UFDouble value = this.getUFDoubleValue(this.item.getNtaxmnyKey());
        return value;
    }

    @Override
    public UFDouble getNtaxnetprice() {
        UFDouble value = this.getUFDoubleValue(this.item.getNtaxnetpriceKey());
        return value;
    }

    @Override
    public UFDouble getNtaxprice() {
        UFDouble value = this.getUFDoubleValue(this.item.getNtaxpriceKey());
        return value;
    }

    @Override
    public UFDouble getNtaxrate() {
        UFDouble value = this.getUFDoubleValue(this.item.getNtaxrateKey());
        return value;
    }

    @Override
    public UFDouble getNtotalnum() {
        UFDouble value = this.getUFDoubleValue(this.item.getNtotalnumKey());
        return value;
    }

    @Override
    public UFDouble getNtotalorigmny() {
        UFDouble value = this.getUFDoubleValue(this.item.getNtotalorigmnyKey());
        return value;
    }

    @Override
    public UFDouble getNtotalorigtaxmny() {
        UFDouble value = this.getUFDoubleValue(this.item.getNtotalorigtaxmnyKey());
        return value;
    }

    @Override
    public String getPk_group() {
        String value = this.getString(this.item.getPk_group());
        if (value == null) {
            value = this.getStringFromHead(this.item.getPk_group());
        }
        return value;
    }

    @Override
    public String getPk_org() {
        String value = this.getString(this.item.getPk_org());
        if (value == null) {
            value = this.getStringFromHead(this.item.getPk_org());
        }
        return value;
    }

    @Override
    public UFDouble getQualifiedNum() {
        UFDouble value = this.getUFDoubleValue(this.item.getQualifiedNumKey());
        return value;
    }

    @Override
    public IRelationForItems getRelationForItem() {
        return this.item;
    }

    @Override
    public UFDouble getUnQualifiedNum() {
        UFDouble value = this.getUFDoubleValue(this.item.getUnQualifiedNumKey());
        return value;
    }

    @Override
    public boolean hasItemKey(String key) {
        BillItem billItem = null;
        billItem = this.getBillCardPanel().getHeadItem(key);
        return billItem != null;
    }

    @Override
    public void setBillDate(UFDate date) {
        this.setAttributeValue(this.item.getBillDate(), date);
    }

    @Override
    public void setCastunitid(String value) {
        this.setAttributeValue(this.item.getUnQualifiedNumKey(), value);
    }

    @Override
    public void setCcurrencyid(String value) {
        this.setAttributeValue(this.item.getCcurrencyidKey(), value);
    }

    @Override
    public void setCorigcurrencyid(String value) {
        this.setAttributeValue(this.item.getCorigcurrencyidKey(), value);
    }

    @Override
    public void setCqtcurrencyid(String value) {
        this.setAttributeValue(this.item.getCqtcurrencyidKey(), value);
    }

    @Override
    public void setCqtunitid(String value) {
        this.setAttributeValue(this.item.getUnQualifiedNumKey(), value);
    }

    @Override
    public void setCunitid(String value) {
        this.setAttributeValue(this.item.getCunitidKey(), value);
    }

    @Override
    public void setFtaxtypeflag(int value) {
        this.setAttributeValue(this.item.getFtaxtypeflagKey(), Integer.valueOf(value));
    }

    @Override
    public void setNaskqtorigprice(UFDouble value) {
        this.setAttributeValue(this.item.getNaskqtorigpriceKey(), value);
    }

    @Override
    public void setNaskqtorigtaxprc(UFDouble value) {
        this.setAttributeValue(this.item.getNaskqtorigtaxprcKey(), value);
    }

    @Override
    public void setNaskqtprice(UFDouble value) {
        this.setAttributeValue(this.item.getNaskqtpriceKey(), value);
    }

    @Override
    public void setNaskqttaxprice(UFDouble value) {
        this.setAttributeValue(this.item.getNaskqttaxpriceKey(), value);
    }

    @Override
    public void setNassistnum(UFDouble value) {
        this.setAttributeValue(this.item.getNassistnumKey(), value);
    }

    @Override
    public void setNchangerate(String value) {
        this.setAttributeValue(this.item.getNchangerateKey(), value);
    }

    @Override
    public void setNcostmny(UFDouble value) {
        this.setAttributeValue(this.item.getNcostmnyKey(), value);
    }

    @Override
    public void setNcostprice(UFDouble value) {
        this.setAttributeValue(this.item.getNcostpriceKey(), value);
    }

    @Override
    public void setNdiscount(UFDouble value) {
        this.setAttributeValue(this.item.getNdiscountKey(), value);
    }

    @Override
    public void setNdiscountrate(UFDouble value) {
        this.setAttributeValue(this.item.getNdiscountrateKey(), value);
    }

    @Override
    public void setNexchangerate(UFDouble value) {
        this.setAttributeValue(this.item.getNexchangerateKey(), value);
    }

    @Override
    public void setNglobalexchgrate(UFDouble value) {
        this.setAttributeValue(this.item.getNglobalexchgrateKey(), value);
    }

    @Override
    public void setNglobalmny(UFDouble value) {
        this.setAttributeValue(this.item.getNglobalmnyKey(), value);
    }

    @Override
    public void setNglobaltaxmny(UFDouble value) {
        this.setAttributeValue(this.item.getNglobaltaxmnyKey(), value);
    }

    @Override
    public void setNgroupexchgrate(UFDouble value) {
        this.setAttributeValue(this.item.getNgroupexchgrateKey(), value);
    }

    @Override
    public void setNgroupmny(UFDouble value) {
        this.setAttributeValue(this.item.getNgroupmnyKey(), value);
    }

    @Override
    public void setNgrouptaxmny(UFDouble value) {
        this.setAttributeValue(this.item.getNgrouptaxmnyKey(), value);
    }

    @Override
    public void setNitemdiscountrate(UFDouble value) {
        this.setAttributeValue(this.item.getNitemdiscountrateKey(), value);
    }

    @Override
    public void setNmny(UFDouble value) {
        this.setAttributeValue(this.item.getNmnyKey(), value);
    }

    @Override
    public void setNnetprice(UFDouble value) {
        this.setAttributeValue(this.item.getNnetpriceKey(), value);
    }

    @Override
    public void setNnum(UFDouble value) {
        this.setAttributeValue(this.item.getNnumKey(), value);
    }

    @Override
    public void setNorigdiscount(UFDouble value) {
        this.setAttributeValue(this.item.getNorigdiscountKey(), value);
    }

    @Override
    public void setNorigmny(UFDouble value) {
        this.setAttributeValue(this.item.getNorigmnyKey(), value);
    }

    @Override
    public void setNorignetprice(UFDouble value) {
        this.setAttributeValue(this.item.getNorignetpriceKey(), value);
    }

    @Override
    public void setNorigprice(UFDouble value) {
        this.setAttributeValue(this.item.getNorigpriceKey(), value);
    }

    @Override
    public void setNorigtax(UFDouble value) {
        this.setAttributeValue(this.item.getNorigtaxKey(), value);
    }

    @Override
    public void setNorigtaxmny(UFDouble value) {
        this.setAttributeValue(this.item.getNorigtaxmnyKey(), value);
    }

    @Override
    public void setNorigtaxnetprice(UFDouble value) {
        this.setAttributeValue(this.item.getNorigtaxnetpriceKey(), value);
    }

    @Override
    public void setNorigtaxprice(UFDouble value) {
        this.setAttributeValue(this.item.getNorigtaxpriceKey(), value);
    }

    @Override
    public void setNprice(UFDouble value) {
        this.setAttributeValue(this.item.getNpriceKey(), value);
    }

    @Override
    public void setNqtnetprice(UFDouble value) {
        this.setAttributeValue(this.item.getNqtnetpriceKey(), value);
    }

    @Override
    public void setNqtorignetprice(UFDouble value) {
        this.setAttributeValue(this.item.getNqtorignetpriceKey(), value);
    }

    @Override
    public void setNqtorigprice(UFDouble value) {
        this.setAttributeValue(this.item.getNqtorigpriceKey(), value);
    }

    @Override
    public void setNqtorigtaxnetprc(UFDouble value) {
        this.setAttributeValue(this.item.getNqtorigtaxnetprcKey(), value);
    }

    @Override
    public void setNqtorigtaxprice(Object value) {
        this.setAttributeValue(this.item.getNqtorigtaxpriceKey(), value);
    }

    @Override
    public void setNqtprice(UFDouble value) {
        this.setAttributeValue(this.item.getNqtpriceKey(), value);
    }

    @Override
    public void setNqttaxnetprice(UFDouble value) {
        this.setAttributeValue(this.item.getNqttaxnetpriceKey(), value);
    }

    @Override
    public void setNqttaxprice(UFDouble value) {
        this.setAttributeValue(this.item.getNqttaxpriceKey(), value);
    }

    @Override
    public void setNqtunitnum(UFDouble value) {
        this.setAttributeValue(this.item.getNqtunitnumKey(), value);
    }

    @Override
    public void setNqtunitrate(String value) {
        this.setAttributeValue(this.item.getNqtunitrateKey(), value);
    }

    @Override
    public void setNtax(UFDouble value) {
        this.setAttributeValue(this.item.getNtaxKey(), value);
    }

    @Override
    public void setNtaxmny(UFDouble value) {
        this.setAttributeValue(this.item.getNtaxmnyKey(), value);
    }

    @Override
    public void setNtaxnetprice(UFDouble value) {
        this.setAttributeValue(this.item.getNtaxnetpriceKey(), value);
    }

    @Override
    public void setNtaxprice(UFDouble value) {
        this.setAttributeValue(this.item.getNtaxpriceKey(), value);
    }

    @Override
    public void setNtaxrate(UFDouble value) {
        this.setAttributeValue(this.item.getNtaxrateKey(), value);
    }

    @Override
    public void setNtotalnum(UFDouble value) {
        this.setAttributeValue(this.item.getNtotalnumKey(), value);
    }

    @Override
    public void setNtotalorigmny(UFDouble value) {
        this.setAttributeValue(this.item.getNtotalorigmnyKey(), value);
    }

    @Override
    public void setNtotalorigtaxmny(UFDouble value) {
        this.setAttributeValue(this.item.getNtotalorigtaxmnyKey(), value);
    }

    @Override
    public void setPk_group(String value) {
        this.setAttributeValue(this.item.getPk_group(), value);
    }

    @Override
    public void setPk_org(String value) {
        this.setAttributeValue(this.item.getPk_org(), value);
    }

    @Override
    public void setQualifiedNum(UFDouble value) {
        this.setAttributeValue(this.item.getQualifiedNumKey(), value);
    }

    @Override
    public void setUnQualifiedNum(UFDouble value) {
        this.setAttributeValue(this.item.getUnQualifiedNumKey(), value);
    }

    protected BillCardPanel getBillCardPanel() {
        return this.cardPanel;
    }

    private UFDate getDate(String key) {
        Object obj = getHeadItemValue(key);
        UFDate date = ValueUtils.getUFDate(obj);
        return date;
    }

    protected Object getHeadItemValue(String key) {
        if (this.cardPanel.getHeadItem(key) == null) {
            return null;
        }
        return this.cardPanel.getHeadItem(key).getValueObject();
    }

    private int getInt(String key) {
        Object obj = getHeadTailItemValue(key);
        int value = ValueUtils.getInt(obj);
        return value;
    }

    protected Object getHeadTailItemValue(String key) {
        if (this.cardPanel.getHeadTailItem(key) == null) {
            return null;
        }
        return this.cardPanel.getHeadTailItem(key).getValueObject();
    }

    private String getString(String key) {
        Object value = getHeadTailItemValue(key);
        String str = ValueUtils.getString(value);
        return str;
    }

    private String getStringFromHead(String itemkey) {
        Object value = getHeadItemValue(itemkey);
        String str = ValueUtils.getString(value);
        return str;
    }

    private UFDouble getUFDoubleValue(String key) {
        Object value = getHeadTailItemValue(key);
        UFDouble d = ValueUtils.getUFDouble(value);
        return d;
    }

    private void setAttributeValue(String key,
                                   Object value) {
        this.cardPanel.setHeadItem(key, value);
    }

    @Override
    public UFDouble getNcaltaxmny() {
        UFDouble value = this.getUFDoubleValue(this.item.getNcaltaxmnyKey());
        return value;
    }

    @Override
    public UFDouble getNdeductibleTaxrate() {
        UFDouble value = this.getUFDoubleValue(this.item.getNdeductibleTaxrateKey());
        return value;
    }

    @Override
    public UFDouble getNdeductibletax() {
        UFDouble value = this.getUFDoubleValue(this.item.getNdeductibletaxKey());
        return value;
    }

    @Override
    public void setNcaltaxmny(UFDouble value) {
        this.setAttributeValue(this.item.getNcaltaxmnyKey(), value);

    }

    @Override
    public void setNdeductibleTaxrate(UFDouble value) {
        this.setAttributeValue(this.item.getNdeductibleTaxrateKey(), value);

    }

    @Override
    public void setNdeductibletax(UFDouble value) {
        this.setAttributeValue(this.item.getNdeductibletaxKey(), value);

    }

}
