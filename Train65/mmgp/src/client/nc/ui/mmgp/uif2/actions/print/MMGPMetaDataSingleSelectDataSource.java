package nc.ui.mmgp.uif2.actions.print;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource;
import nc.ui.mmgp.uif2.scale.MMGPCardNumScaleBean;
import nc.ui.mmgp.uif2.scale.MMGPNumScaleBean;
import nc.ui.mmgp.uif2.scale.MMGPScaleBean;
import nc.vo.mmgp.util.MMArrayUtil;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;

@SuppressWarnings("serial")
public class MMGPMetaDataSingleSelectDataSource extends
		MetaDataSingleSelectDataSource implements BeanFactoryAware {
	private List<MMGPScaleBean> scaleBeans = new ArrayList<MMGPScaleBean>();

	public MMGPMetaDataSingleSelectDataSource() {
	}

	@Override
	public Object[] getMDObjects() {
		Object[] objs = super.getMDObjects();
		if (MMArrayUtil.isEmpty(objs)) {
			return null;
		}
		PrintNumScaleUtils.setPrintScale(objs, getModel().getContext()
				.getPk_group(), scaleBeans);
		return objs;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		ListableBeanFactory factory = (ListableBeanFactory) beanFactory;
		this.deal(factory);
	}

	protected void deal(ListableBeanFactory factory) {
	    Collection<MMGPScaleBean> commonScaleBean =
                BeanFactoryUtils.beansOfTypeIncludingAncestors(factory, MMGPScaleBean.class).values();
        scaleBeans.addAll(commonScaleBean);
        
		Collection<MMGPNumScaleBean> numScaleBeans = BeanFactoryUtils
				.beansOfTypeIncludingAncestors(factory, MMGPNumScaleBean.class)
				.values();
		scaleBeans.addAll(numScaleBeans);
		Collection<MMGPCardNumScaleBean> cardBeans = BeanFactoryUtils
				.beansOfTypeIncludingAncestors(factory,
						MMGPCardNumScaleBean.class).values();
		scaleBeans.addAll(cardBeans);

	}
}
