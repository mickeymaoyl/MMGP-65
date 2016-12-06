package nc.bs.mmgp.dao;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.mmgp.processor.MMGPResultSetProcessor;
import nc.bs.mmgp.processor.ResultSetProcessorAdaptor;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.mapping.IMappingMeta;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.pub.SuperVO;

/**
 * <b> 封装BaseDAO </b>
 * <p>
 * 增加泛型支持
 * </p>
 * 创建日期:2011-1-19
 *
 * @author wangweiu
 */
public class MMGPCmnDAO {
	/**
	 * 封装BaseDAO.
	 */
	private BaseDAO basDAO = null;

	/**
	 * 默认构造函数，将使用当前数据源
	 */
	public MMGPCmnDAO() {
		basDAO = new BaseDAO();
	}

	/**
	 * 有参构造函数，将使用指定数据源
	 *
	 * @param dataSource
	 *            数据源名称
	 */
	public MMGPCmnDAO(String dataSource) {
		basDAO = new BaseDAO(dataSource);
	}

	/**
	 * 根据SQL 执行数据库查询,并返回ResultSetProcessor处理后的对象 （非 Javadoc）
	 *
	 * @param sql
	 *            查询的SQL
	 * @param processor
	 *            结果集处理器
	 * @return 返回查询结果.
	 * @throws DAOException
	 *             查询发生错误抛出DAOException
	 */
	public Object executeQuery(String sql, ResultSetProcessor processor)
			throws DAOException {
		return basDAO.executeQuery(sql, processor);
	}

	/**
	 * 根据指定SQL 执行有参数的数据库查询,并返回ResultSetProcessor处理后的对象
	 *
	 * @param sql
	 *            查询的SQL
	 * @param parameter
	 *            查询参数
	 * @param processor
	 *            结果集处理器
	 * @return 返回查询结果.
	 * @throws DAOException
	 *             查询发生错误抛出DAOException
	 */
	public Object executeQuery(String sql, SQLParameter parameter,
			ResultSetProcessor processor) throws DAOException {
		return basDAO.executeQuery(sql, parameter, processor);
	}

	/**
	 * 我靠，这方法你都能看到？？你有福了！
	 * <p>
	 * 根据SQL 执行数据库查询,并返回ResultSetProcessor处理后的对象 （非 Javadoc）
	 *
	 * @param sql
	 *            查询的SQL
	 * @param processor
	 *            结果集处理器
	 * @return 返回查询结果.
	 * @throws DAOException
	 *             查询发生错误抛出DAOException
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> T executeQuery(String sql,
			MMGPResultSetProcessor<T> processor) throws DAOException {
		return (T) basDAO.executeQuery(sql, new ResultSetProcessorAdaptor<T>(
				processor));
	}

	/**
	 * 我靠，这方法你都能看到？？你有福了！
	 * <p>
	 * 根据指定SQL 执行有参数的数据库查询,并返回ResultSetProcessor处理后的对象
	 *
	 * @param sql
	 *            查询的SQL
	 * @param parameter
	 *            查询参数
	 * @param processor
	 *            结果集处理器
	 * @return 返回查询结果.
	 * @throws DAOException
	 *             查询发生错误抛出DAOException
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> T executeQuery(String sql,
			SQLParameter parameter, MMGPResultSetProcessor<T> processor)
			throws DAOException {
		return (T) basDAO.executeQuery(sql, parameter,
				new ResultSetProcessorAdaptor<T>(processor));
	}

	/**
	 * 根据指定SQL 执行有参数的数据库更新操作
	 *
	 * @param sql
	 *            更新的sql
	 * @param parameter
	 *            更新参数
	 * @return 执行成功的数量.
	 * @throws DAOException
	 *             更新发生错误抛出DAOException
	 */
	public int executeUpdate(String sql, SQLParameter parameter)
			throws DAOException {
		return basDAO.executeUpdate(sql, parameter);
	}

	/**
	 * 根据指定SQL 执行无参数的数据库更新操作
	 *
	 * @param sql
	 *            更新的sql
	 * @return 执行成功的数量.
	 * @throws DAOException
	 *             更新发生错误抛出DAOException
	 */
	public int executeUpdate(String sql) throws DAOException {
		return basDAO.executeUpdate(sql);
	}

	/**
	 * 根据VO类名查询该VO对应表的所有数据
	 *
	 * @param className
	 *            SuperVo类名
	 * @param <T>
	 *            superVO子类
	 * @return 查询结果集
	 * @throws DAOException
	 *             发生错误抛出DAOException
	 */
	@SuppressWarnings("unchecked")
	public <T extends SuperVO> List<T> retrieveAll(Class<T> className)
			throws DAOException {
		return (List<T>) basDAO.retrieveAll(className);
	}

	/**
	 * 根据VO类名和where条件返回vo集合
	 *
	 * @param <T>
	 *            superVO子类
	 * @param className
	 *            Vo类名称
	 * @param condition
	 *            查询条件
	 * @return 返回Vo集合
	 * @throws DAOException
	 *             发生错误抛出DAOException
	 */
	@SuppressWarnings("unchecked")
	public <T extends SuperVO> List<T> retrieveByClause(Class<T> className,
			String condition) throws DAOException {
		return (List<T>) basDAO.retrieveByClause(className, condition);
	}

	@SuppressWarnings("unchecked")
	public <T extends SuperVO> List<T> retrieveByClause(Class<T> className,
			String condition, SQLParameter params) throws DAOException {
		return (List<T>) basDAO.retrieveByClause(className, condition, params);
	}

	/**
	 * 根据条件和排序返回Vo集合
	 *
	 * @param <T>
	 *            superVO子类
	 * @param className
	 *            VO类名
	 * @param condition
	 *            查询条件
	 * @param orderBy
	 *            排序条件
	 * @return 返回Vo集合
	 * @throws DAOException
	 *             发生错误抛出DAOException
	 */
	@SuppressWarnings("unchecked")
	public <T extends SuperVO> List<T> retrieveByClause(Class<T> className,
			String condition, String orderBy) throws DAOException {
		return (List<T>) basDAO.retrieveByClause(className, condition, orderBy);
	}

	/**
	 * @param <T>
	 * @param className
	 * @param condition
	 * @param orderBy
	 * @param params
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public <T extends SuperVO> List<T> retrieveByClause(Class<T> className,
			String condition, String orderBy, SQLParameter params)
			throws DAOException {
		return (List<T>) basDAO.retrieveByClause(className, condition, orderBy,
				params);
	}

	/**
	 * 根据PK查询指定VO
	 *
	 * @param <T>
	 *            superVO子类
	 * @param VO类名
	 * @param pk
	 *            主键
	 */
	@SuppressWarnings("unchecked")
	public <T extends SuperVO> T retrieveByPK(Class<T> className, String pk)
			throws DAOException {
		return (T) basDAO.retrieveByPK(className, pk);
	}

	/**
	 * 通过公司id和表的主键查询.
	 *
	 * @param <T>
	 * @param clazz
	 * @param pks
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("deprecation")
	public <T extends SuperVO> List<T> retrieveByPKs(Class<T> clazz,
			String[] pks) throws DAOException {
		SuperVO vo = initSuperVOClass(clazz);
		StringBuffer sbWhereCondition = new StringBuffer("(");
		for (int i = 0; i < pks.length; i++) {

			// where条件
			sbWhereCondition.append(vo.getPKFieldName());
			sbWhereCondition.append(" = '");
			sbWhereCondition.append(pks[i]);
			sbWhereCondition.append("'");

			if (i != (pks.length - 1)) {
				sbWhereCondition.append(" or ");
			}

		}
		sbWhereCondition.append(")");

		List<T> result = retrieveByClause(clazz, sbWhereCondition.toString());
		return result;
	}

	private <T extends SuperVO> T initSuperVOClass(Class<T> className) {
		T vo;
		try {
			vo = className.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0046")/*@res "传入的参数类无法进行实例化"*/);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0047")/*@res "传入不合法参数"*/);
		}
		return vo;
	}

	/**
	 * 根据主键返回指定列的VO对象
	 *
	 * @param <T>
	 *            superVO子类
	 */
	@SuppressWarnings("unchecked")
	public <T extends SuperVO> T retrieveByPK(Class<T> className, String pk,
			String[] selectedFields) throws DAOException {
		return (T) basDAO.retrieveByPK(className, pk, selectedFields);
	}

	/**
	 * 插入一个VO对象，如果该VO的主键值非空则插入VO的原有主键
	 *
	 * @param vo
	 * @return
	 * @throws DAOException
	 */
	public String insertVOWithPK(SuperVO vo) throws DAOException {
		setDr(vo);
		return basDAO.insertVOWithPK(vo);
	}

	/**
	 * 插入一个VO对象
	 *
	 * @param vo
	 *            SuperVO对象
	 */
	public String insertVO(SuperVO vo) throws DAOException {
		setDr(vo);
		return basDAO.insertVO(vo);
	}

	/**
	 * 插入一个VO数组如果该VO的主键值非空则插入VO的原有主键
	 *
	 * @param vo
	 * @return
	 * @throws DAOException
	 */
	public String[] insertVOArrayWithPK(SuperVO[] vo) throws DAOException {
		setDr(vo);
		return basDAO.insertVOArrayWithPK(vo);
	}

	/**
	 * 插入VO数组
	 *
	 * @param vo
	 *            VO数组
	 */
	public String[] insertVOArray(SuperVO[] vo) throws DAOException {
		setDr(vo);
		return basDAO.insertVOArray(vo);
	}

	/**
	 * 插入VO集合
	 *
	 * @param vos
	 *            VO集合
	 */
	public <T extends SuperVO> String[] insertVOList(List<T> vos)
			throws DAOException {
		setDr(vos);
		return basDAO.insertVOList(vos);
	}

	/**
	 * 插入带pk的VO集合
	 *
	 * @param vos
	 *            VO集合
	 */
	public <T extends SuperVO> String[] insertVOListWithPK(List<T> vos)
			throws DAOException {
		setDr(vos);
		return basDAO.insertVOArrayWithPK(vos.toArray(new SuperVO[vos.size()]));
	}

	/**
	 * 插入一个VO数组如果该VO的主键值非空则插入VO的原有主键, 作为插入VO数组操作的扩展，可以批量插入非SuperVO子类的VO数据
	 *
	 * @param vos
	 *            待插入的VO数组
	 * @return
	 * @throws DAOException
	 */
	public String[] insertObjectWithPK(Object[] vos, IMappingMeta meta)
			throws DAOException {
		return basDAO.insertObjectWithPK(vos, meta);
	}

	/**
	 * 根据VO对象中指定列更新数据库
	 *
	 * @param vos
	 *            VO对象
	 * @param fieldNames
	 *            指定列
	 * @throws DAOException
	 */
	public void updateVO(SuperVO vo, String[] fieldNames) throws DAOException {
		updateVOArray(new SuperVO[] { vo }, fieldNames);
	}

	/**
	 * 根据VO对象数组更新数据库
	 *
	 * @param vo
	 *            VO对象
	 */
	public int updateVO(SuperVO... vos) throws DAOException {
		return updateVOArray(vos, null);
	}

	/**
	 * 根据VO对象集合更新数据库
	 *
	 * @paramvos VO对象集合
	 */
	public <T extends SuperVO> void updateVOs(List<T> vos) throws DAOException {
		basDAO.updateVOList(vos);

	}

	/**
	 * 根据VO对象数组中指定列更新数据库
	 *
	 * @param vos
	 *            VO对象
	 * @param fieldNames
	 *            指定列
	 */
	public int updateVOArray(SuperVO[] vos, String[] fieldNames)
			throws DAOException {
		return updateVOArray(vos, fieldNames, null, null);

	}

	public int updateVOArray(final SuperVO[] vos, String[] fieldNames,
			String whereClause, SQLParameter param) throws DAOException {
		return basDAO.updateVOArray(vos, fieldNames, whereClause, param);
	}

	/**
	 * 更新所有的VO对象，该对象作为一种更新操作的扩展，可以用于更新非SuperVO子类的VO对象！
	 *
	 * @param vos
	 *            VO对象集合
	 */
	public int updateObject(Object[] vos, IMappingMeta meta)
			throws DAOException {
		return basDAO.updateObject(vos, meta);
	}

	/**
	 * 在数据库中删除一组VO对象。
	 *
	 * @param vos
	 *            VO数组对象
	 * @throws DAOException
	 *             如果删除出错抛出DAOException
	 */
	public void deleteVO(SuperVO... vos) throws DAOException {
		basDAO.deleteVOArray(vos);
	}

	/**
	 * 在数据库中根据类名和PK数组删除一组VO对象集合
	 *
	 * @param className
	 *            要删除的VO类名
	 * @param pks
	 *            PK数组
	 * @throws DAOException
	 *             如果删除出错抛出DAOException
	 */
	public <T extends SuperVO> void deleteByPKs(Class<T> className, String[] pks)
			throws DAOException {
		basDAO.deleteByPKs(className, pks);

	}

	/**
	 * 在数据库中根据类名和条件删除数据
	 *
	 * @param className
	 *            VO类名
	 * @param wherestr
	 *            条件
	 * @throws DAOException
	 *             如果删除出错抛出DAOException
	 */
	public <T extends SuperVO> void deleteByClause(Class<T> className,
			String wherestr) throws DAOException {
		basDAO.deleteByClause(className, wherestr);

	}

	public <T extends SuperVO> void deleteByClause(Class<T> className,
			String wherestr, SQLParameter params) throws DAOException {
		basDAO.deleteByClause(className, wherestr, params);

	}

	/**
	 * 在数据库中根据类名和PK删除一个VO对象集合
	 *
	 * @param classNamep
	 *            VO类名
	 * @param pk
	 *            PK值
	 * @throws DAOException
	 *             如果删除出错抛出DAOException
	 */
	public <T extends SuperVO> void deleteByPK(Class<T> className, String pk)
			throws DAOException {
		basDAO.deleteByPK(className, pk);

	}

	/**
	 * 在数据库中删除一组VO对象集合
	 *
	 * @param vos
	 *            VO对象集合
	 * @throws DAOException
	 *             如果删除出错抛出DAOException
	 */
	public <T extends SuperVO> void deleteVOList(List<T> vos)
			throws DAOException {
		basDAO.deleteVOList(vos);

	}

	/**
	 * 获得数据源类型
	 *
	 * @param key
	 *            nc.vo.pub.oid.OID
	 * @throws java.sql.SQLException
	 *             异常说明。
	 */
	public int getDBType() {

		return basDAO.getDBType();

	}

	/**
	 * 判断数据表是否存在
	 *
	 * @param tableName
	 *            数据表名称
	 * @return
	 * @throws DAOException
	 *             出错抛出DAOException
	 */
	public boolean isTableExisted(String tableName) throws DAOException {
		return basDAO.isTableExisted(tableName);
	}

	public int getMaxRows() {
		return basDAO.getMaxRows();
	}

	public void setMaxRows(int maxRows) {
		basDAO.setMaxRows(maxRows);
	}

	private <T extends SuperVO> void setDr(List<T> vos) {
		if (MMCollectionUtil.isEmpty(vos)) {
			return;
		}
		for (SuperVO vo : vos) {
			vo.setAttributeValue("dr", 0);
		}
	}

	private void setDr(SuperVO... vos) {
		if (MMArrayUtil.isEmpty(vos)) {
			return;
		}
		for (SuperVO vo : vos) {
			vo.setAttributeValue("dr", 0);
		}
	}

}