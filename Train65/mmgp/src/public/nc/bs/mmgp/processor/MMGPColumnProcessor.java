package nc.bs.mmgp.processor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 列值处理器，返回一个Java对象，结果集中只有一行数据，该对象对应与结果集中某一列的值，该处理器通过结果集列的序号或名称来确定列
 */
public class MMGPColumnProcessor<T> extends MMGPAbsractProcessor<T> {

	/**
	 * <code>serialVersionUID</code> 的注释
	 */
	private static final long serialVersionUID = -2578646856668989095L;

	private int columnIndex = 1;

	private String columnName = null;

	public MMGPColumnProcessor() {
		super();
	}

	public MMGPColumnProcessor(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public MMGPColumnProcessor(String columnName) {
		this.columnName = columnName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T processResultSet(ResultSet rs) throws SQLException {

		if (rs.next()) {
			if (this.columnName == null) {
				return (T) rs.getObject(this.columnIndex);
			} else {
				return (T) rs.getObject(this.columnName);
			}

		} else {
			return null;
		}
	}
}
