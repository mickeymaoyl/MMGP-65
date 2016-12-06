package nc.impl.mmgp.uif2;

import nc.bs.bd.baseservice.md.TreeBaseService;
import nc.bs.mmgp.common.CommonUtils;
import nc.itf.mmgp.uif2.IMMGPCmnTreeService;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IBean;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b>树型结构档案后台操作服务</b>
 * <p>
 * 实现新增、修改、删除和启用停用操作.
 * </p>
 * <p>
 * 创建日期:2013-5-9
 * </p>
 * 
 * @since NC V6.3
 * @author wangweiu
 */
public class MMGPCmnTreeService implements IMMGPCmnTreeService {

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends SuperVO> void cmnDeleteTreeVO(T vo) throws BusinessException {
        try {
            createTreeBaseService(vo).deleteVO(vo);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends SuperVO> T cmnInsertTreeVO(T vo) throws BusinessException {
        try {
            CommonUtils.setPk_org_v(vo);
            return createTreeBaseService(vo).insertVO(vo);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends SuperVO> T cmnUpdateTreeVO(T vo) throws BusinessException {
        try {
            return createTreeBaseService(vo).updateVO(vo);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends SuperVO> T cmnEnableTreeVO(T vo) throws BusinessException {
        try {
            return createTreeBaseService(vo).enableVO(vo);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends SuperVO> T cmnDisableTreeVO(T vo) throws BusinessException {
        try {
            return createTreeBaseService(vo).disableVO(vo);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * Creates the tree base service.
     * 
     * @param <T>
     *        the generic type
     * @param object
     *        the object
     * @return the tree base service
     * @throws BusinessException
     *         the business exception
     */
    protected <T extends SuperVO> TreeBaseService<T> createTreeBaseService(T object) throws BusinessException {
        String mdID = getIbean(object).getID();
        MMGPTreeBaseService<T> treeService = new MMGPTreeBaseService<T>(mdID, null);
        return treeService;
    }

    /**
     * Gets the ibean.
     * 
     * @param obj
     *        the obj
     * @return the ibean
     * @throws BusinessException
     *         the business exception
     */
    protected IBean getIbean(Object obj) throws BusinessException {
        return MDBaseQueryFacade.getInstance().getBeanByFullClassName(obj.getClass().getName());
    }

}
