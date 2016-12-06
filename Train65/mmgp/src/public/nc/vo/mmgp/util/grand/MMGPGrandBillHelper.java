package nc.vo.mmgp.util.grand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.pubapp.pattern.model.tool.BillHelper;
import nc.vo.pubapp.pattern.pub.MapList;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * �����ﵥ�ݹ���.
 * </p>
 * 
 * @see BillHelper
 * @since �������� May 20, 2013
 * @author wangweir
 */
public class MMGPGrandBillHelper {

    /**
     * ��ʵ���б�
     */
    private List<ISuperVO> parentList = new ArrayList<ISuperVO>();

    /**
     * ��ʵ���Ԫ���ݺ���ʵ���б��ӳ��
     */
    private MapList<IVOMeta, ISuperVO> itemIndex = new MapList<IVOMeta, ISuperVO>();

    /**
     * һ�����ӵ��ݵĸ���������Ĭ�Ϲ��캯��
     * 
     * @param bills
     *        һ�����ӵ���
     */
    public MMGPGrandBillHelper(IBill[] bills) {
        this.init(bills);
    }

    /**
     * ��ȡ��ʵ���Ԫ���ݺ���ʵ���б��ӳ��
     * 
     * @return ��ʵ���Ԫ���ݺ���ʵ���б��ӳ��
     */
    public MapList<IVOMeta, ISuperVO> getItemIndex() {
        return this.itemIndex;
    }

    /**
     * ��ȡ��ʵ���б�
     * 
     * @return ��ʵ���б�
     */
    public List<ISuperVO> getParentList() {
        return this.parentList;
    }

    private void init(IBill[] bills) {
        int length = bills.length;

        IBillMeta billMeta = bills[0].getMetaData();
        IVOMeta[] childMetas = billMeta.getChildren();

        for (int i = 0; i < length; i++) {
            ISuperVO header = bills[i].getParent();
            if (header == null) {
                continue;
            }
            this.parentList.add(header);

            for (IVOMeta voMeta : childMetas) {
                ISuperVO[] items = bills[i].getChildren(voMeta);
                if (items == null) {
                    continue;
                }
                // �������.
                List<ISuperVO> allVOList = new ArrayList<ISuperVO>();
                MMGPGrandBillUtil.getInstance().getAllChildVOs(items, allVOList);
                allVOList.addAll(Arrays.asList(items));
                for (ISuperVO item : allVOList) {
                    this.itemIndex.put(item.getMetaData(), item);
                }
            }
        }
    }
}
