package nc.ui.mmgp.uif2.view.value;

import java.util.HashMap;
import java.util.Map;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.uif2.LoginContext;

public class MMGPDefaultValue implements IDefaultValue {

	/** �û���¼��������Ҫע�� */
	private LoginContext context;
	/** �������� */
	private String billtype;
	
	private Map<String, Object> keyValues = new HashMap<String, Object>();
	
	@Override
	public Map<String, Object> getHeadTailValue() {

		// ��������
		if (billtype != null) {
			keyValues.put(MMGlobalConst.VBILLTYPECODE, billtype);
		}
		// ����״̬
		keyValues.put(MMGlobalConst.FSTATUSFLAG,
				BillStatusEnum.FREE.toIntValue());
		
		/* Aug 20, 2013 wangweir �Ƶ�����Ĭ�ϲ����� Begin */
		// ��������
		keyValues.put(MMGlobalConst.DBILLDATE, null);
		/* Aug 20, 2013 wangweir End */
		
		
		/* Jun 8, 2013 wangweir �����ˡ�����ʱ�䡢�Ƶ��ˡ��Ƶ�ʱ�� ��Ҫ�ڱ���ʱ�����ϣ����Ǹ�Ĭ��ֵ Begin */
		// ����ʱ��
//		keyValues.put(MMGlobalConst.CREATIONTIME, AppContext.getInstance()
//				.getServerTime());
		// �Ƶ�ʱ��
//		keyValues.put(MMGlobalConst.DMAKEDATE, AppContext.getInstance()
//				.getServerTime());
		/* Jun 8, 2013 wangweir End */
		// ����״̬
		keyValues.put(IBaseServiceConst.ENABLESTATE_FIELD,
				IPubEnumConst.ENABLESTATE_ENABLE);
		if (context != null) {
			// ��֯
			keyValues.put(MMGlobalConst.PK_ORG, context.getPk_org());
			// ����
			keyValues.put(MMGlobalConst.PK_GROUP, context.getPk_group());
			// ������
//			keyValues.put(MMGlobalConst.CREATOR, context.getPk_loginUser());
			// �Ƶ���
//			keyValues.put(MMGlobalConst.VBILLMAKER, context.getPk_loginUser());
		}
		addCusKeyValues(keyValues);
		return keyValues;
	}
	
	protected void addCusKeyValues(Map<String, Object> keyValues){

	}

	public void setContext(LoginContext context) {
		this.context = context;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

    /**
     * @return the keyValues
     */
    protected Map<String, Object> getKeyValues() {
        return keyValues;
    }

    /**
     * @param keyValues the keyValues to set
     */
    protected void setKeyValues(Map<String, Object> keyValues) {
        this.keyValues = keyValues;
    }

    /**
     * @return the context
     */
    protected LoginContext getContext() {
        return context;
    }

    /**
     * @return the billtype
     */
    protected String getBilltype() {
        return billtype;
    }

}
