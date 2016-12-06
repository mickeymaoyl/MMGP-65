package nc.ui.mmgp.uif2.validation;

import nc.bs.uif2.validation.IValidationService;
import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;
import nc.ui.uif2.model.HierachicalDataAppModel;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
/**
 *
 * <b> 树主子表档案删除校验 </b>
 * <p>
 *     详细描述功能
 * </p>
 * @since:
 * 创建日期:Sep 18, 2014
 * @author:liwsh
 */
public class MMGPBillTreeDeleteValidation implements IValidationService {

    /**
     * 左树模型
     */
    private HierachicalDataAppModel treeModel = null;


    @Override
    public void validate(Object obj) throws ValidationException {

        int childCount = this.getTreeModel().getSelectedNode().getChildCount();

        if (childCount > 0) {
            ValidationException exp = new ValidationException();
            exp.addValidationFailure(new ValidationFailure(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0034")/*@res "存在下级子节点，不能删除！"*/));
            ExceptionUtils.wrappException(exp);
        }

    }



    public HierachicalDataAppModel getTreeModel() {
        return treeModel;
    }

    public void setTreeModel(HierachicalDataAppModel treeModel) {
        this.treeModel = treeModel;
    }

}