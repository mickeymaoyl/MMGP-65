/**
 * 
 */
package nc.vo.mmgp.pub;

/**
 * �Ƚϵ�ö����
 * 
 * @author zjy
 */
public enum CompareEnum {
    /**
     * ���ڣ����ڵ��ڣ����ڣ�С�ڵ���
     */
    GREATER(">"), GERATEREQ(">="), EQ("="), LESS("<"), LESSEQ("<=");

    /**
     * SQL���ַ���
     */
    private String sqlString;

    /**
     * SQL���ַ���
     * 
     * @return the sqlString
     */
    public String getSqlString() {
        return sqlString;
    }

    /**
     * ʹ��SQL�ַ������캯��
     * 
     * @param sqlString
     *        SQL���ַ���
     */
    private CompareEnum(String sqlString) {
        this.sqlString = sqlString;
    }

}
