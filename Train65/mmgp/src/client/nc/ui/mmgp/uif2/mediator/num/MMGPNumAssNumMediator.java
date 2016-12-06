package nc.ui.mmgp.uif2.mediator.num;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;
import nc.ui.pubapp.uif2app.model.IAppModelEx;
import nc.ui.uif2.editor.IBillCardPanelEditor;
import nc.vo.pubapp.calculator.data.RelationItemForCal;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 主辅计量公共mediator,暂时不支持表头
 * </p>
 * 
 * @since 创建日期 Jun 13, 2013
 * @author wangweir
 */
public class MMGPNumAssNumMediator extends RelationItemForCal {

    /**
     * 未定义的辅计量单位类型
     */
    public static final int UNDEFINE_CONVERTTYPE = -1;

    /**
     * 卡片编辑接口
     */
    private IBillCardPanelEditor editor;

    private IAppModelEx model;

    /* 主辅计量相关字段，工具类给出默认值，如果字段与默认值不一致，请注入 */

    /**
     * 物料档案id
     */
    private String cmaterialvid = "cmaterialvid";

    /**
     * 单位重量
     */
    private String unitweight = "cmaterialvid.unitweight";

    /**
     * 总重量
     */
    private String ntotalweight = "ntotalweight";

    /* Jun 13, 2013 wangweir End */

    private boolean isMaterialMultSelect = true;

    /**
     * 辅计量单位类型;默认-1表示对类型不做控制
     * <p>
     * /** 物料基本信息-辅计量单位类型-采购默认单位 public static final int MATERIAL_CONVERT_PU = 1;
     * </p>
     * <p>
     * /** 物料基本信息-辅计量单位类型-生产默认单位 public static final int MATERIAL_CONVERT_PROD = 2;
     * </p>
     * <p>
     * /** 物料基本信息-辅计量单位类型-库存默认单位 public static final int MATERIAL_CONVERT_STOCK = 3;
     * </p>
     * <p>
     * /** 物料基本信息-辅计量单位类型-销售默认单位 public static final int MATERIAL_CONVERT_SALE = 4;
     * </p>
     * <p>
     * /** 物料基本信息-辅计量单位类型-零售默认单位 public static final int MATERIAL_CONVERT_RETAIL = 5;
     * </p>
     * <p>
     * /** 物料基本信息-辅计量单位类型-件数管理 public static final int MATERIAL_CONVERT_PIECE = 6;
     * </p>
     * 
     * @see nc.pubitf.uapbd.IMaterialPubService_C
     */
    private int materialConvertType = UNDEFINE_CONVERTTYPE;

    /**
     * 强制使用固定换算率逻辑，及强制换算率不允许修改
     */
    private boolean isForceFixChangeRate = false;

    /**
     * 开启表头主辅计量事件,默认关闭
     */
    private boolean supportHeadEvent = false;

    /**
     * 开启表体主辅计量事件
     */
    private boolean supportBodyEvent = true;

    /**
     * 
     */
    private List<MMGPNumAssnumCalPara> numAssnumCalPara;

    /**
     * 
     */
    private IMaterialMultSelectProcess materialMultSelectProcess;

    /**
     * 初始化工具类
     */
    public void init() {
        if (this.supportBodyEvent) {
            this.getModel().addAppEventListener(
                CardBodyBeforeEditEvent.class,
                new MMGPNumAssNumBodyBeforeEditHandler(this));
            this.getModel().addAppEventListener(
                CardBodyAfterEditEvent.class,
                new MMGPNumAssNumBodyAfterEditHandler(this));
        }

        if (this.supportHeadEvent) {
            this.getModel().addAppEventListener(
                CardHeadTailBeforeEditEvent.class,
                new MMGPNumAssNumHeadBeforeEditHandler(this));
            this.getModel().addAppEventListener(
                CardHeadTailAfterEditEvent.class,
                new MMGPNumAssNumHeadAfterEditHandler(this));
        }

        MMGPNumAssnumCalPara defaultNumAssCalNum = new MMGPNumAssnumCalPara();
        defaultNumAssCalNum.setNassistnum(this.getNassistnumKey());
        defaultNumAssCalNum.setNnum(this.getNnumKey());
        defaultNumAssCalNum.setNtotalweight(this.getNtotalweight());
        this.getNumAssnumCalPara().add(defaultNumAssCalNum);
    }

    /**
     * @return the editor
     */
    public IBillCardPanelEditor getEditor() {
        return editor;
    }

    /**
     * @param editor
     *        the editor to set
     */
    public void setEditor(IBillCardPanelEditor editor) {
        this.editor = editor;
    }

    /**
     * @return the cmaterialvid
     */
    public String getCmaterialvid() {
        return cmaterialvid;
    }

    /**
     * @param cmaterialvid
     *        the cmaterialvid to set
     */
    public void setCmaterialvid(String cmaterialvid) {
        this.cmaterialvid = cmaterialvid;
    }

    /**
     * @return the model
     */
    public IAppModelEx getModel() {
        return model;
    }

    /**
     * @param model
     *        the model to set
     */
    public void setModel(IAppModelEx model) {
        this.model = model;
    }

    /**
     * @return the isMaterialMultSelect
     */
    public boolean isMaterialMultSelect() {
        return isMaterialMultSelect;
    }

    /**
     * @param isMaterialMultSelect
     *        the isMaterialMultSelect to set
     */
    public void setMaterialMultSelect(boolean isMaterialMultSelect) {
        this.isMaterialMultSelect = isMaterialMultSelect;
    }

    /**
     * @return the materialConvertType
     */
    public int getMaterialConvertType() {
        return materialConvertType;
    }

    /**
     * @param materialConvertType
     *        the materialConvertType to set
     */
    public void setMaterialConvertType(int materialConvertType) {
        this.materialConvertType = materialConvertType;
    }

    /**
     * @return the isForceFixChangeRate
     */
    public boolean isForceFixChangeRate() {
        return isForceFixChangeRate;
    }

    /**
     * @param isForceFixChangeRate
     *        the isForceFixChangeRate to set
     */
    public void setForceFixChangeRate(boolean isForceFixChangeRate) {
        this.isForceFixChangeRate = isForceFixChangeRate;
    }

    /**
     * @return the supportHeadEvent
     */
    public boolean isSupportHeadEvent() {
        return supportHeadEvent;
    }

    /**
     * @param supportHeadEvent
     *        the supportHeadEvent to set
     */
    public void setSupportHeadEvent(boolean supportHeadEvent) {
        this.supportHeadEvent = supportHeadEvent;
    }

    /**
     * @return the supportBodyEvent
     */
    public boolean isSupportBodyEvent() {
        return supportBodyEvent;
    }

    /**
     * @param supportBodyEvent
     *        the supportBodyEvent to set
     */
    public void setSupportBodyEvent(boolean supportBodyEvent) {
        this.supportBodyEvent = supportBodyEvent;
    }

    /**
     * @return the unitweight
     */
    public String getUnitweight() {
        return unitweight;
    }

    /**
     * @param unitweight
     *        the unitweight to set
     */
    public void setUnitweight(String unitweight) {
        this.unitweight = unitweight;
    }

    /**
     * @return the numAssnumCalPara
     */
    public List<MMGPNumAssnumCalPara> getNumAssnumCalPara() {
        if (this.numAssnumCalPara == null) {
            this.numAssnumCalPara = new ArrayList<MMGPNumAssnumCalPara>();
        }
        return numAssnumCalPara;
    }

    /**
     * @param numAssnumCalPara
     *        the numAssnumCalPara to set
     */
    public void setNumAssnumCalPara(List<MMGPNumAssnumCalPara> numAssnumCalPara) {
        this.numAssnumCalPara = numAssnumCalPara;
    }

    /**
     * @return the ntotalweight
     */
    public String getNtotalweight() {
        return ntotalweight;
    }

    /**
     * @param ntotalweight
     *        the ntotalweight to set
     */
    public void setNtotalweight(String ntotalweight) {
        this.ntotalweight = ntotalweight;
    }

    public void afterMaterialChanged(CardBodyAfterEditEvent e,
                                     int[] changedRows) {
        if (this.getMaterialMultSelectProcess() == null) {
            return;
        }
        this.getMaterialMultSelectProcess().afterMaterialMultSelected(e, changedRows);
    }

    /**
     * @return the materialMultSelectProcess
     */
    public IMaterialMultSelectProcess getMaterialMultSelectProcess() {
        return materialMultSelectProcess;
    }

    /**
     * @param materialMultSelectProcess
     *        the materialMultSelectProcess to set
     */
    public void setMaterialMultSelectProcess(IMaterialMultSelectProcess materialMultSelectProcess) {
        this.materialMultSelectProcess = materialMultSelectProcess;
    }

}
