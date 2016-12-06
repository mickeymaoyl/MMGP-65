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
 * <b> ��װBaseDAO </b>
 * <p>
 * ���ӷ���֧��
 * </p>
 * ��������:2011-1-19
 *
 * @author wangweiu
 */
public class MMGPCmnDAO {
	/**
	 * ��װBaseDAO.
	 */
	private BaseDAO basDAO = null;

	/**
	 * Ĭ�Ϲ��캯������ʹ�õ�ǰ����Դ
	 */
	public MMGPCmnDAO() {
		basDAO = new BaseDAO();
	}

	/**
	 * �вι��캯������ʹ��ָ������Դ
	 *
	 * @param dataSource
	 *            ����Դ����
	 */
	public MMGPCmnDAO(String dataSource) {
		basDAO = new BaseDAO(dataSource);
	}

	/**
	 * ����SQL ִ�����ݿ��ѯ,������ResultSetProcessor�����Ķ��� ���� Javadoc��
	 *
	 * @param sql
	 *            ��ѯ��SQL
	 * @param processor
	 *            �����������
	 * @return ���ز�ѯ���.
	 * @throws DAOException
	 *             ��ѯ���������׳�DAOException
	 */
	public Object executeQuery(String sql, ResultSetProcessor processor)
			throws DAOException {
		return basDAO.executeQuery(sql, processor);
	}

	/**
	 * ����ָ��SQL ִ���в��������ݿ��ѯ,������ResultSetProcessor�����Ķ���
	 *
	 * @param sql
	 *            ��ѯ��SQL
	 * @param parameter
	 *            ��ѯ����
	 * @param processor
	 *            �����������
	 * @return ���ز�ѯ���.
	 * @throws DAOException
	 *             ��ѯ���������׳�DAOException
	 */
	public Object executeQuery(String sql, SQLParameter parameter,
			ResultSetProcessor processor) throws DAOException {
		return basDAO.executeQuery(sql, parameter, processor);
	}

	/**
	 * �ҿ����ⷽ���㶼�ܿ����������и��ˣ�
	 * <p>
	 * ����SQL ִ�����ݿ��ѯ,������ResultSetProcessor�����Ķ��� ���� Javadoc��
	 *
	 * @param sql
	 *            ��ѯ��SQL
	 * @param processor
	 *            �����������
	 * @return ���ز�ѯ���.
	 * @throws DAOException
	 *             ��ѯ���������׳�DAOException
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> T executeQuery(String sql,
			MMGPResultSetProcessor<T> processor) throws DAOException {
		return (T) basDAO.executeQuery(sql, new ResultSetProcessorAdaptor<T>(
				processor));
	}

	/**
	 * �ҿ����ⷽ���㶼�ܿ����������и��ˣ�
	 * <p>
	 * ����ָ��SQL ִ���в��������ݿ��ѯ,������ResultSetProcessor�����Ķ���
	 *
	 * @param sql
	 *            ��ѯ��SQL
	 * @param parameter
	 *            ��ѯ����
	 * @param processor
	 *            �����������
	 * @return ���ز�ѯ���.
	 * @throws DAOException
	 *             ��ѯ���������׳�DAOException
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> T executeQuery(String sql,
			SQLParameter parameter, MMGPResultSetProcessor<T> processor)
			throws DAOException {
		return (T) basDAO.executeQuery(sql, parameter,
				new ResultSetProcessorAdaptor<T>(processor));
	}

	/**
	 * ����ָ��SQL ִ���в��������ݿ���²���
	 *
	 * @param sql
	 *            ���µ�sql
	 * @param parameter
	 *            ���²���
	 * @return ִ�гɹ�������.
	 * @throws DAOException
	 *             ���·��������׳�DAOException
	 */
	public int executeUpdate(String sql, SQLParameter parameter)
			throws DAOException {
		return basDAO.executeUpdate(sql, parameter);
	}

	/**
	 * ����ָ��SQL ִ���޲��������ݿ���²���
	 *
	 * @param sql
	 *            ���µ�sql
	 * @return ִ�гɹ�������.
	 * @throws DAOException
	 *             ���·��������׳�DAOException
	 */
	public int executeUpdate(String sql) throws DAOException {
		return basDAO.executeUpdate(sql);
	}

	/**
	 * ����VO������ѯ��VO��Ӧ�����������
	 *
	 * @param className
	 *            SuperVo����
	 * @param <T>
	 *            superVO����
	 * @return ��ѯ�����
	 * @throws DAOException
	 *             ���������׳�DAOException
	 */
	@SuppressWarnings("unchecked")
	public <T extends SuperVO> List<T> retrieveAll(Class<T> className)
			throws DAOException {
		return (List<T>) basDAO.retrieveAll(className);
	}

	/**
	 * ����VO������where��������vo����
	 *
	 * @param <T>
	 *            superVO����
	 * @param className
	 *            Vo������
	 * @param condition
	 *            ��ѯ����
	 * @return ����Vo����
	 * @throws DAOException
	 *             ���������׳�DAOException
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
	 * �������������򷵻�Vo����
	 *
	 * @param <T>
	 *            superVO����
	 * @param className
	 *            VO����
	 * @param condition
	 *            ��ѯ����
	 * @param orderBy
	 *            ��������
	 * @return ����Vo����
	 * @throws DAOException
	 *             ���������׳�DAOException
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
	 * ����PK��ѯָ��VO
	 *
	 * @param <T>
	 *            superVO����
	 * @param VO����
	 * @param pk
	 *            ����
	 */
	@SuppressWarnings("unchecked")
	public <T extends SuperVO> T retrieveByPK(Class<T> className, String pk)
			throws DAOException {
		return (T) basDAO.retrieveByPK(className, pk);
	}

	/**
	 * ͨ����˾id�ͱ��������ѯ.
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

			// where����
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
			throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0046")/*@res "����Ĳ������޷�����ʵ����"*/);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0047")/*@res "���벻�Ϸ�����"*/);
		}
		return vo;
	}

	/**
	 * ������������ָ���е�VO����
	 *
	 * @param <T>
	 *            superVO����
	 */
	@SuppressWarnings("unchecked")
	public <T extends SuperVO> T retrieveByPK(Class<T> className, String pk,
			String[] selectedFields) throws DAOException {
		return (T) basDAO.retrieveByPK(className, pk, selectedFields);
	}

	/**
	 * ����һ��VO���������VO������ֵ�ǿ������VO��ԭ������
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
	 * ����һ��VO����
	 *
	 * @param vo
	 *            SuperVO����
	 */
	public String insertVO(SuperVO vo) throws DAOException {
		setDr(vo);
		return basDAO.insertVO(vo);
	}

	/**
	 * ����һ��VO���������VO������ֵ�ǿ������VO��ԭ������
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
	 * ����VO����
	 *
	 * @param vo
	 *            VO����
	 */
	public String[] insertVOArray(SuperVO[] vo) throws DAOException {
		setDr(vo);
		return basDAO.insertVOArray(vo);
	}

	/**
	 * ����VO����
	 *
	 * @param vos
	 *            VO����
	 */
	public <T extends SuperVO> String[] insertVOList(List<T> vos)
			throws DAOException {
		setDr(vos);
		return basDAO.insertVOList(vos);
	}

	/**
	 * �����pk��VO����
	 *
	 * @param vos
	 *            VO����
	 */
	public <T extends SuperVO> String[] insertVOListWithPK(List<T> vos)
			throws DAOException {
		setDr(vos);
		return basDAO.insertVOArrayWithPK(vos.toArray(new SuperVO[vos.size()]));
	}

	/**
	 * ����һ��VO���������VO������ֵ�ǿ������VO��ԭ������, ��Ϊ����VO�����������չ���������������SuperVO�����VO����
	 *
	 * @param vos
	 *            �������VO����
	 * @return
	 * @throws DAOException
	 */
	public String[] insertObjectWithPK(Object[] vos, IMappingMeta meta)
			throws DAOException {
		return basDAO.insertObjectWithPK(vos, meta);
	}

	/**
	 * ����VO������ָ���и������ݿ�
	 *
	 * @param vos
	 *            VO����
	 * @param fieldNames
	 *            ָ����
	 * @throws DAOException
	 */
	public void updateVO(SuperVO vo, String[] fieldNames) throws DAOException {
		updateVOArray(new SuperVO[] { vo }, fieldNames);
	}

	/**
	 * ����VO��������������ݿ�
	 *
	 * @param vo
	 *            VO����
	 */
	public int updateVO(SuperVO... vos) throws DAOException {
		return updateVOArray(vos, null);
	}

	/**
	 * ����VO���󼯺ϸ������ݿ�
	 *
	 * @paramvos VO���󼯺�
	 */
	public <T extends SuperVO> void updateVOs(List<T> vos) throws DAOException {
		basDAO.updateVOList(vos);

	}

	/**
	 * ����VO����������ָ���и������ݿ�
	 *
	 * @param vos
	 *            VO����
	 * @param fieldNames
	 *            ָ����
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
	 * �������е�VO���󣬸ö�����Ϊһ�ָ��²�������չ���������ڸ��·�SuperVO�����VO����
	 *
	 * @param vos
	 *            VO���󼯺�
	 */
	public int updateObject(Object[] vos, IMappingMeta meta)
			throws DAOException {
		return basDAO.updateObject(vos, meta);
	}

	/**
	 * �����ݿ���ɾ��һ��VO����
	 *
	 * @param vos
	 *            VO�������
	 * @throws DAOException
	 *             ���ɾ�������׳�DAOException
	 */
	public void deleteVO(SuperVO... vos) throws DAOException {
		basDAO.deleteVOArray(vos);
	}

	/**
	 * �����ݿ��и���������PK����ɾ��һ��VO���󼯺�
	 *
	 * @param className
	 *            Ҫɾ����VO����
	 * @param pks
	 *            PK����
	 * @throws DAOException
	 *             ���ɾ�������׳�DAOException
	 */
	public <T extends SuperVO> void deleteByPKs(Class<T> className, String[] pks)
			throws DAOException {
		basDAO.deleteByPKs(className, pks);

	}

	/**
	 * �����ݿ��и�������������ɾ������
	 *
	 * @param className
	 *            VO����
	 * @param wherestr
	 *            ����
	 * @throws DAOException
	 *             ���ɾ�������׳�DAOException
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
	 * �����ݿ��и���������PKɾ��һ��VO���󼯺�
	 *
	 * @param classNamep
	 *            VO����
	 * @param pk
	 *            PKֵ
	 * @throws DAOException
	 *             ���ɾ�������׳�DAOException
	 */
	public <T extends SuperVO> void deleteByPK(Class<T> className, String pk)
			throws DAOException {
		basDAO.deleteByPK(className, pk);

	}

	/**
	 * �����ݿ���ɾ��һ��VO���󼯺�
	 *
	 * @param vos
	 *            VO���󼯺�
	 * @throws DAOException
	 *             ���ɾ�������׳�DAOException
	 */
	public <T extends SuperVO> void deleteVOList(List<T> vos)
			throws DAOException {
		basDAO.deleteVOList(vos);

	}

	/**
	 * �������Դ����
	 *
	 * @param key
	 *            nc.vo.pub.oid.OID
	 * @throws java.sql.SQLException
	 *             �쳣˵����
	 */
	public int getDBType() {

		return basDAO.getDBType();

	}

	/**
	 * �ж����ݱ��Ƿ����
	 *
	 * @param tableName
	 *            ���ݱ�����
	 * @return
	 * @throws DAOException
	 *             �����׳�DAOException
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