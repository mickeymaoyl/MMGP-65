package nc.ui.mmgp.pub.comp;

import java.awt.Component;
import java.awt.Container;

import nc.ui.bd.ref.costomize.RefCustomizedDlg;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.IBillObject;
import nc.ui.pub.bill.IBillObjectEditor;
import nc.vo.mmgp.pub.MMValueNameBillObject;
import nc.vo.mmgp.util.MMStringUtil;

/**
 * 参照类型面板
 * @author chaozg
 *
 */
public class MMUIRefPane extends UIRefPane implements IBillObjectEditor {

	private static final long serialVersionUID = -2851160261054912369L;

	
	private Container parent = null;

	private RefCustomizedDlg dlg = null;
	
	
	public MMUIRefPane (java.awt.Container parent){
		this.parent = parent;
		getDlg().setReturnCodeDefaultValue(true);
	}
	
	public IBillObject getBillObject() {
		String value = getDlg().getResultStr();
		return new MMValueNameBillObject(value,value);
	}

	public void setBillObject(IBillObject billObject) {
		
		if (MMStringUtil.isObjectStrEmpty(billObject)) {
			getDlg().setText("");
			return ;
		}
		getDlg().setText(billObject.toString());
		

	}
	
	

	public int showEditDialog(Component parent) {
		int result = getDlg().showModal();
        if (result == UIDialog.ID_OK) {
            return IBillObjectEditor.OK_OPTION;
        }
        return IBillObjectEditor.CANCEL_OPTION;
	}
	
	
	public RefCustomizedDlg getDlg() {
		if (dlg == null) {
			dlg = new RefCustomizedDlg(parent);
		}
		
		return dlg;
	}
	
	
	
}
