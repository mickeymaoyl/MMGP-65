package nc.bs.mmgp.sql;

import java.util.Arrays;
import java.util.Collection;

import nc.bs.mmgp.common.VoUtils;
import nc.vo.mmgp.pub.CompareEnum;
import nc.vo.pub.lang.UFDate;

/**
 * <b> SQL构造器 </b>
 * <p>
 * 用于构造一段SQL
 * </p>
 * 创建日期:2010-3-5
 * 
 * @author zjy
 * @deprecated  不建议拼sql
 */
public class SQLBuilder {

    /**
     * 一个空格
     */
    private static final String ONE_SPACE = " ";

    /**
     * 空白Table
     */
    private static final String DEFAULT_TABLE = "";

    /**
     * 保存的SQL语句
     */
    private StringBuilder sql = new StringBuilder();

    /**
     * And 拼接
     * <P>
     * 拼接And SQL，类似【and c = 'v'】 c = code ， 'v' = Value 如果Value为null，或者"",返回 ""
     * </P>
     * 
     * @param code
     *        c
     * @param value
     *        'c'
     * @return 本身
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
     * And 拼接
     * <P>
     * 拼接And SQL，类似【and c = 'v'】 c = code ， 'v' = Value 如果Value为null，或者"",返回 ""
     * </P>
     * 
     * @param code
     *        c
     * @param value
     *        'c'
     * @param compare
     *        MMCompareEnum
     * @return 本身
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
     * And 拼接
     * <P>
     * 拼接And SQL，类似【and c = 'v'】 c = code ， 'v' = Value 如果Value为null，或者"",返回 ""
     * </P>
     * 
     * @param code
     *        c
     * @param value
     *        'c'
     * @return 本身
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
     * And 拼接
     * <P>
     * 拼接And SQL，类似【and c = 'v'】 c = code ， 'v' = Value 如果Value为null，或者"",返回 ""
     * </P>
     * 
     * @param code
     *        c
     * @param value
     *        'c'
     * @param compare
     *        比较符
     * @return 本身
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
     * 连接一起
     * 
     * @param code
     *        编码
     * @param code2
     * @param value
     *        值
     */
    private void connectNum(String tableName,
                            String code,
                            Number value) {
        this.connectNum(tableName, code, value, CompareEnum.EQ);
    }

    /**
     * 连接一起
     * 
     * @param tableName
     *        tableName
     * @param code
     *        编码
     * @param value
     *        值
     * @param compare
     *        比较符
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
     * OR拼接
     * <P>
     * 拼接OR SQL，类似【and c = 'v'】 c = code ， 'v' = Value 如果Value为null，或者"",返回 ""
     * </P>
     * 
     * @param code
     *        c
     * @param value
     *        'c'
     * @return 本身
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
     * 等于 拼接
     * <P>
     * 拼接等于SQL，类似【c = 'v'】 c = code ， 'v' = Value 如果Value为null，或者"",返回 ""
     * </P>
     * 
     * @param code
     *        c
     * @param value
     *        'c'
     * @return 本身
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
     * 连接一起
     * 
     * @param code
     *        编码
     * @param value
     *        值
     */
    public void connect(String code,
                        String value) {
        this.connect(code, value, CompareEnum.EQ, true);
    }

    /**
     * 连接一起
     * 
     * @param code
     *        编码
     * @param value
     *        值
     * @param compare
     *        比较符
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
     * 左括号
     * 
     * @return 本身
     */
    public SQLBuilder l() {
        this.sql.append("(");
        return this;
    }

    /**
     * 右括号
     * 
     * @return 本身
     */
    public SQLBuilder r() {
        this.sql.append(")");
        return this;
    }

    /**
     * And
     * 
     * @return 本身
     */
    public SQLBuilder and() {
        this.sql.append(" And ");
        return this;
    }

    /**
     * OR
     * 
     * @return 本身
     */
    public SQLBuilder or() {
        this.sql.append(" OR ");
        return this;
    }

    /**
     * 转变成SQL语句
     * 
     * @return SQL
     */
    public String toSQL() {
        return this.sql.toString();
    }

    /**
     * 拼接字符串
     * 
     * @param s
     *        字符串
     * @return 本身
     */
    public SQLBuilder append(String s) {
        this.sql.append(s);
        return this;
    }

    /**
     * 删除最后的字符串
     * 
     * @param s
     *        字符串
     * @return 本身
     */
    public SQLBuilder delLast(String s) {
        this.sql.delete(this.sql.lastIndexOf(s), this.sql.length());
        return this;
    }

    /**
     * 拼接字段
     * 
     * @param tableName
     *        表名
     * @param fields
     *        将要选择的字段
     * @return 本身
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
        /** 不减一的话会抛出StringIndexOutOfBoundsException。2010-3-16 21:05:56 */
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
        /** 不减一的话会抛出StringIndexOutOfBoundsException。2010-3-16 21:05:56 */
        this.sql.deleteCharAt(this.sql.length() - 1);
        this.sql.append(SQLBuilder.ONE_SPACE);
        return this;
    }

    /**
     * 拼接Select字段
     * 
     * @param fields
     *        将要选择的字段
     * @return 本身
     */
    public SQLBuilder select(String[] fields) {
        return this.joint(null, Arrays.asList(fields));
    }

    /**
     * 拼接Select字段
     * 
     * @param fields
     *        将要选择的字段
     * @param tableName
     *        表名
     * @return 本身
     */
    public SQLBuilder select(String tableName,
                             String[] fields) {
        return this.joint(tableName, Arrays.asList(fields));
    }

    /**
     * 拼接Select字段
     * 
     * @param fields
     *        将要选择的字段
     * @param tableName
     *        表名
     * @param alias
     *        别名
     * @return 本身
     */
    public SQLBuilder select(String tableName,
                             String[] fields,
                             String[] alias) {
        return this.joint(tableName, fields, alias);
    }

    /**
     * Select
     * 
     * @return 本身
     */
    public SQLBuilder select() {
        this.sql.append(" Select ");
        return this;
    }

    /**
     * delete
     * 
     * @return 本身
     */
    public SQLBuilder delete() {
        this.sql.append(" delete ");
        return this;
    }

    /**
     * values
     * 
     * @return 本身
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
     *        表名
     * @return 本身
     */
    public SQLBuilder values() {
        this.sql.append(" values ");
        return this;
    }

    /**
     * insertInto
     * 
     * @return 本身
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
     *        表名
     * @return 本身
     */
    public SQLBuilder from(String table) {
        this.sql.append(" from " + table);
        return this;
    }

    /**
     * 删除第一个出现的单词
     * 
     * @param token
     *        单词
     * @return 本身
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
     * @return 本身
     */
    public SQLBuilder in() {
        this.sql.append(" IN ");
        return this;
    }

    /**
     * IN
     * 
     * @param values
     *        值
     * @return 本身
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
     * 关联字段
     * 
     * @param tableName
     *        表明
     * @param codes
     *        字段
     * @return 自身
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
     *        字段
     * @param values
     *        值
     * @return 本身
     */
    public SQLBuilder in(String code,
                         String[] values) {
        return this.in(DEFAULT_TABLE, code, values);
    }

    /**
     * @param tablename
     *        table
     * @param code
     *        字段
     * @param values
     *        值
     * @return 本身
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
     *        字段
     * @param values
     *        值
     * @return 本身
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
     * @return 本身
     */
    public SQLBuilder where() {
        this.sql.append(" WHERE ");
        return this;
    }

    /**
     * where拼接
     * <P>
     * 拼接where SQL，类似【where c = 'v'】 c = code ， 'v' = Value 如果Value为null，或者"",返回 ""
     * </P>
     * 
     * @param code
     *        c
     * @param value
     *        'c'
     * @return 本身
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
     * where拼接
     * <P>
     * 拼接where SQL，类似【where c = v】 c = code ， 'v' = Value 如果Value为null，或者"",返回 ""
     * </P>
     * 
     * @param code
     *        c
     * @param value
     *        'c'
     * @return 本身
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
     * @return 本身
     */
    public SQLBuilder distinct() {
        this.sql.append(" DISTINCT ");
        return this;
    }

    /**
     * dr
     * 
     * @return 本身
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
     * @return 本身
     */
    public SQLBuilder innerjoin() {
        this.sql.append(" inner join ");
        return this;
    }

    /**
     * Inner join
     * 
     * @param table
     *        表名
     * @return 本身
     */
    public SQLBuilder innerjoin(String tableName) {
        this.innerjoin();
        return this.append(tableName);

    }

    /**
     * inner notexists
     * 
     * @return 本身
     */
    public SQLBuilder notexists() {
        this.sql.append(" not exists ");
        return this;
    }

    /**
     * on
     * 
     * @return 本身
     */
    public SQLBuilder on() {
        this.sql.append(" on ");
        return this;
    }

    /**
     * 起始日期：xxx >= '2010-07-05 00:00:00'
     * 
     * @param date
     *        数据库字段
     * @param ufDate
     *        传入的起始日期
     * @return 本身
     */
    public SQLBuilder dateFrom(String date,
                               UFDate ufDate) {
        this.sql.append(date).append(" >= '").append(ufDate.toString()).append("' ");
        return this;
    }

    /**
     * 结束日期：xxx <= '2010-07-05 00:00:00'
     * 
     * @param date
     *        数据库字段
     * @param ufDate
     *        传入的起始日期
     * @return 本身
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
     * @return 本身
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
     * @return 本身
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
     * is Null 操作
     * 
     * @param code
     *        编码
     * @return 自身
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
     *        编码
     * @return 自身
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
     *        表名
     * @param codes
     *        编码
     * @return 自身
     */
    public SQLBuilder groupBy(String tableName,
                              String[] codes) {
        this.sql.append(" Group by ");
        this.contactBy2(tableName, codes);
        return this;
    }

    /**
     * Sum操作
     * 
     * @param tableName
     *        表明
     * @param filed
     *        字段
     * @param alias
     *        别名
     * @return 自身
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
     * On 连接
     * 
     * @param tableName1
     *        tableName1
     * @param filed1
     *        tableName1的字段
     * @param tableName2
     *        tableName2
     * @param filed2
     *        tableName2的字段
     * @return 本身
     */
    public SQLBuilder on(String tableName1,
                         String filed1,
                         String tableName2,
                         String filed2) {
        return this.onAnd(tableName1, filed1, tableName2, filed2, false);
    }

    /**
     * On 连接，但是后面跟随着And操作
     * 
     * @param tableName1
     *        tableName1
     * @param filed1
     *        tableName1的字段
     * @param tableName2
     *        tableName2
     * @param filed2
     *        tableName2的字段
     * @return 本身
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
