package nc.ui.mmgp.uif2.scale.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.ui.mmgp.uif2.scale.MMGPListNumScaleBean;
import nc.ui.mmgp.uif2.scale.MMGPNumScaleBean;
import nc.ui.mmgp.uif2.scale.MMGPScaleBean;
import nc.ui.mmgp.uif2.scale.mediator.MMGPPanelNumScaleMediator;
import nc.ui.pubapp.billref.dest.ITransferListViewProcessor;
import nc.ui.pubapp.uif2app.view.FractionFixMediator;
import nc.ui.uif2.editor.BillListView;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.vo.mmgp.util.MMSystemUtil;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 拉单界面精度及换算率显示处理类
 * </p>
 * 
 * @since 创建日期 Sep 27, 2013
 * @author wangweir
 */
public class MMGPTransferListViewProcessor implements ITransferListViewProcessor, BeanFactoryAware {

    private List<MMGPScaleBean> scaleBeans = new ArrayList<MMGPScaleBean>();

    private AbstractUIAppModel model;

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.billref.dest.ITransferListViewProcessor#processAfter(nc.ui.uif2.editor.BillListView,
     * java.lang.Object[])
     */
    @Override
    public void processAfter(BillListView listView,
                             Object[] bills) {

    }

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.billref.dest.ITransferListViewProcessor#processBefore(nc.ui.uif2.editor.BillListView,
     * java.lang.Object[])
     */
    @Override
    public void processBefore(BillListView listView,
                              Object[] bills) {
        MMGPPanelNumScaleMediator mnm = new MMGPPanelNumScaleMediator();
        mnm.setContext(this.getModel().getContext());

        mnm.setBillListPanel(listView.getBillListPanel());

        mnm.setNewScaleBeanList(scaleBeans);

        mnm.init();

        FractionFixMediator fractionFixMediator = new FractionFixMediator(listView);
        try {
            fractionFixMediator.afterPropertiesSet();
        } catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }
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
     * @return the scaleBeans
     */
    public List<MMGPScaleBean> getScaleBeans() {
        return scaleBeans;
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
