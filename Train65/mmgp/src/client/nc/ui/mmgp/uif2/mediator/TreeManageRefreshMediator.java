package nc.ui.mmgp.uif2.mediator;

import nc.ui.mmgp.uif2.model.MMGPTreeMangeModelDataManager;
import nc.ui.uif2.model.AbstractTreeManageQueryAndRefreshMrg;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ������ڵ�ѡ�����ı䣬��̬�����ұߵ����� modify by liwsh 2014-09-18 ���Ӷ��ұ����ӱ��֧��
 * </p>
 *
 * @since: ��������:Sep 18, 2014
 * @author:liwsh
 */
public class TreeManageRefreshMediator extends AbstractTreeManageQueryAndRefreshMrg {

    @Override
    protected void doLeftTreeModelSelectedChanged() {

        Object selectedObj = getLeftTreeModel().getSelectedData();

        //���ѡ�и��ڵ㣬���û��Լ������Ӳ��������ݣ������
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
            ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0024")/*@res "�޷�ʶ���VO���ͣ�"*/);
        }

        ((MMGPTreeMangeModelDataManager) getRightManageDataManager()).initManageModelBySelectNode(pk_node);

    }
}