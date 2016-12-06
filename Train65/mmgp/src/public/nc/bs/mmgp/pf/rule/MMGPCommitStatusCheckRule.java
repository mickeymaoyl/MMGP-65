package nc.bs.mmgp.pf.rule;

import nc.impl.pubapp.pattern.rule.ICompareRule;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.pub.pf.IPfRetCheckInfo;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * ��ͻ���VO�������������иı��˵���״̬�������жϲ���ȷ �˴��޸�Ϊ��ԭVO�����ж�
 * 
 * @author zhumh
 * @see nc.bs.pubapp.pub.rule.CommitStatusCheckRule
 */
public class MMGPCommitStatusCheckRule<T extends MMGPAbstractBill> implements
		ICompareRule<T> {

	@Override
	public void process(T[] vos, T[] originVOs) {
		if (originVOs == null) {
			return;
		}
		for (Object bill : originVOs) {
			IFlowBizItf bizItf = PfMetadataTools.getBizItfImpl(bill,
					IFlowBizItf.class);
			Integer approveStatus = bizItf.getApproveStatus();
			if (approveStatus == null
					|| IPfRetCheckInfo.NOSTATE != approveStatus.intValue()) {
				ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl
						.getNCLangRes().getStrByID("pubapp_0",
								"CommitStatusCheckRule-0000")/*
															 * ����״̬����ȷ�������ύ ��
															 */);
			}
		}
	}

}
