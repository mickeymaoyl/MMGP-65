package nc.vo.mmgp.util.grand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.md.data.access.NCObject;
import nc.md.model.IAttribute;
import nc.md.model.IBean;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.pubapp.pattern.model.meta.entity.vo.PseudoColumnAttribute;
import nc.vo.pubapp.pattern.model.tool.VOTool;

/**
 * <b> 简要描述功能 </b> 主子孙后台数据与前台数据合并工具.
 * <p>
 * 重写原有逻辑，原有逻辑为统一处理则为:
 * <ul>
 * <li>如果前台有伪列而后台没有则用后台数据覆盖前台数据.
 * <li>如果前后台均有伪列则走伪列对照处理逻辑.
 * <li>如果前后台伪列均为空则走主键对照逻辑.
 * </ul>
 * 由于存在在后台增加子孙数据的可能所以修改现有逻辑，当前逻辑规则为:
 * <ul>
 * <li>遍历所有数据优先走伪列合并，后走主键合并，最后将没有<br>
 * 主键和伪列的数据当后台规则增加的数据直接加入到前台处理.<br>
 * <li>后台删除的数据不返回前台，前台需要根据VO状态进行数据过滤.
 * </ul>
 * 
 * @since 创建日期 May 20, 2013
 * @author wangweir
 */
public class MMGPGrandClientBillCombinServer<E extends IBill> {

    /**
     * 批量将后台传回前台的单据的内容合并到前台单据中
     * 
     * @param clientBills
     *        前台单据
     * @param serverBills
     *        台传回前台的单据
     */
    public void combine(E[] clientBills,
                        E[] serverBills) {
        int length = clientBills.length;
        for (int i = 0; i < length; i++) {
            this.combine(clientBills[i], serverBills[i]);
        }
    }

    private void combine(E clientBill,
                         E serverBill) {
        IBillMeta billMeta = clientBill.getMetaData();
        IVOMeta parentMeta = billMeta.getParent();
        VOTool tool = new VOTool();
        if (parentMeta != null) {
            tool.combine(clientBill.getParent(), serverBill.getParent());
        }
        IVOMeta[] children = billMeta.getChildren();
        if (children == null) {
            return;
        }
        this.newCombine(clientBill, serverBill);
    }

    /**
     * 合并子表数据.
     * 
     * @param clientData
     *        客户端数据，可以是ISuperVO , IBill.
     * @param serverData
     *        服务端数据，可以是ISuperVO , IBill.
     */
    private void newCombine(E clientBill,
                            E serverBill) {
        List<IAttribute> childAttrList = this.getChildAttr(clientBill);
        for (IAttribute attrTemp : childAttrList) {
            ISuperVO[] clientData = this.getChildData(clientBill, attrTemp);
            ISuperVO[] serverData = this.getChildData(serverBill, attrTemp);
            this.combineChildData(clientData, serverData);
        }
    }

    private ISuperVO[] combineChildData(ISuperVO[] clientData,
                                        ISuperVO[] serverData) {
        if (clientData == null || serverData == null) {
            return serverData;
        }
        ServerDataWrapper serverDataWrapper = new ServerDataWrapper(serverData);
        boolean isMatched = false;
        for (ISuperVO clientVOTemp : clientData) {
            ISuperVO serverVOTemp = serverDataWrapper.mathServerVO(clientVOTemp);
            if (serverVOTemp == null) {
                continue;
            }
            isMatched = true;
            this.combineVO(clientVOTemp, serverVOTemp);
        }
        // 如果一条数据也没有匹配成功，则用后台数据覆盖前台.
        if (!isMatched) {
            return serverData;
        }
        List<ISuperVO> aloneDataList = serverDataWrapper.getAloneDataList();
        if (aloneDataList.isEmpty()) {
            return clientData;
        }
        ISuperVO[] allNewData = this.combineAloneData(clientData, aloneDataList);
        // For GC.
        serverDataWrapper = null;
        return allNewData;
    }

    private void combineVO(ISuperVO clientVO,
                           ISuperVO serverVO) {
        MMGPGrandVOTool tool = new MMGPGrandVOTool();
        tool.combine(clientVO, serverVO);
        this.combineGCData(clientVO, serverVO);
    }

    private void combineGCData(ISuperVO clientVO,
                               ISuperVO serverVO) {
        List<IAttribute> childAttrList = this.getChildAttr(clientVO);
        for (IAttribute attrTemp : childAttrList) {
            ISuperVO[] clientData = this.getChildData(clientVO, attrTemp);
            ISuperVO[] serverData = this.getChildData(serverVO, attrTemp);
            ISuperVO[] combineData = this.combineChildData(clientData, serverData);
            this.setChildData(clientVO, combineData, attrTemp);
        }
    }

    private ISuperVO[] combineAloneData(ISuperVO[] clientData,
                                        List<ISuperVO> aloneDataList) {
        List<ISuperVO> allDataList = new ArrayList<ISuperVO>();
        List<ISuperVO> filterDataList = this.filterDeleteData(clientData);
        allDataList.addAll(filterDataList);
        allDataList.addAll(aloneDataList);
        return allDataList.toArray(new ISuperVO[0]);
    }

    private List<ISuperVO> filterDeleteData(ISuperVO[] clientData) {
        List<ISuperVO> filterDataList = new ArrayList<ISuperVO>();
        for (ISuperVO clientVO : clientData) {
            int voStatus = clientVO.getStatus();
            if (voStatus == VOStatus.DELETED) {
                continue;
            }
            filterDataList.add(clientVO);
        }
        return filterDataList;
    }

    private List<IAttribute> getChildAttr(Object data) {
        NCObject ncObj = NCObject.newInstance(data);
        IBean bean = ncObj.getRelatedBean();
        return MMGPGrandBusiEntityUtil.getInstance().queryChildAttr(bean);
    }

    private List<IAttribute> getChildAttr(ISuperVO vo) {
        return MMGPGrandBusiEntityUtil.getInstance().queryChildAttr(vo);
    }

    private ISuperVO[] getChildData(Object vo,
                                    IAttribute attr) {
        return (ISuperVO[]) attr.getAccessStrategy().getValue(vo, attr);
    }

    private void setChildData(ISuperVO parVO,
                              ISuperVO[] childVOs,
                              IAttribute attr) {
        attr.getAccessStrategy().setValue(parVO, attr, childVOs);
    }

    public class ServerDataWrapper {
        /** 伪列数据. */
        private Map<Integer, ISuperVO> pseudoColDataMap = null;

        /** 主键数据. */
        private Map<String, ISuperVO> primaryKeyDataMap = null;

        /** 不含伪列与主键数据. */
        private List<ISuperVO> aloneDataList = null;

        public ServerDataWrapper(ISuperVO[] serverData) {
            this.initData(serverData);
        }

        public ISuperVO mathServerVO(ISuperVO clientVO) {
            String name = PseudoColumnAttribute.PSEUDOCOLUMN;
            Object rowNo = clientVO.getAttributeValue(name);
            ISuperVO matchServerVO = null;
            // gaotx 2014-08-02 解决界面ts不同步的问题
            if (rowNo != null) {
                matchServerVO = this.matchRowNoData(rowNo);
            }
            if (matchServerVO == null) {
                Object primaryKey = clientVO.getPrimaryKey();
                matchServerVO = this.matchPrimaryKeyData(primaryKey);
            }
            // } else {
            // Object primaryKey = clientVO.getPrimaryKey();
            // matchServerVO = this.matchPrimaryKeyData(primaryKey);
            // }
            return matchServerVO;
        }

        private void initData(ISuperVO[] serverData) {
            String name = PseudoColumnAttribute.PSEUDOCOLUMN;
            for (ISuperVO voTemp : serverData) {
                Object rowNo = voTemp.getAttributeValue(name);
                if (rowNo != null) {
                    this.addRowNoData(rowNo, voTemp);
                    continue;
                }
                String primaryKey = voTemp.getPrimaryKey();
                if (primaryKey != null) {
                    this.addPrimaryKeyData(primaryKey, voTemp);
                    continue;
                }
                this.addAloneData(voTemp);
            }
        }

        private ISuperVO matchRowNoData(Object rowNo) {
            return this.getPseudoColDataMap().get(rowNo);
        }

        private ISuperVO matchPrimaryKeyData(Object primaryKey) {
            return this.getPrimaryKeyDataMap().get(primaryKey);
        }

        private void addRowNoData(Object rowNo,
                                  ISuperVO vo) {
            this.getPseudoColDataMap().put((Integer) rowNo, vo);
        }

        private void addPrimaryKeyData(String primaryKey,
                                       ISuperVO vo) {
            this.getPrimaryKeyDataMap().put(primaryKey, vo);
        }

        private void addAloneData(ISuperVO vo) {
            this.getAloneDataList().add(vo);
        }

        public Map<Integer, ISuperVO> getPseudoColDataMap() {
            if (this.pseudoColDataMap == null) {
                this.pseudoColDataMap = new HashMap<Integer, ISuperVO>();
            }
            return this.pseudoColDataMap;
        }

        public Map<String, ISuperVO> getPrimaryKeyDataMap() {
            if (this.primaryKeyDataMap == null) {
                this.primaryKeyDataMap = new HashMap<String, ISuperVO>();
            }
            return this.primaryKeyDataMap;
        }

        public List<ISuperVO> getAloneDataList() {
            if (this.aloneDataList == null) {
                this.aloneDataList = new ArrayList<ISuperVO>();
            }
            return this.aloneDataList;
        }
    }

}
