package nc.bs.mmgp.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * sql�����࣬�ṩһЩ��sql�����صķ���
 * 
 * @author zhoujuna
 */
public class SqlUtils {

	public static final int IN_SQL_MAX_LEN = 100; // ����û�г�������

	public static final String DR_CONDITION = " isnull(dr,0) = 0 ";

	public static final String UNSEAL_CONDITION = " isnull(bisseal,'N')='N'";

	/**
	 * �������и�涨�ĳ���,����֯��in���������
	 */
	public static List<String> inSQLByLens(String[] in) {

		return inSQLByLens(in, true);
	}

	public static List<String> inSQLByLens(String[] in, boolean haveQuote) {

		int pos = 0;

		List<String> inValueList = Arrays.asList(in);
		List<List<String>> subLists = new ArrayList<List<String>>();
		// int endPos = pos + IN_SQL_MAX_LEN;
		int endPos = 0; // zhaoxbc �޸�ǰ����ע�Ͳ���
		while (endPos < inValueList.size()) {
			endPos = pos + IN_SQL_MAX_LEN;
			if (endPos > inValueList.size()) {
				endPos = inValueList.size();
			}
			subLists.add(inValueList.subList(pos, endPos));
			pos = endPos;

		}

		List<String> inSQLList = new ArrayList<String>();
		for (List<String> list : subLists) {
			inSQLList.add(inSql(list.toArray(new String[0]), haveQuote));
		}
		return inSQLList;

	}

	public static String toArrayString(Collection<String> repeatValues,
			boolean haveQuote) {
		return toArrayString(repeatValues, ",", haveQuote);
	}

	public static String toArrayString(Collection<String> repeatValues,
			String splitchar) {
		return toArrayString(repeatValues, splitchar, false);
	}

	public static String toArrayString(Collection<String> repeatValues,
			String splitchar, boolean haveQuote) {
		StringBuilder toString = new StringBuilder();
		if (repeatValues.isEmpty()) {
			return toString.toString();
		}
		int index = 0;
		for (Object object : repeatValues) {
			if (haveQuote) {
				toString.append("'");
			}
			toString.append(object);
			if (haveQuote) {
				toString.append("'");
			}
			if (index < (repeatValues.size() - 1)) {
				toString.append(splitchar);
			}

			index++;
		}
		return toString.toString();
	}

	/**
	 * ��֯�����ΪString ('XXXX','XXXX','')
	 */
	public static String inSql(String[] in, boolean haveQuote) {

		String inSql = toArrayString(Arrays.asList(in), haveQuote);

		StringBuffer where = new StringBuffer();

		where.append("(");
		where.append(inSql);
		where.append(")");

		// where = "(" + where.substring( 0 , where.length() - 1 ) + ")";
		return where.toString();
	}

	/**
	 * ��֯�����ΪString ('XXXX','XXXX','')
	 */
	public static String inSql(String[] in) {

		return inSql(in, true);
	}

	/**
	 * ��֯�����ΪString
	 * 
	 * @param in
	 *            String[]
	 * @param isFilterSame
	 *            boolean ������ͬ��
	 * @return String ('XXXX','XXXX','')
	 */
	public static String inSqlFilterSame(String[] in) {

		Set<String> insqlSet = new HashSet<String>();
		for (int i = 0; i < in.length; i++) {
			insqlSet.add(in[i]);
		}
		return inSql(insqlSet.toArray(new String[insqlSet.size()]));
	}

	// codesync_xg_v01 "���Ӱ�int����ת����sql�е�in����" add -begin
	public static String inSqlInt(int[] in) {
		StringBuffer where = new StringBuffer();
		where.append("(");
		for (int i = 0; i < in.length; i++) {
			where.append(in[i]);
			if (i < in.length - 1) {
				where.append(",");
			}
		}
		where.append(")");
		return where.toString();

	}
	// codesync_xg_v01 add -end

	public static String inSql(List<String> in, boolean haveQuote) {
		return inSql(in.toArray(new String[0]), haveQuote);
	}
	public static String inSql(List<String> in) {
		return inSql(in,true);
	}
	
	
	protected List<String> inSQLByLens(List<String> in) {

		String[] inArr = new String[in.size()];
		in.toArray(inArr);
		List<String> inCondList = SqlUtils.inSQLByLens(inArr, true);
		return inCondList;
	}
}
