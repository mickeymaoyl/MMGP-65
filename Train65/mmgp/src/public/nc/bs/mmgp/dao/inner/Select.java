package nc.bs.mmgp.dao.inner;

import java.util.ArrayList;
import java.util.List;

import nc.bs.mmgp.common.CommonUtils;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IAttribute;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class Select {
	List<String> fullFields = new ArrayList<String>();

	public Select addFiled(String... fields) {
		for (String field : fields) {
			fullFields.add(field);
		}
		return this;
	}

	public Select addFiled(Class<? extends SuperVO> clazz, String... fields) {
		String tableName = CommonUtils.getTableName(clazz);
		for (String field : fields) {
			fullFields.add(tableName + "." + field);
		}
		return this;
	}

	public Select addAllFileds(Class<? extends SuperVO> clazz) {

		IBusinessEntity entity;

		try {
			entity = (IBusinessEntity) MDBaseQueryFacade.getInstance()
					.getBeanByFullClassName(clazz.getName());

		} catch (MetaDataException e) {
			ExceptionUtils.wrappException(e);
			// 实际上走不到此行
			return this;
		}
		if (entity != null) {
			String tableName = entity.getTable().getName();
			List<IAttribute> attributes = entity.getAttributes();
			for (IAttribute attribute : attributes) {
				if (!attribute.isCalculation()&& !attribute.isNotSerialize() && !"status".equals(attribute.getName())) {
					// 去掉计算属性
					// 去掉不可序列化
					fullFields.add(tableName + "." + attribute.getName());
				}
			}

			return this;
		}

		// 如果没有找到元数据，则用创建对象的方法
		SuperVO emptyObj = CommonUtils.newObject(clazz);
		@SuppressWarnings("deprecation")
		String table = emptyObj.getTableName();
		for (String field : emptyObj.getAttributeNames()) {
			// 去除伪列 wrr update
			if (!"PSEUDOCOLUMN".equalsIgnoreCase(field)) {
				fullFields.add(table + "." + field);
			}
		}
		return this;

	}

	public String toSql() {
		StringBuffer sb = new StringBuffer();
		sb.append(fullFields.get(0));
		for (int i = 1; i < fullFields.size(); i++) {
			sb.append(",");
			sb.append(fullFields.get(i));
		}
		return sb.toString();
	}
}
