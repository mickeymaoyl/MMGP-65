package nc.bs.mmgp.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import nc.jdbc.framework.processor.ProcessorUtils;

public class MMGPMapProcessor extends MMGPAbsractProcessor<Map<String, Object>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8807351449765951985L;

	@Override
	public Map<String, Object> processResultSet(ResultSet rs)
			throws SQLException {
		return rs.next() ? ProcessorUtils.toMap(rs) : null;
	}
}
