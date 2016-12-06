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
 * <b> 简要描述功能 </b>
 * <p>
 * 主子孙单据工具.
 * </p>
 * 
 * @see BillHelper
 * @since 创建日期 May 20, 2013
 * @author wangweir
 */
public class MMGPGrandBillHelper {

    /**
     * 主实体列表
     */
    private List<ISuperVO> parentList = new ArrayList<ISuperVO>();

    /**
     * 子实体的元数据和子实体列表的映射
     */
    private MapList<IVOMeta, ISuperVO> itemIndex = new MapList<IVOMeta, ISuperVO>();

    /**
     * 一主多子单据的辅助操作类默认构造函数
     * 
     * @param bills
     *        一主多子单据
     */
    public MMGPGrandBillHelper(IBill[] bills) {
        this.init(bills);
    }

    /**
     * 获取子实体的元数据和子实体列表的映射
     * 
     * @return 子实体的元数据和子实体列表的映射
     */
    public MapList<IVOMeta, ISuperVO> getItemIndex() {
        return this.itemIndex;
    }

    /**
     * 获取主实体列表
     * 
     * @return 主实体列表
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
                // 插入孙表.
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
