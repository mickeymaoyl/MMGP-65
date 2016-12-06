package nc.ui.mmgp.uif2.actions.treetable;
import javax.swing.Action;

import nc.ui.mmgp.pub.bill.treetable.MMGPBillTreeTableModelNode;
import nc.uitheme.ui.ImageIconLoader;

/**
 * @Description: TODO
    <p>
          ��ϸ��������
    </p>
 * @data:2014-5-15����10:01:55
 * @author: tangxya
 */
public class MMGPTreeLeftMoveAction extends MMGPTreeBaseMoveAction{


	    /**
	 *
	 */
	private static final long serialVersionUID = -7529492869011778140L;
		public MMGPTreeLeftMoveAction() {
	        this.setBtnName(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0017")/*@res "����"*/);
	        this.setCode("LeftMove");
	        this.putValue(Action.SHORT_DESCRIPTION,nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0017")/*@res "����"*/);
	        this.putValue(Action.SMALL_ICON, ImageIconLoader.loadImageIconImple(
	                "/nc/ui/mmgp/pub/resource/move_left.png"));
	    }

	    @Override
	    public Boolean judgeNodeCannotMove(MMGPBillTreeTableModelNode selectNode) {

	      //��һ���ڵ��޷�����
	        return selectNode.getParent().equals(selectNode.getRoot()) ;
	    }
	    @Override
	    public MMGPBillTreeTableModelNode getAfterMovedParent(MMGPBillTreeTableModelNode selectNode) {

	        return (MMGPBillTreeTableModelNode) selectNode.getParent().getParent();
	    }
	    @Override
	    public int getMovingSitOfParent(MMGPBillTreeTableModelNode selectNode) {
	        MMGPBillTreeTableModelNode parent = (MMGPBillTreeTableModelNode) selectNode.getParent();
	        MMGPBillTreeTableModelNode grand =  (MMGPBillTreeTableModelNode) parent.getParent();
	        return grand.getIndex(parent) + 1;
	    }
	    @Override
	    public MMGPBillTreeTableModelNode getMinTreeRootForUpdate(MMGPBillTreeTableModelNode selectNode) {

	        return (MMGPBillTreeTableModelNode) selectNode.getParent().getParent();
	    }

	}

