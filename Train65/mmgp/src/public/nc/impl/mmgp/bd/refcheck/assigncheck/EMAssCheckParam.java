package nc.impl.mmgp.bd.refcheck.assigncheck;

import nc.vo.mmgp.MMGlobalConst;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.pub.Constructor;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 档案取消分配参数VO
 * </p>
 * 
 * @since 创建日期 Nov 18, 2013
 * @author wangweir
 */
public class EMAssCheckParam implements IAssignCheckParam {

    private Class< ? extends SuperVO> voClass;

    private String errormsg;

    private String tableName;

    private String[] refDocFields;

    public EMAssCheckParam(Class< ? extends SuperVO> voClass,
                           String[] refDocFields,
                           String errormsg) {
        this(voClass, errormsg);
        this.refDocFields = refDocFields;
    }

    public EMAssCheckParam(Class< ? extends SuperVO> voClass,
                           String refDocField,
                           String errormsg) {
        this(voClass, new String[]{refDocField }, errormsg);
    }

    @SuppressWarnings("deprecation")
    public EMAssCheckParam(Class< ? extends SuperVO> voClass,
                           String errormsg) {
        this.voClass = voClass;
        this.errormsg = errormsg;
        SuperVO vo = Constructor.construct(this.getVoClass());
        // IAttributeMeta[] referenceAttrs = vo.getMetaData().getStatisticInfo().getReferenceAttributes();
        this.tableName = vo.getTableName();
    }

    /**
     * @return the voClass
     */
    public Class< ? extends SuperVO> getVoClass() {
        return voClass;
    }

    /**
     * @param voClass
     *        the voClass to set
     */
    public void setVoClass(Class< ? extends SuperVO> voClass) {
        this.voClass = voClass;
    }

    /**
     * @return the errormsg
     */
    public String getErrormsg() {
        return errormsg;
    }

    /**
     * @param errormsg
     *        the errormsg to set
     */
    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    @Override
    public String getGroupField() {
        return MMGlobalConst.PK_GROUP;
    }

    @Override
    public String getOrgField() {
        return MMGlobalConst.PK_ORG;
    }

    @Override
    public String[] getDocField() {
        return this.refDocFields;
    }

    @Override
    public String getErrorMsg() {
        return this.errormsg;
    }

    @Override
    public String getTableName() {
        return this.tableName;
    }

}
