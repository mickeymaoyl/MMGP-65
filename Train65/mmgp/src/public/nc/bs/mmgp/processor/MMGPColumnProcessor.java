package nc.bs.mmgp.processor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ��ֵ������������һ��Java���󣬽������ֻ��һ�����ݣ��ö����Ӧ��������ĳһ�е�ֵ���ô�����ͨ��������е���Ż�������ȷ����
 */
public class MMGPColumnProcessor<T> extends MMGPAbsractProcessor<T> {

	/**
	 * <code>serialVersionUID</code> ��ע��
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
