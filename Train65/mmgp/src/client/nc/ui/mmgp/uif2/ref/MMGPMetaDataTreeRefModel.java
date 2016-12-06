package nc.ui.mmgp.uif2.ref;

import java.util.Map;

import nc.bs.logging.Logger;
import nc.itf.bd.config.mode.IBDMode;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.bd.ref.RefPubUtil;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.vo.bd.config.BDModeSelectedVO;
import nc.vo.bd.meta.IBDObject;
import nc.vo.bd.ref.RefInfoVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.util.BDModeManager;
import nc.vo.util.VisibleUtil;

import org.apache.commons.lang.StringUtils;

/**
 * ͨ�����β��ա�
 * <p>
 * ��֧����Ԫ����ʵ�������--�Ҹ�ʵ����Ҫʵ��IBDObject��<br>
 * bd_refinfo��� para1�ֶδ���Ҫ��ʾ�Ĳ����ֶΡ�<br>
 * Ĭ��dr=0
 * 
 * @author wangweiu
 */
public class MMGPMetaDataTreeRefModel extends AbstractRefTreeModel {
    private IBusinessEntity bean;

    private String pk_org;

    public MMGPMetaDataTreeRefModel(String refName) {
        super();
        setRefNodeName(refName);
        init(refName);
        if (getBean().getAttributeByName("enablestate") != null) {
            setAddEnableStateWherePart(true);
        }
    }

    protected void init(String refName) {
        RefInfoVO refInfo = RefPubUtil.getRefinfoVO(refName);
        try {
            this.setBean((IBusinessEntity) MDBaseQueryFacade.getInstance().getBeanByFullName(
                refInfo.getModuleName() + "." + refInfo.getMetadataTypeName()));
        } catch (MetaDataException e) {
            ExceptionUtils.wrappException(e);
            return;
        }
        Map<String, String> map = this.getBean().getBizInterfaceMapInfo(IBDObject.class.getName());
        String pkField = this.getBean().getPrimaryKey().getPKColumn().getName();
        String fatherField = map.get("pid");

        String codeField = map.get("code");
        this.setRefCodeField(codeField);
        String nameField = map.get("name");

        this.setRefTitle(getBean().getDisplayName());
        this.setTableName(this.getBean().getTable().getName());
        this.setPkFieldCode(pkField);

        this.setRefNameField(nameField);
        this.setFieldCode(new String[]{codeField, nameField });
        this.setFieldName(new String[]{
            this.getBean().getAttributeByName(codeField).getDisplayName(),
            this.getBean().getAttributeByName(nameField).getDisplayName() });
        this.setHiddenFieldCode(new String[]{pkField, fatherField });

        setFatherField(fatherField);
        setChildField(pkField);

        resetFieldName();
// wangfan3 2014-8-2 ��ʼ����ʱ�� ��û������pk_org,���ʱ��������ݣ��ᱨ�ܿ�ģʽ�Ĵ���
//        /* Nov 6, 2013 wangweir ��qichf�����ˣ�ΪʲôҪִ��˫�ر�׼��ˮƽ��Ʒ����ҵ��Ʒ����һ�£�������ҵ��Ʒ��Ҫ�޸ģ� Begin */
//        this.reloadData1();
//        /* Nov 6, 2013 wangweir End */
    }

    @Override
    public String getWherePart() {
        String wherePart = super.getWherePart();
        if (wherePart == null) {
            wherePart = "  isnull(dr,0)=0";
        } else {
            wherePart += " and isnull(dr,0)=0";
        }
        return wherePart;
    }

    // @Override
    // public void setPk_org(String pk_org) {
    // this.pk_org = pk_org;
    // super.setPk_org(pk_org);
    // }

    @Override
    public void setPara1(String para1) {
        super.setPara1(para1);
        if (StringUtils.isBlank(para1)) {
            return;
        }
    }

    private IBusinessEntity getBean() {
        return this.bean;
    }

    private void setBean(IBusinessEntity bean) {
        this.bean = bean;
    }

    @Override
    protected String getEnvWherePart() {
        try {
            return VisibleUtil.getRefVisibleCondition(this.getPk_group(), this.getPk_org(), bean.getID());
        } catch (Exception e) {
            Logger.error(e, e);
        }
        return null;
    }

    @Override
    public void setPk_org(String pk_org) {
        this.pk_org = pk_org;
        super.setPk_org(pk_org);
    }

    @Override
    public String getPk_org() {
        if (isGlobalVisible()) {
            // ȫ�ֵ�������
            return pk_org;
        } else {
            return super.getPk_org();
        }
    }

    /**
     * 
     */
    protected boolean isGlobalVisible() {
        BDModeSelectedVO bdModeVO =
                BDModeManager.getInstance().getBDModeSelectedVOByMDClassID(this.getBean().getID());
        if (bdModeVO == null) {
            return false;
        }

        return IBDMode.SCOPE_GLOBE == bdModeVO.getVisiblescope();
    }

    @Override
    public void filterValueChanged(ValueChangedEvent changedValue) {
        // ��ѯģ���Ϲ�ѡ�༯�ź���Ҫ��Ӧ�����л��¼�
        super.filterValueChanged(changedValue);
        String[] pk_orgs = (String[]) changedValue.getNewValue();
        if (pk_orgs != null && pk_orgs.length > 0) {
            this.setPk_org(pk_orgs[0]);
        }
    }
}
