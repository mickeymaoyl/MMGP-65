/**
 * 
 */
package nc.ui.train.sale.ld;

import java.awt.Container;

import nc.ui.pub.pf.BillSourceVar;
import nc.ui.pubapp.billref.src.view.SourceRefDlg;

/**
 * @author maoyulong
 *
 */
public class SaleSourceDlg extends SourceRefDlg {

	/**
	 * @param parent
	 * @param bsVar
	 */
	public SaleSourceDlg(Container parent, BillSourceVar bsVar) {
		super(parent, bsVar);
		// TODO �Զ����ɵĹ��캯�����
	}

	/**
	 * @param parent
	 * @param bsVar
	 * @param reset
	 */
	public SaleSourceDlg(Container parent, BillSourceVar bsVar, boolean reset) {
		super(parent, bsVar, reset);
		// TODO �Զ����ɵĹ��캯�����
	}

	@Override
	public String getRefBillInfoBeanPath() {
		// TODO �Զ����ɵķ������
		  return "nc/ui/train/sale/ld/tr11_tr12.xml";
	}

}
