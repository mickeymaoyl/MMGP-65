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
 * ���ò�ѯ�����������ˡ� nc.ui.pubapp.uif2app.query2.refregion.CommonTwoLayerListener ������Ѿ�������ʹ���ˣ�������������һ������
 * 
 * @since 6.0
 * @author muxh,liubq
 */
public class CommonQueryFilter extends AbstractLinkageColumnListener {
    
    private CriteriaChangedEvent event = null;

    /**
     * ���캯��
     * 
     * @param qryCondDLGDelegator
     *        ��ѯģ��Ի������
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
     * ������֯���������������֯Ψһ������֯���ˣ����򣬰����Ź���
     * 
     * @param fathervalues
     *        ��֯
     * @param editor
     *        ��Ҫ���ù����������ֶ�
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
        // // ���֮ǰ��ֵ
        // refPane.getRefModel().clearData();
        // �������֯Ψһ����������֯���ˣ������ſɼ�
        if (diffValues.size() == 1) {
            refPane.setPk_org(fatherValues.get(0).getSqlString());
            refPane.setMultiCorpRef(false);
        } else {
            // refPane.setPk_org(null);
            refPane.setMultiCorpRef(true);
        }
    }

}
