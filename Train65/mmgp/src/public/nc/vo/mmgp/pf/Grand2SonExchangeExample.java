package nc.vo.mmgp.pf;

import java.util.Collection;

import nc.vo.pf.change.ChangeVOAdjustContext;
import nc.vo.pf.change.ExchangeRuleVO;
import nc.vo.pf.change.PfBillMappingUtil;
import nc.vo.pf.change.RuleTypeEnum;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * �����������VOӳ��ʾ��
 * 
 * ��ʾ�����ڣ��ɹ��嵥 ת ��Ŀ�ɹ��ƻ�
 * 
 * @author tanglj
 * 
 */
public class Grand2SonExchangeExample extends MMGPGrandExchange {

	@Override
	public AggregatedValueObject adjustBeforeChange(
			AggregatedValueObject srcVO, ChangeVOAdjustContext adjustContext)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AggregatedValueObject adjustAfterChange(AggregatedValueObject srcVO,
			AggregatedValueObject destVO, ChangeVOAdjustContext adjustContext)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AggregatedValueObject[] batchAdjustBeforeChange(
			AggregatedValueObject[] srcVOs, ChangeVOAdjustContext adjustContext)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ExchangeRuleVO> getGrandSplitItemVOCon() {
		Collection<ExchangeRuleVO> cons = new java.util.ArrayList<ExchangeRuleVO>();
		// �ֶ�ӳ�䣺 itemtd.creplmaterialvid-> items.tditems.creplmaterialvid
		ExchangeRuleVO ruleVO = new ExchangeRuleVO();
		ruleVO.setRuleType(RuleTypeEnum.MOVE.toInt());
		ruleVO.setDest_attr("itemtd.creplmaterialvid");
		ruleVO.setRuleData("items.tditems.creplmaterialvid");
		cons.add(ruleVO);
		// ��ʽ��itemtd.creplmaterialid
		// ->iif(isempty(items.tditems.creplmaterialvid),null,items.tditems.creplmaterialvid)
		ruleVO = new ExchangeRuleVO();
		ruleVO.setRuleType(RuleTypeEnum.FORMULA.toInt());
		ruleVO.setDest_attr("itemtd.creplmaterialid");
		ruleVO.setRuleData("iif(isempty(items.tditems.creplmaterialvid),null,items.tditems.creplmaterialvid)");
		cons.add(ruleVO);
		// ��ֵ��itemtd.pk_group -> SYSGROUP
		ruleVO = new ExchangeRuleVO();
		ruleVO.setRuleType(RuleTypeEnum.ASSIGN.toInt());
		ruleVO.setDest_attr("itemtd.pk_group");
		ruleVO.setRuleData(PfBillMappingUtil.ENV_LOGINGROUP);
		cons.add(ruleVO);
		return cons;
	}

}
