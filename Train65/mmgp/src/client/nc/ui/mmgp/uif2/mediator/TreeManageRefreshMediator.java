package nc.ui.mmgp.uif2.mediator;

import nc.ui.mmgp.uif2.model.MMGPTreeMangeModelDataManager;
import nc.ui.uif2.model.AbstractTreeManageQueryAndRefreshMrg;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 左边树节点选择发生改变，动态加载右边的数据 modify by liwsh 2014-09-18 增加对右边主子表的支持
 * </p>
 *
 * @since: 创建日期:Sep 18, 2014
 * @author:liwsh
 */
public class TreeManageRefreshMediator extends AbstractTreeManageQueryAndRefreshMrg {

    @Override
    protected void doLeftTreeModelSelectedChanged() {

        Object selectedObj = getLeftTreeModel().getSelectedData();

        //如果选中根节点，由用户自己决定加不加载数据，传入空
        if (selectedObj == null) {
            ((MMGPTreeMangeModelDataManager) getRightManageDataManager()).initManageModelBySelectNode(null);
            return;
        }

        String pk_node = null;
        if (selectedObj instanceof SuperVO) {
            pk_node = ((SuperVO) selectedObj).getPrimaryKey();
        } else if (selectedObj instanceof IBill) {
            pk_node = ((IBill) selectedObj).getPrimaryKey();
        } else {
            ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0024")/*@res "无法识别的VO类型！"*/);
        }

        ((MMGPTreeMangeModelDataManager) getRightManageDataManager()).initManageModelBySelectNode(pk_node);

    }
}