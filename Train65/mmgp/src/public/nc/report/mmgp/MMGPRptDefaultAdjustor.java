package nc.report.mmgp;

import java.io.Serializable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import nc.itf.iufo.freereport.extend.IAreaCondition;
import nc.itf.iufo.freereport.extend.IBusiFormat;
import nc.itf.iufo.freereport.extend.IReportDataAdjustor2;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.data.IRowData;
import nc.pub.smart.metadata.Field;
import nc.pub.smart.metadata.MetaData;
import nc.pub.smart.model.SmartModel;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.lang.UFDouble;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.FreeReportContextKey;
import com.ufida.report.anareport.areaset.AreaContentSet;
import com.ufida.report.anareport.areaset.AreaContentSetUtil;
import com.ufida.report.anareport.areaset.AreaFieldSet;
import com.ufida.report.anareport.base.BaseQueryCondition;
import com.ufida.report.anareport.data.DefaultRowData;
import com.ufida.report.anareport.data.MemoryRowData;
import com.ufida.report.anareport.model.AbsAnaReportModel;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.crosstable.CrossTableModel;

/**
 * 库存报表默认界面调试器
 * 
 * @since 6.0
 * @version 2011-4-14 上午10:38:38
 * @author jinjya
 */
public class MMGPRptDefaultAdjustor implements IReportDataAdjustor2, Serializable {

    private static final long serialVersionUID = 71901596692954125L;

    // 报表传递对象
    private transient MMGPReportContextWrapper tranMap;

    // 是否隐藏关联字段
    private transient boolean isHiddenRelatedField = false;

    private transient IContext context = null;

    private transient AbsAnaReportModel reportModel;

    private transient IAreaCondition areaCond;

    /**
     * 待隐藏字段
     * 
     * @return
     */
    protected String[] getHidenFields() {
        return null;
    }

    /**
     * 获得隐藏字段
     * 
     * @return
     */
    protected Field[] getHidenFieldAry() {
        return null;
    }

    @Override
    public void doAreaAdjust(IContext contexts,
                             String areaPK,
                             IAreaCondition areaConds,
                             AbsAnaReportModel reportModels) {
        this.context = contexts;
        this.areaCond = areaConds;
        this.reportModel = reportModels;
        // 区域引用的语义模型
        SmartModel smart = reportModels.getAreaData(areaPK).getSmartModel();
        if (smart == null) return;
        // 获取查询Where条件
        this.tranMap = new MMGPReportContextWrapper(contexts);
        String[] hidenFields = this.getHidenFields();
        Field[] hidenFieldAry = this.getHidenFieldAry();
        if (MMArrayUtil.isEmpty(hidenFields) && MMArrayUtil.isEmpty(hidenFieldAry)) {
            // 设置隐藏区域内容
            AreaContentSet contentSet = new AreaContentSet();
            contentSet.setAreaPk(areaPK);
            contentSet.setSmartModelDefID(smart.getId());
            contentSet.setDetailFldNames(null);
            if (this.isHiddenRelatedField) {
                AreaContentSetUtil.resetExCellByHideRelatedFields(contentSet, true, reportModels);
            } else {
                AreaContentSetUtil.resetExCellByHideFields(contentSet, true, reportModels);
            }
            return;
        }
        List<AreaFieldSet> AreaFieldList = new ArrayList<AreaFieldSet>();
        if (!MMArrayUtil.isEmpty(hidenFields)) {
            for (String field : hidenFields) {
                AreaFieldList.add(new AreaFieldSet(buildField(field)));
            }
        }
        if (!MMArrayUtil.isEmpty(hidenFieldAry)) {
            for (Field field : hidenFieldAry) {
                AreaFieldList.add(new AreaFieldSet(field));
            }
        }
        // 设置隐藏区域内容
        AreaContentSet contentSet = new AreaContentSet();
        contentSet.setAreaPk(areaPK);
        contentSet.setSmartModelDefID(smart.getId());
        contentSet.setDetailFldNames(AreaFieldList.toArray(new AreaFieldSet[0]));
        // 是否处理关联字段
        if (this.isHiddenRelatedField) {
            AreaContentSetUtil.resetExCellByHideRelatedFields(contentSet, true, reportModels);
        } else {
            AreaContentSetUtil.resetExCellByHideFields(contentSet, true, reportModels);
        }
    }

    /**
     * 此处风险太大！
     * 
     * @param key
     * @return
     */
    public static Field buildField(String key) {
        Field fd = new Field();
        fd.setFldname(key);
        if (key.startsWith("n")) fd.setDataType(Types.NUMERIC);
        else
            fd.setDataType(Types.VARCHAR);
        return fd;
    }

    @Override
    public void doReportAdjust(IContext contexts,
                               AnaReportModel reportModels) {
        return;
    }

    @Override
    public CrossTableModel doAdjustCrossHeader(String areaPK,
                                               IContext context,
                                               CrossTableModel crossTabel,
                                               AbsAnaReportModel reportModel) {
        return null;
    }

    /**
     * 数据集后处理
     * 
     * @return
     */
    protected void dataSetProcessor(DataSet ds,
                                    SmartModel smart) {
    }

    /**
     * 报表后处理数据集
     */
    @Override
    public final DataSet doAdjustDataSet(String areaPK,
                                         IContext context,
                                         DataSet ds,
                                         AbsAnaReportModel reportModel) {
        this.context = context;
        this.reportModel = reportModel;
        Object[][] datas = ds.getDatas();
        MetaData metaData = ds.getMetaData();
        if (datas == null || datas.length == 0) return ds;
        SmartModel smart = reportModel.getAreaData(areaPK).getSmartModel();
        if (smart == null) return ds;

        // 业务组后处理数据集
        this.dataSetProcessor(ds, smart);

        // 获取查询条件
        BaseQueryCondition condition =
                (BaseQueryCondition) context.getAttribute(FreeReportContextKey.KEY_IQUERYCONDITION);
        // if (condition == null || !(condition instanceof ReportQueryCondition)) {
        // return ds;
        // }
        this.tranMap = new MMGPReportContextWrapper(context);
        // 检查是否前处理精度
        // if (!this.tranMap.isPreScaleProcess()) {
        // return ds;
        // }
        IBusiFormat reportbusiformat = condition.getBusiFormat(areaPK, smart);
        if (!(reportbusiformat instanceof ReportScaleProcess)) {
            return ds;
        }
        // 获得报表定义精度策略
        ReportScaleProcess scale = (ReportScaleProcess) reportbusiformat;
        IRowData[] rows = new IRowData[datas.length];
        int length = rows.length;
        for (int i = 0; i < length; i++) {
            MemoryRowData rowData = new MemoryRowData(metaData, datas[i]);
            rows[i] = new DefaultRowData(rowData);
            for (int j = 0, count = datas[i].length; j < count; j++) {
                Object value = datas[i][j];
                if (!(value instanceof Number)) continue;
                Number num = (Number) value;
                // 转换成UFDOUBLE
                UFDouble ufValue = new UFDouble(num.toString());
                // 获取对应索引字段
                Field field = metaData.getField(j);
                if (field == null) continue;
                // 字段名
                String fldName = field.getFldname();
                // 取精度
                int digital = scale.getDataDigital(fldName, null, rows[i]);
                // 重新设置取精度后的数据
                datas[i][j] = this.getDigitUFDouble(ufValue, digital);
            }
        }
        return ds;
    }

    /**
     * @param ufValue
     * @param digital
     * @return
     */
    private Object getDigitUFDouble(UFDouble ufValue,
                                    int digital) {
        if (ufValue == null) {
            return null;
        }
        return ufValue.setScale(0 - digital, UFDouble.ROUND_HALF_UP);
    }

    public MMGPReportContextWrapper getTranMap() {
        return this.tranMap;
    }

    public boolean isHiddenRelatedField() {
        return this.isHiddenRelatedField;
    }

    public void setHiddenRelatedField(boolean isHiddenRelatedField) {
        this.isHiddenRelatedField = isHiddenRelatedField;
    }

    public IContext getContext() {
        return this.context;
    }

    public AbsAnaReportModel getReportModel() {
        return this.reportModel;
    }

    public IAreaCondition getAreaCond() {
        return this.areaCond;
    }
}
