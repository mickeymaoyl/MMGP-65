package nc.ui.mmgp.uif2.meta;

import java.util.Map;

import nc.bs.uif2.BusinessExceptionAdapter;
import nc.md.MDBaseQueryFacade;
import nc.md.data.access.NCObject;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.vo.bd.meta.BDObjectAdpaterFactory;
import nc.vo.bd.meta.IBDObject;
import nc.vo.bd.meta.NCObject2BDObjectAdapter;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * 扩展自uap，增加了aggVO的支持
 * 
 * @author wangweiu
 */
public class MMGPCommonVOMetaBDObjectAdapterFactory extends BDObjectAdpaterFactory {

    @Override
    public IBDObject createBDObject(Object obj) {
        if (obj == null || obj instanceof String) {
            return null;
        }

        if (obj instanceof AggregatedValueObject) {
            AggregatedValueObject avo = (AggregatedValueObject) obj;
            return createSingleObject(avo.getParentVO());
        }

        // 单表支持递归搜索元数据 modified by liwsh 2014-04-23 用于项目BOM根据子类获取IBDObject
        return createSingleObject(obj);
    }

    private IBDObject createSingleObject(Object obj) {

        if (obj == null) return null;

        if (obj instanceof String) return null;

        // 递归搜索父类所对应的元数据，以支持继承
        try {
            String className = obj.getClass().getName();
            IBean bean = null;
            while (bean == null) {
                bean = MDBaseQueryFacade.getInstance().getBeanByFullClassName(className);
                if (bean != null) {
                    break;
                }
                Class superClass = Class.forName(className).getSuperclass();
                if (superClass == null) {
                    break;
                }
                className = superClass.getName();
            }

            if (bean == null) return null;
            
            Map<String, String> name_path_map =
                    ((IBusinessEntity) bean).getBizInterfaceMapInfo(IBDObject.class.getName());
            return new NCObject2BDObjectAdapter(NCObject.newInstance(obj), name_path_map);
        } catch (nc.md.model.MetaDataException e) {
            throw new BusinessExceptionAdapter(e);
        } catch (ClassNotFoundException e) {
            nc.vo.pubapp.pattern.exception.ExceptionUtils.wrappException(e);
            return null;
        }
    }

}
