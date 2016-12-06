package nc.ui.mmgp.uif2.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nc.bs.uif2.validation.IBatchValidationService;
import nc.bs.uif2.validation.ValidationException;
import nc.vo.mmgp.util.MMCollectionUtil;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 May 27, 2013
 * @author wangweir
 */
public class MMGPCompositeValidation implements IBatchValidationService {

    private List<IBatchValidationService> validators = new ArrayList<IBatchValidationService>();

    /*
     * (non-Javadoc)
     * @see nc.bs.uif2.validation.IValidationService#validate(java.lang.Object)
     */
    @Override
    public void validate(Object obj) throws ValidationException {
        for (IBatchValidationService validator : this.getValidators()) {
            validator.validate(obj);
        }
    }

    /*
     * (non-Javadoc)
     * @see nc.bs.uif2.validation.IBatchValidationService#unNecessaryData(java.util.List)
     */
    @Override
    public int[] unNecessaryData(List<Object> rows) {
        Set<Integer> unNecessaryDatas = new HashSet<Integer>();
        for (IBatchValidationService validator : this.getValidators()) {
            int[] oneUnNecessaryRows = validator.unNecessaryData(rows);
            if (oneUnNecessaryRows == null || oneUnNecessaryRows.length == 0) {
                continue;
            }

            for (int row : oneUnNecessaryRows) {
                unNecessaryDatas.add(row);
            }
        }

        if (MMCollectionUtil.isEmpty(unNecessaryDatas)) {
            return null;
        }

        int[] allUnNecessaryRows = new int[unNecessaryDatas.size()];
        int i = 0;
        Iterator<Integer> iter = unNecessaryDatas.iterator();
        while (iter.hasNext()) {
            allUnNecessaryRows[i] = iter.next();
            i++;
        }

        return allUnNecessaryRows;
    }

    /**
     * @return the validators
     */
    public List<IBatchValidationService> getValidators() {
        return validators;
    }

    /**
     * @param validators
     *        the validators to set
     */
    public void setValidators(List<IBatchValidationService> validators) {
        this.validators.clear();
        if (MMCollectionUtil.isNotEmpty(validators)) {
            this.validators.addAll(validators);
        }
    }

}
