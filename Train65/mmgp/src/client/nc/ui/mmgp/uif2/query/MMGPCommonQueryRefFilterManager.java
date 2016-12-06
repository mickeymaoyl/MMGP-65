package nc.ui.mmgp.uif2.query;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * <b> </b>
 * @author chenleif
 * @date 2013-6-19
 * @description
 */
public class MMGPCommonQueryRefFilterManager implements BeanFactoryAware {
	private Collection<MMGPCommonQueryRefFilter> refFilters;
	
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		ListableBeanFactory factory = (ListableBeanFactory) beanFactory;
		refFilters = BeanFactoryUtils
				.beansOfTypeIncludingAncestors(factory, MMGPCommonQueryRefFilter.class)
				.values();
	}

	public Collection<MMGPCommonQueryRefFilter> getRefFilters() {
		return refFilters;
	}

	public void setRefFilters(Collection<MMGPCommonQueryRefFilter> refFilters) {
		this.refFilters = refFilters;
	}

}
