package nc.ui.mmgp.uif2.actions.treetable;

import javax.swing.Action;

import nc.ui.mmgp.pub.bill.treetable.MMGPBillTreeTableModelNode;
import nc.uitheme.ui.ImageIconLoader;


/**
 * @Description: TODO
    <p>
          详细功能描述
    </p>
 * @data:2014-5-15上午10:03:35
 * @author: tangxya
 */
public class MMGPTreeRightMoveAction extends MMGPTreeBaseMoveAction{

	    /**
	 *
	 */
	private static final long serialVersionUID = 4419440569055703490L;

		public MMGPTreeRightMoveAction() {
	        this.setBtnName(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0018")/*@res "向右"*/);
	        this.setCode("RightMove");
	        this.putValue(Action.SHORT_DESCRIPTION,nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0018")/*@res "向右"*/);
	        this.putValue(Action.SMALL_ICON, ImageIconLoader.loadImageIconImple(
	                "/nc/ui/mmgp/pub/resource/move_right.png"));
	    }


	    @Override
	    public Boolean judgeNodeCannotMove(MMGPBillTreeTableModelNode selectNode) {

	        //没有最小哥哥话 无法右移
	        return selectNode.getPreviousSibling()==null;
	    }

	    @Override
	    public MMGPBillTreeTableModelNode getAfterMovedParent(MMGPBillTreeTableModelNode selectNode) {

	        return (MMGPBillTreeTableModelNode) selectNode.getPreviousSibling();
	    }

	    @Override
	    public int getMovingSitOfParent(MMGPBillTreeTableModelNode selectNode) {

	        return selectNode.getPreviousSibling().getChildCount();
	    }

	    @Override
	    public MMGPBillTreeTableModelNode getMinTreeRootForUpdate(MMGPBillTreeTableModelNode selectNode) {

	        return (MMGPBillTreeTableModelNode) selectNode.getParent();
	    }


	}


