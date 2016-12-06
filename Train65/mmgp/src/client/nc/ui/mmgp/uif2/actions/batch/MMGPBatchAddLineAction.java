package nc.ui.mmgp.uif2.actions.batch;

import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.pubapp.uif2app.actions.batch.BatchAddLineAction;
import nc.ui.uif2.model.BatchBillTableModel;
import nc.vo.pub.SuperVO;

/**
 * ��չ��pubapp������
 * <p>
 * ���ݹ��ܽڵ����õ�Ԫ���ݣ��Զ�����voclassname������������
 * 
 * @author wangweiu
 * 
 */
public class MMGPBatchAddLineAction extends BatchAddLineAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2527607103502224110L;

	@Override
	public void setModel(BatchBillTableModel model) {
		super.setModel(model);
		setVoClassName(MMGPMetaUtils.getClassFullName(model.getContext()));
	}
	
	@Override
	protected void setDefaultData(Object obj)
	{
		super.setDefaultData(obj);
		String pk_org = getModel().getContext().getPk_org();
		SuperVO vo = (SuperVO) obj;
		vo.setAttributeValue("pk_org", pk_org);
		vo.setAttributeValue("pk_group", getModel().getContext().getPk_group());
	}

}
