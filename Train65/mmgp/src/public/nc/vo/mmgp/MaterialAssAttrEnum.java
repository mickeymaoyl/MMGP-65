package nc.vo.mmgp;
/**
 * <b> 物料固定辅助属性枚举</b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since: 创建日期:Aug 20, 2013
 * @author:gaotx
 */
public enum MaterialAssAttrEnum {
    STOCKSTATE("cstateid", Integer.valueOf(1)), 
    PROJECT("cprojectid", Integer.valueOf(2)), 
    VENDOR("cvendorid",Integer.valueOf(3)), 
    PRODUCTOR("cproductorid", Integer.valueOf(4)), 
    CUSTOMER("ccustomerid", Integer.valueOf(5));

    String attrName = null;

    Integer position = null;

    MaterialAssAttrEnum(String attrName,
                        Integer position) {
        this.attrName = attrName;
        this.position = position;

    }

    public String getAttrName() {
        return this.attrName;
    }

    public Integer getPosition() {
        return this.position;
    }
}
