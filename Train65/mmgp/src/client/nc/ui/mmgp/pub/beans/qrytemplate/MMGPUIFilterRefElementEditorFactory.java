package nc.ui.mmgp.pub.beans.qrytemplate;

import java.util.List;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.querytemplate.IQueryConditionDLG;
import nc.ui.querytemplate.meta.FilterMeta;
import nc.ui.querytemplate.valueeditor.IFieldValueElementEditor;
import nc.ui.querytemplate.valueeditor.IFieldValueElementEditorFactory;
import nc.ui.querytemplate.valueeditor.RefElementEditor;
import nc.ui.querytemplate.valueeditor.UIRefpaneCreator;
import nc.vo.mmgp.util.MMStringUtil;

/**
 * ��ѯģ��ע�����ɵ�Ԫ�����
 * @author chendwa
 *
 */
public class MMGPUIFilterRefElementEditorFactory implements IFieldValueElementEditorFactory {

    // Ҫ���й��˵Ĳ������б�
    protected List<String> fieldNameList = null;

    // �������ս��й��˵Ĺ�������
    protected List<String> wherepartList = null;

    // �������յĹ����������˷�ʽ��true Ϊ���ǣ�falseΪ׷��
    protected List<Boolean> overwriteList = null;

    // ��ѯ�Ի���
    protected final IQueryConditionDLG querydlg;

    /**
     * 
     * @param querydlg ��ѯ�Ի���
     * @param fieldNameList��Ҫ���й��˵Ĳ������б�
     * @param wherepartList���������ս��й��˵Ĺ�������
     * @param overwrite���������յĹ����������˷�ʽ��true Ϊ���ǣ�falseΪ׷��
     */
    public MMGPUIFilterRefElementEditorFactory(IQueryConditionDLG querydlg,
                                               List<String> fieldNameList,
                                               List<String> wherepartList,
                                               List<Boolean> overwrite) {
        this.querydlg = querydlg;
        this.fieldNameList = fieldNameList;
        this.wherepartList = wherepartList;
        this.overwriteList = overwrite;

    }

    /**
     * ��֤���˲����Ƿ���ȷ
     * @param newFieldNameList�����������б�
     * @param newWherepartList�����չ��������б�
     * @param newOverwrite���������յĹ����������˷�ʽ�б�
     * @return
     */
    private boolean validateFilterPara(List<String> newFieldNameList,
                                       List<String> newWherepartList,
                                       List<Boolean> newOverwrite) {
        if (newFieldNameList.isEmpty() || newWherepartList.isEmpty() || newOverwrite.isEmpty()) {
            return false;
        }
        int nameNum = newFieldNameList.size();
        int wherePartNum = newWherepartList.size();
        int overwritenum = newOverwrite.size();

        if (nameNum != wherePartNum && wherePartNum != overwritenum) {
            return false;
        }
        return true;
    }

    /**
     * 
     * ��Ҫ˵��
     */
    public IFieldValueElementEditor createFieldValueElementEditor(FilterMeta meta) {
        boolean result = validateFilterPara(fieldNameList, wherepartList, overwriteList);
        
        if (!result) {
            throw new IllegalArgumentException();
        }

        IFieldValueElementEditor iFieldValueElementEditor = null;

        for (int i = 0; i < fieldNameList.size(); i++) {
            String name = fieldNameList.get(i);
            if (name.equals(meta.getFieldCode())) {
                String condition = wherepartList.get(i);
                Boolean overwrite = overwriteList.get(i);
                iFieldValueElementEditor = buildRefElementEditor(meta, condition, overwrite);
                break;
            }
        }
        
        return iFieldValueElementEditor;
    }

    /**
     * 
     * @param meta
     * @param conditon ��������
     * @param overwrite�����˷�ʽ��true Ϊ���ǣ�falseΪ׷��
     * @return
     */
    protected IFieldValueElementEditor buildRefElementEditor(FilterMeta meta,
                                                           String conditon,
                                                           Boolean overwrite) {
        UIRefPane refPane = new UIRefpaneCreator(querydlg.getQryCondEditor().getQueryContext()).createUIRefPane(meta);
        if (overwrite) {
            // �����д
            refPane.setWhereString(conditon);
        } else {
            String originalConditon = refPane.getRefModel().getWherePart();
            String appendCondtionString = conditon;
            if(MMStringUtil.isEmpty(originalConditon)) {
            	refPane.setWhereString(appendCondtionString);
            } else {
            	refPane.setWhereString(originalConditon + " AND "+ appendCondtionString);
            }
        }
        return new RefElementEditor(refPane, meta.getReturnType());
    }
}
