package nc.ui.mmgp.base.lineaction;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import nc.ui.sm.login.ClientAssistant;

/**
 * �в���������
 * @deprecated
 */
@SuppressWarnings("serial")
public final class MMGPTableLineAction extends AbstractAction {

    private int type = -1;

    private IMMLineAction action = null;

    private MMGPTableLineAction(IMMLineAction action,
                                int type,
                                String name,
                                Icon icon) {
        super(name, icon);
        this.action = action;
        this.type = type;
        putValue(Action.SHORT_DESCRIPTION, name);
    }

    public void actionPerformed(ActionEvent e) {
        action.doLineAction(type);
    }

    /**
     * ע�ᰴť��ͼ�ꡢ����
     * @param action
     * @param type
     * @return
     */
    public static MMGPTableLineAction getInstance(IMMLineAction action,
                                                  int type) {

        String[] names =
                new String[]{
                    nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000012"/* @res"������" */),
                    nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000016"/* @res"������" */),
                    nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000013"/* @res"ɾ����" */),
                    nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000014"/* @res"������" */),
                    nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000015"/* @res"ճ����" */),
                    nc.ui.ml.NCLangRes.getInstance().getStrByID("_Bill", "UPP_Bill-000011"/* @res"ճ������β" */) };
        String[] iconpath =
                new String[]{
                    "/images/toolbar/icon2/addline.gif",
                    "/images/toolbar/icon2/insertline.gif",
                    "/images/toolbar/icon2/deleteline.gif",
                    "/images/toolbar/icon2/copy.gif",
                    "/images/toolbar/icon2/paste.gif",
                    "/images/toolbar/icon2/paste.gif" };

        Icon icon = ClientAssistant.loadImageIcon(iconpath[type]);

        String name = names[type];

        return new MMGPTableLineAction(action, type, name, icon);
    }

}
