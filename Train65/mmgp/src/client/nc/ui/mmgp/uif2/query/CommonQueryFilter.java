package nc.ui.mmgp.uif2.query;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.query2.QueryConditionDLGDelegator;
import nc.ui.pubapp.uif2app.query2.refregion.AbstractLinkageColumnListener;
import nc.ui.querytemplate.CriteriaChangedEvent;
import nc.ui.querytemplate.filtereditor.FilterEditorWrapper;
import nc.ui.querytemplate.filtereditor.IFilterEditor;
import nc.ui.querytemplate.value.IFieldValueElement;
import nc.ui.querytemplate.valueeditor.ref.CompositeRefPanel;

/**
 * 设置查询条件关联过滤。 nc.ui.pubapp.uif2app.query2.refregion.CommonTwoLayerListener 这个类已经不建议使用了，所以做了这样一个基类
 * 
 * @since 6.0
 * @author muxh,liubq
 */
public class CommonQueryFilter extends AbstractLinkageColumnListener {
    
    private CriteriaChangedEvent event = null;

    /**
     * 构造函数
     * 
     * @param qryCondDLGDelegator
     *        查询模板对话框代理
     */
    public CommonQueryFilter(QueryConditionDLGDelegator qryCondDLGDelegator) {
        super(qryCondDLGDelegator);
    }

    @Override
    public void criteriaChanged(CriteriaChangedEvent event) {
        super.criteriaChanged(event);
        this.setEvent(event);
    }

    /**
     * @param event
     */
    protected void setEvent(CriteriaChangedEvent event) {
        this.event = event;
    }

    /**
     * @return
     */
    protected CriteriaChangedEvent getEvent() {
        return this.event;
    }

    /**
     * 设置组织过滤条件，如果组织唯一，该组织过滤；否则，按集团过滤
     * 
     * @param fathervalues
     *        组织
     * @param editor
     *        需要设置过滤条件的字段
     */
    @Override
    protected void processLinkageLogic(List<IFieldValueElement> fatherValues,
                                       IFilterEditor editor) {
        List<String> diffValues = new ArrayList<String>();
        for (IFieldValueElement fve : fatherValues) {
            if (fve.getSqlString() == null) {
                continue;
            }
            if (diffValues.contains(fve.getSqlString())) {
                continue;
            }

            diffValues.add(fve.getSqlString());
        }

        FilterEditorWrapper wrapper = new FilterEditorWrapper(editor);
        JComponent component = wrapper.getFieldValueElemEditorComponent();
        UIRefPane refPane = null;
        if (component instanceof UIRefPane) {
            refPane = (UIRefPane) component;
        } else if (component instanceof CompositeRefPanel) {
            refPane = ((CompositeRefPanel) component).getStdRefPane();
        }
        if (refPane == null) {
            return;
        }
        if (refPane.getRefModel() == null) {
            return;
        }
        // // 清空之前的值
        // refPane.getRefModel().clearData();
        // 如果主组织唯一，则按照主组织过滤；否则集团可见
        if (diffValues.size() == 1) {
            refPane.setPk_org(fatherValues.get(0).getSqlString());
            refPane.setMultiCorpRef(false);
        } else {
            // refPane.setPk_org(null);
            refPane.setMultiCorpRef(true);
        }
    }

}
