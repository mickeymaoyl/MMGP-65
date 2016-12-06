package nc.bs.mmgp.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.jdbc.framework.processor.ProcessorUtils;

public class MMGPMapListProcessor extends
		MMGPAbsractProcessor<List<Map<String, Object>>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1326176242022317638L;

	@Override
	public List<Map<String, Object>> processResultSet(ResultSet rs)
			throws SQLException {
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		while (rs.next()) {
			results.add(ProcessorUtils.toMap(rs));
		}
		return results;
	}

}
