package nc.bs.mmgp.processor;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class MMGPAbsractProcessor<T extends Object> implements
		MMGPResultSetProcessor<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4956889142157662918L;

	public final T handleResultSet(ResultSet rs) throws SQLException {
		if (rs == null)
			throw new IllegalArgumentException(
					"resultset parameter can't be null");
		try {
			return processResultSet(rs);
		} catch (SQLException e) {
			throw new SQLException("the resultsetProcessor error!"
					+ e.getMessage(), e.getSQLState());
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {

				}

		}

	}

	/**
	 * 处理结果集返回需要的对象
	 * 
	 * @param rs
	 *            结果集
	 * @return 需要的对象
	 * @throws SQLException如果发生错误
	 */
	public abstract T processResultSet(ResultSet rs) throws SQLException;

}
