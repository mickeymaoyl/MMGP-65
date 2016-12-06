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
 * ��汨��Ĭ�Ͻ��������
 * 
 * @since 6.0
 * @version 2011-4-14 ����10:38:38
 * @author jinjya
 */
public class MMGPRptDefaultAdjustor implements IReportDataAdjustor2, Serializable {

    private static final long serialVersionUID = 71901596692954125L;

    // �����ݶ���
    private transient MMGPReportContextWrapper tranMap;

    // �Ƿ����ع����ֶ�
    private transient boolean isHiddenRelatedField = false;

    private transient IContext context = null;

    private transient AbsAnaReportModel reportModel;

    private transient IAreaCondition areaCond;

    /**
     * �������ֶ�
     * 
     * @return
     */
    protected String[] getHidenFields() {
        return null;
    }

    /**
     * ��������ֶ�
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
        // �������õ�����ģ��
        SmartModel smart = reportModels.getAreaData(areaPK).getSmartModel();
        if (smart == null) return;
        // ��ȡ��ѯWhere����
        this.tranMap = new MMGPReportContextWrapper(contexts);
        String[] hidenFields = this.getHidenFields();
        Field[] hidenFieldAry = this.getHidenFieldAry();
        if (MMArrayUtil.isEmpty(hidenFields) && MMArrayUtil.isEmpty(hidenFieldAry)) {
            // ����������������
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
        // ����������������
        AreaContentSet contentSet = new AreaContentSet();
        contentSet.setAreaPk(areaPK);
        contentSet.setSmartModelDefID(smart.getId());
        contentSet.setDetailFldNames(AreaFieldList.toArray(new AreaFieldSet[0]));
        // �Ƿ�������ֶ�
        if (this.isHiddenRelatedField) {
            AreaContentSetUtil.resetExCellByHideRelatedFields(contentSet, true, reportModels);
        } else {
            AreaContentSetUtil.resetExCellByHideFields(contentSet, true, reportModels);
        }
    }

    /**
     * �˴�����̫��
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
     * ���ݼ�����
     * 
     * @return
     */
    protected void dataSetProcessor(DataSet ds,
                                    SmartModel smart) {
    }

    /**
     * ����������ݼ�
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

        // ҵ����������ݼ�
        this.dataSetProcessor(ds, smart);

        // ��ȡ��ѯ����
        BaseQueryCondition condition =
                (BaseQueryCondition) context.getAttribute(FreeReportContextKey.KEY_IQUERYCONDITION);
        // if (condition == null || !(condition instanceof ReportQueryCondition)) {
        // return ds;
        // }
        this.tranMap = new MMGPReportContextWrapper(context);
        // ����Ƿ�ǰ������
        // if (!this.tranMap.isPreScaleProcess()) {
        // return ds;
        // }
        IBusiFormat reportbusiformat = condition.getBusiFormat(areaPK, smart);
        if (!(reportbusiformat instanceof ReportScaleProcess)) {
            return ds;
        }
        // ��ñ����徫�Ȳ���
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
                // ת����UFDOUBLE
                UFDouble ufValue = new UFDouble(num.toString());
                // ��ȡ��Ӧ�����ֶ�
                Field field = metaData.getField(j);
                if (field == null) continue;
                // �ֶ���
                String fldName = field.getFldname();
                // ȡ����
                int digital = scale.getDataDigital(fldName, null, rows[i]);
                // ��������ȡ���Ⱥ������
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
