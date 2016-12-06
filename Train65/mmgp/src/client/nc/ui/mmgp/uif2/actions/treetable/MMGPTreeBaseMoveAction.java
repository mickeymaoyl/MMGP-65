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
          ��ϸ��������
    </p>
 * @data:2014-5-15����9:51:11
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
	     * �ж�ѡ�нڵ��ܷ��ƶ�
	     * 
	     * @param selectNode ѡ�еĽڵ�
	     * @return
	     */
	    abstract public Boolean judgeNodeCannotMove(MMGPBillTreeTableModelNode selectNode);
	        
	    
	    
	    /**
	     * ��ȡ�ƶ�����Ϊ�¸��ڵ�Ľڵ�
	     * 
	     * @param selectNode ѡ�еĽڵ�
	     * @return
	     */
	    abstract  public MMGPBillTreeTableModelNode getAfterMovedParent(MMGPBillTreeTableModelNode selectNode);
	    
	    /**
	     * ��ȡ�����ƶ����¸��ڵ��е�λ��
	     * 
	     * @param selectNode ѡ�еĽڵ�
	     * @return
	     */
	    abstract  public int getMovingSitOfParent(MMGPBillTreeTableModelNode selectNode);
	    
	    
	    /**
	     * ��ȡ��Ҫ����innercode����С���ĸ��ڵ�
	     * 
	     * @param selectNode ѡ�еĽڵ�
	     * @return
	     */
	    abstract public MMGPBillTreeTableModelNode getMinTreeRootForUpdate(MMGPBillTreeTableModelNode selectNode);

	    @Override
	    public void doAction(ActionEvent arg0) throws Exception {

	        editor.getBillCardPanel().stopEditing();

	        MMGPBillModelTreeTableAdapter billModelTreeAdapter =
	                (MMGPBillModelTreeTableAdapter) editor.getBillCardPanel().getBodyPanel().getTableModel();
	        
	        int selectrow = editor.getBillCardPanel().getBodyPanel().getTable().getSelectedRow();

	        if(selectrow == -1){ //ѡ�ж�ʧ
	            return;
	        }
	        MMGPBillTreeTableModelNode selectNode = billModelTreeAdapter.getTreeNode(selectrow);
	        
	        if(judgeNodeCannotMove(selectNode)){
	            return;
	        }
	        //һ��Ҫ��ͨ��selectNodeȡֵ����ɾ�С�˳��һ�������ң�����
	        DefaultMutableTreeNode insertroot =  constructTree(selectNode,billModelTreeAdapter);
	        MMGPBillTreeTableModelNode newParent = getAfterMovedParent(selectNode);
	        MMGPBillTreeTableModelNode minTreeRoot = getMinTreeRootForUpdate(selectNode);
	        int newIndexOfParent = getMovingSitOfParent(selectNode);
	        //ɾ��֮��selectNode��parent����Ϊnull
	        billModelTreeAdapter.delLine(new int[]{selectrow});
	        
	        MMGPBillTreeTableTools.insertTree(insertroot, newParent, newIndexOfParent, billModelTreeAdapter);
	       /* insertTree(insertroot,newParent,newIndexOfParent,billModelTreeAdapter);*/
	        
	        MMGPBillTreeTableModelNode curNode= (MMGPBillTreeTableModelNode) newParent.getChildAt(newIndexOfParent);
	       
	       
	        final int row = billModelTreeAdapter.getRow(curNode);
	        //billModelTreeAdapter.delLine���̲߳���ȫ�ģ��п��ܕ�Ӱ��ѡ�е�rowֵ
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
	     * @Description:����ѡ�����ڵ㣬����һ��ͬ�ṹ����
	     * 
	     * @param: @param selectNode ѡ�����ڵ�
	     * @param: @param billModelTreeAdapter ����ģ��
	     * @param: @return
	     * @return:DefaultMutableTreeNode �����ڵ�
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
	     * @Description: �ҵ�ѡ�����ڵ��Ӧ�ı���VO,�����µ����ڵ�
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
