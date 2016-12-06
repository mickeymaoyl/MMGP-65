package nc.ui.mmgp.uif2.mediator.num;

import java.util.Collections;
import java.util.Map;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.calculator.data.BillCardPanelDataSet;
import nc.ui.pubapp.pub.scale.UIScaleUtils;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.pubapp.uif2app.view.util.RefMoreSelectedUtils;
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
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Jun 13, 2013
 * @author wangweir
 */
public class MMGPNumAssNumBodyAfterEditHandler implements IAppEventHandler<CardBodyAfterEditEvent> {

    private MMGPNumAssNumMediator mmgpNumAssNumMediator;

    public MMGPNumAssNumBodyAfterEditHandler(MMGPNumAssNumMediator mmgpNumAssNumMediator) {
        this.mmgpNumAssNumMediator = mmgpNumAssNumMediator;
    }

    @Override
    public void handleAppEvent(CardBodyAfterEditEvent e) {
        if (e.getKey().equals(this.mmgpNumAssNumMediator.getCmaterialvid())) {
            int[] changedRows = this.materialChanged(e);
            this.mmgpNumAssNumMediator.afterMaterialChanged(e, changedRows);
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

    protected void astunitChanged(CardBodyAfterEditEvent e) {
        String materialID =
                (String) e
                    .getBillCardPanel()
                    .getBodyValueAt(e.getRow(), this.mmgpNumAssNumMediator.getCmaterialvid());

        String astUnitID = (String) e.getValue();
        if (MMStringUtil.isEmptyWithTrim(astUnitID)) {
            this.clearValue(e, e.getRow(), new String[]{this.mmgpNumAssNumMediator.getNchangerateKey() });
            return;
        }

        // 主计量单位
        String measDoc =
                (String) e.getBillCardPanel().getBodyValueAt(e.getRow(), this.mmgpNumAssNumMediator.getCunitidKey());

        String changeRate = MMGPNumAssNumInnerUtils.getChangeRate(materialID, measDoc, astUnitID);

        e.getBillCardPanel().setBodyValueAt(
            changeRate,
            e.getRow(),
            this.mmgpNumAssNumMediator.getNchangerateKey(),
            e.getTableCode());

        for (MMGPNumAssnumCalPara numAssnumCalPara : this.mmgpNumAssNumMediator.getNumAssnumCalPara()) {
            this.calcNumAssNum(e, this.mmgpNumAssNumMediator.getNchangerateKey(), numAssnumCalPara);
        }
    }

    /**
     * 辅数量改变
     * 
     * @param e
     * @param mmgpNumAssnumCalPara
     */
    protected void assistNumChanged(CardBodyAfterEditEvent e,
                                    MMGPNumAssnumCalPara mmgpNumAssnumCalPara) {
        this.calcNumAssNum(e, e.getKey(), mmgpNumAssnumCalPara);
    }

    /**
     * 数量改变
     * 
     * @param e
     * @param mmgpNumAssnumCalPara
     */
    protected void numChanged(CardBodyAfterEditEvent e,
                              MMGPNumAssnumCalPara mmgpNumAssnumCalPara) {
        this.calcNumAssNum(e, e.getKey(), mmgpNumAssnumCalPara);
    }

    /**
     * 换算率改变
     * 
     * @param e
     */
    protected void changeRateChanged(CardBodyAfterEditEvent e) {
        for (MMGPNumAssnumCalPara numAssnumCalPara : this.mmgpNumAssNumMediator.getNumAssnumCalPara()) {
            this.calcNumAssNum(e, e.getKey(), numAssnumCalPara);
        }
    }

    /**
     * 辅计量改变
     * 
     * @param e
     */
    protected void calcNumAssNum(CardBodyAfterEditEvent e,
                                 String changeKey,
                                 MMGPNumAssnumCalPara mmgpNumAssnumCalPara) {
        MMGPNumAssNumInnerUtils.configureNumAssNumMediator(this.mmgpNumAssNumMediator, mmgpNumAssnumCalPara);

        BillCardPanelDataSet billCardPanelDataSet =
                new BillCardPanelDataSet(e.getBillCardPanel(), e.getRow(), this.mmgpNumAssNumMediator);

        ScaleUtils scale = UIScaleUtils.getScaleUtils();
        Calculator tool = new Calculator(billCardPanelDataSet, scale);
        Condition cond = new Condition();
        cond.setRelationItem(this.mmgpNumAssNumMediator);

        String materialId =
                (String) e.getBillCardPanel().getBodyValueAt(e.getRow(), mmgpNumAssNumMediator.getCmaterialvid());
        String astUnitId =
                (String) e.getBillCardPanel().getBodyValueAt(e.getRow(), mmgpNumAssNumMediator.getCastunitidKey());

        String oldChangeRate =
                (String) e.getBillCardPanel().getBodyValueAt(e.getRow(), mmgpNumAssNumMediator.getNchangerateKey());
        boolean isFixChangeRate =
                MMGPNumAssNumInnerUtils.isFixChangeRate(mmgpNumAssNumMediator, materialId, astUnitId);

        cond.setIsFixNchangerate(isFixChangeRate);

        cond.setIsCalLocalCurr(false);
        tool.calculateOnlyNumAssNumQtNum(cond, changeKey);

        this.calcWeight(e, changeKey);

        if (isFixChangeRate) {
            return;
        }

        /* Sep 27, 2013 wangweir 增加空处理 Begin */
        String newChangeRate =
                (String) e.getBillCardPanel().getBodyValueAt(e.getRow(), mmgpNumAssNumMediator.getNchangerateKey());
        if (MMStringUtil.isEmpty(newChangeRate)
            || MMStringUtil.isEmpty(oldChangeRate)
            || newChangeRate.equals(oldChangeRate)) {
            return;
        }

        // 处理其他字段
        for (MMGPNumAssnumCalPara numAssnumCalPara : this.mmgpNumAssNumMediator.getNumAssnumCalPara()) {
            if (numAssnumCalPara.equals(mmgpNumAssnumCalPara)) {
                continue;
            }
            calcNumAssNum(e, this.mmgpNumAssNumMediator.getNchangerateKey(), numAssnumCalPara);
        }
    }

    protected void calcWeight(CardBodyAfterEditEvent e,
                              String changeKey) {
        BillCardPanelDataSet billCardPanelDataSet =
                new BillCardPanelDataSet(e.getBillCardPanel(), e.getRow(), this.mmgpNumAssNumMediator);

        if (!MMGPNumAssNumInnerUtils.canCalcWeight(billCardPanelDataSet, this.mmgpNumAssNumMediator)) {
            return;
        }

        UFDouble unitWeight =
                ValueUtils.getUFDouble(e.getBillCardPanel().getBodyValueAt(
                    e.getRow(),
                    mmgpNumAssNumMediator.getUnitweight()));

        UFDouble num =
                ValueUtils.getUFDouble(e.getBillCardPanel().getBodyValueAt(
                    e.getRow(),
                    mmgpNumAssNumMediator.getNnumKey()));

        UFDouble totalWeight = null;
        if (unitWeight != null && num != null) {
            totalWeight = unitWeight.multiply(num);
        }

        e.getBillCardPanel().setBodyValueAt(totalWeight, e.getRow(), mmgpNumAssNumMediator.getNtotalweight());
    }

    /**
     * 物料改变
     * 
     * @param e
     * @return
     */
    protected int[] materialChanged(CardBodyAfterEditEvent e) {
        BillCardPanel billCardPanel = e.getBillCardPanel();
        RefMoreSelectedUtils utils = new RefMoreSelectedUtils(billCardPanel);
        int[] rows = utils.refMoreSelected(e.getRow(), e.getKey(), true);

        if (rows == null || rows.length == 0) {
            return new int[0];
        }

        String[] materialIDs = this.getMaterialIds(e, billCardPanel, rows);

        Map<String, String> material2AstUnitID = this.getMeasdoc(materialIDs);

        for (int row : rows) {
            // 清空相关字段
            this.clearValue(e, row, getClearItems());

            String materialId =
                    (String) billCardPanel.getBodyValueAt(row, this.mmgpNumAssNumMediator.getCmaterialvid());
            // 主计量单位
            String measDoc = (String) billCardPanel.getBodyValueAt(row, this.mmgpNumAssNumMediator.getCunitidKey());

            // 辅计量
            String astUnitID = this.getAstUnitID(material2AstUnitID, materialId, measDoc);

            // 换算率
            String changeRate = MMGPNumAssNumInnerUtils.getChangeRate(materialId, measDoc, astUnitID);

            billCardPanel.setBodyValueAt(measDoc, row, this.mmgpNumAssNumMediator.getCunitidKey(), e.getTableCode());
            billCardPanel.setBodyValueAt(
                astUnitID,
                row,
                this.mmgpNumAssNumMediator.getCastunitidKey(),
                e.getTableCode());
            billCardPanel.setBodyValueAt(
                changeRate,
                row,
                this.mmgpNumAssNumMediator.getNchangerateKey(),
                e.getTableCode());

            billCardPanel.getBillModel(e.getTableCode()).execEditFormulas(row);
        }
        billCardPanel.getBillModel(e.getTableCode()).loadLoadRelationItemValue(rows);
        return rows;
    }

    protected String[] getClearItems() {
        return MMGPNumAssNumInnerUtils.getClearItems(this.mmgpNumAssNumMediator);
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

    protected String[] getMaterialIds(CardBodyAfterEditEvent e,
                                      BillCardPanel billCardPanel,
                                      int[] rows) {
        String[] materialIds = new String[rows.length];
        for (int i = 0; i < rows.length; i++) {
            materialIds[i] = (String) billCardPanel.getBodyValueAt(rows[i], e.getKey());
        }
        return materialIds;
    }

    /**
     * @param e
     * @param itemkey
     * @param value
     */
    protected void setBodyValue(CardBodyAfterEditEvent e,
                                String itemkey,
                                String value) {
        e.getBillCardPanel().setBodyValueAt(value, e.getRow(), itemkey, e.getTableCode());
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

    /**
     * 清空值
     * 
     * @param e
     * @param row
     * @param clearKeys
     */
    private void clearValue(CardBodyAfterEditEvent e,
                            int row,
                            String[] clearKeys) {
        for (String clearKey : clearKeys) {
            if (clearKey == null) {
                continue;
            }

            e.getBillCardPanel().setBodyValueAt(null, row, clearKey, e.getTableCode());
        }
    }
}
