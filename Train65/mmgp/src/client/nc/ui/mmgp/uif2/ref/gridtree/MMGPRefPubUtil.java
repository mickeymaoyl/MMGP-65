package nc.ui.mmgp.uif2.ref.gridtree;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.IRefConst;
import nc.vo.bd.ref.RefcolumnVO;
import nc.vo.bd.ref.ReftableVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

public class MMGPRefPubUtil {
    public static RefcolumnVO[] getTableColumnSequences(AbstractRefModel model) {

        if (model == null) {
            return null;
        }
        RefcolumnVO[] vos = null;
        ReftableVO vo = ((MMGPTreeGridRefModel) model).getTableRefTableVO(model.getPk_corp());

        // ������û�����Ŀ��Ϣ
        if (vo != null) {

            vos = vo.getColumnVOs();
            if (vos == null || vos.length <= 0) {
                return vos;
            }
            nc.vo.bd.ref.RefcolumnVO columnvo = null;
            int length = vos.length;
            // �������󣬼�����
            for (int i = 0; i < length - 1; i++) {

                for (int j = i; j < length; j++) {
                    int i_index = vos[i].getLocateshowindex().intValue();
                    int j_index = vos[j].getLocateshowindex().intValue();
                    if (i_index > j_index) {
                        //
                        columnvo = vos[i];
                        vos[i] = vos[j];
                        vos[j] = columnvo;
                    }
                }

            }
            // ����resid
            for (int i = 0; i < vos.length; i++) {

                vos[i].setResid(model.getFieldShowName(vos[i].getFieldname()));
            }

            // �û�û�в�����Ŀ����Ĭ�ϵ���Ŀ��Ϣ
        } else {
            vo = getTableNewReftableVO(null, model);
            vos = vo.getColumnVOs();

        }

        return vos;

    }

    /**
     * <p>
     * <strong>����޸��ˣ�sxj</strong>
     * <p>
     * <strong>����޸����ڣ�2006-4-17</strong>
     * <p>
     * 
     * @param
     * @return void
     * @exception BusinessException
     * @since NC5.0
     */
    public static ReftableVO getTableNewReftableVO(ReftableVO groupVO,
                                                   AbstractRefModel arefModel) {
        MMGPTreeGridRefModel refModel = (MMGPTreeGridRefModel) arefModel;
        ReftableVO reftableVO = null;
        if (refModel == null) {
            return null;
        }
        // �����Ե�ԭ��ÿ��Ҫ����ȡfieldName
        // refModel.setFieldName(null);
        // refModel.resetFieldName();
        // ������Ϣ
        reftableVO = new ReftableVO();
        reftableVO.setPk_corp(refModel.getPk_corp());
        reftableVO.setDefaultfieldcount(Integer.valueOf(refModel.getTableDefaultFieldCount()));
        reftableVO.setRefnodename(refModel.getTableRefNodeName());
        reftableVO.setReftablename(refModel.getTablebean().getTable().getName());
        reftableVO.setWherepart(refModel.getTableWherePart());

        // �ӱ���Ϣ refColumn
        int lencode = refModel.getTableFieldCode().length;
        int hiddenLenth = refModel.getTableHiddenFieldCode() == null ? 0 : refModel.getTableHiddenFieldCode().length;
        int length = lencode + hiddenLenth;
        boolean isColumnShow = false;
        RefcolumnVO[] vos = new RefcolumnVO[length];
        for (int i = 0; i < length; i++) {
            vos[i] = new RefcolumnVO();
            vos[i].setTablename(refModel.getTablebean().getTable().getName());
            vos[i].setColumnshowindex(Integer.valueOf(i));
            vos[i].setLocateshowindex(Integer.valueOf(i));
            vos[i].setIspkfield(UFBoolean.valueOf(false));
            vos[i].setIshiddenfield(UFBoolean.valueOf(false));
            // �ֶ�����
            setFieldType(groupVO, refModel, vos, i);

            if (i >= lencode) {// ������

                isColumnShow = false;

                vos[i].setFieldname(refModel.getTableHiddenFieldCode()[i - lencode]);
                vos[i].setIshiddenfield(UFBoolean.valueOf(true));
                // ������
                if (refModel.getTablePkFieldCode().equals(refModel.getTableHiddenFieldCode()[i - lencode])) {
                    vos[i].setIspkfield(UFBoolean.valueOf(true));
                }

            } else {//
                if (i < refModel.getTableDefaultFieldCount()) {
                    isColumnShow = true;
                } else {
                    isColumnShow = false;
                }
                vos[i].setFieldname(refModel.getTableFieldCode()[i]);

                if (i < refModel.getFieldName().length) {
                    vos[i].setFieldshowname(refModel.getTableFieldName()[i]);

                } else {
                    vos[i].setFieldshowname(refModel.getTableFieldCode()[i]);
                }

                vos[i].setResid(refModel.getTableFieldShowName(refModel.getTableFieldCode()[i]));
            }

            vos[i].setIscolumnshow(UFBoolean.valueOf(isColumnShow));
            vos[i].setIslocateshow(UFBoolean.valueOf(isColumnShow));

        }
        reftableVO.setColumnVOs(vos);

        return reftableVO;
    }

    private static void setFieldType(ReftableVO groupVO,
                                     AbstractRefModel refModel,
                                     RefcolumnVO[] vos,
                                     int i) {
        if (groupVO != null && groupVO.getColumnVOs() != null && i < groupVO.getColumnVOs().length) {

            vos[i].setDatatype(groupVO.getColumnVOs()[i].getDatatype());

        } else {
            if (refModel.getIntFieldType() == null) {
                vos[i].setDatatype(Integer.valueOf(IRefConst.CHARTYPE)); // Ĭ��Ϊ0,�ַ���
            } else {
                vos[i].setDatatype(Integer.valueOf(refModel.getIntFieldType()[i]));
            }

        }
    }
    
    public static RefcolumnVO[] getColumnSequences(AbstractRefModel model) {

        if (model == null) {
            return null;
        }
        RefcolumnVO[] vos = null;
        ReftableVO vo = model.getRefTableVO(model.getPk_corp());

        // ������û�����Ŀ��Ϣ
        if (vo != null) {

            vos = vo.getColumnVOs();
            if (vos == null || vos.length <= 0) {
                return vos;
            }
            nc.vo.bd.ref.RefcolumnVO columnvo = null;
            int length = vos.length;
            // �������󣬼�����
            for (int i = 0; i < length - 1; i++) {

                for (int j = i; j < length; j++) {
                    int i_index = vos[i].getLocateshowindex().intValue();
                    int j_index = vos[j].getLocateshowindex().intValue();
                    if (i_index > j_index) {
                        //
                        columnvo = vos[i];
                        vos[i] = vos[j];
                        vos[j] = columnvo;
                    }
                }

            }
            // ����resid
            for (int i = 0; i < vos.length; i++) {

                vos[i].setResid(model.getFieldShowName(vos[i].getFieldname()));
            }

            // �û�û�в�����Ŀ����Ĭ�ϵ���Ŀ��Ϣ
        } else {
            vo = getNewReftableVO(null, model);
            vos = vo.getColumnVOs();

        }

        return vos;

    }
    
    /**
     * <p>
     * <strong>����޸��ˣ�sxj</strong>
     * <p>
     * <strong>����޸����ڣ�2006-4-17</strong>
     * <p>
     * 
     * @param
     * @return void
     * @exception BusinessException
     * @since NC5.0
     */
    public static ReftableVO getNewReftableVO(ReftableVO groupVO,
            AbstractRefModel refModel) {
        ReftableVO reftableVO = null;
        if (refModel == null) {
            return null;
        }
        // �����Ե�ԭ��ÿ��Ҫ����ȡfieldName
        // refModel.setFieldName(null);
        // refModel.resetFieldName();
        // ������Ϣ
        reftableVO = new ReftableVO();
        reftableVO.setPk_corp(refModel.getPk_corp());
        reftableVO.setDefaultfieldcount(Integer.valueOf(refModel
                .getDefaultFieldCount()));
        reftableVO.setRefnodename(refModel.getRefNodeName());
        reftableVO.setReftablename(refModel.getTableName());
        reftableVO.setWherepart(refModel.getWherePart());

        // �ӱ���Ϣ refColumn
        int lencode = refModel.getFieldCode().length;
        int hiddenLenth = refModel.getHiddenFieldCode() == null ? 0 : refModel
                .getHiddenFieldCode().length;
        int length = lencode + hiddenLenth;
        boolean isColumnShow = false;
        RefcolumnVO[] vos = new RefcolumnVO[length];
        for (int i = 0; i < length; i++) {
            vos[i] = new RefcolumnVO();
            vos[i].setTablename(refModel.getTableName());
            vos[i].setColumnshowindex(Integer.valueOf(i));
            vos[i].setLocateshowindex(Integer.valueOf(i));
            vos[i].setIspkfield(UFBoolean.valueOf(false));
            vos[i].setIshiddenfield(UFBoolean.valueOf(false));
            // �ֶ�����
            setFieldType(groupVO, refModel, vos, i);

            if (i >= lencode) {// ������

                isColumnShow = false;

                vos[i].setFieldname(refModel.getHiddenFieldCode()[i - lencode]);
                vos[i].setIshiddenfield(UFBoolean.valueOf(true));
                // ������
                if (refModel.getPkFieldCode().equals(
                        refModel.getHiddenFieldCode()[i - lencode])) {
                    vos[i].setIspkfield(UFBoolean.valueOf(true));
                }

            } else {//
                if (i < refModel.getDefaultFieldCount()) {
                    isColumnShow = true;
                } else {
                    isColumnShow = false;
                }
                vos[i].setFieldname(refModel.getFieldCode()[i]);

                if (i < refModel.getFieldName().length) {
                    vos[i].setFieldshowname(refModel.getFieldName()[i]);

                } else {
                    vos[i].setFieldshowname(refModel.getFieldCode()[i]);
                }

                vos[i].setResid(refModel.getFieldShowName(refModel
                        .getFieldCode()[i]));
            }

            vos[i].setIscolumnshow(UFBoolean.valueOf(isColumnShow));
            vos[i].setIslocateshow(UFBoolean.valueOf(isColumnShow));

        }
        reftableVO.setColumnVOs(vos);

        return reftableVO;
    }
}
