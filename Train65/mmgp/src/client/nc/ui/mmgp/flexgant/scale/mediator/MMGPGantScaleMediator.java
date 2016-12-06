package nc.ui.mmgp.flexgant.scale.mediator;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;

import nc.ui.mmgp.flexgant.model.MMGPGantChartModel;
import nc.ui.mmgp.flexgant.scale.GantNumScaleUtil;
import nc.ui.mmgp.flexgant.scale.MMGPGantScaleProcessor;
import nc.ui.mmgp.uif2.scale.MMGPScaleBean;

public class MMGPGantScaleMediator implements BeanFactoryAware {

    
    Collection<MMGPScaleBean> scaleBeanList ;
    
    private nc.vo.uif2.LoginContext context;
    
    public MMGPGantChartModel chartModel;
    
    
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        ListableBeanFactory factory = (ListableBeanFactory) beanFactory;
        this.deal(factory);
    }

    protected void deal(ListableBeanFactory factory) {
        Collection<MMGPScaleBean> scaleBeans = BeanFactoryUtils
                .beansOfTypeIncludingAncestors(factory, MMGPScaleBean.class)
                .values();

        this.setScaleBeanList(scaleBeans);
        this.setGantScale();
    }
    
    protected void setGantScale() {
        if (getChartModel() == null || getScaleBeanList() == null) {
            return;
        }
        MMGPGantScaleProcessor processor = new MMGPGantScaleProcessor(
                getContext().getPk_group(), getChartModel());
        GantNumScaleUtil.setScale(processor,
                getScaleBeanList().toArray(new MMGPScaleBean[0]));

    }

    public Collection<MMGPScaleBean> getScaleBeanList() {
        return scaleBeanList;
    }

    public void setScaleBeanList(Collection<MMGPScaleBean> scaleBeanList) {
        this.scaleBeanList = scaleBeanList;
    }

    public nc.vo.uif2.LoginContext getContext() {
        return context;
    }

    public void setContext(nc.vo.uif2.LoginContext context) {
        this.context = context;
    }

    public MMGPGantChartModel getChartModel() {
        return chartModel;
    }

    public void setChartModel(MMGPGantChartModel chartModel) {
        this.chartModel = chartModel;
    }

  
}
