package nc.ui.mmgp.flexgant.view.treetable;

import java.util.Comparator;

import com.dlsc.flexgantt.model.treetable.KeyColumn;

import nc.ui.mmgp.flexgant.model.MMGPGantContext;
import nc.ui.pub.beans.constenum.IConstEnum;
import nc.ui.pub.bill.BillColumnCompareUtil;
import nc.ui.pubapp.gantt.model.AppGantContext;
import nc.ui.pubapp.gantt.model.AppGanttChartNode;
import nc.vo.pub.lang.MultiLangText;

/**
 * @Description: 以VO作为模型的甘特图树表节点
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-5-22下午7:59:39
 * @author: tangxya
 */
@SuppressWarnings({"serial", "restriction" })
public class MMGPGanttChartNode extends AppGanttChartNode {

    public MMGPGanttChartNode() {
        super();
    }

    public MMGPGanttChartNode(boolean allowsChildren) {
        super(allowsChildren);
    }

    public MMGPGanttChartNode(Object userObject,
                              MMGPGantContext context) {
        super(userObject, context);
    }

    public MMGPGanttChartNode(Object userObject,
                              boolean allowsChildren) {
        super(userObject, allowsChildren);
    }

    public MMGPGanttChartNode(Object userObject) {
        super(userObject);
    }

    public MMGPGanttChartNode(Object userObj,
                              AppGantContext context) {
        super(userObj, context);
    }

    @SuppressWarnings("unchecked")
    protected int compare(int modelIndex,
                          Object value1,
                          Object value2,
                          boolean ascending) {
        if (value1 == null) {
            return -1;
        }

        if (value2 == null) {
            return +1;
        }
        Comparator comparator = null;
        if (modelIndex == KeyColumn.MODEL_INDEX) {
            comparator = getKeyComparator();
        } else {
            comparator = getComparator(modelIndex);
        }
        int result = 0;
        if (comparator != null) {
            result = comparator.compare(value1, value2);
        } else if (value1 instanceof Comparable) {
            result = ((Comparable) value1).compareTo(value2);
        } else {
            if (value1 instanceof IConstEnum || value1 instanceof MultiLangText) {
                value1 = value1.toString();
                value2 = value2.toString();
                result = BillColumnCompareUtil.compare(value1, value2);
            } else {
                throw new IllegalArgumentException("unable to sort model index " //$NON-NLS-1$
                    + modelIndex
                    + " - no comparator has been specified and the " //$NON-NLS-1$
                    + "column value does not implement the Comparable " //$NON-NLS-1$
                    + "interface, value type was = " //$NON-NLS-1$
                    + value1.getClass().getName());
            }

        }

        if (!ascending) {
            if (result != 0) {
                result = -result;
            }
        }
        return result;
    }

    /**
     * 
     * 设置NODE的编辑性
     * @param b
     */
    public void setNodeEditable(boolean b) {
        int count = getContext().getItemContainer().getCount();
        for (int i = 0; i < count; i++) {
            if (!getContext().getItemContainer().getItem(i).isEdit()) {
                continue;
            }
            if (getContext().getItemContainer().getItem(i).isKeyValue()) {
                this.setKeyEditable(b);
            }
            this.setValueEditable(i, b);
        }
    }

    /**
     * 
     * 设置NODE中某一格的编辑性
     * @param itemKey
     * @param b
     */
    public void setValueEditable(String itemKey,
                                 boolean b) {
        int modelIndex = getContext().getItemContainer().getItem(itemKey).getShowOrder();
        if (getContext().getItemContainer().getItem(modelIndex) == null) {
           return;
        }
        if (getContext().getItemContainer().getItem(modelIndex).isKeyValue()) {
            this.setKeyEditable(b);
        }
        this.setValueEditable(modelIndex, b);
    }
}
