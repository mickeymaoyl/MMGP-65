package nc.ui.mmgp.uif2.query;

import nc.ui.pubapp.uif2app.query2.refedit.IRefFilter;

/**
 * <b>查询面板通用参照过滤 </b>
 * @author chenleif
 * @date 2013-6-19
 * @description
 */
public class MMGPCommonQueryRefFilter {
	private String tableName;
	private String fieldCode;
	private String whereSql;
	private IRefFilter filter;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public IRefFilter getFilter() {
		return filter;
	}
	public void setFilter(IRefFilter filter) {
		this.filter = filter;
	}

	public String getWhereSql() {
		return whereSql;
	}
	public void setWhereSql(String whereSql) {
		this.whereSql = whereSql;
	}
	public String getFieldCode() {
		return fieldCode;
	}
	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}
}
