package nc.ui.mmgp.flexgant.view.treetable;

/**
 * 
 */
import java.util.Enumeration;
import java.util.Vector;


import com.dlsc.flexgantt.model.treetable.TreeTableColumn;
/**
 * ����ͼ�Ķ��ͷ
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
     * ��������:(2003-7-14 10:53:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getText() {
        return text;
    }

    /**
     * �б�ͷ��ɾ��Column ��������:(2002-9-25 15:49:00)
     */
    public boolean removeColumn(MMGPGantColumnGroup ptg,
                                TreeTableColumn tc) {
        boolean retFlag = false;
        if (tc != null) {
            for (int i = 0; i < ptg.v.size(); i++) {
                Object tmpObj = ptg.v.get(i);
                if (tmpObj instanceof MMGPGantColumnGroup) {
                    retFlag = removeColumn((MMGPGantColumnGroup) tmpObj, tc);
                    // ����ҵ�����
                    if (retFlag) break;
                } else if (tmpObj instanceof TreeTableColumn) {
                    // �ж��Ƿ���ҵĶ���
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
     * �б�ͷ��ɾ��ColumnGrp ɾ������true,���򷵻�false; ��������:(2002-9-25 15:49:00)
     */
    public boolean removeColumnGrp(MMGPGantColumnGroup ptg,
                                   MMGPGantColumnGroup tg) {
        boolean retFlag = false;
        if (tg != null) {
            for (int i = 0; i < ptg.v.size(); i++) {
                Object tmpObj = ptg.v.get(i);
                if (tmpObj instanceof MMGPGantColumnGroup) {
                    // �ж��Ƿ���ҵĶ���
                    if (tmpObj == tg) {
                        ptg.v.remove(i);
                        retFlag = true;
                        break;
                    } else {
                        retFlag = removeColumnGrp((MMGPGantColumnGroup) tmpObj, tg);
                        // ����ҵ�����
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
     * ��������:(2003-7-14 10:53:26)
     * 
     * @param newText
     *        java.lang.String
     */
    public void setText(java.lang.String newText) {
        text = newText;
    }

}
