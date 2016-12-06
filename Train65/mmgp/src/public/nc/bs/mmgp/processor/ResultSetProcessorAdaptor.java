package nc.bs.mmgp.processor;

import java.sql.ResultSet;
import java.sql.SQLException;

import nc.jdbc.framework.processor.ResultSetProcessor;

public class ResultSetProcessorAdaptor<T> implements ResultSetProcessor {
	private static final long serialVersionUID = 1L;

	private MMGPResultSetProcessor<T> mmgpProcessor;

	/**
	 * 
	 */

	public ResultSetProcessorAdaptor(MMGPResultSetProcessor<T> mmgpProcessor) {
		this.mmgpProcessor = mmgpProcessor;
	}

	@Override
	public T handleResultSet(ResultSet rs) throws SQLException {
		return mmgpProcessor.handleResultSet(rs);
	}

}
