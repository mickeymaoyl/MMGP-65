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
 * <b> ��������֯Ȩ����չ�� </b>
 * <p>
 * ��֯���ż��Ļ��ܱ�������ά�Ȳ�������֯����ʱ������ƽ̨��Ĭ�ϵ���֯��չ���л�ƴ����֯SQL������SQL���� ��ʱ���Խ����ɱ���Ԥ���е����� -> ����ע�� ->
 * ��֯Ȩ����չ��ע��Ϊ���࣬���ཫ��ƴ����ƽ̨��ͬ��SQL���رܴ��� <br/>
 * <br/>
 * ע�����ڱ���Ԥ���а�������֯�ֶΣ����ܹ�������չ�࣬������ܻ�������⡣
 * </p>
 * 
 * @since �������� 2013-11-7
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

        // ����ڵ������Ǽ�������
        if (NODE_TYPE.GROUP_NODE.equals(nodeType)) {
            // �ɹ�Ŀǰû�м��ż�����
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
        // ����ڵ���������֯����
        else if (NODE_TYPE.ORG_NODE.equals(nodeType)) {
            // ��ȡ��ǰ��֯Ȩ��
            String[] orgPks = (String[]) context.getAttribute(FreePrivateContextKey.KEY_MAINORG_ORG);
            // ���ݱ���ƽ̨�����Ľ��飬ȡ����֯��ѯ�����е�һ��PKֵ���������ţ�
            String cond_org = (String) context.getAttribute(FreePrivateContextKey.KEY_USER_PK_ORG);
            if (orgPks == null || StringUtils.isBlank(cond_org)) {
                return null;
            }

            // ����ƽ̨��SQL�ǡ���֯�ֶ� in Ȩ��PK���顱���˴�Ϊ����ѯ�����е���֯PK in Ȩ��PK���顱��
            SqlBuilder sqlBuilder = new SqlBuilder();
            sqlBuilder.append(cond_org, orgPks);
            // ����ɸѡ������������
            FilterItem item = new FilterItem();
            item.setExpression(sqlBuilder.toString());
            filterDes.addFilter(item);
        }
        return filterDes;
    }

}
