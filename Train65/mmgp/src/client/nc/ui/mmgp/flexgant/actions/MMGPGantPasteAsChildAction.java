package nc.ui.mmgp.flexgant.actions;

import nc.ui.ml.NCLangRes;
import nc.ui.pubapp.gantt.model.AppGantContext;

/**
 * @author wangfan3
 * 
 * ����ͼ ճ�� ճ��Ϊ�¼�
 *
 */
public class MMGPGantPasteAsChildAction extends MMGPGantPasterAction {

	public MMGPGantPasteAsChildAction(AppGantContext context, Integer number) {
		super(context, NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0120")/*ճ��Ϊ�¼�*/, number);
	}

}
