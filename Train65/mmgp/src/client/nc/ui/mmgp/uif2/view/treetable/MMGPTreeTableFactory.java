package nc.ui.mmgp.uif2.view.treetable;

import java.util.Comparator;

import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.treetable.IBillTreeTableModel;
import nc.ui.pub.bill.treetable.ITableTreeFactory;
import nc.ui.pub.bill.treetable.ITreeTable;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * @Description: TODO
 *               <p>
 *               œÍœ∏π¶ƒ‹√Ë ˆ
 *               </p>
 * @data:2014-5-15…œŒÁ11:19:08
 * @author: tangxya
 */
public class MMGPTreeTableFactory implements ITableTreeFactory {

	private Comparator<CircularlyAccessibleValueObject> comparator;

	public Comparator<CircularlyAccessibleValueObject> getComparator() {
		return comparator;
	}

	public void setComparator(
			Comparator<CircularlyAccessibleValueObject> comparator) {
		this.comparator = comparator;
	}

	@Override
	public ITreeTable creatTreeTable(BillScrollPane sp,
			IBillTreeTableModel treeTableModel) {

		MMGPTreeTable tabletree = new MMGPTreeTable(treeTableModel, sp,
				comparator);

		return tabletree;
	}

}
