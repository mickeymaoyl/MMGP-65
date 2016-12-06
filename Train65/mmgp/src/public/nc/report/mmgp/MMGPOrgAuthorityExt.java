package nc.report.mmgp;

import nc.pub.smart.metadata.Field;
import nc.pub.smart.model.SmartModel;
import nc.pub.smart.model.descriptor.FilterDescriptor;
import nc.pub.smart.model.descriptor.FilterItem;
import nc.vo.bd.pub.NODE_TYPE;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

import org.apache.commons.lang.StringUtils;

import com.ufida.dataset.IContext;
import com.ufida.iufo.report.extend.DefaultOrgAuthorityExt;
import com.ufida.report.anareport.FreePrivateContextKey;
import com.ufida.zior.exception.MessageException;

/**
 * <b> 报表主组织权限扩展类 </b>
 * <p>
 * 组织或集团级的汇总报表，汇总维度不包含组织或集团时，由于平台在默认的组织扩展类中会拼接组织SQL，导致SQL出错。 此时可以将自由报表预置中的数据 -> 功能注册 ->
 * 组织权限扩展类注册为此类，此类将会拼出与平台不同的SQL，回避错误。 <br/>
 * <br/>
 * 注：如在报表预置中包含主组织字段，则不能关联此扩展类，否则可能会出现问题。
 * </p>
 * 
 * @since 创建日期 2013-11-7
 * @author dongchx
 */
public class MMGPOrgAuthorityExt extends DefaultOrgAuthorityExt {

    @Override
    public FilterDescriptor getMainOrgDescriptor(IContext context,
                                                 String areaPK,
                                                 SmartModel smartModel,
                                                 NODE_TYPE nodeType,
                                                 Field groupField,
                                                 boolean isGroupSet,
                                                 Field orgFieldSet,
                                                 Field orgField) throws MessageException {
        FilterDescriptor filterDes = new FilterDescriptor();

        // 如果节点类型是集团类型
        if (NODE_TYPE.GROUP_NODE.equals(nodeType)) {
            // 采购目前没有集团级报表
            super.getMainOrgDescriptor(
                context,
                areaPK,
                smartModel,
                nodeType,
                groupField,
                isGroupSet,
                orgFieldSet,
                orgField);
        }
        // 如果节点类型是组织类型
        else if (NODE_TYPE.ORG_NODE.equals(nodeType)) {
            // 获取当前组织权限
            String[] orgPks = (String[]) context.getAttribute(FreePrivateContextKey.KEY_MAINORG_ORG);
            // 根据报表平台刘丽的建议，取到组织查询条件中第一个PK值（带单引号）
            String cond_org = (String) context.getAttribute(FreePrivateContextKey.KEY_USER_PK_ORG);
            if (orgPks == null || StringUtils.isBlank(cond_org)) {
                return null;
            }

            // 报表平台的SQL是“组织字段 in 权限PK数组”，此处为“查询条件中的组织PK in 权限PK数组”。
            SqlBuilder sqlBuilder = new SqlBuilder();
            sqlBuilder.append(cond_org, orgPks);
            // 构造筛选描述器并返回
            FilterItem item = new FilterItem();
            item.setExpression(sqlBuilder.toString());
            filterDes.addFilter(item);
        }
        return filterDes;
    }

}
