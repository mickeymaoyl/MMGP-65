package nc.bs.mmgp.dao.inner;

public class Order {
	private String orderType = "asc";
	private String field;

	public Order(String field) {
		this.field = field;
	}

	public Order(String field, String orderType) {
		this.field = field;
		this.orderType = orderType;
	}

	public String toSql() {
		return field + " " + orderType;
	}

}
