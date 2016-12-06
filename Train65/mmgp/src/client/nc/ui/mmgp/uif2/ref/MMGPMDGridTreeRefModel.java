package nc.ui.mmgp.uif2.ref;

import java.util.Map;

import nc.itf.bd.config.mode.IBDMode;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.ui.bd.ref.AbstractRefGridTreeModel;
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

import org.apache.commons.lang.StringUtils;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 仅支持有元数据实体的数据--且改实体需要实现IBDObject。<br>
 * bd_refinfo里的 para1字段代表要显示的参照字段。<br>
 * 默认dr=0
 * </p>
 * 创建日期:2012-11-23
 * 
 * @author wangweiu
 */
public class MMGPMDGridTreeRefModel extends AbstractRefGridTreeModel {
    private IBusinessEntity gridbean;

    private IBusinessEntity clsbean;

    private String pk_org;

    public MMGPMDGridTreeRefModel(String refName) {
        super();
        setRefNodeName(refName);
        init(refName);
        // setClassResouceID("opclass");
    }

    protected void init(String refName) {
        RefInfoVO refInfo = RefPubUtil.getRefinfoVO(refName);
        try {
            gridbean =
                    (IBusinessEntity) MDBaseQueryFacade.getInstance().getBeanByFullName(
                        refInfo.getModuleName() + "." + refInfo.getMetadataTypeName());
        } catch (MetaDataException e) {
            ExceptionUtils.wrappException(e);
            return;
        }
        Map<String, String> map = gridbean.getBizInterfaceMapInfo(IBDObject.class.getName());
        String pkField = gridbean.getPrimaryKey().getPKColumn().getName();
        // String fatherField = map.get("pid");

        String codeField = map.get("code");
        this.setRefCodeField(codeField);
        String nameField = map.get("name");

        this.setRefTitle(gridbean.getDisplayName());
        this.setTableName(gridbean.getTable().getName());
        this.setPkFieldCode(pkField);

        this.setRefNameField(nameField);
        this.setFieldCode(new String[]{codeField, nameField });
        this.setFieldName(new String[]{
            gridbean.getAttributeByName(codeField).getDisplayName(),
            gridbean.getAttributeByName(nameField).getDisplayName() });

        resetFieldName();
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

    /**
     * 参数1是要显示的列
     */
    @Override
    public void setPara1(String para1) {
        super.setPara1(para1);
        if (StringUtils.isBlank(para1)) {
            return;
        }
        String[] fields = para1.split(";");
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fieldNames.length; i++) {
            fieldNames[i] = gridbean.getAttributeByName(fields[i]).getDisplayName();
        }
        this.setFieldCode(fields);
        this.setFieldName(fieldNames);

        setDefaultFieldCount(fields.length);
    }

    /**
     * 参数2是树的元数据名称（模块.元数据实体名）
     */
    @Override
    public void setPara2(String para2) {
        super.setPara2(para2);
        if (StringUtils.isBlank(para2)) {
            return;
        }
        String[] infos = para2.split(";");
        String treeClass = infos[0].trim();
        String treeRootName = null;
        if (infos.length == 2) {
            treeRootName = infos[1];
        }else if(infos.length == 4){
        	treeRootName = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(infos[2],infos[3]);
        }

        try {
            clsbean = (IBusinessEntity) MDBaseQueryFacade.getInstance().getBeanByFullName(treeClass);

        } catch (MetaDataException e) {
            ExceptionUtils.wrappException(e);
            return;
        }
        Map<String, String> map = clsbean.getBizInterfaceMapInfo(IBDObject.class.getName());
        String classTableName = clsbean.getTable().getName();
        String classPkField = clsbean.getPrimaryKey().getPKColumn().getName();
        String classFatherField = map.get("pid");

        String classCodeField = map.get("code");
        String classNameField = map.get("name");

        setClassFieldCode(new String[]{
            classCodeField,
            classNameField,
            classTableName + "." + classPkField,
            classFatherField });
        setFatherField(classFatherField);
        setChildField(classPkField);
        setClassJoinField(classTableName + "." + classPkField);
        setClassTableName(classTableName);
        setClassDefaultFieldCount(2);
        this.setHiddenFieldCode(new String[]{gridbean.getPrimaryKey().getPKColumn().getName(), classPkField });
        String gridTableName = gridbean.getTable().getName();
        setDocJoinField(gridTableName + "." + classPkField);

        setClassWherePart(" dr = 0 ");

        setRootName(treeRootName);

        if (gridbean.getAttributeByName("enablestate") != null) {
            setAddEnableStateWherePart(true);
        }

        if (clsbean.getAttributeByName("enablestate") != null) {
            setAddEnableStateWherePart(true);
        }
    }

    @Override
    protected String getEnvWherePart() {
        try {
            return VisibleUtil.getRefVisibleCondition(this.getPk_group(), this.getPk_org(), gridbean.getID());
        } catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }
        return null;
    }

    /**
     * wrr update 考虑管控模式
     */
    @Override
    public String getClassWherePart() {
        String classWherePart = super.getClassWherePart();
        if (classWherePart == null) {
            classWherePart = "  isnull(dr,0)=0";
        } else {
            classWherePart += " and isnull(dr,0)=0";
        }

        if (!MMStringUtil.isEmpty(classWherePart)) {
            String clsEnvWherePart = getClsEnvWherePart();
            if (!MMStringUtil.isEmpty(clsEnvWherePart)) {
                classWherePart = classWherePart + " and " + clsEnvWherePart;
            }
        }
        return classWherePart;
    }

    public IBusinessEntity getClsbean() {
        return clsbean;
    }

    public void setClsbean(IBusinessEntity clsbean) {
        this.clsbean = clsbean;
    }

    protected String getClsEnvWherePart() {
        try {
            return VisibleUtil.getRefVisibleCondition(this.getPk_group(), this.getPk_org(), clsbean.getID());
        } catch (Exception e) {
            ExceptionUtils.wrappException(e);
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
        BDModeSelectedVO bdModeVO = BDModeManager.getInstance().getBDModeSelectedVOByMDClassID(gridbean.getID());
        if (bdModeVO == null) {
            return false;
        }

        return IBDMode.SCOPE_GLOBE == bdModeVO.getVisiblescope();
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
