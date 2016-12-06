package nc.ui.mmgp.uif2.scale.mediator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.ui.mmgp.uif2.scale.MMGPCardNumScaleBean;
import nc.ui.mmgp.uif2.scale.MMGPScaleBean;
import nc.ui.uif2.editor.IBillCardPanelEditor;
import nc.vo.mmgp.util.MMSystemUtil;
import nc.vo.uif2.LoginContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
/**
 * 单表或树卡界面精度处理Mediator
 *
 */
public class MMGPCardNumScaleMediator implements BeanFactoryAware {
	/**
	 * 卡片界面Editor
	 */
    private IBillCardPanelEditor editor;
    /**
     * 上下文
     */
    private LoginContext context;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        ListableBeanFactory factory = (ListableBeanFactory) beanFactory;

        MMSystemUtil.assertTrue(context != null);

        this.deal(factory);
    }
    /**
     * 加载精度设置bean，并进行初始化
     * @param factory
     */
    protected void deal(ListableBeanFactory factory) {
        List<MMGPScaleBean> allScaleBeans = new ArrayList<MMGPScaleBean>();
        Collection<MMGPScaleBean> scaleBeans =
                BeanFactoryUtils.beansOfTypeIncludingAncestors(factory, MMGPScaleBean.class).values();
        allScaleBeans.addAll(scaleBeans);

        Collection<MMGPCardNumScaleBean> cardBeans =
                BeanFactoryUtils.beansOfTypeIncludingAncestors(factory, MMGPCardNumScaleBean.class).values();

        MMGPPanelNumScaleMediator mnm = new MMGPPanelNumScaleMediator();
        mnm.setContext(getContext());

        mnm.setBillCardPanel(getEditor().getBillCardPanel());

        mnm.setNewScaleBeanList(scaleBeans);
        mnm.setCardScaleBeanList(cardBeans);

        mnm.init();
    }

    public IBillCardPanelEditor getEditor() {
        return editor;
    }

    public void setEditor(IBillCardPanelEditor editor) {
        this.editor = editor;
    }

    public LoginContext getContext() {
        return context;
    }

    public void setContext(LoginContext context) {
        this.context = context;
    }
}
