package nc.bs.mmgp.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ��ֵ������������һ����ArrayList���󣬽�������ж������ݣ��ö����Ӧ��������ĳһ�е�ֵ���ô�����ͨ��������е���Ż�������ȷ����
 */
public class MMGPColumnListProcessor<T> extends MMGPAbsractProcessor<List<T>> {
    /**
	 * <code>serialVersionUID</code> ��ע��
	 */
	private static final long serialVersionUID = -851727907824262100L;


	private int columnIndex = 1;


    private String columnName = null;


    public MMGPColumnListProcessor() {
        super();
    }


    public MMGPColumnListProcessor(int columnIndex) {
        this.columnIndex = columnIndex;
    }


    public MMGPColumnListProcessor(String columnName) {
        this.columnName = columnName;
    }

    @SuppressWarnings("unchecked")
	public List<T> processResultSet(ResultSet rs) throws SQLException {
         List<T> result = new ArrayList<T>();
        while (rs.next()) {
            if (this.columnName == null) {
                result.add((T) rs.getObject(this.columnIndex));
            } else {
                result.add((T) rs.getObject(this.columnName));
            }
        }
        return result;
    }
 }

