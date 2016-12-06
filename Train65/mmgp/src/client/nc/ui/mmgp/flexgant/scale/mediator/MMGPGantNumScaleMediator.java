package nc.ui.mmgp.flexgant.scale.mediator;

import java.util.Collection;

import nc.ui.mmgp.flexgant.model.MMGPGantChartModel;
import nc.ui.mmgp.flexgant.scale.GantNumScaleUtil;
import nc.ui.mmgp.flexgant.scale.MMGPGantScaleProcessor;
import nc.ui.mmgp.uif2.scale.MMGPNumScaleBean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * @Description: TODO
 *               <p>
 *               ÏêÏ¸¹¦ÄÜÃèÊö
 *               </p>
 * @data:2014-5-30ÏÂÎç2:17:34
 * @author: tangxya
 */
public class MMGPGantNumScaleMediator implements BeanFactoryAware {

	private nc.vo.uif2.LoginContext context;

	private Collection<MMGPNumScaleBean> scaleBeanList;

	public MMGPGantChartModel chartModel;

	protected void setGantScale() {
		if (getChartModel() == null || getScaleBeanList() == null) {
			return;
		}
		MMGPGantScaleProcessor processor = new MMGPGantScaleProcessor(
				getContext().getPk_group(), getChartModel());
		GantNumScaleUtil.setScale(processor,
				getScaleBeanList().toArray(new MMGPNumScaleBean[0]));

	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		ListableBeanFactory factory = (ListableBeanFactory) beanFactory;
		this.deal(factory);
	}

	protected void deal(ListableBeanFactory factory) {
		Collection<MMGPNumScaleBean> scaleBeans = BeanFactoryUtils
				.beansOfTypeIncludingAncestors(factory, MMGPNumScaleBean.class)
				.values();

		this.setScaleBeanList(scaleBeans);
		this.setGantScale();
	}

	public nc.vo.uif2.LoginContext getContext() {
		return context;
	}

	public void setContext(nc.vo.uif2.LoginContext context) {
		this.context = context;
	}

	public Collection<MMGPNumScaleBean> getScaleBeanList() {
		return scaleBeanList;
	}

	public void setScaleBeanList(Collection<MMGPNumScaleBean> scaleBeanList) {
		this.scaleBeanList = scaleBeanList;
	}

	public MMGPGantChartModel getChartModel() {
		return chartModel;
	}

	public void setChartModel(MMGPGantChartModel chartModel) {
		this.chartModel = chartModel;
	}

}
