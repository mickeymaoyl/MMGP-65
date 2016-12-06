package nc.bs.mmgp.dao.inner;

public enum JoinType {
	INNER(" inner join "), LEFT(" left outer join "), RIGHT(
			" right outer join ");
	private String sql;

	private JoinType(String sql) {
		this.sql = sql;
	}

	public String toSql() {
		return sql;
	}
}
