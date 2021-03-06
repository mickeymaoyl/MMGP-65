package nc.ui.mmgp.uif2.actions.treetable;

import nc.ui.mmgp.pub.bill.treetable.MMGPBillModelTreeTableAdapter;
import nc.ui.mmgp.pub.bill.treetable.MMGPBillTreeTableModelNode;
import nc.ui.mmgp.uif2.actions.MMGPBodyAddLineAction;
import nc.ui.pub.bill.treetable.IBillTreeTableNode;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;

/**
 * <b> 树表表体增行按钮 </b>
 * <p>
 * 详细描述功能@李思远
 * </p>
 * 
 * @since: 创建日期:Jun 24, 2014
 * @author:gaotx
 */
public class MMGPTreeBodyAddLineAction extends MMGPBodyAddLineAction {

    private SuperVO currBodyVO;

    public SuperVO getCurrBodyVO() {
        return currBodyVO;
    }

    public void setCurrBodyVO(SuperVO currBodyVO) {
        this.currBodyVO = currBodyVO;
    }

    @Override
    public void doAction() {
        super.doAction();

        MMGPBillModelTreeTableAdapter model = (MMGPBillModelTreeTableAdapter) getCardPanel().getBillModel();
        // 获得当前界面选中行
        int selectRow = getCurrentSelectIndex();
        // 获得当前界面选中节点
        MMGPBillTreeTableModelNode parent = model.getTreeNode(selectRow);
        // 获得当前界面选中节点下最后插入的节点
        MMGPBillTreeTableModelNode newChild = (MMGPBillTreeTableModelNode) parent.getLastChild();
        // 获得最后插入的节点在datavector中位置
        int childRow = model.getDataRow(newChild);
        // 获得插入行VO
        CircularlyAccessibleValueObject curVO = model.getBodyValueRowVO(childRow, currBodyVO.getClass().getName());
        // 对插入行VO赋值
        model.setNodeDataByRowVO((IBillTreeTableNode) parent, (IBillTreeTableNode) newChild, curVO);
        // 加载执行公式
        model.loadLoadRelationItemValue();

    }
}
