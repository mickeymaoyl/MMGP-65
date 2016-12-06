package nc.ui.mmgp.uif2.actions.treetable;

import javax.swing.Action;

import nc.ui.mmgp.pub.bill.treetable.MMGPBillTreeTableModelNode;
import nc.uitheme.ui.ImageIconLoader;

/**
 * @Description: TODO
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-5-15上午9:56:45
 * @author: tangxya
 */
public class MMGPTreeDownMoveAction extends MMGPTreeBaseMoveAction {

	public MMGPTreeDownMoveAction() {
		this.setBtnName(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0016")/*@res "向下"*/);
		this.setCode("DownMove");
		this.putValue(Action.SHORT_DESCRIPTION, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0016")/*@res "向下"*/);

		this.putValue(
				Action.SMALL_ICON,
				ImageIconLoader
						.loadImageIconImple("/nc/ui/mmgp/pub/resource/move_down.png"));
	}

	@Override
	public Boolean judgeNodeCannotMove(MMGPBillTreeTableModelNode selectNode) {

		// 没有弟弟节点
		return selectNode.getNextSibling() == null;
	}

	@Override
	public MMGPBillTreeTableModelNode getAfterMovedParent(
			MMGPBillTreeTableModelNode selectNode) {

		return (MMGPBillTreeTableModelNode) selectNode.getParent();
	}

	@Override
	public int getMovingSitOfParent(MMGPBillTreeTableModelNode selectNode) {

		MMGPBillTreeTableModelNode parent = (MMGPBillTreeTableModelNode) selectNode
				.getParent();

		return parent.getIndex(selectNode) + 1;
	}

	@Override
	public MMGPBillTreeTableModelNode getMinTreeRootForUpdate(
			MMGPBillTreeTableModelNode selectNode) {

		return (MMGPBillTreeTableModelNode) selectNode.getParent();
	}

}