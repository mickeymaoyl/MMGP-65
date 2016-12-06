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
 * <b> 简要描述功能 </b>
 * <p>
 * “取消分配前校验”和“参数改为强分配校验”前校验
 * </p>
 * 
 * @since 创建日期 Nov 18, 2013
 * @author wangweir
 */
public abstract class EMAbstractAssignRefChecker implements IBusinessListener {

    @Override
    public void doAction(IBusinessEvent e) throws BusinessException {
        // 检查物料可见性范围，取消分配前校验和参数据修改为强分配校验
        if (!isEventSourceIDMatch(e) || !isEventTypeMatch(e)) {
            return;
        }
        BDCommonEvent event = (BDCommonEvent) e;
        // 获取临时表对象
        IVisibleLostValidateContext context = (IVisibleLostValidateContext) event.getObjs()[0];
        // 构造检查语句
        String[] checkSQL = getCheckSQL(context);
        // 执行语句
        DBAUtil.execBatchSql(checkSQL);
    }

    /**
     * 事件类型是否需要检查
     * 
     * @param e
     * @return
     */
    protected boolean isEventTypeMatch(IBusinessEvent e) {
        return IBDEventType.MULTIMODE_PARA_CHANGE_TO_TRUE.equals(e.getEventType())
            || IEventType.TYPE_CANCELASSIGN_CHECK.equals(e.getEventType());
    }

    /**
     * 事件源ID是否需要检查
     * 
     * @param e
     * @return
     */
    protected abstract boolean isEventSourceIDMatch(IBusinessEvent e);

    /**
     * <p>
     * 说明：
     * <li>构造检查语句</li>
     * </p>
     * 
     * @param context
     * @return
     * @date 2013-11-5 下午10:06:03
     * @since NC6.31
     */
    private String[] getCheckSQL(IVisibleLostValidateContext context) {
        // 获取已分配临时表信息
        INewVisibleDocTempTbInfo visibleDocInfo = context.getNewVisibleDocInfo();
        // 获取结果集临时表信息
        ICheckResultTempTbInfo checkResultInfo = context.getCheckResultInfo();
        // 参数修改监听时，检查未分配的数据;取消分配时，检查分配后已使用的数据
        String exitsstr = context.isVisibleLostData() ? " exists " : " not exists ";
        // // 引用档案名称/单据名称
        // String errormsg = "客户物料码";
        // 先查询未分配但已引用的数据，再插入到检查结果表中
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
