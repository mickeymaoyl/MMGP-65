package nc.bs.mmgp.dao.inner;

import nc.impl.pubapp.pattern.database.IDQueryBuilder;

public class SimpleWhere {
	private StringBuffer conditionSQL = new StringBuffer();

	public SimpleWhere or(SimpleWhere or) {
		conditionSQL.append(" or ");
		conditionSQL.append(or.toSql());

		return this;
	}

	public SimpleWhere and(SimpleWhere and) {
		conditionSQL.append(" and ");
		conditionSQL.append(and.toSql());
		return this;
	}

	public SimpleWhere eq(String field, Object value) {
		addField(field, value, Operator.EQ);
		return this;
	}

	public SimpleWhere dr() {
		addField("isnull(dr,0)", 0, Operator.EQ);
		return this;
	}

	public SimpleWhere gt(String field, Object value) {
		addField(field, value, Operator.GT);
		return this;
	}

	public SimpleWhere lt(String field, Object value) {
		addField(field, value, Operator.LT);
		return this;
	}

	public SimpleWhere ge(String field, Object value) {
		addField(field, value, Operator.GE);
		return this;
	}

	public SimpleWhere le(String field, Object value) {
		addField(field, value, Operator.LE);
		return this;
	}

	public SimpleWhere in(String field, String[] values) {
		return in(field, values, true);
	}

	public SimpleWhere in(String field, String[] values, boolean haveQuote) {
		IDQueryBuilder builder = new IDQueryBuilder();
		addCondition(builder.buildSQL(field, values));
		return this;
	}

	public SimpleWhere like(String field, String value) {
		addField(field, value, Operator.LIKE);
		return this;
	}

	public final void addCondition(String condition) {
		if (conditionSQL.length() > 0) {
			conditionSQL.append(" and ");
		}
		conditionSQL.append(condition);

	}

	private boolean haveQuote(Object value) {
		return !(value instanceof Number);
	}

	protected void addField(String field, Object value, Operator op) {
		Object sqlValue;
		if (haveQuote(value)) {
			sqlValue = "'" + value.toString() + "'";
		} else {
			sqlValue = value;
		}
		String condition = field + op.toString() + sqlValue;
		addCondition(condition);
	}

	public String toSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		sb.append(conditionSQL.toString());
		sb.append(")");
		return sb.toString();
	}

	private static enum Operator {
		EQ("="), GT(">"), LT("<"), GE(">="), LE("<="), LIKE("like"), IN("in");
		private String str;

		private Operator(String str) {
			this.str = str;
		}

		@Override
		public String toString() {
			return str;
		}
	}
}
