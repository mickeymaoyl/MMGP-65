package nc.bs.mmgp.processor;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface MMGPResultSetProcessor<T> extends Serializable {

	public T handleResultSet(ResultSet rs) throws SQLException;
}
