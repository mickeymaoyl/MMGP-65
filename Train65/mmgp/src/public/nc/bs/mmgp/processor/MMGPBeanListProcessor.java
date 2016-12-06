package nc.bs.mmgp.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import nc.jdbc.framework.processor.ProcessorUtils;

public class MMGPBeanListProcessor<T> extends MMGPAbsractProcessor<List<T>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8598346314717375952L;

	private Class<T> type = null;

	public MMGPBeanListProcessor(Class<T> type) {
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> processResultSet(ResultSet rs) throws SQLException {
		return ProcessorUtils.toBeanList(rs, type);
	}

}
