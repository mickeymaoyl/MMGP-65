package nc.ui.mmgp.uif2.ref;

import java.util.Map;

import nc.bs.logging.Logger;
import nc.itf.bd.config.mode.IBDMode;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.RefPubUtil;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.vo.bd.config.BDModeSelectedVO;
import nc.vo.bd.meta.IBDObject;
import nc.vo.bd.ref.RefInfoVO;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.util.BDModeManager;
import nc.vo.util.VisibleUtil;

public abstract class AbstractMMGPMetaDataRefModel extends AbstractRefModel {
    private IBusinessEntity bean;

    private String pk_org;

    public AbstractMMGPMetaDataRefModel(String refName) {
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
        // String fatherField = map.get("pid");

        String codeField = map.get("code");
        this.setRefCodeField(codeField);
        String nameField = map.get("name");

        this.setRefTitle(getBean().getDisplayName());
        this.setTableName(this.getBean().getTable().getName());
        this.setPkFieldCode(pkField);

        this.setRefNameField(nameField);
		// tangxya 2015-05-05 物理托盘档案 没有名称字段
		if (MMStringUtil.isNotEmpty(nameField)) {
			this.setFieldCode(new String[] { codeField, nameField });
			this.setFieldName(new String[] {
					this.getBean().getAttributeByName(codeField)
							.getDisplayName(),
					this.getBean().getAttributeByName(nameField)
							.getDisplayName() });
		} else {
			this.setFieldCode(new String[] { codeField });
			this.setFieldName(new String[] { this.getBean()
					.getAttributeByName(codeField).getDisplayName() });
		}
        this.setHiddenFieldCode(new String[]{pkField });

        initByMetaData(bean);

        resetFieldName();
        
        /* Nov 6, 2013 wangweir 对qichf无语了，为什么要执行双重标准。水平产品与行业产品现象一致，但是行业产品需要修改！ Begin */
        this.reloadData1();
        /* Nov 6, 2013 wangweir End */
    }

    protected void initByMetaData(IBusinessEntity bean2) {

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

    public IBusinessEntity getBean() {
        return this.bean;
    }

    private void setBean(IBusinessEntity bean) {
        this.bean = bean;
    }

    @Override
    public void setPk_org(String pk_org) {
        this.pk_org = pk_org;
        super.setPk_org(pk_org);
    }

    @Override
    public String getPk_org() {
        if (isGlobalVisible()) {
            // 全局档案参照
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
    protected String getEnvWherePart() {
        try {
            return VisibleUtil.getRefVisibleCondition(this.getPk_group(), this.getPk_org(), bean.getID());
        } catch (Exception e) {
            // ExceptionUtils.wrappException(e);
            Logger.error(e, e);
        }
        return null;
    }

    @Override
    public void filterValueChanged(ValueChangedEvent changedValue) {
        // 查询模板上勾选多集团后，需要响应集团切换事件
        super.filterValueChanged(changedValue);
        String[] pk_orgs = (String[]) changedValue.getNewValue();
        if (MMArrayUtil.isEmpty(pk_orgs)) {
            this.setPk_org(null);
        } else {
            this.setPk_org(pk_orgs[0]);
        }
    }

}
