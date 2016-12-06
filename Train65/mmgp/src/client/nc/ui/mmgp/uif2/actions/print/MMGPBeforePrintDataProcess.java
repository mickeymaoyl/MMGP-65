package nc.ui.mmgp.uif2.actions.print;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;

import nc.ui.mmgp.uif2.scale.MMGPListNumScaleBean;
import nc.ui.mmgp.uif2.scale.MMGPNumScaleBean;
import nc.ui.mmgp.uif2.scale.MMGPScaleBean;
import nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction.IBeforePrintDataProcess;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMSystemUtil;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Jul 23, 2013
 * @author wangweir
 */
public class MMGPBeforePrintDataProcess implements IBeforePrintDataProcess, BeanFactoryAware {

    private List<MMGPScaleBean> scaleBeans = new ArrayList<MMGPScaleBean>();

    protected AbstractUIAppModel model;

    /*
     * (non-Javadoc)
     * @see
     * nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction.IBeforePrintDataProcess#processData(java.lang.Object[])
     */
    @Override
    public Object[] processData(Object[] datas) {
        if (MMArrayUtil.isEmpty(datas)) {
            return null;
        }
        PrintNumScaleUtils.setPrintScale(datas, getModel().getContext().getPk_group(), scaleBeans);
        return datas;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
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
        
        Collection<MMGPNumScaleBean> numScaleBeans =
                BeanFactoryUtils.beansOfTypeIncludingAncestors(factory, MMGPNumScaleBean.class).values();
        scaleBeans.addAll(numScaleBeans);
        Collection<MMGPListNumScaleBean> listBeans =
                BeanFactoryUtils.beansOfTypeIncludingAncestors(factory, MMGPListNumScaleBean.class).values();
        scaleBeans.addAll(listBeans);
    }

    /**
     * @return the model
     */
    public AbstractUIAppModel getModel() {
        return model;
    }

    /**
     * @param model
     *        the model to set
     */
    public void setModel(AbstractUIAppModel model) {
        this.model = model;
    }
}
