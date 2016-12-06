package nc.bs.mmgp.pf.bp;

import nc.bs.pubapp.pub.rule.BillCodeCheckRule;
import nc.bs.pubapp.pub.rule.CheckNotNullRule;
import nc.bs.pubapp.pub.rule.CreateBillCodeRule;
import nc.bs.pubapp.pub.rule.OrgDisabledCheckRule;
import nc.impl.mmgp.uif2.rule.MMGPFieldLengthCheckRule;
import nc.impl.mmgp.uif2.rule.MMGPFillInsertDataRule;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.BusinessException;

/**
 * <b>单据新增操作BP</b>
 * <p>
 * 实现单据新增操作的BP类，默认增加了以下规则：</br>
 * 前规则：
 * <ul>
 * <li>补充默认值的规则</li>
 * <li>单据字段长度检查规则</li>
 * <li>单据是否存在表体数据的规则</li>
 * <li>创建单据号规则</li>
 * </ul>
 * 后规则：
 * <ul>
 * <li>单据号校验规则</li>
 * </ul>
 * </p>
 * <p>
 * 创建日期:2013-5-9
 * </p>
 * 
 * @param <T>
 *        VO类型
 * @since  NC V6.3
 * @author zhumh
 */
public class MMGPInsertBP<T extends MMGPAbstractBill> {

	/** The bp tmplate. */
	protected InsertBPTemplate<T> bpTmplate;

	/**
     * Instantiates a new mMGP insert bp.
     * 
     * @param point
     *        the point
     */
	public MMGPInsertBP(IPluginPoint point) {
		super();
		bpTmplate = new InsertBPTemplate<T>(point);
	}

	/**
     * Insert.
     * 
     * @param bills
     *        the bills
     * @return the t[]
     * @throws BusinessException
     *         the business exception
     */
	public T[] insert(T[] bills) throws BusinessException {

		addBeforeRule(bpTmplate.getAroundProcesser(), bills);
		addAfterRule(bpTmplate.getAroundProcesser(), bills);
		return bpTmplate.insert(bills);
	}

	/**
     * Adds the before rule.
     * 
     * @param processer
     *        the processer
     * @param bills
     *        the bills
     */
	@SuppressWarnings("unchecked")
	protected void addBeforeRule(AroundProcesser<T> processer, T[] bills) {

		// 补充默认值的规则
		IRule<T> rule = new MMGPFillInsertDataRule();
		processer.addBeforeRule(rule);
		// 单据字段长度检查规则
		rule = new MMGPFieldLengthCheckRule();
		processer.addBeforeRule(rule);
		// 单据是否存在表体数据的规则
		rule = new CheckNotNullRule();
		processer.addBeforeRule(rule);
        // 组织停用校验
        rule = new OrgDisabledCheckRule(MMGlobalConst.PK_ORG, null);
        processer.addBeforeRule(rule);
        // 长度检查
        rule = new MMGPFieldLengthCheckRule();
        processer.addBeforeRule(rule);
        
		if (!MMArrayUtil.isEmpty(bills)) {
			// 获取审批流相关字段信息
			IFlowBizItf flowInfo = PfMetadataTools.getBizItfImpl(
					bills[0].getParent(), IFlowBizItf.class);
			String billtype = flowInfo.getBilltype();
			String vbillcode = flowInfo
					.getColumnName(IFlowBizItf.ATTRIBUTE_BILLNO);
			// 创建单据号的规则
			rule = new CreateBillCodeRule(billtype, vbillcode,
					MMGlobalConst.PK_GROUP, MMGlobalConst.PK_ORG);
			processer.addBeforeRule(rule);
		}
	}

	/**
     * Adds the after rule.
     * 
     * @param processer
     *        the processer
     * @param bills
     *        the bills
     */
	protected void addAfterRule(AroundProcesser<T> processer, T[] bills) {

		if (!MMArrayUtil.isEmpty(bills)) {
			// 获取审批流相关字段信息
			IFlowBizItf flowInfo = PfMetadataTools.getBizItfImpl(
					bills[0].getParent(), IFlowBizItf.class);
			String billtype = flowInfo.getBilltype();
			String vbillcode = flowInfo
					.getColumnName(IFlowBizItf.ATTRIBUTE_BILLNO);
			// 单据号校验规则
			@SuppressWarnings("unchecked")
			IRule<T> rule = new BillCodeCheckRule(billtype, vbillcode,
					MMGlobalConst.PK_GROUP, MMGlobalConst.PK_ORG);
			processer.addAfterRule(rule);
		}
	}

}
