package nc.ui.mmgp.uif2.actions.report;

import nc.ui.pubapp.uif2app.query2.DefaultQueryConditionDLG;
import nc.ui.pubapp.uif2app.query2.QueryConditionDLGDelegator;
import nc.ui.querytemplate.operator.IOperatorConstants;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.IQueryConstants;
import nc.vo.uap.rbac.FuncSubInfo;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.FreeReportContextKey;

/**
 * ����֯Ȩ�����ù���
 * 
 * 1�������ѯ�������֯Ȩ�޹���
 * 2��ƴ������֯Ȩ�޹��������������ڱ���Ԫ����
 * 3���������֯Ȩ��CONDITION
 * 
 * @since 6.0
 * @version 2011-8-30 ����09:03:46
 * @author jinjya
 */
public class MMGPMainOrgPowerUtil {
  //����֯ID
  private String[] pk_orgs;
  
  /**
   * MainOrgPowerUtil������
   *
   * @param context
   */
  public MMGPMainOrgPowerUtil(IContext context){
    FuncSubInfo fucSubInfo = (FuncSubInfo)context.getAttribute(FreeReportContextKey.FUNC_NODE_INFO);
    if(fucSubInfo!=null){
      this.pk_orgs = fucSubInfo.getFuncPermissionPkorgs();
    }
  }
  /**
   * ���ò�ѯ��汾����֯Ȩ��
   * 
   * @param delegator ��ѯ������
   * @param pk_org  ����֯�ֶ�
   */
  public void setMainOrgPower(QueryConditionDLGDelegator delegator,String pk_org){
    delegator.registerNeedPermissionOrgFieldCode(pk_org,this.pk_orgs);
  }
  
  /**
   * ���ò�ѯ�������֯Ȩ��
   * 
   * @param dlg  Ĭ�ϲ�ѯ�Ի���
   * @param pk_org ����֯�ֶ�
   */
  public void setMainOrgPower(DefaultQueryConditionDLG dlg,String pk_org){
    dlg.registerNeedPermissionOrgFieldCode(pk_org,this.pk_orgs);
  }
  
  /**
   * �������֯Ȩ��CONDITION
   * 
   * �����Ȩ�� CONDITION������Ϊ fieldcode is null
   * 
   * @param pk_org  ����֯�ֶ�
   * @return
   */
  public ConditionVO getMainOrgCondition(String fieldcode) {
    ConditionVO cond = new ConditionVO();
    cond.setDataType(IQueryConstants.UFREF);
    cond.setFieldCode(fieldcode);
    cond.setOperaCode(IOperatorConstants.IN);
    StringBuffer valuestr = new StringBuffer();
    if(!MMArrayUtil.isEmpty(this.pk_orgs)){
      valuestr.append("(");
      for(String key:this.pk_orgs){
        valuestr.append("'"+key+"',");
      }
      valuestr.deleteCharAt(valuestr.length()-1);
      valuestr.append(")");
      cond.setValue(valuestr.toString());
    } else {
      cond.setOperaCode(IOperatorConstants.ISNULL);
    }
    return cond;
  }
  
  /**
   * ��������֯Ȩ��
   * 
   * @return
   */
  public String[] getPermissionPkorgs(){
    return this.pk_orgs;
  }
}
