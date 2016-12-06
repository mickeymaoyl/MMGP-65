package nc.ui.mmgp.uif2.actions.grand;

import java.util.Vector;

import nc.ui.mmgp.uif2.actions.MMGPBodyCopyLineAction;
import nc.vo.mmgp.util.MMArrayUtil;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since �������� Nov 21, 2013
 * @author wangweir
 */
public class MMGPGrandBodyCopyLineAction extends MMGPBodyCopyLineAction {
    
    @SuppressWarnings({"rawtypes", "unchecked" })
    @Override
    public void doAction() {
        /* Nov 26, 2013 wangweir ��������ʱ�����ݿ�¡��������������������������� Begin */
        int[] rows = this.getCardPanel().getBodyPanel().getTable().getSelectedRows();
        Object[] copyObjects = null;
        if (rows != null) {
            copyObjects = new Object[rows.length];
            for (int i = 0; i < rows.length; i++) {
                copyObjects[i] = ((Vector) this.getCardPanel().getBillModel().getDataVector().get(i)).clone();
                this.getCardPanel().setBodyValueAt(null, rows[i], "precolumn");
            }
        }
        try {
            super.doAction();
        } finally {
            if (MMArrayUtil.isEmpty(copyObjects)) {
                return;
            }
            for (int i = 0; i < rows.length; i++) {
                this.getCardPanel().getBillModel().getDataVector().set(i, copyObjects[i]);
            }

        }
        /* Nov 26, 2013 wangweir End */
    }
}
