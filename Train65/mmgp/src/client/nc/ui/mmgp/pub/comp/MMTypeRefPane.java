package nc.ui.mmgp.pub.comp;

import java.awt.Component;
import java.awt.Container;

import nc.ui.bill.tools.typeseteditor.AttributeSetFactory;
import nc.ui.bill.tools.typeseteditor.BillItemTypeAttributeSetDialog;
import nc.ui.bill.tools.typeseteditor.ITypeAttributeSet;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.IBillObject;
import nc.ui.pub.bill.IBillObjectEditor;
import nc.vo.mmgp.pub.MMValueNameBillObject;
import nc.vo.mmgp.util.MMStringUtil;

/**
 * �ǲ�������ģ�壬��Ҫ�Ǹ��ݲ�ͬ���Զ����������͵�����ͬ����������ģ��
 * @author chaozg
 *
 */
public class MMTypeRefPane extends UIRefPane implements IBillObjectEditor {

	private static final long serialVersionUID = -4882944462106744714L;

	private ITypeAttributeSet set = null;
	
	private BillItemTypeAttributeSetDialog dlg = null;
	
	private Integer type=-1 ;
	
	private int pos = -1;

	private boolean ismetadata = false;
	
	public MMTypeRefPane(Container parent , Integer type , int pos ,boolean ismetadata) {
		super(parent);
		this.type = type;
		this.pos = pos;
		this.ismetadata = ismetadata;
	}
	
	private BillItemTypeAttributeSetDialog getDlg() {
		if (dlg == null) {
            dlg = new BillItemTypeAttributeSetDialog(this,getITypeAttributeSet());
		}
        return dlg;
	}
	
	private ITypeAttributeSet getITypeAttributeSet(){
		
		if(set == null){
				set = AttributeSetFactory.creatypeAttributSet(type, pos, ismetadata);
		}
		
		return set;
	}

	public IBillObject getBillObject() {
		 return new MMValueNameBillObject(getDlg().getValue(),getDlg().getValue());
	}
	
	
	public void setBillObject(IBillObject billObject) {
		if (MMStringUtil.isObjectStrEmpty(billObject)) {
			getDlg().setValue("");
			return ;
		}
		getDlg().setValue(billObject.toString());
		
	}
	
	public int showEditDialog(Component parent) {
		int result = dlg.showModal();
        if (result == UIDialog.ID_OK) {
            return IBillObjectEditor.OK_OPTION;
        }
        return IBillObjectEditor.CANCEL_OPTION;
	}
}
