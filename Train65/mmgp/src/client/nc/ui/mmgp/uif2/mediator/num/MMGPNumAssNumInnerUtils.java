package nc.ui.mmgp.uif2.mediator.num;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.pubitf.uapbd.IMaterialPubService_C;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.calculator.data.IDataSetForCal;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��������ʹ�õĹ����ࡣ������ݿ������Ч��������ʱ�����Ż�
 * </p>
 * 
 * @since �������� Jun 17, 2013
 * @author wangweir
 */
public class MMGPNumAssNumInnerUtils {

    private static final String DEFAULT_CHANGERATE = "1/1";

    /**
     * �������Ϲ��˸���������
     * 
     * @param astUnitItem
     * @param materialid
     */
    public static void filterAstUnitRef(BillItem astUnitItem,
                                        String materialid) {
        UIRefPane astUnitRefPane = (UIRefPane) astUnitItem.getComponent();
        if (astUnitRefPane == null) {
            return;
        }
        if (MMStringUtil.isEmptyWithTrim(materialid)) {
            astUnitRefPane.getRefModel().addWherePart(" and 1=1 ");
        } else {
            String filterSql =
                    "and pk_measdoc in (select pk_measdoc from "
                        + "bd_materialconvert where pk_material = '"
                        + materialid
                        + "' and dr = 0 union select pk_measdoc from "
                        + "bd_material where pk_material = '"
                        + materialid
                        + "')";
            astUnitRefPane.getRefModel().addWherePart(filterSql);
        }
    }

    /**
     * �������Ƿ�ɱ༭
     * 
     * @param mmgpNumAssNumMediator
     * @param pk_material
     * @param astUnitId
     * @param astUnitId
     * @return
     */
    public static boolean isChangeRateEditable(MMGPNumAssNumMediator mmgpNumAssNumMediator,
                                               String pk_material,
                                               String mainUnitId,
                                               String astUnitId) {
        /* Sep 27, 2013 wangweir ����������λһ��ʱ�������޸Ļ����� Begin */
        if (MMStringUtil.isNotEmpty(mainUnitId) && MMStringUtil.isNotEmpty(astUnitId) && mainUnitId.equals(astUnitId)) {
            return false;
        }
        /* Sep 27, 2013 wangweir End */
        return !isFixChangeRate(mmgpNumAssNumMediator, pk_material, astUnitId);
    }

    /**
     * �Ƿ�̶�������
     * 
     * @param mmgpNumAssNumMediator
     * @param pk_material
     * @param astUnitId
     * @return
     */
    public static boolean isFixChangeRate(MMGPNumAssNumMediator mmgpNumAssNumMediator,
                                          String pk_material,
                                          String astUnitId) {
        if (MMStringUtil.isEmptyWithTrim(pk_material) || MMStringUtil.isEmptyWithTrim(astUnitId)) {
            return true;
        }

        if (mmgpNumAssNumMediator.isForceFixChangeRate()) {
            return true;
        }

        boolean isFixed = true;
        try {
            isFixed =
                    NCLocator
                        .getInstance()
                        .lookup(IMaterialPubService_C.class)
                        .queryIsFixedRateByMaterialAndMeasdoc(pk_material, astUnitId);
        } catch (BusinessException e1) {
            ExceptionUtils.wrappException(e1);
        }
        return isFixed;
    }

    /**
     * ��������id�븨������ȡ������
     * 
     * @param materialID
     * @param astUnitID
     * @param astUnitID
     * @return
     */
    public static String getChangeRate(String materialID,
                                       String mainUnitID,
                                       String astUnitID) {
        if (MMStringUtil.isEmptyWithTrim(mainUnitID)
            || MMStringUtil.isEmptyWithTrim(astUnitID)
            || mainUnitID.equals(astUnitID)) {
            return DEFAULT_CHANGERATE;
        }

        String changeRate = null;
        try {
            changeRate =
                    NCLocator
                        .getInstance()
                        .lookup(IMaterialPubService_C.class)
                        .queryMainMeasRateByMaterialAndMeasdoc(materialID, astUnitID);
        } catch (BusinessException e1) {
            ExceptionUtils.wrappException(e1);
        }
        return changeRate;
    }

    /**
     * ��������id����������λ���ͻ�ȡ����������
     * 
     * @param materialIds
     * @param materialConvertType
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> queryMeasdocIDByPksAndType(String[] materialIds,
                                                                 int materialConvertType) {
        try {
            return NCLocator
                .getInstance()
                .lookup(IMaterialPubService_C.class)
                .queryMeasdocIDByPksAndType(materialIds, materialConvertType);
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        return Collections.EMPTY_MAP;
    }

    /**
     * �Ƿ���Ҫ��������
     * 
     * @param dataSetForCal
     * @param mmgpNumAssNumMediator
     * @return
     */
    public static boolean canCalcWeight(IDataSetForCal dataSetForCal,
                                        MMGPNumAssNumMediator mmgpNumAssNumMediator) {
        boolean hasUnitWeight =
                mmgpNumAssNumMediator.getUnitweight() != null
                    && dataSetForCal.hasItemKey(mmgpNumAssNumMediator.getUnitweight());
        boolean hasTotalWeight =
                mmgpNumAssNumMediator.getNtotalweight() != null
                    && dataSetForCal.hasItemKey(mmgpNumAssNumMediator.getNtotalweight());
        boolean hasNum =
                mmgpNumAssNumMediator.getNnumKey() != null
                    && dataSetForCal.hasItemKey(mmgpNumAssNumMediator.getNnumKey());

        return hasUnitWeight && hasTotalWeight && hasNum;
    }

    public static void configureNumAssNumMediator(MMGPNumAssNumMediator mmgpNumAssNumMediator,
                                                  MMGPNumAssnumCalPara mmgpNumAssnumCalPara) {
        mmgpNumAssNumMediator.setnumKey(mmgpNumAssnumCalPara.getNnum());
        mmgpNumAssNumMediator.setNassistnumKey(mmgpNumAssnumCalPara.getNassistnum());
        mmgpNumAssNumMediator.setNtotalweight(mmgpNumAssnumCalPara.getNtotalweight());
    }

    /**
     * @param mmgpNumAssNumMediator
     * @param changeKey
     * @return
     */
    public static MMGPNumAssnumCalPara getNumKeyChanged(MMGPNumAssNumMediator mmgpNumAssNumMediator,
                                                        String changeKey) {
        for (MMGPNumAssnumCalPara numAssnumCalPara : mmgpNumAssNumMediator.getNumAssnumCalPara()) {
            if (numAssnumCalPara.getNnum().equals(changeKey)) {
                return numAssnumCalPara;
            }
        }
        return null;
    }

    /**
     * @param mmgpNumAssNumMediator
     * @param changeKey
     * @return
     */
    public static MMGPNumAssnumCalPara getAssNumKeyChanged(MMGPNumAssNumMediator mmgpNumAssNumMediator,
                                                           String changeKey) {
        for (MMGPNumAssnumCalPara numAssnumCalPara : mmgpNumAssNumMediator.getNumAssnumCalPara()) {
            if (numAssnumCalPara.getNassistnum().equals(changeKey)) {
                return numAssnumCalPara;
            }
        }
        return null;
    }

    /**
     * ��ȡ�����л�ʱ��Ҫ��յ��ֶ�
     * 
     * @param mmgpNumAssNumMediator
     * @return
     */
    public static String[] getClearItems(MMGPNumAssNumMediator mmgpNumAssNumMediator) {
        Set<String> cleanItems = new HashSet<String>();
        for (MMGPNumAssnumCalPara para : mmgpNumAssNumMediator.getNumAssnumCalPara()) {
            cleanItems.add(para.getNnum());
            cleanItems.add(para.getNassistnum());
            cleanItems.add(para.getNtotalweight());
        }
        cleanItems.add(mmgpNumAssNumMediator.getCastunitidKey());
        cleanItems.add(mmgpNumAssNumMediator.getNnumKey());
        cleanItems.add(mmgpNumAssNumMediator.getNassistnumKey());
        cleanItems.add(mmgpNumAssNumMediator.getNchangerateKey());
        cleanItems.add(mmgpNumAssNumMediator.getNtotalweight());

        return cleanItems.toArray(new String[0]);
    }
}
