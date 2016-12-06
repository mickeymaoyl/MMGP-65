package nc.ui.mmgp.uif2.validation;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pubapp.uif2app.model.DefaultBatchValidateService;
import nc.ui.pubapp.uif2app.view.value.AbstractBlankChildrenFilter;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ���޸Ŀ��й�����
 * </p>
 * 
 * @since �������� May 27, 2013
 * @author wangweir
 */
public class MMGPBatchBlankValidateService extends DefaultBatchValidateService {

    /** �ӱ���й�����. */
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
                // ���˿���
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
