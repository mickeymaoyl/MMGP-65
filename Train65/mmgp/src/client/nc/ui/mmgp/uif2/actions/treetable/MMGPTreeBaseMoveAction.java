package nc.ui.mmgp.uif2.actions.treetable;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import nc.ui.mmgp.pub.bill.treetable.MMGPBillModelTreeTableAdapter;
import nc.ui.mmgp.pub.bill.treetable.MMGPBillTreeTableModelNode;
import nc.ui.mmgp.pub.bill.treetable.MMGPBillTreeTableTools;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.UIState;
import nc.ui.uif2.editor.BillForm;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;

/**
 * @Description: TODO
    <p>
          详细功能描述
    </p>
 * @data:2014-5-15上午9:51:11
 * @author: tangxya
 */
@SuppressWarnings("restriction")
public abstract class MMGPTreeBaseMoveAction extends NCAction{
	
	    /**
	 * 
	 */
	private static final long serialVersionUID = -3929874398427989303L;
		private String innercodeKey = "innercode";
	    
	    public String getInnercodeKey() {
	        return innercodeKey;
	    }
	    public void setInnercodeKey(String innercodeKey) {
	        this.innercodeKey = innercodeKey;
	    }
	    private SuperVO currBodyVO;
	    
	    public SuperVO getCurrBodyVO() {
	        return currBodyVO;
	    }
	    public void setCurrBodyVO(SuperVO currBodyVO) {
	        this.currBodyVO = currBodyVO;
	    }
	    
		private BillManageModel model = null;
	    
	    public BillManageModel getModel() {
	        return this.model;
	    }
	    
	    public void setModel(BillManageModel model) {
	        this.model = model;
	        model.addAppEventListener(this);
	    }
	    
	    protected BillForm editor;
	    
	    public BillForm getEditor() {
	        return editor;
	    }
	    public void setEditor(BillForm editor) {
	        this.editor = editor;
	    }
	    @Override
	    protected boolean isActionEnable() {

	        if ((this.getModel().getUiState() == UIState.ADD || this.getModel().getUiState() == UIState.EDIT)
	                && editor.getBillCardPanel().getBodyPanel().getTable().getSelectedRowCount() == 1) {
	            return true;
	        }
	        return false;
	    }
	    /**
	     * 判断选中节点能否移动
	     * 
	     * @param selectNode 选中的节点
	     * @return
	     */
	    abstract public Boolean judgeNodeCannotMove(MMGPBillTreeTableModelNode selectNode);
	        
	    
	    
	    /**
	     * 获取移动后作为新父节点的节点
	     * 
	     * @param selectNode 选中的节点
	     * @return
	     */
	    abstract  public MMGPBillTreeTableModelNode getAfterMovedParent(MMGPBillTreeTableModelNode selectNode);
	    
	    /**
	     * 获取即将移动到新父节点中的位置
	     * 
	     * @param selectNode 选中的节点
	     * @return
	     */
	    abstract  public int getMovingSitOfParent(MMGPBillTreeTableModelNode selectNode);
	    
	    
	    /**
	     * 获取需要更新innercode的最小树的根节点
	     * 
	     * @param selectNode 选中的节点
	     * @return
	     */
	    abstract public MMGPBillTreeTableModelNode getMinTreeRootForUpdate(MMGPBillTreeTableModelNode selectNode);

	    @Override
	    public void doAction(ActionEvent arg0) throws Exception {

	        editor.getBillCardPanel().stopEditing();

	        MMGPBillModelTreeTableAdapter billModelTreeAdapter =
	                (MMGPBillModelTreeTableAdapter) editor.getBillCardPanel().getBodyPanel().getTableModel();
	        
	        int selectrow = editor.getBillCardPanel().getBodyPanel().getTable().getSelectedRow();

	        if(selectrow == -1){ //选中丢失
	            return;
	        }
	        MMGPBillTreeTableModelNode selectNode = billModelTreeAdapter.getTreeNode(selectrow);
	        
	        if(judgeNodeCannotMove(selectNode)){
	            return;
	        }
	        //一定要先通过selectNode取值。再删行。顺序一定不能乱！！！
	        DefaultMutableTreeNode insertroot =  constructTree(selectNode,billModelTreeAdapter);
	        MMGPBillTreeTableModelNode newParent = getAfterMovedParent(selectNode);
	        MMGPBillTreeTableModelNode minTreeRoot = getMinTreeRootForUpdate(selectNode);
	        int newIndexOfParent = getMovingSitOfParent(selectNode);
	        //删除之后selectNode中parent会置为null
	        billModelTreeAdapter.delLine(new int[]{selectrow});
	        
	        MMGPBillTreeTableTools.insertTree(insertroot, newParent, newIndexOfParent, billModelTreeAdapter);
	       /* insertTree(insertroot,newParent,newIndexOfParent,billModelTreeAdapter);*/
	        
	        MMGPBillTreeTableModelNode curNode= (MMGPBillTreeTableModelNode) newParent.getChildAt(newIndexOfParent);
	       
	       
	        final int row = billModelTreeAdapter.getRow(curNode);
	        //billModelTreeAdapter.delLine是线程不安全的，有可能會影响选中的row值
	        Runnable run = new Runnable() {

	            @Override

	            public void run() { 

	                editor.getBillCardPanel().getBodyPanel().getTable().getSelectionModel().setSelectionInterval(row, row);

	                }

	        };       
	        SwingUtilities.invokeLater(run);
	        int inercodeIndex = MMGPBillTreeTableTools.getInercodeIndex(billModelTreeAdapter, innercodeKey);
	        MMGPBillTreeTableTools.setBodyInnercode(minTreeRoot,inercodeIndex);

	    }
	    /**
	     * 
	     * @Description:根据选中树节点，构造一棵同结构的树
	     * 
	     * @param: @param selectNode 选中树节点
	     * @param: @param billModelTreeAdapter 树表模型
	     * @param: @return
	     * @return:DefaultMutableTreeNode 新树节点
	     *
	     */
	    public DefaultMutableTreeNode constructTree(MMGPBillTreeTableModelNode selectNode,
	    		MMGPBillModelTreeTableAdapter billModelTreeAdapter) {
	        Stack<MMGPBillTreeTableModelNode> stack = new Stack<MMGPBillTreeTableModelNode>();
	        stack.push(selectNode);
	        Map<MMGPBillTreeTableModelNode, DefaultMutableTreeNode> node2nodeMap = new HashMap<MMGPBillTreeTableModelNode, DefaultMutableTreeNode>();
	        while (!stack.isEmpty()) {
	        	MMGPBillTreeTableModelNode curnode = stack.pop();
	            DefaultMutableTreeNode curcreatnode = node2nodeMap.get(curnode);
	            if (curcreatnode == null) {
	                curcreatnode = getnodeVO(billModelTreeAdapter, curnode);
	                node2nodeMap.put(curnode, curcreatnode);
	            }
	            for (int i = 0; i < curnode.getChildCount(); i++) {
	            	MMGPBillTreeTableModelNode child = (MMGPBillTreeTableModelNode) curnode.getChildAt(i);
	                DefaultMutableTreeNode childnode = getnodeVO(billModelTreeAdapter, child);
	                node2nodeMap.put(child, childnode);
	                curcreatnode.add(childnode);
	                stack.push(child);
	            }
	        }
	        return node2nodeMap.get(selectNode);
	    }
	    /**
	     * 
	     * @Description: 找到选中树节点对应的表体VO,构造新的树节点
	     * @param: @param billModelTreeAdapter
	     * @param: @param curnode
	     * @param: @return
	     * @return:DefaultMutableTreeNode
	     *
	     */
	    public DefaultMutableTreeNode getnodeVO(MMGPBillModelTreeTableAdapter billModelTreeAdapter,
	    		MMGPBillTreeTableModelNode curnode) {
	        int row = getModelRow(billModelTreeAdapter, curnode);
	        CircularlyAccessibleValueObject curVO = billModelTreeAdapter.getBodyValueRowVO(row,this.getCurrBodyVO().getClass().getName());
	        DefaultMutableTreeNode curcreatnode = new DefaultMutableTreeNode(curVO);
	        return curcreatnode;
	    }
	    public int getModelRow(MMGPBillModelTreeTableAdapter billModelTreeAdapter,

	    		MMGPBillTreeTableModelNode curnode

	                           ) {

	        int row = billModelTreeAdapter.getDataVector().indexOf(curnode.getData());

	        return row;

	    }
	    
}
