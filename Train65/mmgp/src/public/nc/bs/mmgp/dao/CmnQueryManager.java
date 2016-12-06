package nc.bs.mmgp.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.bs.mmgp.common.CommonUtils;
import nc.bs.mmgp.dao.inner.JoinBean;
import nc.bs.mmgp.dao.inner.JoinType;
import nc.bs.mmgp.dao.inner.Order;
import nc.bs.mmgp.dao.inner.Select;
import nc.bs.mmgp.dao.inner.SimpleWhere;
import nc.bs.mmgp.processor.MMGPResultSetProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.pub.SuperVO;

/**
 * 使用例子
 * <P>
 * List<Map<String, Object>> queryResult = (List<Map<String, Object>>)
 * CmnQueryManager .select(new Select() .addFiled(OPConfigItem_118.class,
 * OPConfigItem_118.PK_OPCONFIGITEM, OPConfigItem_118.VFIRSTGROUP,
 * OPConfigItem_118.VSECONDGROUP, OPConfigItem_118.VTHIRDGROUP) .addFiled(
 * InstruTagNo_118.class, InstruTagNo_118.PK_INSTAGNO, // InstruTagNo_118.VCODE,
 * InstruTagNo_118.VNAME, InstruTagNo_118.NRATE, InstruTagNo_118.PK_MEASDOC,
 * InstruTagNo_118.VDATATYPE, InstruTagNo_118.NMEASVALUECEIL,
 * InstruTagNo_118.NMEASVALUEFLOOR) // 带别名的就自己拼吧
 * .addFiled("dai_instagno_118.vcode vinstagnocode",
 * "dai_instagno_118.vname vinstagnoname", "bd_measdoc.name unitname",
 * "pbd_sysatt_118.vname typename")) .from(OPConfigItem_118.class)
 * .innerJoin(InstruTagNo_118.class) .on(InstruTagNo_118.PK_INSTAGNO,
 * OPConfigItem_118.class, OPConfigItem_118.PK_INSTAGNO)
 * .leftJoin(MeasdocVO.class) .on(MeasdocVO.PK_MEASDOC, InstruTagNo_118.class,
 * InstruTagNo_118.PK_MEASDOC) .leftJoin(SysAttVO_118.class)
 * .on(SysAttVO_118.VCODE, InstruTagNo_118.class, InstruTagNo_118.VDATATYPE)
 * .where(new SimpleWhere().eq("ppm_opconfigitem_118.dr", 0).eq(
 * OPConfigItem_118.PK_OPCONFIG, pk_opconfig)) .orderBy( new
 * Order(OPConfigItem_118.NSORTNO + "," + OPConfigItem_118.VFIRSTGROUP + "," +
 * OPConfigItem_118.VSECONDGROUP + "," + OPConfigItem_118.VTHIRDGROUP))
 * .getResult(new MapListProcessor());
 * 
 * @author wangweiu
 * 
 */
public class CmnQueryManager {
	private Class<? extends SuperVO> fromClass;
	private List<JoinBean> joins = new ArrayList<JoinBean>();
	private SimpleWhere where;
	private List<Order> orders = new ArrayList<Order>();
	private Select select;

	private CmnQueryManager(Select select) {
		this.select = select;
	}

	public CmnQueryManager from(Class<? extends SuperVO> clazz) {
		fromClass = clazz;
		return this;
	}

	public static CmnQueryManager select(Select select) {

		return new CmnQueryManager(select);
	}

	public CmnQueryManager orderBy(Order... orderBy) {
		orders.addAll(Arrays.asList(orderBy));
		return this;
	}

	public CmnQueryManager innerJoin(Class<? extends SuperVO> clazz) {
		joins.add(new JoinBean(JoinType.INNER, clazz));
		return this;
	}

	public CmnQueryManager innerJoin(String tableName) {
		joins.add(new JoinBean(JoinType.INNER, tableName));
		return this;
	}

	public CmnQueryManager leftJoin(Class<? extends SuperVO> clazz) {
		joins.add(new JoinBean(JoinType.LEFT, clazz));
		return this;
	}

	public CmnQueryManager leftJoin(String tableName) {
		joins.add(new JoinBean(JoinType.LEFT, tableName));
		return this;
	}

	public CmnQueryManager rightJoin(Class<? extends SuperVO> clazz) {
		joins.add(new JoinBean(JoinType.RIGHT, clazz));
		return this;
	}

	public CmnQueryManager rightJoin(String tableName) {
		joins.add(new JoinBean(JoinType.RIGHT, tableName));
		return this;
	}

	public CmnQueryManager on(String thisFieldName,
			Class<? extends SuperVO> otherClazz, String otherFieldName) {
		return on(thisFieldName, otherClazz, otherFieldName, null);
	}

	public CmnQueryManager on(String thisFieldName, String otherFieldName) {
		return on(thisFieldName, otherFieldName, null);
	}

	public CmnQueryManager on(String thisFieldName,
			Class<? extends SuperVO> otherClazz, String otherFieldName,
			SimpleWhere where) {
		JoinBean current = joins.get(joins.size() - 1);
		String thisTable = current.getTableName();
		String otherTable = CommonUtils.getTableName(otherClazz);
		current.setOn(thisTable + "." + thisFieldName + " = " + otherTable
				+ "." + otherFieldName);
		current.setWhere(where);
		return this;
	}

	public CmnQueryManager on(String thisFieldName, String otherFieldName,
			SimpleWhere where) {
		JoinBean current = joins.get(joins.size() - 1);
		String thisTable = current.getTableName();
		current.setOn(thisTable + "." + thisFieldName + " = " + otherFieldName);
		current.setWhere(where);
		return this;
	}

	public CmnQueryManager where(SimpleWhere con) {
		where = con;
		return this;
	}

	List<String> groupBys = new ArrayList<String>();

	public CmnQueryManager groupBy(String... groupFields) {
		for (String groupField : groupFields) {
			groupBys.add(groupField);
		}
		return this;
	}

	/**
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> List<T> getResultList(Class<T> clazz)
			throws DAOException {
		return (List<T>) getResult(new BeanListProcessor(clazz));
	}

	/**
	 * 哎呀呀，这方法已经不推荐使用了,但我又不想加@Deprecated<br>
	 * ---加了以前的代码都变成屎黄色了，看着难受。 强烈推荐
	 * <P>
	 * 推荐：
	 * nc.bs.mmgp.dao.CmnQueryManager.getResult(MMGPResultSetProcessor<E>
	 *      mmgpProcessor) throws DAOException
	 * 
	 * @param processor
	 * @return
	 * @throws DAOException
	 * @see <E> E
	 */

	public Object getResult(ResultSetProcessor processor) throws DAOException {
		String sql = getSql();
		Logger.debug("debug:" + sql);
		MMGPCmnDAO cmnDAO = new MMGPCmnDAO();
		return cmnDAO.executeQuery(sql, processor);
	}

	/**
	 * 被你发现了?你有福了！<br>
	 * 带泛型哦！ 不需要强转！ 心动不？赶紧用吧
	 * <p>
	 * Map<String,Object> m = CmnQueryManager.select(null).getResult2(new
	 * MMGPMapProcessor());<br>
	 * 
	 * @param mmgpProcessor
	 * @return
	 * @throws DAOException
	 */
	public <E extends Object> E getResult(
			MMGPResultSetProcessor<E> mmgpProcessor) throws DAOException {

		String sql = getSql();
		Logger.debug("debug:" + sql);
		MMGPCmnDAO cmnDAO = new MMGPCmnDAO();
		return cmnDAO.executeQuery(sql, mmgpProcessor);
	}

	public String getSql() {
		StringBuffer sb = new StringBuffer("select ");
		sb.append(select.toSql());
		sb.append(" from ");
		sb.append(CommonUtils.getTableName(fromClass));
		for (JoinBean joinBean : joins) {
			sb.append(joinBean.toSql());
		}

		if (where != null) {
			sb.append(" where ");
			sb.append(where.toSql());
		}
		if (MMCollectionUtil.isNotEmpty(groupBys)) {
			sb.append(" group by ");
			sb.append(groupBys.get(0));
			for (int i = 1; i < groupBys.size(); i++) {
				String groupByField = groupBys.get(i);
				sb.append(",");
				sb.append(groupByField);
			}
		}

		if (MMCollectionUtil.isNotEmpty(orders)) {
			sb.append(" order by ");
			sb.append(orders.get(0).toSql());
			for (int i = 1; i < orders.size(); i++) {
				Order orderBy = orders.get(i);
				sb.append(",");
				sb.append(orderBy.toSql());
			}
		}

		return sb.toString();

	}

}
