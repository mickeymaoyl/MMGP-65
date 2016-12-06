package nc.ui.mmgp.uif2.mediator.mny;

import nc.bs.bd.currinfo.ExchangeRateCache;
import nc.ui.mmgp.uif2.mediator.num.MMGPNumAssnumCalPara;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.vo.mmgp.util.MMGPCurrencyUtil;
import nc.vo.mmgp.util.MMGPOrgUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class MMGPMnyExchgHeadAfterEditHandler implements
		IAppEventHandler<CardHeadTailAfterEditEvent> {

	protected MMGPMnyExchgMediator mnyExchgMediator;

	public MMGPMnyExchgHeadAfterEditHandler(
			MMGPMnyExchgMediator mnyExchgMediator) {
		this.mnyExchgMediator = mnyExchgMediator;
	}

	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent e) {
		// 编辑财务组织
		if (e.getKey().equals(mnyExchgMediator.getCorgvid())) {
			afterEditFinanceOrg(e);
		} else if (e.getKey().equals(mnyExchgMediator.getCorigcurrencyid())) {
			afterEditCorgcurrencyid(e);
		} else if (e.getKey().equals(mnyExchgMediator.getNexchgrate())) {
			afterEditNexchgrate(e);
		} else {
			for (MMGPNumAssnumCalPara calPara : this.mnyExchgMediator
					.getMnyExchgCalPara()) {
				if (e.getKey().equals(calPara.getNnum())) {
					UFDouble outrate = getOutRate(e.getBillCardPanel());
					this.calOrginMny(outrate, calPara, e);
					return;
				} else if (e.getKey().equals(calPara.getNassistnum())) {
					UFDouble outrate = getOutRate(e.getBillCardPanel());
					this.calLocalMny(outrate, calPara, e);
					return;
				}
			}
		}
	}

	protected UFDouble getOutRate(BillCardPanel billCardPanel) {
		UFDouble outrate = (UFDouble) billCardPanel.getHeadItem(
				mnyExchgMediator.getNexchgrate()).getValueObject();
		return outrate;
	}

	private void afterEditNexchgrate(CardHeadTailAfterEditEvent e) {
		UFDouble outrate = getOutRate(e.getBillCardPanel());
		calLocalMny(outrate, e);
	}

	protected void afterEditOrginMny(CardHeadTailAfterEditEvent e) {
		UFDouble outrate = getOutRate(e.getBillCardPanel());
		calLocalMny(outrate, e);

	}

	protected void afterEditCorgcurrencyid(CardHeadTailAfterEditEvent e) {
		String financeOrg_v = (String) e.getBillCardPanel()
				.getHeadItem(mnyExchgMediator.getCorgvid()).getValueObject();
		if (financeOrg_v == null) {
			e.getBillCardPanel().getHeadItem(mnyExchgMediator.getNexchgrate())
					.setValue(null);
			return;
		}
		UFDouble outrate = getMnyExchge(e, financeOrg_v);
		e.getBillCardPanel().getHeadItem(mnyExchgMediator.getNexchgrate())
				.setValue(outrate);
		calLocalMny(outrate, e);
	}

	private UFDouble getMnyExchge(CardHeadTailAfterEditEvent e,
			String financeOrg_v) {
		String financeOrg = MMGPOrgUtil.getOrgIDByVID(financeOrg_v);
		String curr_field = this.mnyExchgMediator.getCcurrencyid();
		String ccurrencyid = null;
		if (curr_field == null) {
			// 获取组织本币币种
			try {
				ccurrencyid = MMGPCurrencyUtil.getOrgLocalCurrPK(financeOrg);
			} catch (BusinessException e1) {
				ExceptionUtils.wrappException(e1);
			}
		} else {
			ccurrencyid = (String) e.getBillCardPanel()
					.getHeadTailItem(curr_field).getValueObject();
		}
		// 原币币种
		String currType = (String) e.getBillCardPanel()
				.getHeadItem(mnyExchgMediator.getCorigcurrencyid())
				.getValueObject();
		UFDouble outrate = null;
		if(currType == null){
			return outrate;
		}
		if (ccurrencyid.equals(currType)) {
			outrate = UFDouble.ONE_DBL;
		} else {
			UFDate date = AppContext.getInstance().getBusiDate();
			try {
				outrate = MMGPCurrencyUtil.getRate(financeOrg, currType, date,
						ExchangeRateCache.RATE_TYPE_MIDDLE);
			} catch (BusinessException e1) {
				ExceptionUtils.wrappException(e1);
			}
		}
		return outrate;
	}

	protected void afterEditFinanceOrg(CardHeadTailAfterEditEvent e) {
		String financeOrg_v = (String) e.getBillCardPanel()
				.getHeadItem(mnyExchgMediator.getCorgvid()).getValueObject();
		if (financeOrg_v == null) {
			e.getBillCardPanel().getHeadItem(mnyExchgMediator.getNexchgrate())
					.setValue(null);
			return;
		}
		UFDouble outrate = getMnyExchge(e, financeOrg_v);
		e.getBillCardPanel().getHeadItem(mnyExchgMediator.getNexchgrate())
				.setValue(outrate);
		calLocalMny(outrate, e);
	}

	protected void calLocalMny(UFDouble outrate, CardHeadTailAfterEditEvent e) {
		for (MMGPNumAssnumCalPara calPara : this.mnyExchgMediator
				.getMnyExchgCalPara()) {
			calLocalMny(outrate, calPara, e);
		}

	}

	protected void calOrginMny(UFDouble outrate, CardHeadTailAfterEditEvent e) {
		for (MMGPNumAssnumCalPara calPara : this.mnyExchgMediator
				.getMnyExchgCalPara()) {
			calOrginMny(outrate, calPara, e);
		}

	}

	/**
	 * 
	 * 根据原币计算本币金额
	 * 
	 * @param outrate
	 * @param calPara
	 * @param e
	 */
	protected void calLocalMny(UFDouble outrate, MMGPNumAssnumCalPara calPara,
			CardHeadTailAfterEditEvent e) {
		if (outrate == null) {
			return;
		}
		String ncurrencymny_field = calPara.getNnum();
		String norigcurrencymny_field = calPara.getNassistnum();
		UFDouble norigcurrencymny = (UFDouble) e.getBillCardPanel()
				.getHeadItem(norigcurrencymny_field).getValueObject();
		if (norigcurrencymny != null) {
			UFDouble ncurrencymny = norigcurrencymny.multiply(outrate);
			e.getBillCardPanel().getHeadItem(ncurrencymny_field)
					.setValue(ncurrencymny);
		}

	}

	/**
	 * 根据本币计算原币
	 * 
	 * @param outrate
	 * @param calPara
	 * @param e
	 */
	private void calOrginMny(UFDouble outrate, MMGPNumAssnumCalPara calPara,
			CardHeadTailAfterEditEvent e) {
		if (outrate == null) {
			return;
		}
		String ncurrencymny_field = calPara.getNnum();
		String norigcurrencymny_field = calPara.getNassistnum();
		UFDouble ncurrencymny = (UFDouble) e.getBillCardPanel()
				.getHeadItem(ncurrencymny_field).getValueObject();
		if (ncurrencymny != null) {
			UFDouble norigcurrencymny = ncurrencymny.div(outrate);
			e.getBillCardPanel().getHeadItem(norigcurrencymny_field)
					.setValue(norigcurrencymny);
		}
	}

}
