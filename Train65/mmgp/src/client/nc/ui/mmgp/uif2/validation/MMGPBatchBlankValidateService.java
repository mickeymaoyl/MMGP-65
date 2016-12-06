package nc.ui.mmgp.uif2.validation;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pubapp.uif2app.model.DefaultBatchValidateService;
import nc.ui.pubapp.uif2app.view.value.AbstractBlankChildrenFilter;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 批修改空行过滤器
 * </p>
 * 
 * @since 创建日期 May 27, 2013
 * @author wangweir
 */
public class MMGPBatchBlankValidateService extends DefaultBatchValidateService {

    /** 子表空行过滤器. */
    private AbstractBlankChildrenFilter blankChildrenFilter;

    public void setBlankChildrenFilter(AbstractBlankChildrenFilter nullChildrenJudgment) {
        this.blankChildrenFilter = nullChildrenJudgment;
    }

    public AbstractBlankChildrenFilter getBlankChildrenFilter() {
        return this.blankChildrenFilter;
    }

    @Override
    public int[] unNecessaryData(List<Object> rows) {
        if (null != this.getBlankChildrenFilter()) {
            List<Integer> setIndex = new ArrayList<Integer>();
            int i = 0;
            for (Object rowdata : rows) {
                // 过滤空行
                if (this.getBlankChildrenFilter().isBlank(
                    this.getEditor().getBillCardPanel(),
                    i,
                    (CircularlyAccessibleValueObject) rowdata)) {
                    setIndex.add(i);
                }
                i++;
            }
            i = 0;
            int[] iresult = new int[setIndex.size()];
            for (Integer irow : setIndex) {
                iresult[i++] = irow;
            }
            return iresult;
        }
        return null;

    }

}
