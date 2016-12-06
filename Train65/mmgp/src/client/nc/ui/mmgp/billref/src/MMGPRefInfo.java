package nc.ui.mmgp.billref.src;

import java.util.List;

import nc.md.MDBaseQueryFacade;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.md.model.IComponent;
import nc.md.model.MetaDataException;
import nc.ui.pubapp.billref.src.RefInfo;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.CarrierRuntimeException;
import nc.vo.pubapp.pattern.pub.Constructor;

/**
 * 
 * @author wangrra
 *
 */
public class MMGPRefInfo extends RefInfo {
	
	private String tabCode;
		
	@Override
	public SuperVO getHeadVO() {
		SuperVO superVO = super.getHeadVO();

		if (superVO != null) {
			return superVO;
		}

		List<IBusinessEntity> entities = getBusinessEntities();
		IBusinessEntity primary = getPrimaryBusinessEntity(entities);
		if(primary != null){
			return (SuperVO) getSuperVO(getClassByName(primary
					.getFullClassName()));
		}
		return null;
	}

	@Override
	public SuperVO getBodyVO() {

		SuperVO bodyVO = super.getBodyVO();

		if (bodyVO != null) {
			return bodyVO;
		}

		List<IBusinessEntity> entities = getBusinessEntities();
		IBusinessEntity childEntity = getSpecifiedBusinessEntity(entities, getTabCode());
		
		if(childEntity != null){
			return (SuperVO) getSuperVO(getClassByName(childEntity
					.getFullClassName()));
		}

		return null;

	}

	private IBusinessEntity getSpecifiedBusinessEntity(List<IBusinessEntity> entities, String tabCode){
		for (IBusinessEntity entity : entities) {
			String fullName = entity.getFullName();
			if(tabCode == null){
				return entity;
			}
			if(fullName.contains(tabCode)){
				return entity;
			}

		}
		return null;
	}
	
	private IBusinessEntity getPrimaryBusinessEntity(List<IBusinessEntity> entities){
		for (IBusinessEntity entity : entities) {
			if (entity.isPrimary()) {
				return entity;
			}
		}
		return null;
	}
	
	private List<IBusinessEntity> getBusinessEntities() {
		String aggClsName = getBillVO().getClass().getName();
		IBean bean;

		try {
			bean = MDBaseQueryFacade.getInstance().getBeanByFullClassName(
					aggClsName);
		} catch (MetaDataException e) {
			throw new CarrierRuntimeException(e);
		}

		IComponent component = (IComponent) bean.getContainer();
		List<IBusinessEntity> entities = component.getBusinessEntities();
		return entities;
	}

	@SuppressWarnings("unchecked")
	protected Class<ISuperVO> getClassByName(String classFullName) {
		try {
			return (Class<ISuperVO>) Class.forName(classFullName);
		} catch (ClassNotFoundException e) {
			throw new CarrierRuntimeException(e);
		}
	}

	protected ISuperVO getSuperVO(Class<? extends ISuperVO> parentClass) {
		ISuperVO vo = Constructor.construct(parentClass);
		return vo;
	}

	public String getTabCode() {
		return tabCode;
	}

	public void setTabCode(String tabCode) {
		this.tabCode = tabCode;
	}

}
