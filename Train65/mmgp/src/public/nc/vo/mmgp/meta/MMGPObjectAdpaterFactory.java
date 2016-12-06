package nc.vo.mmgp.meta;

import java.util.Map;

import nc.bs.uif2.BusinessExceptionAdapter;
import nc.md.MDBaseQueryFacade;
import nc.md.data.access.NCObject;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.vo.bd.meta.IBDObject;
import nc.vo.bd.meta.IBDObjectAdapterFactory;
import nc.vo.bd.meta.NCObject2BDObjectAdapter;

/**
 * @Description:  实现从元数据的 IBusiInfo 接口到 IBDObject 的适配器工厂；
    <p>
          详细功能描述
    </p>
 * @data:2014-5-23上午9:49:04
 * @author: tangxya
 */
public class MMGPObjectAdpaterFactory implements IBDObjectAdapterFactory{

	IBean bean=null;
	public MMGPObjectAdpaterFactory(String className ){
		try {
			 bean = MDBaseQueryFacade.getInstance().getBeanByFullClassName(className);
		} catch (MetaDataException e) {
			throw new BusinessExceptionAdapter(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see nc.vo.bd.meta.IBDObjectAdapterFactory#createBDObject(java.lang.Object)
	 */
	@Override
	public IBDObject createBDObject(Object obj) {
		if(obj == null)
			return null;
		
		if(obj instanceof String)
			return null;	
		
		if(bean==null)
			return null;
		
		Map<String, String> name_path_map = ((IBusinessEntity)bean).getBizInterfaceMapInfo(IBDObject.class.getName());
		return new NCObject2BDObjectAdapter(NCObject.newInstance(obj), name_path_map);
	}

}
