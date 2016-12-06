package nc.bs.mmgp.dao.inner;

import nc.bs.mmgp.common.CommonUtils;
import nc.vo.pub.SuperVO;

public class JoinBean {
	private JoinType type = JoinType.INNER;
	private String tableName;
//	private Class<? extends SuperVO> joinClass;
	private String on;
	private SimpleWhere where;

	public JoinBean(JoinType joinType, Class<? extends SuperVO> joinClass) {
		type = joinType;
		tableName = CommonUtils.getTableName(joinClass);
	}
	
	public JoinBean(JoinType joinType, String table) {
		type = joinType;
		this.tableName = table;
	}

//	public Class<? extends SuperVO> getJoinClass() {
//		return joinClass;
//	}

	public String getTableName() {
		return tableName;
	}

	public String getOn() {
		return on;
	}

	public void setOn(String on) {
		this.on = on;
	}

	public SimpleWhere getWhere() {
		return where;
	}

	public void setWhere(SimpleWhere where) {
		this.where = where;
	}

	public String toSql() {
		StringBuffer sb = new StringBuffer();
		sb.append(type.toSql());
		sb.append(tableName);
		sb.append(" on ");
		sb.append(on);
		if (where != null) {
			sb.append(" and (");
			sb.append(where.toSql());
			sb.append(" )");
		}

		return sb.toString();
	}
}
