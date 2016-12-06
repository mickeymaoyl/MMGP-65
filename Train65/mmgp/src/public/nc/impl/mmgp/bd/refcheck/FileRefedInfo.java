package nc.impl.mmgp.bd.refcheck;

import java.io.Serializable;

/**
 * 档案引用后不可修改注册信息
 * 
 * @since 6.0
 * @version 2011-6-7 下午02:47:19
 * @author chendb
 */
public class FileRefedInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private EntityBaseInfo[] entityBaseInfos;

    private String errInfo;

    private String modulecode;

    private String[] noEditFileds;

    /**
     * @param modulecode
     *        模块编码
     * @param entityInfos
     *        实体基本信息组：指定单据中，参照当前档案的字段，所处实体的基本信息，可根据需要定义数组个数 例：采购订单中，"采购订单明细" 和 " 到货计划" 中均包含参照 "物料档案" 的物料，若仅需对 "采购订单明细"
     *        中引用物料后，物料档案不能修改进行控制，则只传入"采购订单明细"实体信息即可 ，即： { EntityBaseInfo("实体vo类全名"," 实体中参照当前档案的字段名称","实体中组织名称") }
     * @param noEditFileds
     *        档案中，引用后不能修改的字段名称集
     * @param errInfo
     *        错误提示信息
     */
    public FileRefedInfo(String modulecode,
                         EntityBaseInfo[] entityInfos,
                         String[] noEditFileds,
                         String errInfo) {
        this.modulecode = modulecode;
        this.entityBaseInfos = entityInfos;
        this.noEditFileds = noEditFileds;
        this.errInfo = errInfo;
    }

    /**
     * 单据提供的基本信息
     * 
     * @return
     */
    public EntityBaseInfo[] getEntityBaseInfos() {
        return this.entityBaseInfos;
    }

    /**
     * 错误提示信息
     * 
     * @return
     */
    public String getErrInfo() {
        return this.errInfo;
    }

    /**
     * 模块编码
     * 
     * @return
     */
    public String getModulecode() {
        return this.modulecode;
    }

    /**
     * 档案中，引用后不能修改的字段名称集
     * 
     * @return
     */
    public String[] getnoEditFileds() {
        return this.noEditFileds;
    }

    /**
     * @return the noEditFileds
     */
    public String[] getNoEditFileds() {
        return noEditFileds;
    }

    /**
     * @param noEditFileds
     *        the noEditFileds to set
     */
    public void setNoEditFileds(String[] noEditFileds) {
        this.noEditFileds = noEditFileds;
    }

    /**
     * @param entityBaseInfos
     *        the entityBaseInfos to set
     */
    public void setEntityBaseInfos(EntityBaseInfo[] entityBaseInfos) {
        this.entityBaseInfos = entityBaseInfos;
    }

    /**
     * @param errInfo
     *        the errInfo to set
     */
    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo;
    }

    /**
     * @param modulecode
     *        the modulecode to set
     */
    public void setModulecode(String modulecode) {
        this.modulecode = modulecode;
    }

}
