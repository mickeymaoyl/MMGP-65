package nc.impl.mmgp.bd.refcheck;

import java.io.Serializable;

/**
 * 实体基本信息
 * 
 * @since 6.0
 * @version 2011-6-9 下午08:45:31
 * @author chendb
 */
public class EntityBaseInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fieldName;

    private String orgName;

    private String voclassName;

    /**
     * @param voclassName
     *        实体vo类全名
     * @param fieldName
     *        实体中参照当前档案的字段名称
     */
    public EntityBaseInfo(String voclassName,
                          String fieldName) {
        this.voclassName = voclassName;
        this.fieldName = fieldName;
    }

    /**
     * 需进行组织级别控制时 例：与 "物料基本信息" 构成主子结构的 "物料采购信息"，"物料库存信息"等
     * 
     * @param voclassName
     *        实体vo类全名
     * @param fieldName
     *        实体中参照当前档案的字段名称
     * @param orgName
     *        实体中组织名称
     */
    public EntityBaseInfo(String voclassName,
                          String fieldName,
                          String orgName) {
        this.voclassName = voclassName;
        this.fieldName = fieldName;
        this.orgName = orgName;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public String getVoclassName() {
        return this.voclassName;
    }
}
