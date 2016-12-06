package nc.impl.mmgp.uif2.rule;

import java.util.ArrayList;
import java.util.List;

import nc.impl.pubapp.bd.userdef.UserDefCheckUtils;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.IVOMeta;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Nov 18, 2013
 * @author wangweir
 */
public class MMGPUserDefSaveRule<E extends AbstractBill> implements IRule<E> {

    private String[] prefixs;

    //
    private String[] ruleCodes;

    @SuppressWarnings("rawtypes")
    private Class[] voClasses;

    /**
     * 
     */
    public MMGPUserDefSaveRule() {
        super();
    }

    public MMGPUserDefSaveRule(@SuppressWarnings("rawtypes") Class[] voClasses) {
        super();
        this.voClasses = voClasses;
    }

    public MMGPUserDefSaveRule(String[] prefixs,
                               @SuppressWarnings("rawtypes") Class[] voClasses) {
        super();
        this.prefixs = prefixs;
        this.voClasses = voClasses;
    }

    /**
     * 单据可能包含多个实体，需要指定每个实体所引用自定义项的编码规则，以及字段前缀 注意几个数组参数中的元素位置要一一对应
     * 
     * @param ruleCodes
     *        自定义项规则编码
     * @param prefixs
     *        自定义项前缀
     * @param voClasses
     *        自定义项所在实体的class
     */
    public MMGPUserDefSaveRule(String[] ruleCodes,
                               String[] prefixs,
                               @SuppressWarnings("rawtypes") Class[] voClasses) {
        super();
        this.ruleCodes = ruleCodes;
        this.prefixs = prefixs;
        this.voClasses = voClasses;

    }

    @Override
    public void process(E[] vos) {
        if (MMArrayUtil.isEmpty(vos)) {
            return;
        }

        if (this.getVoClasses() == null) {
            @SuppressWarnings("rawtypes")
            Class[] voClass = this.getVoClassesInner(vos[0]);
            if (MMArrayUtil.isNotEmpty(voClass) && voClass.length <= 2) {
                // 此处<=2 是由UserDefCheckUtils决定的
                this.setVoClasses(voClass);
            }
        }

        if (this.getVoClasses() == null) {
            return;
        }

        UserDefCheckUtils.check(vos, this.ruleCodes, this.prefixs, this.voClasses);
    }

    /**
     * @param aggVO
     * @return
     */
    @SuppressWarnings("rawtypes")
    protected Class[] getVoClassesInner(E aggVO) {
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
     * @return the prefixs
     */
    public String[] getPrefixs() {
        return prefixs;
    }

    /**
     * @param prefixs
     *        the prefixs to set
     */
    public void setPrefixs(String[] prefixs) {
        this.prefixs = prefixs;
    }

    /**
     * @return the ruleCodes
     */
    public String[] getRuleCodes() {
        return ruleCodes;
    }

    /**
     * @param ruleCodes
     *        the ruleCodes to set
     */
    public void setRuleCodes(String[] ruleCodes) {
        this.ruleCodes = ruleCodes;
    }

    /**
     * @return the voClasses
     */
    @SuppressWarnings("rawtypes")
    public Class[] getVoClasses() {
        return voClasses;
    }

    /**
     * @param voClasses
     *        the voClasses to set
     */
    public void setVoClasses(@SuppressWarnings("rawtypes") Class[] voClasses) {
        this.voClasses = voClasses;
    }

}
