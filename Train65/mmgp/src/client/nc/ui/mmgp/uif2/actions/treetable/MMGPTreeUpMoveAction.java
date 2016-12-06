package nc.ui.mmgp.uif2.actions.treetable;

import javax.swing.Action;

import nc.ui.mmgp.pub.bill.treetable.MMGPBillTreeTableModelNode;
import nc.uitheme.ui.ImageIconLoader;

/**
 * @Description: TODO
 *               <p>
 *               ��ϸ��������
 *               </p>
 * @data:2014-5-15����10:04:57
 * @author: tangxya
 */
public class MMGPTreeUpMoveAction extends MMGPTreeBaseMoveAction {

	/**
	 *
	 */
	private static final long serialVersionUID = -304084787758052663L;

	public MMGPTreeUpMoveAction() {
		this.setBtnName(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0019")/*@res "����"*/);
		this.setCode("UpMove");
		this.putValue(Action.SHORT_DESCRIPTION, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0019")/*@res "����"*/);
		this.putValue(
				Action.SMALL_ICON,
				ImageIconLoader
						.loadImageIconImple("/nc/ui/mmgp/pub/resource/move_up.png"));
	}

	@Override
	public Boolean judgeNodeCannotMove(MMGPBillTreeTableModelNode selectNode) {
		// û�и��ڵ�
		return selectNode.getPreviousSibling() == null;
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
		return parent.getIndex(selectNode) - 1;
	}

	@Override
	public MMGPBillTreeTableModelNode getMinTreeRootForUpdate(
			MMGPBillTreeTableModelNode selectNode) {

		return (MMGPBillTreeTableModelNode) selectNode.getParent();
	}

}