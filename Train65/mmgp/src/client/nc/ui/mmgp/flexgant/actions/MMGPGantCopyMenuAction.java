package nc.ui.mmgp.flexgant.actions;

import nc.ui.ml.NCLangRes;
import nc.ui.pubapp.gantt.model.AppGantContext;

/**
 * @author wangfan3
 * 
 * ���� һ���˵�
 *
 */
public class MMGPGantCopyMenuAction extends MMGPGantMenuAction{

	public MMGPGantCopyMenuAction( AppGantContext context,
			Integer number) {
		super(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0115")/*����*/, context, number);
	}

}
