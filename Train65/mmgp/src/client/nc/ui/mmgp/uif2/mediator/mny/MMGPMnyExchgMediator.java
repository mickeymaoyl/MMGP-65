package nc.ui.mmgp.uif2.mediator.mny;

import java.util.List;

import nc.ui.mmgp.uif2.mediator.num.MMGPNumAssnumCalPara;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;
import nc.ui.pubapp.uif2app.model.IAppModelEx;

/**
 * 
 * ԭ�ұ��һ��ʻ��㴦����
 * 
 * @author wangfan3
 * 
 */
public class MMGPMnyExchgMediator {

	/**
	 * ����
	 */
	private String nexchgrate = "nexchgrate";

	/**
	 * ������֯id
	 */
	private String corgvid = "pk_org";

	/**
	 * ԭ�ұ���
	 */
	private String corigcurrencyid = "corigcurrencyid";

	/**
	 * ���ұ���
	 */
	private String ccurrencyid = null;

	/**
     * 
     */
	private List<MMGPNumAssnumCalPara> mnyExchgCalPara;

	/**
	 * ������ͷ���ʻ����¼�,Ĭ�Ϲر�
	 */
	private boolean supportHeadEvent = false;

	/**
	 * ����������ʻ����¼�
	 */
	private boolean supportBodyEvent = true;

	private IAppModelEx model;

	private MMGPMnyExchgHeadAfterEditHandler afterEdithandler;

	/**
	 * ��ʼ��������
	 */
	public void init() {
		if (this.supportHeadEvent) {
			this.getModel().addAppEventListener(
					CardHeadTailBeforeEditEvent.class,
					new MMGPMnyExchgHeadBeforeEditHandler(this));
			this.getModel().addAppEventListener(
					CardHeadTailAfterEditEvent.class,
					this.getAfterEdithandler());
		}
	}

	public boolean isSupportHeadEvent() {
		return supportHeadEvent;
	}

	public void setSupportHeadEvent(boolean supportHeadEvent) {
		this.supportHeadEvent = supportHeadEvent;
	}

	public boolean isSupportBodyEvent() {
		return supportBodyEvent;
	}

	public void setSupportBodyEvent(boolean supportBodyEvent) {
		this.supportBodyEvent = supportBodyEvent;
	}

	/**
	 * @return the model
	 */
	public IAppModelEx getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(IAppModelEx model) {
		this.model = model;
	}

	public String getNexchgrate() {
		return nexchgrate;
	}

	public void setNexchgrate(String nexchgrate) {
		this.nexchgrate = nexchgrate;
	}

	public String getCorgvid() {
		return corgvid;
	}

	public void setCorgvid(String corgvid) {
		this.corgvid = corgvid;
	}

	public String getCorigcurrencyid() {
		return corigcurrencyid;
	}

	public void setCorigcurrencyid(String corigcurrencyid) {
		this.corigcurrencyid = corigcurrencyid;
	}

	public String getCcurrencyid() {
		return ccurrencyid;
	}

	public void setCcurrencyid(String ccurrencyid) {
		this.ccurrencyid = ccurrencyid;
	}

	public List<MMGPNumAssnumCalPara> getMnyExchgCalPara() {
		return mnyExchgCalPara;
	}

	public void setMnyExchgCalPara(List<MMGPNumAssnumCalPara> mnyExchgCalPara) {
		this.mnyExchgCalPara = mnyExchgCalPara;
	}

	public MMGPMnyExchgHeadAfterEditHandler getAfterEdithandler() {
		if (afterEdithandler == null) {
			afterEdithandler = new MMGPMnyExchgHeadAfterEditHandler(this);
		}
		return afterEdithandler;
	}

	public void setAfterEdithandler(
			MMGPMnyExchgHeadAfterEditHandler afterEdithandler) {
		this.afterEdithandler = afterEdithandler;
	}
}
