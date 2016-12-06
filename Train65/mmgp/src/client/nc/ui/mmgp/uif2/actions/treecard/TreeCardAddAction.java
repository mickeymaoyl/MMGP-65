package nc.ui.mmgp.uif2.actions.treecard;

import nc.ui.mmgp.uif2.actions.MMGPAddAction;
import nc.ui.pubapp.uif2app.event.OrgChangedEvent;
import nc.ui.uif2.AppEvent;
import nc.vo.mmgp.util.MMStringUtil;

/**
 * ���� �����й����������ӣ�ֻ��ѡ���˾���Ĺ����������ӡ���ť�ſ���
 * 
 * @author yangwm
 * 
 */
@SuppressWarnings({ "serial" })
public class TreeCardAddAction extends MMGPAddAction {

	private String pk_org;

	@Override
	protected boolean isActionEnable() {
		return super.isActionEnable() && MMStringUtil.isNotEmpty(pk_org);
	}

	@Override
	public void handleEvent(AppEvent event) {
		super.handleEvent(event);
		if (OrgChangedEvent.class.getName().equals(event.getType())) {
			OrgChangedEvent orgChangedEvent = (OrgChangedEvent) event;
			pk_org = orgChangedEvent.getNewPkOrg();
			updateStatus();
		}
	}
}
