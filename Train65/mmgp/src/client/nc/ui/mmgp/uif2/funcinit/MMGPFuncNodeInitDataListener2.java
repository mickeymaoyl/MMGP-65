package nc.ui.mmgp.uif2.funcinit;

import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener;
import nc.vo.mmgp.util.MMStringUtil;

public class MMGPFuncNodeInitDataListener2 extends
		DefaultFuncNodeInitDataListener {
	@Override
	public String getVoClassName() {
		String superVoclass = super.getVoClassName();
		if (MMStringUtil.isEmpty(superVoclass)) {
			superVoclass = MMGPMetaUtils.getClassFullName(getContext());
		}
		return superVoclass;
	}
}
