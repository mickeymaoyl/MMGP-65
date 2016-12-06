package nc.ui.mmgp.uif2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.util.remotecallcombination.IRemoteCallCombinatorUser;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 合并远程调用
 * </p>
 * 
 * @since 创建日期 Oct 9, 2013
 * @author wangweir
 */
public class MMGPUIF2RemoteCallCombinatorCaller extends UIF2RemoteCallCombinatorCaller implements BeanFactoryAware {

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        ListableBeanFactory factory = (ListableBeanFactory) beanFactory;

        this.deal(factory);
    }

    /**
     * @param factory
     */
    private void deal(ListableBeanFactory factory) {
        Collection<IRemoteCallCombinatorUser> remoteCallCombinatorUserBeans =
                BeanFactoryUtils.beansOfTypeIncludingAncestors(factory, IRemoteCallCombinatorUser.class).values();
        List<IRemoteCallCombinatorUser> remoteCallComninator = new ArrayList<IRemoteCallCombinatorUser>();
        if (MMCollectionUtil.isNotEmpty(remoteCallCombinatorUserBeans)) {
            remoteCallComninator.addAll(remoteCallCombinatorUserBeans);
        }

        this.setRemoteCallers(remoteCallComninator);
    }

}
