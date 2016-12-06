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
 * 主组织权限设置工具
 * 
 * 1、处理查询面版主组织权限过滤
 * 2、拼接主组织权限过滤描述器，用于报表元数据
 * 3、获得主组织权限CONDITION
 * 
 * @since 6.0
 * @version 2011-8-30 上午09:03:46
 * @author jinjya
 */
public class MMGPMainOrgPowerUtil {
  //主组织ID
  private String[] pk_orgs;
  
  /**
   * MainOrgPowerUtil构造子
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
   * 设置查询面版本主组织权限
   * 
   * @param delegator 查询代理器
   * @param pk_org  主组织字段
   */
  public void setMainOrgPower(QueryConditionDLGDelegator delegator,String pk_org){
    delegator.registerNeedPermissionOrgFieldCode(pk_org,this.pk_orgs);
  }
  
  /**
   * 设置查询面版主组织权限
   * 
   * @param dlg  默认查询对话框
   * @param pk_org 主组织字段
   */
  public void setMainOrgPower(DefaultQueryConditionDLG dlg,String pk_org){
    dlg.registerNeedPermissionOrgFieldCode(pk_org,this.pk_orgs);
  }
  
  /**
   * 获得主组织权限CONDITION
   * 
   * 如果无权限 CONDITION的条件为 fieldcode is null
   * 
   * @param pk_org  主组织字段
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
   * 获阳主组织权限
   * 
   * @return
   */
  public String[] getPermissionPkorgs(){
    return this.pk_orgs;
  }
}
