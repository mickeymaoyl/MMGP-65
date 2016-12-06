package nc.ui.mmgp.flexgant.actions;

import nc.ui.ml.NCLangRes;
import nc.ui.pubapp.gantt.model.AppGantContext;

/**
 * @author wangfan3
 * 
 * 甘特图 粘贴 粘贴为下级
 *
 */
public class MMGPGantPasteAsChildAction extends MMGPGantPasterAction {

	public MMGPGantPasteAsChildAction(AppGantContext context, Integer number) {
		super(context, NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0120")/*粘贴为下级*/, number);
	}

}
