package nc.vo.mmgp.pf;

import java.util.Collection;

import nc.vo.pf.change.ChangeVOAdjustContext;
import nc.vo.pf.change.ExchangeRuleVO;
import nc.vo.pf.change.PfBillMappingUtil;
import nc.vo.pf.change.RuleTypeEnum;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * �������������VOӳ��ʾ��
 * 
 * ��ʾ�����ڣ������嵥 ת  ����ƻ�ά��
 * 
 * @author tanglj
 *
 */
public class Grand2GrandExchangeExample extends MMGPGrandExchange {

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
		// �ֶ�ӳ�䣺distrplanbodys.rpllist.nnum -> yg_palletlist_b.palletltrep.nnum
		ExchangeRuleVO ruleVO = new ExchangeRuleVO();
		ruleVO.setRuleType(RuleTypeEnum.MOVE.toInt());
		ruleVO.setDest_attr("distrplanbodys.rpllist.nnum");
		ruleVO.setRuleData("yg_palletlist_b.palletltrep.nnum");
		cons.add(ruleVO);
		// �ֶ�ӳ�䣺distrplanbodys.rpllist.ccustomerid -> yg_palletlist_b.palletltrep.ccustomerid
		ruleVO = new ExchangeRuleVO();
		ruleVO.setRuleType(RuleTypeEnum.MOVE.toInt());
		ruleVO.setDest_attr("distrplanbodys.rpllist.ccustomerid");
		ruleVO.setRuleData("yg_palletlist_b.palletltrep.ccustomerid");
		cons.add(ruleVO);
		// ��ʽ��distrplanbodys.rpllist.vmemo -> iif(isempty(yg_palletlist_b.palletltrep.ccustomerid),null,yg_palletlist_b.palletltrep.ccustomerid)
		ruleVO = new ExchangeRuleVO();
		ruleVO.setRuleType(RuleTypeEnum.FORMULA.toInt());
		ruleVO.setDest_attr("distrplanbodys.rpllist.vmemo");
		ruleVO.setRuleData("iif(isempty(yg_palletlist_b.palletltrep.ccustomerid),null,yg_palletlist_b.palletltrep.ccustomerid)");
		cons.add(ruleVO);
		// ��ֵ: distrplanbodys.rpllist.pk_group -> SYSGROUP
		ruleVO = new ExchangeRuleVO();
		ruleVO.setRuleType(RuleTypeEnum.ASSIGN.toInt());
		ruleVO.setDest_attr("distrplanbodys.rpllist.pk_group");
		ruleVO.setRuleData(PfBillMappingUtil.ENV_LOGINGROUP);
		cons.add(ruleVO);
		return cons;
	}
}
