package nc.impl.mmgp.bd.refcheck.assigncheck;

import java.util.ArrayList;
import java.util.List;

import nc.bs.bd.assignservice.bizvalidate.ICheckResultTempTbInfo;
import nc.bs.bd.assignservice.bizvalidate.INewVisibleDocTempTbInfo;
import nc.bs.bd.assignservice.bizvalidate.IVisibleLostValidateContext;
import nc.bs.bd.pub.IBDEventType;
import nc.bs.bd.util.DBAUtil;
import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IBusinessListener;
import nc.bs.businessevent.IEventType;
import nc.bs.businessevent.bd.BDCommonEvent;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.BusinessException;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ȡ������ǰУ�顱�͡�������Ϊǿ����У�顱ǰУ��
 * </p>
 * 
 * @since �������� Nov 18, 2013
 * @author wangweir
 */
public abstract class EMAbstractAssignRefChecker implements IBusinessListener {

    @Override
    public void doAction(IBusinessEvent e) throws BusinessException {
        // ������Ͽɼ��Է�Χ��ȡ������ǰУ��Ͳ������޸�Ϊǿ����У��
        if (!isEventSourceIDMatch(e) || !isEventTypeMatch(e)) {
            return;
        }
        BDCommonEvent event = (BDCommonEvent) e;
        // ��ȡ��ʱ�����
        IVisibleLostValidateContext context = (IVisibleLostValidateContext) event.getObjs()[0];
        // ���������
        String[] checkSQL = getCheckSQL(context);
        // ִ�����
        DBAUtil.execBatchSql(checkSQL);
    }

    /**
     * �¼������Ƿ���Ҫ���
     * 
     * @param e
     * @return
     */
    protected boolean isEventTypeMatch(IBusinessEvent e) {
        return IBDEventType.MULTIMODE_PARA_CHANGE_TO_TRUE.equals(e.getEventType())
            || IEventType.TYPE_CANCELASSIGN_CHECK.equals(e.getEventType());
    }

    /**
     * �¼�ԴID�Ƿ���Ҫ���
     * 
     * @param e
     * @return
     */
    protected abstract boolean isEventSourceIDMatch(IBusinessEvent e);

    /**
     * <p>
     * ˵����
     * <li>���������</li>
     * </p>
     * 
     * @param context
     * @return
     * @date 2013-11-5 ����10:06:03
     * @since NC6.31
     */
    private String[] getCheckSQL(IVisibleLostValidateContext context) {
        // ��ȡ�ѷ�����ʱ����Ϣ
        INewVisibleDocTempTbInfo visibleDocInfo = context.getNewVisibleDocInfo();
        // ��ȡ�������ʱ����Ϣ
        ICheckResultTempTbInfo checkResultInfo = context.getCheckResultInfo();
        // �����޸ļ���ʱ�����δ���������;ȡ������ʱ�����������ʹ�õ�����
        String exitsstr = context.isVisibleLostData() ? " exists " : " not exists ";
        // // ���õ�������/��������
        // String errormsg = "�ͻ�������";
        // �Ȳ�ѯδ���䵫�����õ����ݣ��ٲ��뵽���������
        StringBuilder checkSQLBuilder = new StringBuilder();
        checkSQLBuilder.append("insert into ").append(checkResultInfo.getTempTbName());
        checkSQLBuilder.append("(").append(ICheckResultTempTbInfo.COL_GROUP_ID).append(", ");
        checkSQLBuilder.append(ICheckResultTempTbInfo.COL_ORG_ID).append(", ");
        checkSQLBuilder.append(ICheckResultTempTbInfo.COL_DOC_ID).append(", ");
        checkSQLBuilder.append(ICheckResultTempTbInfo.COL_MSG).append(") ");
        checkSQLBuilder.append(" select distinct %s, %s, %s, '%s' from %s d ");
        checkSQLBuilder.append(" where ").append(exitsstr);
        checkSQLBuilder.append(" (select 1 from ").append(visibleDocInfo.getTempTbName());
        checkSQLBuilder.append(" where ").append(INewVisibleDocTempTbInfo.COL_GROUP_ID);
        checkSQLBuilder.append("=d.%s and ").append(INewVisibleDocTempTbInfo.COL_ORG_ID);
        checkSQLBuilder.append("=d.%s and ").append(INewVisibleDocTempTbInfo.COL_DOC_ID);
        checkSQLBuilder.append("=d.%s) and coalesce(d.dr, 0) = 0 ");

        String checkSQL = checkSQLBuilder.toString();

        IAssignCheckParam[] checkParams = this.getAssignCheckParam();

        if (MMArrayUtil.isEmpty(checkParams)) {
            return new String[0];
        }

        List<String> allCheckSQL = new ArrayList<String>();
        for (IAssignCheckParam checkParam : checkParams) {
            String[] docFields = checkParam.getDocField();
            if (MMArrayUtil.isEmpty(docFields)) {
                continue;
            }

            for (String docField : docFields) {
                String oneCheckSQL =
                        String.format(
                            checkSQL,
                            checkParam.getGroupField(),
                            checkParam.getOrgField(),
                            docField,
                            checkParam.getErrorMsg(),
                            checkParam.getTableName(),
                            checkParam.getGroupField(),
                            checkParam.getOrgField(),
                            docField);
                allCheckSQL.add(oneCheckSQL);
            }
        }
        return allCheckSQL.toArray(new String[allCheckSQL.size()]);
    }

    protected abstract IAssignCheckParam[] getAssignCheckParam();
}
