package nc.impl.mmgp.bd.refcheck;

/**
 * 实体基本信息 - 针对多版本的档案 关注其他版本是否被引用，即：只要有一个版本被引用，该档案就不能被修改
 * 
 * @since 6.0
 * @version 2011-6-27 下午04:33:11
 * @author chendb
 */
public class EntityInfoForVersion extends EntityBaseInfo {

    private static final long serialVersionUID = 1L;

    private String srcFieldName;

    /**
     * @param voclassName
     *        实体vo类全名
     * @param fieldName
     *        实体中参照当前档案最新版本的字段名称
     * @param srcFieldName
     *        实体中参照当前档案原始版本的字段名称
     */
    public EntityInfoForVersion(String voclassName,
                                String fieldName,
                                String srcFieldName) {
        super(voclassName, fieldName);
        this.srcFieldName = srcFieldName;
    }

    /**
     * 需进行组织级别控制时 例：与 "物料基本信息" 构成主子结构的 "物料采购信息"，"物料库存信息"等
     * 
     * @param voclassName
     *        实体vo类全名
     * @param fieldName
     *        实体中参照当前档案最新版本的字段名称
     * @param srcFieldName
     *        实体中参照当前档案原始版本的字段名称
     * @param orgName
     *        实体中组织名称
     */
    public EntityInfoForVersion(String voclassName,
                                String fieldName,
                                String srcFieldName,
                                String orgName) {
        super(voclassName, fieldName, orgName);
        this.srcFieldName = srcFieldName;
    }

    public String getSrcFieldName() {
        return this.srcFieldName;
    }

    public void setSrcFieldName(String srcFieldName) {
        this.srcFieldName = srcFieldName;
    }

}
