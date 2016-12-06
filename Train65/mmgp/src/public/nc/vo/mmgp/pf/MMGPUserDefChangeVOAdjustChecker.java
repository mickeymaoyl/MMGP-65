package nc.vo.mmgp.pf;

import java.util.ArrayList;
import java.util.List;

import nc.impl.pubapp.bd.userdef.UserDefCheckUtils;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pf.change.ChangeVOAdjustContext;
import nc.vo.pf.change.IChangeVOAdjust;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.IVOMeta;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * Ĭ�ϸ����ļ����ֻ֧��Ŀ�ĵ���ʱ���ӱ�ĳ���������ֻ��һ���ӱ�
 * ����ӱ���Ҫ����getVoClasses��getPrefixs��getRuleCodes����������
 * ������������������ֵ��˳�����еģ�����һһ��Ӧ��
 * </p>
 * 
 * @since �������� Oct 29, 2013
 * @author wangweir
 */
public class MMGPUserDefChangeVOAdjustChecker implements IChangeVOAdjust {

    /*
     * (non-Javadoc)
     * @see nc.vo.pf.change.IChangeVOAdjust#adjustBeforeChange(nc.vo.pub.AggregatedValueObject,
     * nc.vo.pf.change.ChangeVOAdjustContext)
     */
    @Override
    public AggregatedValueObject adjustBeforeChange(AggregatedValueObject srcVO,
                                                    ChangeVOAdjustContext adjustContext) throws BusinessException {
        return srcVO;
    }

    /*
     * (non-Javadoc)
     * @see nc.vo.pf.change.IChangeVOAdjust#adjustAfterChange(nc.vo.pub.AggregatedValueObject,
     * nc.vo.pub.AggregatedValueObject, nc.vo.pf.change.ChangeVOAdjustContext)
     */
    @Override
    public AggregatedValueObject adjustAfterChange(AggregatedValueObject srcVO,
                                                   AggregatedValueObject destVO,
                                                   ChangeVOAdjustContext adjustContext) throws BusinessException {
        AggregatedValueObject[] destVOs =
                this.batchAdjustAfterChange(
                    new AggregatedValueObject[]{srcVO },
                    new AggregatedValueObject[]{destVO },
                    adjustContext);
        if (MMArrayUtil.isEmpty(destVOs)) {
            return null;
        }
        return destVOs[0];
    }

    /*
     * (non-Javadoc)
     * @see nc.vo.pf.change.IChangeVOAdjust#batchAdjustBeforeChange(nc.vo.pub.AggregatedValueObject[],
     * nc.vo.pf.change.ChangeVOAdjustContext)
     */
    @Override
    public AggregatedValueObject[] batchAdjustBeforeChange(AggregatedValueObject[] srcVOs,
                                                           ChangeVOAdjustContext adjustContext)
            throws BusinessException {
        return srcVOs;
    }

    /*
     * (non-Javadoc)
     * @see nc.vo.pf.change.IChangeVOAdjust#batchAdjustAfterChange(nc.vo.pub.AggregatedValueObject[],
     * nc.vo.pub.AggregatedValueObject[], nc.vo.pf.change.ChangeVOAdjustContext)
     */
    @Override
    public AggregatedValueObject[] batchAdjustAfterChange(AggregatedValueObject[] srcVOs,
                                                          AggregatedValueObject[] destVOs,
                                                          ChangeVOAdjustContext adjustContext)
            throws BusinessException {
        this.checkUserDef(destVOs);
        return destVOs;
    }

    /**
     * @param vos
     */
    protected void checkUserDef(AggregatedValueObject[] vos) {
        if (MMArrayUtil.isEmpty(vos)) {
            return;
        }
        UserDefCheckUtils.check(
            (AbstractBill[]) vos,
            this.getRuleCodes(vos),
            this.getPrefixs(vos),
            this.getVoClasses((AbstractBill) vos[0]));
    }

    /**
     * @param aggVO
     * @return
     */
    @SuppressWarnings("rawtypes")
    protected Class[] getVoClasses(AbstractBill aggVO) {
        IBillMeta billMeta = aggVO.getMetaData();
        IVOMeta parentVOMeta = billMeta.getParent();
        List<Class> voClasses = new ArrayList<Class>();
        if (parentVOMeta != null) {
            Class parentClass = billMeta.getVOClass(parentVOMeta);
            voClasses.add(parentClass);
        }
        IVOMeta[] childrenVOMetas = billMeta.getChildren();
        if (MMArrayUtil.isNotEmpty(childrenVOMetas)) {
            for (IVOMeta childrenVOMeta : childrenVOMetas) {
                Class childClass = billMeta.getVOClass(childrenVOMeta);
                voClasses.add(childClass);
            }
        }

        return voClasses.toArray(new Class[0]);
    }

    /**
     * UserDefCheckUtils���ṩĬ��ֵ��Ĭ��ֵΪ��vdef�� vbdef.�������������Ĭ��ֵ�������. Ŀ�ĵ���Ϊ����ӱ�����Ҫ���⴦��
     * 
     * @param vos
     * @return
     */
    protected String[] getPrefixs(AggregatedValueObject[] vos) {
        return null;
    }

    /**
     * @param vos
     * @return
     */
    protected String[] getRuleCodes(AggregatedValueObject[] vos) {
        return null;
    }

}
