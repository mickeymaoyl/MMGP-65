package nc.ui.mmgp.uif2.actions.grand;

import java.util.Vector;

import nc.ui.mmgp.uif2.actions.MMGPBodyCopyLineAction;
import nc.vo.mmgp.util.MMArrayUtil;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Nov 21, 2013
 * @author wangweir
 */
public class MMGPGrandBodyCopyLineAction extends MMGPBodyCopyLineAction {
    
    @SuppressWarnings({"rawtypes", "unchecked" })
    @Override
    public void doAction() {
        /* Nov 26, 2013 wangweir 拷贝数据时将数据克隆，避免在主子孙表出现孙表拷贝错误 Begin */
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
