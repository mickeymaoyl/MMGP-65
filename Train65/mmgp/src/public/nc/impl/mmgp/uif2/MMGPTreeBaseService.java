package nc.impl.mmgp.uif2;

import nc.bs.bd.baseservice.md.ITreeUpdateWithChildren;
import nc.bs.bd.baseservice.md.TreeBaseService;
import nc.bs.bd.baseservice.validator.BDTreeParentEnableValidator;
import nc.bs.bd.baseservice.validator.BDTreeUpdateLoopValidator;
import nc.bs.businessevent.EventDispatcher;
import nc.bs.businessevent.IEventType;
import nc.bs.businessevent.UsePermChangeEvent;
import nc.bs.uif2.validation.Validator;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IBean;
import nc.md.model.MetaDataException;
import nc.vo.bd.pub.DistributedAddBaseValidator;
import nc.vo.bd.pub.SingleDistributedUpdateValidator;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.util.BDUniqueRuleValidate;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��������־û�������.���ͨ��У�����
 * </p>
 * 
 * @since �������� May 27, 2013
 * @author wangweir
 */
public class MMGPTreeBaseService<T extends SuperVO> extends TreeBaseService<T> {

    /**
     * 
     */
    private static final String ENABLESTATE = "enablestate";

    /**
     * @param mdID
     * @param subAttributeNames
     */
    public MMGPTreeBaseService(String mdID,
                               String[] subAttributeNames) {
        super(mdID, subAttributeNames);
    }

    /*
     * (non-Javadoc)
     * @see nc.bs.bd.baseservice.md.TreeBaseService#getInsertValidator()
     */
    @Override
    protected Validator[] getInsertValidator() {
        // �ϼ�ͣ��У��
        BDTreeParentEnableValidator parentValidator = new BDTreeParentEnableValidator(null);

        IBean bean = null;
        try {
            bean = MDBaseQueryFacade.getInstance().getBeanByID(getMDId());
        } catch (MetaDataException e) {
            ExceptionUtils.wrappException(e);
        }
        Validator[] validators;
        if (bean != null && bean.getAttributeByName(ENABLESTATE) != null) {
            validators =
                    new Validator[]{new BDUniqueRuleValidate(), new DistributedAddBaseValidator(), parentValidator };
        } else {
            validators = new Validator[]{new BDUniqueRuleValidate(), new DistributedAddBaseValidator() };
        }
        return validators;
    }

    /*
     * (non-Javadoc)
     * @see nc.bs.bd.baseservice.md.TreeBaseService#getUpdateValidator(nc.vo.pub.SuperVO)
     */
    @Override
    protected Validator[] getUpdateValidator(T oldVO) {
        // ���¼�ѭ������У��
        BDTreeUpdateLoopValidator loopValidator = new BDTreeUpdateLoopValidator();
        // �ϼ�ͣ��У��
        BDTreeParentEnableValidator parentValidator = new BDTreeParentEnableValidator(oldVO);

        IBean bean = null;
        try {
            bean = MDBaseQueryFacade.getInstance().getBeanByID(getMDId());
        } catch (MetaDataException e) {
            ExceptionUtils.wrappException(e);
        }
        Validator[] validators;
        if (bean != null && bean.getAttributeByName(ENABLESTATE) != null) {
            validators =
                    new Validator[]{
                        new BDUniqueRuleValidate(),
                        new SingleDistributedUpdateValidator(),
                        loopValidator,
                        parentValidator };
        } else {
            validators =
                    new Validator[]{new BDUniqueRuleValidate(), new SingleDistributedUpdateValidator(), loopValidator };
        }

        return validators;
    }

    /*
     * (non-Javadoc)
     * @see nc.bs.bd.baseservice.md.TreeBaseService#deleteVO(nc.vo.pub.SuperVO)
     */
    @Override
    public void deleteVO(T vo) throws BusinessException {
        super.deleteVO(vo);
        // ����Ȩ�ޱ�������¼�
        EventDispatcher.fireEvent(new UsePermChangeEvent(this.getMDId(), IEventType.TYPE_DELETE_AFTER));
    }

    /*
     * (non-Javadoc)
     * @see nc.bs.bd.baseservice.md.TreeBaseService#insertVO(nc.vo.pub.SuperVO)
     */
    @Override
    public T insertVO(T vo) throws BusinessException {
        T result = super.insertVO(vo);
        // ����Ȩ�ޱ�������¼�
        EventDispatcher.fireEvent(new UsePermChangeEvent(this.getMDId(), IEventType.TYPE_INSERT_AFTER));
        return result;
    }

    /*
     * (non-Javadoc)
     * @see nc.bs.bd.baseservice.md.TreeBaseService#insertVOs(T[])
     */
    @Override
    public void insertVOs(T[] vos) throws BusinessException {
        super.insertVOs(vos);
        // ����Ȩ�ޱ�������¼�
        EventDispatcher.fireEvent(new UsePermChangeEvent(this.getMDId(), IEventType.TYPE_INSERT_AFTER));
    }

    /*
     * (non-Javadoc)
     * @see nc.bs.bd.baseservice.md.TreeBaseService#updateVO(nc.vo.pub.SuperVO)
     */
    @Override
    public T updateVO(T vo) throws BusinessException {
        T result = super.updateVO(vo);
        // ����Ȩ�ޱ�������¼�
        EventDispatcher.fireEvent(new UsePermChangeEvent(this.getMDId(), IEventType.TYPE_UPDATE_AFTER));
        return result;
    }

    /*
     * (non-Javadoc)
     * @see nc.bs.bd.baseservice.md.TreeBaseService#updateVOWithSub(nc.vo.pub.SuperVO,
     * nc.bs.bd.baseservice.md.ITreeUpdateWithChildren)
     */
    @Override
    public T updateVOWithSub(T vo,
                             ITreeUpdateWithChildren<T> updatWithChildren) throws BusinessException {
        T result = super.updateVOWithSub(vo, updatWithChildren);
        // ����Ȩ�ޱ�������¼�
        EventDispatcher.fireEvent(new UsePermChangeEvent(this.getMDId(), IEventType.TYPE_UPDATE_AFTER));
        return result;
    }

    /*
     * (non-Javadoc)
     * @see nc.bs.bd.baseservice.md.TreeBaseService#enableVO(nc.vo.pub.SuperVO)
     */
    @Override
    public T enableVO(T vo) throws BusinessException {
        T result = super.enableVO(vo);
        // ����Ȩ�ޱ�������¼�
        EventDispatcher.fireEvent(new UsePermChangeEvent(this.getMDId(), IEventType.TYPE_UPDATE_AFTER));
        return result;
    }

    /*
     * (non-Javadoc)
     * @see nc.bs.bd.baseservice.md.TreeBaseService#disableVO(nc.vo.pub.SuperVO)
     */
    @Override
    public T disableVO(T vo) throws BusinessException {
        T result = super.disableVO(vo);
        // ����Ȩ�ޱ�������¼�
        EventDispatcher.fireEvent(new UsePermChangeEvent(this.getMDId(), IEventType.TYPE_UPDATE_AFTER));
        return result;
    }

}
