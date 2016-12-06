package nc.ui.mmgp.uif2.actions.treetable;

import nc.ui.mmgp.pub.bill.treetable.MMGPBillModelTreeTableAdapter;
import nc.ui.mmgp.pub.bill.treetable.MMGPBillTreeTableModelNode;
import nc.ui.mmgp.uif2.actions.MMGPBodyInsertLineAction;
import nc.ui.pub.bill.treetable.IBillTreeTableNode;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;

/**
 * <b> �����������а�ť </b>
 * <p>
 * ��ϸ��������@��˼Զ
 * </p>
 * 
 * @since: ��������:Jun 24, 2014
 * @author:gaotx
 */
public class MMGPTreeBodyInsertLineAction extends MMGPBodyInsertLineAction {
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
        // ��õ�ǰ����ѡ����
        int selectRow = getCurrentSelectIndex();
        // ��õ�ǰ����ѡ�нڵ�ĸ��ڵ㣬��Ϊ�Ѿ������� ����ѡ�нڵ�λ��+1
        MMGPBillTreeTableModelNode parent = (MMGPBillTreeTableModelNode) model.getTreeNode(selectRow + 1).getParent();
        // ��ò���Ľڵ�
        MMGPBillTreeTableModelNode newChild = model.getTreeNode(selectRow);
        // //��ò���Ľڵ���datavector��λ��
        int childRow = model.getDataRow(newChild);
        // ��ò�����VO
        CircularlyAccessibleValueObject curVO = model.getBodyValueRowVO(childRow, currBodyVO.getClass().getName());
        // �Բ�����VO��ֵ
        model.setNodeDataByRowVO((IBillTreeTableNode) parent, (IBillTreeTableNode) newChild, curVO);
        // ����ִ�й�ʽ
        model.loadLoadRelationItemValue();

    }

}
