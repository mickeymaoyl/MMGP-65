package nc.ui.mmgp.uif2.mediator.num;

import java.util.Collections;
import java.util.Map;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.pub.scale.UIScaleUtils;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.vo.mmgp.util.MMMapUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.calculator.Calculator;
import nc.vo.pubapp.calculator.Condition;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.scale.ScaleUtils;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 表头主辅计量编辑后处理
 * </p>
 * 
 * @since 创建日期 Jun 14, 2013
 * @author wangweir
 */
public class MMGPNumAssNumHeadAfterEditHandler implements IAppEventHandler<CardHeadTailAfterEditEvent> {

    private MMGPNumAssNumMediator mmgpNumAssNumMediator;

    public MMGPNumAssNumHeadAfterEditHandler(MMGPNumAssNumMediator mmgpNumAssNumMediator) {
        this.mmgpNumAssNumMediator = mmgpNumAssNumMediator;
    }

    @Override
    public void handleAppEvent(CardHeadTailAfterEditEvent e) {
        if (e.getKey().equals(this.mmgpNumAssNumMediator.getCmaterialvid())) {
            this.materialChanged(e);
        } else if (e.getKey().equals(this.mmgpNumAssNumMediator.getCastunitidKey())) {
            this.astunitChanged(e);
        } else if (e.getKey().equals(this.mmgpNumAssNumMediator.getNchangerateKey())) {
            this.changeRateChanged(e);
        } else if (MMGPNumAssNumInnerUtils.getNumKeyChanged(this.mmgpNumAssNumMediator, e.getKey()) != null) {
            this.numChanged(e, MMGPNumAssNumInnerUtils.getNumKeyChanged(this.mmgpNumAssNumMediator, e.getKey()));
        } else if (MMGPNumAssNumInnerUtils.getAssNumKeyChanged(this.mmgpNumAssNumMediator, e.getKey()) != null) {
            this.assistNumChanged(
                e,
                MMGPNumAssNumInnerUtils.getAssNumKeyChanged(this.mmgpNumAssNumMediator, e.getKey()));
        }
    }

    /**
     * 辅数量改变
     * 
     * @param e
     * @param mmgpNumAssnumCalPara
     */
    protected void assistNumChanged(CardHeadTailAfterEditEvent e,
                                    MMGPNumAssnumCalPara mmgpNumAssnumCalPara) {
        this.calcNumAssNum(e, e.getKey(), mmgpNumAssnumCalPara);
    }

    /**
     * 数量改变
     * 
     * @param e
     * @param mmgpNumAssnumCalPara
     */
    protected void numChanged(CardHeadTailAfterEditEvent e,
                              MMGPNumAssnumCalPara mmgpNumAssnumCalPara) {
        this.calcNumAssNum(e, e.getKey(), mmgpNumAssnumCalPara);
    }

    /**
     * @param e
     * @param changeKey
     */
    protected void calcWeight(CardHeadTailAfterEditEvent e,
                              String changeKey) {
        MMGPBillCardPanelHeadDataSet billCardPanelDataSet =
                new MMGPBillCardPanelHeadDataSet(e.getBillCardPanel(), this.mmgpNumAssNumMediator);

        if (!MMGPNumAssNumInnerUtils.canCalcWeight(billCardPanelDataSet, this.mmgpNumAssNumMediator)) {
            return;
        }

        UFDouble unitWeight =
                ValueUtils.getUFDouble(e
                    .getBillCardPanel()
                    .getHeadTailItem(mmgpNumAssNumMediator.getUnitweight())
                    .getValueObject());

        UFDouble num =
                ValueUtils.getUFDouble(e
                    .getBillCardPanel()
                    .getHeadTailItem(mmgpNumAssNumMediator.getNnumKey())
                    .getValueObject());

        UFDouble totalWeight = null;
        if (unitWeight != null && num != null) {
            totalWeight = unitWeight.multiply(num);
        }

        e.getBillCardPanel().setHeadItem(mmgpNumAssNumMediator.getNtotalweight(), totalWeight);
    }

    /**
     * 换算率改变
     * 
     * @param e
     */
    protected void changeRateChanged(CardHeadTailAfterEditEvent e) {
        for (MMGPNumAssnumCalPara numAssnumCalPara : this.mmgpNumAssNumMediator.getNumAssnumCalPara()) {
            this.calcNumAssNum(e, e.getKey(), numAssnumCalPara);
        }
    }

    protected void astunitChanged(CardHeadTailAfterEditEvent e) {
        String materialID = getMarterialID(e);

        String astUnitID = (String) e.getValue();
        if (MMStringUtil.isEmptyWithTrim(astUnitID)) {
            e.getBillCardPanel().setHeadItem(this.mmgpNumAssNumMediator.getNchangerateKey(), null);
            return;
        }

        // 主计量单位
        String measDoc =
                (String) e
                    .getBillCardPanel()
                    .getHeadTailItem(this.mmgpNumAssNumMediator.getCunitidKey())
                    .getValueObject();

        String changeRate = MMGPNumAssNumInnerUtils.getChangeRate(materialID, measDoc, astUnitID);

        e.getBillCardPanel().setHeadItem(this.mmgpNumAssNumMediator.getNchangerateKey(), changeRate);

        for (MMGPNumAssnumCalPara numAssnumCalPara : this.mmgpNumAssNumMediator.getNumAssnumCalPara()) {
            this.calcNumAssNum(e, this.mmgpNumAssNumMediator.getNchangerateKey(), numAssnumCalPara);
        }
    }

    protected String getMarterialID(CardHeadTailAfterEditEvent e) {
        String materialID =
                (String) e
                    .getBillCardPanel()
                    .getHeadTailItem(mmgpNumAssNumMediator.getCmaterialvid())
                    .getValueObject();
        return materialID;
    }

    /**
     * 辅计量改变
     * 
     * @param e
     * @param mmgpNumAssnumCalPara
     */
    protected void calcNumAssNum(CardHeadTailAfterEditEvent e,
                                 String changeKey,
                                 MMGPNumAssnumCalPara mmgpNumAssnumCalPara) {
        MMGPNumAssNumInnerUtils.configureNumAssNumMediator(this.mmgpNumAssNumMediator, mmgpNumAssnumCalPara);

        MMGPBillCardPanelHeadDataSet billCardPanelDataSet =
                new MMGPBillCardPanelHeadDataSet(e.getBillCardPanel(), this.mmgpNumAssNumMediator);

        ScaleUtils scale = UIScaleUtils.getScaleUtils();
        Calculator tool = new Calculator(billCardPanelDataSet, scale);
        Condition cond = new Condition();
        cond.setRelationItem(this.mmgpNumAssNumMediator);

        String astUnitId =
                (String) e
                    .getBillCardPanel()
                    .getHeadTailItem(mmgpNumAssNumMediator.getCastunitidKey())
                    .getValueObject();

        String oldChangeRate =
                (String) e
                    .getBillCardPanel()
                    .getHeadTailItem(mmgpNumAssNumMediator.getNchangerateKey())
                    .getValueObject();

        boolean isFixChangeRate =
                MMGPNumAssNumInnerUtils.isFixChangeRate(mmgpNumAssNumMediator, this.getMarterialID(e), astUnitId);
        cond.setIsFixNchangerate(isFixChangeRate);
        cond.setIsCalLocalCurr(false);
        tool.calculateOnlyNumAssNumQtNum(cond, changeKey);
        this.calcWeight(e, changeKey);

        if (isFixChangeRate) {
            return;
        }

        String newChangeRate =
                (String) e
                    .getBillCardPanel()
                    .getHeadTailItem(mmgpNumAssNumMediator.getNchangerateKey())
                    .getValueObject();

        /* Sep 27, 2013 wangweir 增加空处理 Begin */
        if (MMStringUtil.isEmpty(newChangeRate)
            || MMStringUtil.isEmpty(oldChangeRate)
            || newChangeRate.equals(oldChangeRate)) {
            return;
        }
        /* Sep 27, 2013 wangweir End */

        for (MMGPNumAssnumCalPara numAssnumCalPara : this.mmgpNumAssNumMediator.getNumAssnumCalPara()) {
            if (numAssnumCalPara.equals(mmgpNumAssnumCalPara)) {
                continue;
            }
            calcNumAssNum(e, this.mmgpNumAssNumMediator.getNchangerateKey(), mmgpNumAssnumCalPara);
        }
    }

    /**
     * 物料改变
     * 
     * @param e
     */
    protected void materialChanged(CardHeadTailAfterEditEvent e) {
        String materialID = (String) e.getValue();

        Map<String, String> material2AstUnitID = this.getMeasdoc(new String[]{materialID });

        // 清空相关字段
        this.clearValue(e, this.getClearItems());

        // 主计量单位
        BillCardPanel billCardPanel = e.getBillCardPanel();
        String measDoc =
                (String) billCardPanel.getHeadTailItem(this.mmgpNumAssNumMediator.getCunitidKey()).getValueObject();

        // 辅计量
        String astUnitID = this.getAstUnitID(material2AstUnitID, materialID, measDoc);

        // 换算率
        String changeRate = MMGPNumAssNumInnerUtils.getChangeRate(materialID, measDoc, astUnitID);

        billCardPanel.setHeadItem(this.mmgpNumAssNumMediator.getCastunitidKey(), astUnitID);
        billCardPanel.setHeadItem(this.mmgpNumAssNumMediator.getNchangerateKey(), changeRate);

        billCardPanel.getBillData().loadLoadHeadRelation();
        billCardPanel.execHeadTailEditFormulas();
    }

    protected String[] getClearItems() {
        return MMGPNumAssNumInnerUtils.getClearItems(this.mmgpNumAssNumMediator);
    }

    /**
     * @param materialIds
     * @return
     */
    @SuppressWarnings("unchecked")
    protected Map<String, String> getMeasdoc(String[] materialIds) {
        if (this.mmgpNumAssNumMediator.getMaterialConvertType() == MMGPNumAssNumMediator.UNDEFINE_CONVERTTYPE) {
            return Collections.EMPTY_MAP;
        }

        return MMGPNumAssNumInnerUtils.queryMeasdocIDByPksAndType(
            materialIds,
            this.mmgpNumAssNumMediator.getMaterialConvertType());
    }

    private String getAstUnitID(Map<String, String> material2AstUnitID,
                                String materialId,
                                String measDoc) {
        if (MMStringUtil.isEmptyWithTrim(materialId) || MMMapUtil.isEmpty(material2AstUnitID)) {
            return measDoc;
        }

        String astUnitID = material2AstUnitID.get(materialId);
        if (MMStringUtil.isEmptyWithTrim(astUnitID)) {
            return measDoc;
        }

        return astUnitID;
    }

    /**
     * 清空值
     * 
     * @param e
     * @param row
     * @param clearKeys
     */
    private void clearValue(CardHeadTailAfterEditEvent e,
                            String[] clearKeys) {
        for (String clearKey : clearKeys) {
            if (clearKey == null) {
                continue;
            }

            e.getBillCardPanel().setHeadItem(clearKey, null);
        }
    }

}
