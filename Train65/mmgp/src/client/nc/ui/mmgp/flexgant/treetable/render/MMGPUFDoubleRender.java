package nc.ui.mmgp.flexgant.treetable.render;

import javax.swing.SwingConstants;

import nc.bs.logging.Logger;
import nc.ui.format.NCFormater;
import nc.vo.pub.format.FormatResult;
import nc.vo.pub.format.exception.FormatException;

import com.dlsc.flexgantt.swing.treetable.DefaultTreeTableCellRenderer;

public class MMGPUFDoubleRender extends DefaultTreeTableCellRenderer{
    @Override
    public String getText(Object node, Object value) {
        setHorizontalAlignment(SwingConstants.RIGHT);
        if(value == null){
            return null;
        }
        FormatResult format = null;
        try {
            format = NCFormater.formatNumber(value);
            value = format.getValue();
        } catch (FormatException e) {
            Logger.error(e);
            value = "";
        }
        return value.toString();
    }
}
