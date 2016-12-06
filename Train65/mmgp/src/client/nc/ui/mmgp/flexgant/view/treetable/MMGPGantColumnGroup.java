package nc.ui.mmgp.flexgant.view.treetable;

/**
 * 
 */
import java.util.Enumeration;
import java.util.Vector;


import com.dlsc.flexgantt.model.treetable.TreeTableColumn;
/**
 * 甘特图的多表头
 * @author tangxya
 *
 */
public class MMGPGantColumnGroup {
    protected Vector v;

    protected String text;

    public MMGPGantColumnGroup(String text) {
        this.text = text;
        v = new Vector();
    }

    /**
     * @param obj
     *        TableColumn or ColumnGroup
     */
    public void add(Object obj) {
        if (obj == null) {
            return;
        }
        v.addElement(obj);
    }

    /**
     * @param c
     *        TableColumn
     * @param v
     *        ColumnGroups
     */
    public Vector getColumnGroups(TreeTableColumn c,
                                  Vector g) {
        g.addElement(this);
        if (v.contains(c)) return g;
        Enumeration enums = v.elements();
        while (enums.hasMoreElements()) {
            Object obj = enums.nextElement();
            if (obj instanceof MMGPGantColumnGroup) {
                Vector groups = (Vector) ((MMGPGantColumnGroup) obj).getColumnGroups(c, (Vector) g.clone());
                if (groups != null) return groups;
            }
        }
        return null;
    }

    public Object getHeaderValue() {
        return text;
    }

    public int getSize() {
        return v == null ? 0 : v.size();
    }

    /**
     * 创建日期:(2003-7-14 10:53:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getText() {
        return text;
    }

    /**
     * 列表头中删除Column 创建日期:(2002-9-25 15:49:00)
     */
    public boolean removeColumn(MMGPGantColumnGroup ptg,
                                TreeTableColumn tc) {
        boolean retFlag = false;
        if (tc != null) {
            for (int i = 0; i < ptg.v.size(); i++) {
                Object tmpObj = ptg.v.get(i);
                if (tmpObj instanceof MMGPGantColumnGroup) {
                    retFlag = removeColumn((MMGPGantColumnGroup) tmpObj, tc);
                    // 如果找到返回
                    if (retFlag) break;
                } else if (tmpObj instanceof TreeTableColumn) {
                    // 判断是否查找的对象
                    if (tmpObj == tc) {
                        ptg.v.remove(i);
                        retFlag = true;
                        break;
                    }
                }
            }
        }
        return retFlag;
    }

    /**
     * 列表头中删除ColumnGrp 删除返回true,否则返回false; 创建日期:(2002-9-25 15:49:00)
     */
    public boolean removeColumnGrp(MMGPGantColumnGroup ptg,
                                   MMGPGantColumnGroup tg) {
        boolean retFlag = false;
        if (tg != null) {
            for (int i = 0; i < ptg.v.size(); i++) {
                Object tmpObj = ptg.v.get(i);
                if (tmpObj instanceof MMGPGantColumnGroup) {
                    // 判断是否查找的对象
                    if (tmpObj == tg) {
                        ptg.v.remove(i);
                        retFlag = true;
                        break;
                    } else {
                        retFlag = removeColumnGrp((MMGPGantColumnGroup) tmpObj, tg);
                        // 如果找到返回
                        if (retFlag) break;

                    }
                } else if (tmpObj instanceof TreeTableColumn) {
                    break;
                }
            }
        }
        return retFlag;
    }

    /**
     * 创建日期:(2003-7-14 10:53:26)
     * 
     * @param newText
     *        java.lang.String
     */
    public void setText(java.lang.String newText) {
        text = newText;
    }

}
