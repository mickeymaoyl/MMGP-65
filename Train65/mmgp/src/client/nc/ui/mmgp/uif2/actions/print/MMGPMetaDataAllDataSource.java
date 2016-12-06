package nc.ui.mmgp.uif2.actions.print;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.ui.bd.pub.actions.print.MetaDataAllDatasSource;
import nc.ui.mmgp.uif2.scale.MMGPListNumScaleBean;
import nc.ui.mmgp.uif2.scale.MMGPNumScaleBean;
import nc.ui.mmgp.uif2.scale.MMGPScaleBean;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMSystemUtil;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;

@SuppressWarnings("serial")
public class MMGPMetaDataAllDataSource extends MetaDataAllDatasSource implements BeanFactoryAware {
	private List<MMGPScaleBean> scaleBeans = new ArrayList<MMGPScaleBean>();

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

	//
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		ListableBeanFactory factory = (ListableBeanFactory) beanFactory;

		MMSystemUtil.assertTrue(getModel().getContext() != null);

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
		Collection<MMGPListNumScaleBean> listBeans = BeanFactoryUtils
				.beansOfTypeIncludingAncestors(factory,
						MMGPListNumScaleBean.class).values();
		scaleBeans.addAll(listBeans);

	}

}