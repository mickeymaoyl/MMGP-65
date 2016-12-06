package nc.ui.mmgp.uif2.mediator.mny;

import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;
import nc.vo.mmgp.util.MMGPCurrencyUtil;
import nc.vo.mmgp.util.MMGPOrgUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class MMGPMnyExchgHeadBeforeEditHandler implements
		IAppEventHandler<CardHeadTailBeforeEditEvent> {

	private MMGPMnyExchgMediator mnyExchgMediator;

	public MMGPMnyExchgHeadBeforeEditHandler(
			MMGPMnyExchgMediator mnyExchgMediator) {
		this.mnyExchgMediator = mnyExchgMediator;
	}

	@Override
	public void handleAppEvent(CardHeadTailBeforeEditEvent e) {
		if (e.getKey().equals(mnyExchgMediator.getNexchgrate())) {
			this.changeRateBeforeEdit(e);
		} else {
			e.setReturnValue(true);
		}
	}

	/**
	 * 
	 * 编辑汇率前，如果原币币种与本币币种相同，刚不可编辑
	 * 
	 * @param e
	 */
	private void changeRateBeforeEdit(CardHeadTailBeforeEditEvent e) {
		String financeOrg_v = (String) e.getBillCardPanel()
				.getHeadTailItem(this.mnyExchgMediator.getCorgvid())
				.getValueObject();
		if(financeOrg_v == null){
			e.setReturnValue(false);
			return;
		}
		String curr_field = this.mnyExchgMediator.getCcurrencyid();
		String ccurrencyid = null;
		if (curr_field == null) {
			// 获取组织本币币种
			try {
				String financeOrg = MMGPOrgUtil
						.getOrgIDByVID(financeOrg_v);
				ccurrencyid = MMGPCurrencyUtil.getOrgLocalCurrPK(financeOrg);
			} catch (BusinessException e1) {
				ExceptionUtils.wrappException(e1);
			}
		} else {
			ccurrencyid = (String) e.getBillCardPanel()
					.getHeadTailItem(curr_field).getValueObject();
		}
		String corigcurrid = (String) e.getBillCardPanel()
				.getHeadTailItem(this.mnyExchgMediator.getCorigcurrencyid())
				.getValueObject();
		if(corigcurrid == null){
			e.setReturnValue(false);
			return;
		}
		if (ccurrencyid.equals(corigcurrid)) {
			e.setReturnValue(false);
		} else {
			e.setReturnValue(true);
		}
	}

}
