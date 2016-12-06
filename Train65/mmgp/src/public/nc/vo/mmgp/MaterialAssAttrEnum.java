package nc.vo.mmgp;
/**
 * <b> ���Ϲ̶���������ö��</b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since: ��������:Aug 20, 2013
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
