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
 * 查询模板注册生成单元项工厂类
 * @author chendwa
 *
 */
public class MMGPUIFilterRefElementEditorFactory implements IFieldValueElementEditorFactory {

    // 要进行过滤的参照名列表
    protected List<String> fieldNameList = null;

    // 各个参照进行过滤的过滤条件
    protected List<String> wherepartList = null;

    // 各个参照的过滤条件过滤方式，true 为覆盖，false为追加
    protected List<Boolean> overwriteList = null;

    // 查询对话框
    protected final IQueryConditionDLG querydlg;

    /**
     * 
     * @param querydlg 查询对话框
     * @param fieldNameList　要进行过滤的参照名列表
     * @param wherepartList　各个参照进行过滤的过滤条件
     * @param overwrite　各个参照的过滤条件过滤方式，true 为覆盖，false为追加
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
     * 验证过滤参数是否正确
     * @param newFieldNameList　参照名称列表
     * @param newWherepartList　参照过滤条件列表
     * @param newOverwrite　各个参照的过滤条件过滤方式列表
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
     * 简要说明
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
     * @param conditon 过滤条件
     * @param overwrite　过滤方式　true 为覆盖，false为追加
     * @return
     */
    protected IFieldValueElementEditor buildRefElementEditor(FilterMeta meta,
                                                           String conditon,
                                                           Boolean overwrite) {
        UIRefPane refPane = new UIRefpaneCreator(querydlg.getQryCondEditor().getQueryContext()).createUIRefPane(meta);
        if (overwrite) {
            // 如果重写
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
