package nc.ui.mmgp.uif2.query;

import java.util.List;

import nc.ui.pubapp.uif2app.query2.QueryConditionDLGDelegator;
import nc.ui.querytemplate.filtereditor.IFilterEditor;
import nc.ui.querytemplate.value.IFieldValueElement;
import nc.vo.mmgp.MMGlobalConst;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Jul 9, 2013
 * @author wangweir
 */
public class MMGPOrgFilterWithMultiCorp extends CommonQueryFilter {


    /**
     * @param qryCondDLGDelegator
     */
    public MMGPOrgFilterWithMultiCorp(QueryConditionDLGDelegator qryCondDLGDelegator) {
        super(qryCondDLGDelegator);
        this.setFatherPath(MMGlobalConst.PK_ORG);
        qryCondDLGDelegator.registerCriteriaEditorListener(this);
    }
    
    public MMGPOrgFilterWithMultiCorp(QueryConditionDLGDelegator qryCondDLGDelegator, String orgField) {
        super(qryCondDLGDelegator);
        this.setFatherPath(orgField);
        qryCondDLGDelegator.registerCriteriaEditorListener(this);
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.mmgp.uif2.query.CommonQueryFilter#processLinkageLogic(java.util.List,
     * nc.ui.querytemplate.filtereditor.IFilterEditor)
     */
    @Override
    protected void processLinkageLogic(List<IFieldValueElement> fatherValues,
                                       IFilterEditor editor) {
        super.processLinkageLogic(fatherValues, editor);
    }

}
