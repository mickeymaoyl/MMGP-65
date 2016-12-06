package nc.ui.mmgp.uif2.utils;

import nc.ui.bd.ref.RefInitializeCondition;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.pub.lang.UFBoolean;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 *     ���Ų��չ�����
 * </p>
 * @since
 * �������� Sep 22, 2013
 * @author wangweir
 *
 */
public class FilterDeptRefUtils {

    private UIRefPane pane;

    public FilterDeptRefUtils(UIRefPane pane) {
        this.pane = pane;
    }

    /**
     * ���˼����µĲ��ţ�������ҵ��ԪĬ����֯
     *
     * @param pk_org
     *        ��֯
     * @author fanly3
     */
    public void groupFilterRef(String pk_org) {
        this.pane.setMultiCorpRef(true);
        this.pane.getRefModel().setFilterRefNodeName(new String[]{"ҵ��Ԫ"}); /*-=notranslate=-*/
        RefInitializeCondition refInitCon = this.pane.getRefUIConfig().getRefFilterInitconds()[0];
        refInitCon.setDefaultPk(pk_org);
        this.pane.getRefUIConfig().setRefFilterInitconds(new RefInitializeCondition[]{refInitCon });
    }

    public void filtByUsedFlag(UFBoolean bUsedflag) {
        StringBuffer where = new StringBuffer();
        if (bUsedflag != null) {
            where.append(" usedflag = '").append(bUsedflag.toString()).append("' ");
            this.pane.setWhereString(where.toString());
        }
    }

    public void filterItemRefByOrg(String pk_org) {
        if (this.pane == null || this.pane.getRefModel() == null) {
            return;
        }
        this.pane.getRefModel().setPk_org(pk_org);
    }
}