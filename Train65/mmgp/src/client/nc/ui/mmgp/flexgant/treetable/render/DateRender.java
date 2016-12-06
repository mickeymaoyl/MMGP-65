package nc.ui.mmgp.flexgant.treetable.render;

import java.util.Date;

import nc.bs.logging.Logger;
import nc.ui.format.NCFormater;
import nc.vo.pub.format.exception.FormatException;
import nc.vo.pub.lang.UFDate;

import com.dlsc.flexgantt.swing.treetable.DefaultTreeTableCellRenderer;

/**
 * <b> 同步日期数据格式 </b>
 */
public class DateRender extends DefaultTreeTableCellRenderer {

    private static final long serialVersionUID = 4898406590356166624L;

    @Override
    public String getText(Object node,
                          Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            UFDate date = UFDate.getDate((Date) value);
            return date.toLocalString();
        } else if (value instanceof UFDate) {
            try {
                // 解决数据格式修改后，甘特图日期不变的问题
                return NCFormater.formatLiteralDateTime(value).getValue();
            } catch (FormatException e) {
                Logger.error(e);
            }
        } else {
            Logger.error("不支持该数据格式。");
        }
        return value.toString();
    }
}
