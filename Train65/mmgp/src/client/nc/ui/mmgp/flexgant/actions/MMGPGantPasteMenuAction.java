package nc.ui.mmgp.flexgant.actions;

import nc.ui.ml.NCLangRes;
import nc.ui.pubapp.gantt.model.AppGantContext;

/**
 * @author wangfan3
 * 
 * ����ͼ ճ���Ӳ˵�
 *
 */
public class MMGPGantPasteMenuAction extends MMGPGantMenuAction{

	public MMGPGantPasteMenuAction( AppGantContext context,
			Integer number) {
		super(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0122")/*ճ��*/, context, number);
	}

}
