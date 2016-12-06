package nc.ui.mmgp.uif2.validation;

import java.util.List;
import java.util.Map;

import nc.bs.uif2.validation.IValidationService;
import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.mmgp.util.RowDataUniqueCheckUtil;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

/**
 * <b> 前台表体多个字段唯一性校验 </b>
 * <p>
 * 注入uniqueCfgs
 * </p>
 * key为表体VO类名；<br>
 * value是唯一性字段;<br>
 * 
 * @since 创建日期 Jul 2, 2013
 * @author wangweir
 */
public class MMGPMultiBodyUniqueValidator implements IValidationService {

    /**
     * 表体唯一性校验字段配置：</p>key为表体VO类名；</p>value是唯一性字段
     */
    private Map<String, List<String>> uniqueCfgs;

    /*
     * (non-Javadoc)
     * @see nc.bs.uif2.validation.Validator#validate(java.lang.Object)
     */
    @Override
    public void validate(Object obj) throws ValidationException {
        if (!(obj instanceof AbstractBill)) {
            return;
        }

        List<ValidationFailure> failures = RowDataUniqueCheckUtil.check((AbstractBill) obj, uniqueCfgs);
        if (MMCollectionUtil.isEmpty(failures)) {
            return;
        }

        // focus item
        // BillTabVO[] tabVOs =
        // this.getBillform().getBillCardPanel().getBillData().getBillTabVOsByPosition(
        // Integer.valueOf(IBillItem.BODY));
        //
        // for (BillTabVO tabVO : tabVOs) {
        // tabVO.getMetadataclass()
        // }

        throw new ValidationException(failures);
    }

    /**
     * @return the uniqueCfgs
     */
    public Map<String, List<String>> getUniqueCfgs() {
        return uniqueCfgs;
    }

    /**
     * @param uniqueCfgs
     *        the uniqueCfgs to set
     */
    public void setUniqueCfgs(Map<String, List<String>> uniqueCfgs) {
        this.uniqueCfgs = uniqueCfgs;
    }

}
