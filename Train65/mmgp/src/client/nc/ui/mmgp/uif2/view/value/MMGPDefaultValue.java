package nc.ui.mmgp.uif2.view.value;

import java.util.HashMap;
import java.util.Map;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.uif2.LoginContext;

public class MMGPDefaultValue implements IDefaultValue {

	/** 用户登录环境，需要注入 */
	private LoginContext context;
	/** 单据类型 */
	private String billtype;
	
	private Map<String, Object> keyValues = new HashMap<String, Object>();
	
	@Override
	public Map<String, Object> getHeadTailValue() {

		// 单据类型
		if (billtype != null) {
			keyValues.put(MMGlobalConst.VBILLTYPECODE, billtype);
		}
		// 单据状态
		keyValues.put(MMGlobalConst.FSTATUSFLAG,
				BillStatusEnum.FREE.toIntValue());
		
		/* Aug 20, 2013 wangweir 制单日期默认不设置 Begin */
		// 单据日期
		keyValues.put(MMGlobalConst.DBILLDATE, null);
		/* Aug 20, 2013 wangweir End */
		
		
		/* Jun 8, 2013 wangweir 创建人、创建时间、制单人、制单时间 需要在保存时在填上，不是给默认值 Begin */
		// 创建时间
//		keyValues.put(MMGlobalConst.CREATIONTIME, AppContext.getInstance()
//				.getServerTime());
		// 制单时间
//		keyValues.put(MMGlobalConst.DMAKEDATE, AppContext.getInstance()
//				.getServerTime());
		/* Jun 8, 2013 wangweir End */
		// 启用状态
		keyValues.put(IBaseServiceConst.ENABLESTATE_FIELD,
				IPubEnumConst.ENABLESTATE_ENABLE);
		if (context != null) {
			// 组织
			keyValues.put(MMGlobalConst.PK_ORG, context.getPk_org());
			// 集团
			keyValues.put(MMGlobalConst.PK_GROUP, context.getPk_group());
			// 创建人
//			keyValues.put(MMGlobalConst.CREATOR, context.getPk_loginUser());
			// 制单人
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
