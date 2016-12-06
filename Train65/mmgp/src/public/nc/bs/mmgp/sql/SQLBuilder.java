package nc.bs.mmgp.sql;

import java.util.Arrays;
import java.util.Collection;

import nc.bs.mmgp.common.VoUtils;
import nc.vo.mmgp.pub.CompareEnum;
import nc.vo.pub.lang.UFDate;

/**
 * <b> SQL������ </b>
 * <p>
 * ���ڹ���һ��SQL
 * </p>
 * ��������:2010-3-5
 * 
 * @author zjy
 * @deprecated  ������ƴsql
 */
public class SQLBuilder {

    /**
     * һ���ո�
     */
    private static final String ONE_SPACE = " ";

    /**
     * �հ�Table
     */
    private static final String DEFAULT_TABLE = "";

    /**
     * �����SQL���
     */
    private StringBuilder sql = new StringBuilder();

    /**
     * And ƴ��
     * <P>
     * ƴ��And SQL�����ơ�and c = 'v'�� c = code �� 'v' = Value ���ValueΪnull������"",���� ""
     * </P>
     * 
     * @param code
     *        c
     * @param value
     *        'c'
     * @return ����
     */
    public SQLBuilder and(String code,
                          String value) {
        if (VoUtils.isEmpty(value)) {
            return this;
        }
        this.sql.append(" And ");
        this.connect(code, value);
        return this;
    }

    public SQLBuilder and(String tableName,
                          String code,
                          String value) {
        if (VoUtils.isEmpty(value)) {
            return this;
        }
        this.sql.append(" And ");
        if (VoUtils.isNotEmpty(tableName)) {
            this.sql.append(tableName);
            this.sql.append(".");
        }
        this.connect(code, value, false);
        return this;
    }

    private void connect(String code,
                         String value,
                         boolean isAppendBlank) {
        this.connect(code, value, CompareEnum.EQ, isAppendBlank);
    }

    /**
     * And ƴ��
     * <P>
     * ƴ��And SQL�����ơ�and c = 'v'�� c = code �� 'v' = Value ���ValueΪnull������"",���� ""
     * </P>
     * 
     * @param code
     *        c
     * @param value
     *        'c'
     * @param compare
     *        MMCompareEnum
     * @return ����
     */
    public SQLBuilder and(String code,
                          String value,
                          CompareEnum compare) {
        if (VoUtils.isEmpty(value)) {
            return this;
        }
        this.sql.append(" And ");
        this.connect(code, value, compare);
        return this;
    }

    private void connect(String code,
                         String value,
                         CompareEnum compare) {
        this.connect(code, value, compare, true);
    }

    /**
     * And ƴ��
     * <P>
     * ƴ��And SQL�����ơ�and c = 'v'�� c = code �� 'v' = Value ���ValueΪnull������"",���� ""
     * </P>
     * 
     * @param code
     *        c
     * @param value
     *        'c'
     * @return ����
     */
    public SQLBuilder andNum(String code,
                             Number value) {
        if (VoUtils.isEmpty(value)) {
            return this;
        }
        this.sql.append(" And ");
        this.connectNum(DEFAULT_TABLE, code, value);
        return this;
    }

    public SQLBuilder andNum(String tableName,
                             String code,
                             Integer value) {
        if (VoUtils.isEmpty(value)) {
            return this;
        }
        this.sql.append(" And ");
        this.connectNum(tableName, code, value);
        return this;
    }

    /**
     * And ƴ��
     * <P>
     * ƴ��And SQL�����ơ�and c = 'v'�� c = code �� 'v' = Value ���ValueΪnull������"",���� ""
     * </P>
     * 
     * @param code
     *        c
     * @param value
     *        'c'
     * @param compare
     *        �ȽϷ�
     * @return ����
     */
    public SQLBuilder andNum(String code,
                             Number value,
                             CompareEnum compare) {
        if (VoUtils.isEmpty(value)) {
            return this;
        }
        this.sql.append(" And ");
        this.connectNum(DEFAULT_TABLE, code, value, compare);
        return this;
    }

    /**
     * ����һ��
     * 
     * @param code
     *        ����
     * @param code2
     * @param value
     *        ֵ
     */
    private void connectNum(String tableName,
                            String code,
                            Number value) {
        this.connectNum(tableName, code, value, CompareEnum.EQ);
    }

    /**
     * ����һ��
     * 
     * @param tableName
     *        tableName
     * @param code
     *        ����
     * @param value
     *        ֵ
     * @param compare
     *        �ȽϷ�
     */
    private void connectNum(String tableName,
                            String code,
                            Number value,
                            CompareEnum compare) {
        this.sql.append(" ");
        if (VoUtils.isNotEmpty(tableName)) {
            this.sql.append(tableName);
            this.sql.append(".");
        }
        this.sql.append(code);
        this.sql.append(" ");
        this.sql.append(compare.getSqlString());
        this.sql.append(" ");
        this.sql.append(value);
        this.sql.append(" ");
    }

    /**
     * ORƴ��
     * <P>
     * ƴ��OR SQL�����ơ�and c = 'v'�� c = code �� 'v' = Value ���ValueΪnull������"",���� ""
     * </P>
     * 
     * @param code
     *        c
     * @param value
     *        'c'
     * @return ����
     */
    public SQLBuilder or(String code,
                         String value) {
        if (VoUtils.isEmpty(value)) {
            return this;
        }
        this.sql.append(" OR ");
        this.connect(code, value);
        return this;
    }

    /**
     * ���� ƴ��
     * <P>
     * ƴ�ӵ���SQL�����ơ�c = 'v'�� c = code �� 'v' = Value ���ValueΪnull������"",���� ""
     * </P>
     * 
     * @param code
     *        c
     * @param value
     *        'c'
     * @return ����
     */
    public SQLBuilder eq(String code,
                         String value) {
        if (VoUtils.isEmpty(value)) {
            return this;
        }
        this.connect(code, value);
        return this;
    }

    /**
     * ����һ��
     * 
     * @param code
     *        ����
     * @param value
     *        ֵ
     */
    public void connect(String code,
                        String value) {
        this.connect(code, value, CompareEnum.EQ, true);
    }

    /**
     * ����һ��
     * 
     * @param code
     *        ����
     * @param value
     *        ֵ
     * @param compare
     *        �ȽϷ�
     * @param isAppendBlank
     */
    private void connect(String code,
                         String value,
                         CompareEnum compare,
                         boolean isAppendBlank) {
        if (isAppendBlank) {
            this.sql.append(" ");
        }
        this.sql.append(code);
        this.sql.append(" ");
        this.sql.append(compare.getSqlString());
        this.sql.append(" '");
        this.sql.append(value);
        this.sql.append("'");
    }

    /**
     * ������
     * 
     * @return ����
     */
    public SQLBuilder l() {
        this.sql.append("(");
        return this;
    }

    /**
     * ������
     * 
     * @return ����
     */
    public SQLBuilder r() {
        this.sql.append(")");
        return this;
    }

    /**
     * And
     * 
     * @return ����
     */
    public SQLBuilder and() {
        this.sql.append(" And ");
        return this;
    }

    /**
     * OR
     * 
     * @return ����
     */
    public SQLBuilder or() {
        this.sql.append(" OR ");
        return this;
    }

    /**
     * ת���SQL���
     * 
     * @return SQL
     */
    public String toSQL() {
        return this.sql.toString();
    }

    /**
     * ƴ���ַ���
     * 
     * @param s
     *        �ַ���
     * @return ����
     */
    public SQLBuilder append(String s) {
        this.sql.append(s);
        return this;
    }

    /**
     * ɾ�������ַ���
     * 
     * @param s
     *        �ַ���
     * @return ����
     */
    public SQLBuilder delLast(String s) {
        this.sql.delete(this.sql.lastIndexOf(s), this.sql.length());
        return this;
    }

    /**
     * ƴ���ֶ�
     * 
     * @param tableName
     *        ����
     * @param fields
     *        ��Ҫѡ����ֶ�
     * @return ����
     */
    public SQLBuilder joint(String tableName,
                            Collection<String> fields) {
        this.sql.append(SQLBuilder.ONE_SPACE);
        for (String f : fields) {
            if (VoUtils.isNotEmpty(tableName)) {
                this.sql.append(tableName);
                this.sql.append(".");
            }
            this.sql.append(f);
            this.sql.append(",");
        }
        // this.sql.deleteCharAt(this.sql.length());
        /** ����һ�Ļ����׳�StringIndexOutOfBoundsException��2010-3-16 21:05:56 */
        this.sql.deleteCharAt(this.sql.length() - 1);
        this.sql.append(SQLBuilder.ONE_SPACE);
        return this;
    }

    public SQLBuilder joint(String tableName,
                            String[] fields,
                            String[] alias) {
        this.sql.append(SQLBuilder.ONE_SPACE);
        for (int i = 0; i < fields.length; i++) {
            if (VoUtils.isNotEmpty(tableName)) {
                this.sql.append(tableName);
                this.sql.append(".");
            }
            String f = fields[i];
            this.sql.append(f);
            if (!VoUtils.isEmpty(alias) && !VoUtils.isEmpty(alias[i])) {
                this.sql.append(" AS ");
                this.sql.append(alias[i]);
            }
            this.sql.append(",");
        }
        // this.sql.deleteCharAt(this.sql.length());
        /** ����һ�Ļ����׳�StringIndexOutOfBoundsException��2010-3-16 21:05:56 */
        this.sql.deleteCharAt(this.sql.length() - 1);
        this.sql.append(SQLBuilder.ONE_SPACE);
        return this;
    }

    /**
     * ƴ��Select�ֶ�
     * 
     * @param fields
     *        ��Ҫѡ����ֶ�
     * @return ����
     */
    public SQLBuilder select(String[] fields) {
        return this.joint(null, Arrays.asList(fields));
    }

    /**
     * ƴ��Select�ֶ�
     * 
     * @param fields
     *        ��Ҫѡ����ֶ�
     * @param tableName
     *        ����
     * @return ����
     */
    public SQLBuilder select(String tableName,
                             String[] fields) {
        return this.joint(tableName, Arrays.asList(fields));
    }

    /**
     * ƴ��Select�ֶ�
     * 
     * @param fields
     *        ��Ҫѡ����ֶ�
     * @param tableName
     *        ����
     * @param alias
     *        ����
     * @return ����
     */
    public SQLBuilder select(String tableName,
                             String[] fields,
                             String[] alias) {
        return this.joint(tableName, fields, alias);
    }

    /**
     * Select
     * 
     * @return ����
     */
    public SQLBuilder select() {
        this.sql.append(" Select ");
        return this;
    }

    /**
     * delete
     * 
     * @return ����
     */
    public SQLBuilder delete() {
        this.sql.append(" delete ");
        return this;
    }

    /**
     * values
     * 
     * @return ����
     */
    public SQLBuilder values(String[] values) {
        this.sql.append(SQLBuilder.ONE_SPACE);
        for (String f : values) {
            this.sql.append(" '");
            this.sql.append(f);
            this.sql.append("'");
        }
        this.sql.deleteCharAt(this.sql.length() - 1);
        this.sql.append(SQLBuilder.ONE_SPACE);
        return this;
    }

    /**
     * values
     * 
     * @param table
     *        ����
     * @return ����
     */
    public SQLBuilder values() {
        this.sql.append(" values ");
        return this;
    }

    /**
     * insertInto
     * 
     * @return ����
     */
    public SQLBuilder insertInto(String table,
                                 String[] fields) {
        this.sql.append(" insert  into ");
        this.sql.append(table);
        this.sql.append(" ( ");
        this.joint(null, Arrays.asList(fields));
        this.sql.append((" )"));

        return this;
    }

    /**
     * from
     * 
     * @param table
     *        ����
     * @return ����
     */
    public SQLBuilder from(String table) {
        this.sql.append(" from " + table);
        return this;
    }

    /**
     * ɾ����һ�����ֵĵ���
     * 
     * @param token
     *        ����
     * @return ����
     */
    public SQLBuilder deleteFirst(String token) {
        int starIndexOf = this.sql.indexOf(token);
        if (starIndexOf < 0) {
            return this;
        }

        this.sql.delete(starIndexOf, starIndexOf + token.length());
        return this;
    }

    /**
     * IN
     * 
     * @return ����
     */
    public SQLBuilder in() {
        this.sql.append(" IN ");
        return this;
    }

    /**
     * IN
     * 
     * @param values
     *        ֵ
     * @return ����
     */
    public SQLBuilder in(String[] values) {
        in();
        this.sql.append(" (");
        contactBy(values);
        this.sql.append(")");
        return this;
    }

    private void contactBy(String[] values) {
        for (int i = 0; i < values.length - 1; i++) {
            this.sql.append("'").append(values[i]).append("',");
        }
        this.sql.append("'").append(values[values.length - 1]).append("' ");
    }

    private void contactBy2(String[] values) {
        for (int i = 0; i < values.length - 1; i++) {
            this.sql.append(values[i]).append(",");
        }
        this.sql.append(values[values.length - 1]);
    }

    /**
     * �����ֶ�
     * 
     * @param tableName
     *        ����
     * @param codes
     *        �ֶ�
     * @return ����
     */
    public SQLBuilder append(String tableName,
                             String[] codes) {
        this.contactBy2(tableName, codes);
        return this;
    }

    private void contactBy2(String tableName,
                            String[] values) {
        for (int i = 0; i < values.length - 1; i++) {
            if (VoUtils.isNotEmpty(tableName)) {
                this.sql.append(tableName);
                this.sql.append(".");
            }
            this.sql.append(values[i]).append(",");
        }
        if (VoUtils.isNotEmpty(tableName)) {
            this.sql.append(tableName);
            this.sql.append(".");
        }
        this.sql.append(values[values.length - 1]);
    }

    /**
     * IN
     * 
     * @param code
     *        �ֶ�
     * @param values
     *        ֵ
     * @return ����
     */
    public SQLBuilder in(String code,
                         String[] values) {
        return this.in(DEFAULT_TABLE, code, values);
    }

    /**
     * @param tablename
     *        table
     * @param code
     *        �ֶ�
     * @param values
     *        ֵ
     * @return ����
     */
    public SQLBuilder in(String tablename,
                         String code,
                         String[] values) {
        if (VoUtils.isNotEmpty(tablename)) {
            this.sql.append(tablename);
            this.sql.append(".");
        }
        this.sql.append(code);
        in();
        this.sql.append(" (");
        for (int i = 0; i < values.length - 1; i++) {
            this.sql.append("'").append(values[i]).append("',");
        }
        this.sql.append("'").append(values[values.length - 1]).append("' )");
        return this;

    }

    /**
     * IN
     * 
     * @param code
     *        �ֶ�
     * @param values
     *        ֵ
     * @return ����
     */
    public SQLBuilder in(String code,
                         Integer[] values) {
        this.sql.append(code);
        in();
        this.sql.append(" (");
        for (int i = 0; i < values.length - 1; i++) {
            this.sql.append(values[i]).append(",");
        }
        this.sql.append(values[values.length - 1]).append(" )");
        return this;

    }

    /**
     * WHERE
     * 
     * @return ����
     */
    public SQLBuilder where() {
        this.sql.append(" WHERE ");
        return this;
    }

    /**
     * whereƴ��
     * <P>
     * ƴ��where SQL�����ơ�where c = 'v'�� c = code �� 'v' = Value ���ValueΪnull������"",���� ""
     * </P>
     * 
     * @param code
     *        c
     * @param value
     *        'c'
     * @return ����
     */
    public SQLBuilder where(String code,
                            String value) {
        if (VoUtils.isEmpty(value)) {
            return this;
        }
        this.sql.append(" WHERE ");
        this.connect(code, value);
        return this;
    }

    public SQLBuilder where(String defaultTableName,
                            String code,
                            String value) {
        if (VoUtils.isEmpty(value)) {
            return this;
        }
        this.sql.append(" WHERE ");
        this.sql.append(defaultTableName);
        this.sql.append(".");
        this.connect(code, value, false);
        return this;
    }

    public SQLBuilder where(String defaultTableName,
                            String code,
                            Integer value) {
        if (VoUtils.isEmpty(value)) {
            return this;
        }
        this.sql.append(" WHERE ");
        this.connectNum(defaultTableName, code, value);
        return this;
    }

    /**
     * whereƴ��
     * <P>
     * ƴ��where SQL�����ơ�where c = v�� c = code �� 'v' = Value ���ValueΪnull������"",���� ""
     * </P>
     * 
     * @param code
     *        c
     * @param value
     *        'c'
     * @return ����
     */
    public SQLBuilder where(String code,
                            Integer value) {
        if (VoUtils.isEmpty(value)) {
            return this;
        }
        this.sql.append(" WHERE ");
        this.connectNum(DEFAULT_TABLE, code, value);
        return this;
    }

    /**
     * DISTINCT
     * 
     * @return ����
     */
    public SQLBuilder distinct() {
        this.sql.append(" DISTINCT ");
        return this;
    }

    /**
     * dr
     * 
     * @return ����
     */
    public SQLBuilder appendDr() {
        this.sql.append(" and isnull(dr,0) = 0 ");
        return this;
    }

    public SQLBuilder appendDr(String defaultTableName) {
        this.sql.append(" and isnull( ");
        this.sql.append(defaultTableName);
        this.sql.append(".dr,0) = 0 ");
        return this;
    }

    /**
     * inner join
     * 
     * @return ����
     */
    public SQLBuilder innerjoin() {
        this.sql.append(" inner join ");
        return this;
    }

    /**
     * Inner join
     * 
     * @param table
     *        ����
     * @return ����
     */
    public SQLBuilder innerjoin(String tableName) {
        this.innerjoin();
        return this.append(tableName);

    }

    /**
     * inner notexists
     * 
     * @return ����
     */
    public SQLBuilder notexists() {
        this.sql.append(" not exists ");
        return this;
    }

    /**
     * on
     * 
     * @return ����
     */
    public SQLBuilder on() {
        this.sql.append(" on ");
        return this;
    }

    /**
     * ��ʼ���ڣ�xxx >= '2010-07-05 00:00:00'
     * 
     * @param date
     *        ���ݿ��ֶ�
     * @param ufDate
     *        �������ʼ����
     * @return ����
     */
    public SQLBuilder dateFrom(String date,
                               UFDate ufDate) {
        this.sql.append(date).append(" >= '").append(ufDate.toString()).append("' ");
        return this;
    }

    /**
     * �������ڣ�xxx <= '2010-07-05 00:00:00'
     * 
     * @param date
     *        ���ݿ��ֶ�
     * @param ufDate
     *        �������ʼ����
     * @return ����
     */
    public SQLBuilder dateEnd(String date,
                              UFDate ufDate) {
        this.sql.append(date).append(" <= '").append(ufDate.toString()).append("' ");
        return this;
    }

    /**
     * Union
     * 
     * @param mmsql
     *        MMSQLBuilder
     * @return ����
     */
    public SQLBuilder union(SQLBuilder mmsql) {
        this.sql.append(" union ");
        this.append(mmsql);
        return this;
    }

    /**
     * append
     * 
     * @param stuffSQL
     *        MMSQLBuilder
     * @return ����
     */
    public SQLBuilder append(SQLBuilder stuffSQL) {
        this.sql.append(stuffSQL.toSQL());
        return this;
    }

    /**
     * append
     * 
     * @param tableName
     *        tableName
     * @param field
     *        field
     */
    public SQLBuilder append(String tableName,
                             String field) {
        this.sql.append(ONE_SPACE);
        this.sql.append(tableName);
        this.sql.append(".");
        this.sql.append(field);
        return this;

    }

    /**
     * is Null ����
     * 
     * @param code
     *        ����
     * @return ����
     */
    public SQLBuilder isNull(String code) {
        this.sql.append(ONE_SPACE);
        this.sql.append(code);
        this.sql.append(" IS NULL");
        return this;

    }

    /**
     * Group By
     * 
     * @param codes
     *        ����
     * @return ����
     */
    public SQLBuilder groupBy(String[] codes) {
        this.sql.append(" Group by ");
        this.contactBy2(codes);
        return this;
    }

    /**
     * Group By
     * 
     * @param tableName
     *        ����
     * @param codes
     *        ����
     * @return ����
     */
    public SQLBuilder groupBy(String tableName,
                              String[] codes) {
        this.sql.append(" Group by ");
        this.contactBy2(tableName, codes);
        return this;
    }

    /**
     * Sum����
     * 
     * @param tableName
     *        ����
     * @param filed
     *        �ֶ�
     * @param alias
     *        ����
     * @return ����
     */
    public SQLBuilder sum(String tableName,
                          String filed,
                          String alias) {
        this.sql.append(" sum(");
        if (VoUtils.isNotEmpty(tableName)) {
            this.sql.append(tableName);
            this.sql.append(".");
        }
        this.sql.append(filed);
        this.sql.append(") ");
        this.sql.append(alias);
        return this;

    }

    /**
     * On ����
     * 
     * @param tableName1
     *        tableName1
     * @param filed1
     *        tableName1���ֶ�
     * @param tableName2
     *        tableName2
     * @param filed2
     *        tableName2���ֶ�
     * @return ����
     */
    public SQLBuilder on(String tableName1,
                         String filed1,
                         String tableName2,
                         String filed2) {
        return this.onAnd(tableName1, filed1, tableName2, filed2, false);
    }

    /**
     * On ���ӣ����Ǻ��������And����
     * 
     * @param tableName1
     *        tableName1
     * @param filed1
     *        tableName1���ֶ�
     * @param tableName2
     *        tableName2
     * @param filed2
     *        tableName2���ֶ�
     * @return ����
     */
    public SQLBuilder onAnd(String tableName1,
                            String filed1,
                            String tableName2,
                            String filed2) {
        return this.onAnd(tableName1, filed1, tableName2, filed2, true);
    }

    private SQLBuilder onAnd(String tableName1,
                             String filed1,
                             String tableName2,
                             String filed2,
                             boolean isHaveAnd) {
        this.on();
        if (isHaveAnd) {
            this.l();
        }
        fieldEq(tableName1, filed1, tableName2, filed2);
        return this;

    }

    private void fieldEq(String tableName1,
                         String filed1,
                         String tableName2,
                         String filed2) {
        this.sql.append(tableName1);
        this.sql.append(".");
        this.sql.append(filed1);
        this.sql.append(" = ");
        this.sql.append(tableName2);
        this.sql.append(".");
        this.sql.append(filed2);
    }

    @Override
    public String toString() {
        return this.toSQL();
    }

    public SQLBuilder and(String tableName1,
                          String field1,
                          String tableName2,
                          String field2) {
        this.and();
        this.fieldEq(tableName1, field1, tableName2, field2);
        return this;
    }

}
