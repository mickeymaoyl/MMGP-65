package nc.ui.mmgp.flexgant.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.tree.TreePath;

import nc.bs.logging.Logger;
import nc.ui.mmgp.flexgant.listener.ITreeTableModelDecimalListener;
import nc.ui.mmgp.flexgant.policy.MMGPNodeInsertCommand;
import nc.ui.mmgp.flexgant.treetable.MMGPGantItem;
import nc.ui.mmgp.flexgant.treetable.MMGPMetaDataGetGantModelRelationItemValue;
import nc.ui.mmgp.flexgant.view.timelineobject.MMGPDefaultTimelineObjectCreator;
import nc.ui.mmgp.flexgant.view.timelineobject.MMGPTimelineObject;
import nc.ui.mmgp.flexgant.view.treetable.MMGPGanttChartNode;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.beans.constenum.IConstEnum;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.bill.MetaDataGetBillRelationItemValue;
import nc.ui.pubapp.gantt.model.AppGanttChartModel;
import nc.ui.pubapp.gantt.model.AppGanttChartNode;
import nc.ui.pubapp.gantt.model.AppTimelineObject;
import nc.ui.pubapp.gantt.model.IAppTimelineObjectCreateStrategy;
import nc.ui.pubapp.gantt.model.IValueAttributeAdapter;
import nc.ui.pubapp.gantt.ui.treetable.GantItem;
import nc.ui.pubapp.gantt.ui.treetable.IGantItem;
import nc.ui.pubapp.gantt.ui.util.AppGanttDataFormatUtil;
import nc.vo.bill.pub.BillUtil;
import nc.vo.mmgp.util.MMMapUtil;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import com.dlsc.flexgantt.command.ICommand;
import com.dlsc.flexgantt.model.TimeSpan;
import com.dlsc.flexgantt.model.dateline.DatelineModelException;
import com.dlsc.flexgantt.model.gantt.DefaultRelationship;
import com.dlsc.flexgantt.model.gantt.ILayer;
import com.dlsc.flexgantt.model.gantt.IRelationship;
import com.dlsc.flexgantt.model.gantt.Layer;
import com.dlsc.flexgantt.model.gantt.RelationshipType;
import com.dlsc.flexgantt.policy.treetable.INodeEditPolicy;

/**
 * @Description: 基于对象的甘特图模型
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-5-19下午5:07:47
 * @author: tangxya
 */
@SuppressWarnings({"unchecked", "rawtypes" })
public class MMGPGantChartModel extends AppGanttChartModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8759626252961982128L;

    /**
	 * 
	 */
    private IMMGPGantLayerFactory layerFactory;

    private MMGPGantContext context;

    protected MMGPGanttChartNode root = (MMGPGanttChartNode) this.getRoot();

    protected boolean isAddData = false;

    private IAppTimelineObjectCreateStrategy appTimelineObjectCreateStategy;

    private IVOCreateStrategy voCreateSrategy;

    public MMGPGantContext getContext() {
        return context;
    }

    protected ILayer gantLayer;

    public ILayer getGantLayer() {
        if (gantLayer == null) {
            gantLayer = new Layer("gant layer");
        }
        return gantLayer;
    }

    public void setContext(MMGPGantContext context) {
        this.context = context;
        init();
    }

    private void init() {
        if (getRoot() != null) {
            ((MMGPGanttChartNode) getRoot()).setContext(this.getContext());
        }
    }

    public MMGPGantChartModel(MMGPGantContext context) {
        super(new MMGPGanttChartNode(), context);
        this.context = context;
        /* initTreeTableEditor(); */
    }

    public MMGPGantChartModel(MMGPGanttChartNode node,
                              MMGPGantContext context) {
        super(node, context);
        this.context = context;
        /* initTreeTableEditor(); */
    }

    public IMMGPGantLayerFactory getLayerFactory() {
        if (layerFactory == null) {
            layerFactory = MMGPDafaultGantLayerFactory.getInstance();
        }
        return layerFactory;
    }

    public void setLayerFactory(IMMGPGantLayerFactory layerFactory) {
        this.layerFactory = layerFactory;
    }

    protected MMGPGanttChartNode creatChartNode() {
        return new MMGPGanttChartNode();
    }

    protected MMGPGanttChartNode creatChartNode(Object userObject,
                                                MMGPGantContext context) {
        return new MMGPGanttChartNode(userObject, context);
    }

    public void initModel(Map<Object, List<Object>> dataMap) {
        if (root == null) {
            root = creatChartNode();
        }
        if (!isAddData) {
            clear();
        }
        if (dataMap == null || dataMap.size() == 0) {
            return;
        }
        // 设置左边树的值
        Map<String, MMGPGanttChartNode> keyMap = initTreeTableData(dataMap);
        // 设置右边图的值
        initLayerContainerData(dataMap, keyMap);
        nodeStructureChanged(root);
        getContext().getGantchart().getTreeTable().revalidate();
        // Runnable callRevalidate = new Runnable() {
        // public void run() {
        getContext().getGantchart().getTreeTable().repaint();
        // ;
        // }
        // };
        // SwingUtilities.invokeLater(callRevalidate);

        getContext().getGantchart().getTreeTable().expandAll();
    }

    /**
     * @param dataMap
     * @param nodekeyMap
     * 
     * 初始化甘特图右侧时间线
     * 
     * 增加设置焦点对准时间线逻辑　
     */
    protected void initLayerContainerData(Map<Object, List<Object>> dataMap,
                                          Map<String, MMGPGanttChartNode> nodekeyMap) {
        UFDate start = new UFDate().getDateBefore(60);
        UFDate end = start.getDateAfter(365);

        List<AppTimelineObject> timeobjlists = new ArrayList<AppTimelineObject>();
        for (MMGPGanttChartNode node : nodekeyMap.values()) {
            Object userObject = node.getTypedUserObject();
            if( dataMap.get(userObject) == null){
            	continue;
            }
            for (Object obj : dataMap.get(userObject)) {
                MMGPTimelineObject timelineObject =
                        (MMGPTimelineObject) getAppTimelineObjectCreateStategy().createAppTimelineObject(
                            obj,
                            getContext());
                node.addTimelineObject(getLayerFactory().createLayer(timelineObject, this), timelineObject);
                timeobjlists.add(timelineObject);
                if (timelineObject.getTimeSpan().isUndefined()) {
                    continue;
                }
                UFDate curStart = UFDate.getDate(timelineObject.getTimeSpan().getStartTime()).getDateBefore(60);
                UFDate curEnd = UFDate.getDate(timelineObject.getTimeSpan().getEndTime()).getDateAfter(60);
                if (start.compareTo(curStart) > 0) {
                    start = curStart;
                }
                if (end.compareTo(curEnd) < 0) {
                    end = curEnd;
                }
            }
        }
        getContext().getGantchart().setTimeSpan(new TimeSpan(start.toDate(), end.toDate()));
        try {
            getContext()
                .getGantchart()
                .getDatelineModel()
                .requestVisibleTimeSpan(
                    new TimeSpan(start.getDateAfter(55).toDate(), start.getDateAfter(80).toDate()));
        } catch (DatelineModelException e) {
            ExceptionUtils.wrappException(e);
        }
        if (getContext().isNeedRelationShip()) {
            initRelationship(timeobjlists);
        }

    }

    @Override
    public void updateNodeValue(AppGanttChartNode node) {
        IValueAttributeAdapter valueAdapter = getContext().getNodeValueAdapter();
        IGantItem[] items = getContext().getItemContainer().getAllItems();
        if (items == null || items.length == 0) return;
        Object userObj = node.getTypedUserObject();
        for (IGantItem item : items) {

            Object attrValue = valueAdapter.getAttributeValue(userObj, item.getKey());
            Object attributeValue = formatItemValue(attrValue, item);
            if (item.isKeyValue()) {
                setKey(node, attributeValue);
                node.setKeyEditable(item.isEdit());
            } else {
                // tangxya start
                MMGPGantItem gantItem = (MMGPGantItem) item;
                // 先设置精度
                setBillItemDecimalByType(node, gantItem);
                // 转换值
                attributeValue = gantItem.getConverter().convertToBillItem(gantItem.getDataType(), attributeValue);
                // tangxya end
                setColumnValue(node, attributeValue, item.getShowOrder());
                node.setValueEditable(item.getShowOrder(), item.isEdit());

            }

        }
        // 加载元数据关联项公式 暂时先不加载 单据设置node的值需要加载
        /* loadLoadRelationItemValue(null, (MMGPGanttChartNode) node); */
    }

    public void loadLoadRelationItemValue(String itemkey,
                                          MMGPGanttChartNode node) {
        loadLoadRelationItemValue(new String[]{itemkey }, node);

    }

    public void loadLoadRelationItemValue(String[] itemkeys,
                                          MMGPGanttChartNode node) {
        if (node.getKey() instanceof String) {
            Map<String, MMGPGanttChartNode> nodeMap = new HashMap<String, MMGPGanttChartNode>();
            nodeMap.put((String) node.getKey(), node);
            loadLoadRelationItemValue(itemkeys, nodeMap);
        }

    }

    private Object formatItemValue(Object attributeValue,
                                   IGantItem item) {

        Object value = null;
        if (item.getClass().equals(UFDouble.class)) {
            value = AppGanttDataFormatUtil.formatNumberType(attributeValue);
        } else {
            value = attributeValue;
        }
        return value;
    }

    private void initRelationship(List<AppTimelineObject> timeobjlists) {

        Map<Object, AppTimelineObject> indexMap = new HashMap<Object, AppTimelineObject>();
        for (AppTimelineObject timeObj : timeobjlists) {
            Object index = getIndex(timeObj);
            if (index == null) continue;
            indexMap.put(index, timeObj);
        }
        for (AppTimelineObject timeObj : timeobjlists) {
            Object indexObj = getPreIndex(timeObj);
            if (indexObj != null) {
                if (indexObj.getClass().isArray()) {
                    Object[] indexs = (Object[]) indexObj;
                    for (int i = 0; i < indexs.length; i++) {
                        if (indexs[i] != null) {
                            createRelationShips(timeObj, indexMap, indexs[i]);
                        }
                    }
                } else {
                    createRelationShips(timeObj, indexMap, indexObj);
                }
                // AppTimelineObject srcObj = indexMap.get(index);
                // if (srcObj == null)
                // continue;
                // IRelationship newRelationShip = new DefaultRelationship(
                // getTimeObjPath(srcObj), getTimeObjPath(timeObj),
                // timeObj.getRelationshipType());
                // addRelationship(newRelationShip);
            }
        }
    }

    protected void createRelationShips(AppTimelineObject timeObj,
                                       Map<Object, AppTimelineObject> indexMap,
                                       Object index) {
        AppTimelineObject srcObj = indexMap.get(index);
        RelationshipType relatnType = ((MMGPTimelineObject) timeObj).getRelatnType(index);
        if (relatnType == null) {
            relatnType = timeObj.getRelationshipType();
        }
        if (srcObj == null) return;
        IRelationship newRelationShip =
                new DefaultRelationship(getTimeObjPath(srcObj), getTimeObjPath(timeObj), relatnType);
        addRelationship(newRelationShip);
    }

    protected Map<String, MMGPGanttChartNode> initTreeTableData(Map<Object, List<Object>> dataMap) {
    	
        Map<String, MMGPGanttChartNode> keyMap = new HashMap<String, MMGPGanttChartNode>();
        IValueAttributeAdapter nodeValueAdapter = getContext().getNodeValueAdapter();
        for (Object nodeObj : dataMap.keySet()) {
            String keyValue = nodeValueAdapter.getPrimaryKey(nodeObj);
            String parentPk = nodeValueAdapter.getParentKey(nodeObj);

            if (isOneRoot() && parentPk == null && !isAddData) {
                root.setTypedUserObject(nodeObj);
                root.setContext(getContext());
                keyMap.put(keyValue, root);
            } else {
                keyMap.put(keyValue, creatChartNode(nodeObj, getContext()));
            }
        }
        // tangxya add
        Collection<Object> nodeobjs = dataMap.keySet();
        nodeobjs = beforeSetValueToNode(nodeobjs);

        for (Object nodeObj : nodeobjs) {
            String parentPk = nodeValueAdapter.getParentKey(nodeObj);
            String pk = nodeValueAdapter.getPrimaryKey(nodeObj);
            if (pk == null) {
                continue;
            }
            if (parentPk == null && (!isOneRoot() || isAddData)) {
                root.add(keyMap.get(pk));
            } else if (keyMap.containsKey(parentPk)) {
                keyMap.get(parentPk).add(keyMap.get(pk));
            }

        }

        for (MMGPGanttChartNode nodeObj : keyMap.values()) {
            setValueToNode(nodeObj);
        }
        this.setFromCode(true);
        // 加载元数据关联项公式
        loadLoadRelationItemValue(null, keyMap);
        this.setFromCode(false);
        return keyMap;
    }

    /*
     * public void initModel(Map<Object, List<Object>> dataMap, ILayer layer) { setbodyItems(); super.initModel(dataMap,
     * layer); loadLoadRelationItemValue(null, dataTable); TreeTable table =getContext().getGantchart().getTreeTable();
     * int rowCount = table.getRowCount(); TreePath[] treePaths = table.getPathsBetweenRows(0, rowCount - 1); for
     * (TreePath treePath : treePaths) { ((MMGPGanttChartNode) treePath .getLastPathComponent()).getUserObject(); } }
     */

    public Object getNodeItemValue(MMGPGanttChartNode node,
                                   String key) {
        IValueAttributeAdapter valueAdapter = getContext().getNodeValueAdapter();
        return valueAdapter.getAttributeValue(node.getTypedUserObject(), key);
    }

    public void setNodeItemValue(MMGPGanttChartNode node,
                                 String key,
                                 Object value) {

        IValueAttributeAdapter valueAdapter = getContext().getNodeValueAdapter();
        valueAdapter.setAttributeValue(node.getTypedUserObject(), key, value);
    }

    protected Collection<Object> beforeSetValueToNode(Collection<Object> nodeobjs) {
        /* loadLoadRelationItemValue(null, keyMap); */
        return nodeobjs;
    }

    public void setNodeColumnValue(Object value,
                                   MMGPGanttChartNode node,
                                   String itemKey) {
        MMGPGantItem gantItem = getItemByKey(itemKey);
        value = gantItem.getConverter().convertToBillItem(gantItem.getDataType(), value);
        Object oldvalue = getNodeColumnValue(node, itemKey);
        // 转换成pk+name的值组合DefaultConstEnum
        if (value != null && gantItem.getDataType() == IBillItem.UFREF && (value instanceof IConstEnum)) {
            if (oldvalue != null) {
                value = new DefaultConstEnum(((IConstEnum) oldvalue).getValue(), value.toString());
            }
        }
        this.setColumnValue(node, value, getContext().getItemContainer().getItem(itemKey).getShowOrder());
    }

    public void loadLoadRelationItemValue(String[] itemkeys,
                                          Map<String, MMGPGanttChartNode> nodeMap) {
        if (MMMapUtil.isEmpty(nodeMap)) {
            return;
        }

        MMGPMetaDataGetGantModelRelationItemValue gvs = new MMGPMetaDataGetGantModelRelationItemValue();
        // 2011-4-2 解决童伟 自定义关系取值问题
        Map<Integer, IConstEnum[]> valuemap = new HashMap<Integer, IConstEnum[]>();
        // 如果列中有重复主键，记录该列的行号
        Map<String, Map<String, Integer>> keyListMap = new HashMap<String, Map<String, Integer>>();

        // 关联项取值
        getRelationItemValues(itemkeys, gvs, valuemap, keyListMap, nodeMap);

        // 关联项赋值
        setRelationValueToNodeValue(gvs, valuemap, keyListMap, nodeMap);
    }

    private void setRelationValueToNodeValue(MMGPMetaDataGetGantModelRelationItemValue gvs,
                                             Map<Integer, IConstEnum[]> valuemap,
                                             Map<String, Map<String, Integer>> keyListMap,
                                             Map<String, MMGPGanttChartNode> nodeMap) {
        IConstEnum[] o = gvs.getRelationItemValues();

        if (o == null) {
            return;
        }

        for (int i = 0; i < o.length; i++) {
            if (valuemap.get(i) != null) {
                o[i] = valuemap.get(i)[0];
            }
        }

        if (o != null) {
            for (int i = 0; i < o.length; i++) {
                if (o[i].getValue() != null) {
                    Object[] v = (Object[]) o[i].getValue();
                    if (keyListMap.get(o[i].getName()) != null) {
                        // 如果有重复主键的行，用行号重新定位
                        Map<String, Integer> keyMap = keyListMap.get(o[i].getName());
                        if (MMMapUtil.isNotEmpty(keyMap)) {
                            Iterator<String> nodekeys = keyMap.keySet().iterator();
                            while (nodekeys.hasNext()) {
                                String key = nodekeys.next();
                                MMGPGanttChartNode node = nodeMap.get(key);
                                setNodeColumnValue(v[keyMap.get(key)], node, o[i].getName());
                                /*
                                 * setNodeItemValue(node, o[i].getName(), v[keyMap.get(key) - 1]);
                                 */
                                /*
                                 * node.setColumnValue(v[keyMap.get(key)], o[i].getName());
                                 */
                            }
                        }
                    }
                }
            }
        }

    }

    // 关联项取值
    /**
     * @param itemkeys
     * @param erow
     * @param srow
     * @param gvs
     * @param valuemap
     *        key:(原为列号）先为所在列的key
     * @param keyListMap
     *        :key:列名(pk_org) value: Map<String, Integer> key:node 对应的pk，value:从数组取值的位置
     * @param nodeMap
     */
    private void getRelationItemValues(String[] itemkeys,
                                       MMGPMetaDataGetGantModelRelationItemValue gvs,
                                       Map<Integer, IConstEnum[]> valuemap,
                                       Map<String, Map<String, Integer>> keyListMap,
                                       Map<String, MMGPGanttChartNode> nodeMap) {

        GantItem[] billItems = (GantItem[]) getContext().getItemContainer().getAllItems();
        ;/* getBillItems(itemkeys); */

        int m = 0;
        for (int col = 0; col < billItems.length; col++) {// 列
            MMGPGantItem item = (MMGPGantItem) billItems[col];

            if (item.getDataType() == IBillItem.UFREF && item.getMetaDataProperty() != null) {
                ArrayList<IConstEnum> relationitem = getMetaDataRelationItems(item, false);
                if (relationitem != null) {
                    Map<String, Integer> rowMap = new HashMap<String, Integer>();// key:node的pk值，value:第几个值，有可能有重复的pk值，所以需要记录取第几个值

                    Map<String, Integer> pk2RowMap = new HashMap<String, Integer>();// key：pk列的值
                                                                                    // ，主要用于记录具体哪行
                    List<String> pkList = new ArrayList<String>();
                    int dataIndex = 0;

                    Iterator<String> keys = nodeMap.keySet().iterator();
                    while (keys.hasNext()) {// 行
                        String key = keys.next();
                        MMGPGanttChartNode node = nodeMap.get(key);

                        Object o = getNodeColumnValue(node, item.getKey());
                        if (o != null && o instanceof IConstEnum) {
                            String pk = (String) ((IConstEnum) o).getValue();

                            if (pk2RowMap.get(pk) == null) {
                                pk2RowMap.put(pk, dataIndex);
                                pkList.add(pk);
                                dataIndex++;
                            }
                            // 记录各行在新数组中的位置
                            rowMap.put(key, pk2RowMap.get(pk));
                            /* rowMap.put(key, dataIndex); */
                        }
                    }

                    for (int i = 0; i < relationitem.size(); i++) {
                        keyListMap.put(relationitem.get(i).getName(), rowMap);
                    }

                    String[] ids = pkList.toArray(new String[0]);
                    if (ids.length > 0) {
                        gvs.addRelationItem(item, relationitem, ids);
                        if (!(item.getGetBillRelationItemValue() instanceof MetaDataGetBillRelationItemValue)) {
                            valuemap.put(m, item
                                .getGetBillRelationItemValue()
                                .getRelationItemValue(relationitem, ids));
                        }

                        m++;
                    }

                }

            }
        }
    }

    /*
     * IConstEnum Value metadatapath:,Name:itemkey
     */
    protected ArrayList<IConstEnum> getMetaDataRelationItems(MMGPGantItem item,
                                                             boolean isediting) {

        if (item.getDataType() != IBillItem.UFREF) return null;

        if (item.getMetaDataProperty() == null) return null;

        if (item.getMetaDataProperty().isRefAttribute()) return null;

        ArrayList<IConstEnum> ics = new ArrayList<IConstEnum>();

        // 获得ITEM显示属性
        // 加载关联项，显示名称加载
        if (!isediting) {
            String showattname = getRefItemShowAttributeName(item);

            if (showattname != null) {
                IConstEnum ic = new DefaultConstEnum(showattname, item.getKey());
                ics.add(ic);
            }
        }

        // 获得ITEM编辑关联设置默认值属性
        // itemkey,访问路径:AAAA=B.A,BBBB=B.B
        // 编辑关联项
        if (item.getMetaDataRelation() != null && isediting) {
            IConstEnum[] ies = BillUtil.getConstEnumByString(item.getMetaDataRelation());

            for (int i = 0; i < ies.length; i++) {

                MMGPGantItem ritem = getItemByKey(ies[i].getName());

                if (ies[i].getValue() != null) {
                    if (!addRefRelationItem(ics, ritem, (String) ies[i].getValue())) {
                        ics.add(ies[i]);
                    }
                }
            }
        }

        // 获得ITEM关联属性
        if (item.getRelationItem() != null) {
            for (int i = 0; i < item.getRelationItem().size(); i++) {
                MMGPGantItem ritem = item.getRelationItem().get(i);

                if (ritem.getMetaDataAccessPath() != null) {
                    if (!addRefRelationItem(ics, ritem, ritem.getMetaDataAccessPath())) {
                        IConstEnum ic = new DefaultConstEnum(ritem.getMetaDataAccessPath(), ritem.getKey());
                        ics.add(ic);
                    }
                }

            }
        }

        if (ics.size() == 0) ics = null;

        return ics;

    }

    private String getRefItemShowAttributeName(MMGPGantItem item) {
        String showattname = null;

        if (item.getDataType() == IBillItem.UFREF && item.isShow()) {
            // if(item.getDataType() == IBillItem.UFREF){
            if (item.isRefReturnCode()) showattname = item.getMetaDataProperty().getBDCodeAttributeName();
            else
                showattname = item.getMetaDataProperty().getBDNameAttributeName();
        }
        return showattname;
    }

    // itemcontainer 的循环去取 可以优化一下
    public MMGPGantItem getItemByKey(String key) {
        return (MMGPGantItem) getContext().getItemContainer().getItem(key);
    }

    private boolean addRefRelationItem(ArrayList<IConstEnum> ics,
                                       MMGPGantItem ritem,
                                       String path) {

        if (ritem.getDataType() == IBillItem.UFREF) {
            IConstEnum ic = null;
            ic = new DefaultConstEnum(path, ritem.getKey() + IBillItem.ID_SUFFIX);
            ics.add(ic);
            // UFREF处理
            String showattname = getRefItemShowAttributeName(ritem);
            if (showattname != null) {
                ic = new DefaultConstEnum(path + "." + showattname, ritem.getKey());
                ics.add(ic);
                return true;
            }
        }

        return false;
    }

    private Object getIndex(AppTimelineObject timeobj) {
        return getContext().getValueAdapter().getIndex(timeobj.getUserObject());
    }

    private Object getPreIndex(AppTimelineObject timeobj) {
        return getContext().getValueAdapter().getPreIndex(timeobj.getUserObject());
    }

    public void clear() {
        this.clearTimelineObjects();
        this.clearRelationships();
        if (root != null) {
            root.removeAllChildren();
            nodeStructureChanged(root);
        }

    }

    public void addData(List<Object> datas) {
        isAddData = true;
        getContext().setNeedRelationShip(true);
        initModel(datas);
        isAddData = false;
        getContext().setNeedRelationShip(false);
    }

    public IAppTimelineObjectCreateStrategy getAppTimelineObjectCreateStategy() {
        if (appTimelineObjectCreateStategy == null) {
            appTimelineObjectCreateStategy = (IAppTimelineObjectCreateStrategy) new MMGPDefaultTimelineObjectCreator();
        }
        return appTimelineObjectCreateStategy;
    }

    public void setAppTimelineObjectCreateStategy(IAppTimelineObjectCreateStrategy appTimelineObjectCreateStategy) {
        this.appTimelineObjectCreateStategy = appTimelineObjectCreateStategy;
    }

    /**
     * 增行
     * @param parentNode 父节点
     * @param userobject  VO
     * @return
     */
    public MMGPGanttChartNode addLine(MMGPGanttChartNode parentNode,
                                      Object userobject) {
        if (parentNode == null) {
            parentNode = (MMGPGanttChartNode) getRoot();
        }
        int index = parentNode.getChildCount();
        return addLine(parentNode, userobject, index);

    }

    /**
     * 
     * 增行
     * @param parentNode 父节点
     * @param userobject VO
     * @param index 序号
     * @return
     */
    public MMGPGanttChartNode addLine(MMGPGanttChartNode parentNode,
                                      Object userobject,
                                      int index) {
        if (userobject == null && voCreateSrategy != null) {
            userobject = voCreateSrategy.genaralNewVO(this, parentNode);

        }
        MMGPGanttChartNode insertNode = new MMGPGanttChartNode(userobject, this.getContext());
        insertNode = (MMGPGanttChartNode) insertNode(insertNode, parentNode, index);
        if (!this.getContext().getGantchart().getTreeTable().isTreeNodeExpanded(new TreePath(parentNode.getPath()))) {
            this.getContext().getGantchart().getTreeTable().expandPathAnimated(new TreePath(parentNode.getPath()));
        }
        refreshTimeObj(insertNode);
        return insertNode;
    }

    private void setBillItemDecimalByType(AppGanttChartNode node,
                                          MMGPGantItem item) {
        switch (item.getDataType()) {
            case IBillItem.DECIMAL:
            case IBillItem.MONEY:
                setGantItemDecimalByMetaData(item, node);
                break;
        }
    }

    /**
     * 设置对应行node的gantItem的精度
     */
    private void setGantItemDecimalByMetaData(MMGPGantItem item,
                                              AppGanttChartNode node) {
        if (item.getDecimalListener() != null) {
            String source = item.getDecimalListener().getSource();
            Object pkValue = getContext().getNodeValueAdapter().getAttributeValue(node.getTypedUserObject(), source);
            if (pkValue != null)
                item.setDecimalDigits(item.getDecimalListener().getDecimalFromSource(node, pkValue));
        }
    }

    /**
     * 增加精度监听
     */
    public void addDecimalListener(ITreeTableModelDecimalListener bdl) {

        if (bdl instanceof ITreeTableModelDecimalListener) {
            String[] targets = ((ITreeTableModelDecimalListener) bdl).getTarget();
            if (targets != null && targets.length > 0) {
                for (int i = 0; i < targets.length; i++) {
                    MMGPGantItem item = getItemByKey(targets[i]);
                    if (item != null) item.addDecimalListener(bdl);
                    else
                        Logger.error(targets[i] + " col not exist");
                }
            }
        }
    }

    public IVOCreateStrategy getVoCreateSrategy() {
        return voCreateSrategy;
    }

    public void setVoCreateSrategy(IVOCreateStrategy voCreateSrategy) {
        this.voCreateSrategy = voCreateSrategy;
    }

    /* (non-Javadoc)
     * 修复PUBAPP 增行BUG。
     * 在有排序状态下，增行出错。
     * @see nc.ui.pubapp.gantt.model.AppGanttChartModel#insertNode(nc.ui.pubapp.gantt.model.AppGanttChartNode, nc.ui.pubapp.gantt.model.AppGanttChartNode, int)
     */
    public AppGanttChartNode insertNode(AppGanttChartNode node,
                                        AppGanttChartNode parentNode,
                                        int pindex) {
        if (node == null) {
            return null;
        }
        int index = pindex;

        if (index == -1) {
            if (node != null && node.getParent() == parentNode) index = parentNode.getChildCount() - 1;
            else
                index = parentNode.getChildCount();
        }
        node.setContext(this.getContext());
        INodeEditPolicy nodeEditPolicy =
                getContext().getGantchart().getPolicyProvider().getPolicy(INodeEditPolicy.class);
        ICommand insertCommand = nodeEditPolicy.getInsertNodeCommand(parentNode, index, this);
        getContext().getGantchart().commandExecute(insertCommand);
        MMGPGanttChartNode insertNode = (MMGPGanttChartNode) ((MMGPNodeInsertCommand) insertCommand).getChildNode();
        insertNode.setTypedUserObject(node.getTypedUserObject());
        setValueToNode(insertNode);
        getContext().getGantchart().getTreeTable().repaint();
        return insertNode;

    }
    
    /* (non-Javadoc)
     * @see nc.ui.pubapp.gantt.model.AppGanttChartModel#deleteNodes(nc.ui.pubapp.gantt.model.AppGanttChartNode[])
     */
    public void deleteNodes(AppGanttChartNode... nodes) {
    	
    	List<Object> nodeList = new ArrayList<Object>();
    	for (AppGanttChartNode node : nodes) {
    		nodeList.add(node);
    	}
    	INodeEditPolicy nodeEditPolicy = getContext().getGantchart()
				.getPolicyProvider().getPolicy(INodeEditPolicy.class);
    	ICommand deleteCommand = nodeEditPolicy.getDeleteNodesCommand(nodeList,
				this);
		getContext().getGantchart().commandExecute(deleteCommand);
		getContext().getGantchart().getTreeTable().revalidate();
//		if (nodes != null && nodes.length > 0) {
//			List<AppGanttChartNode> leafNodeList = new ArrayList<AppGanttChartNode>();
//			List<AppGanttChartNode> parentNodeList = new ArrayList<AppGanttChartNode>();
//			for (AppGanttChartNode node : nodes) {
//				if (node.isLeaf()) {
//					leafNodeList.add(node);
//				} else {
//					parentNodeList.add(node);
//				}
//			}
//			for (AppGanttChartNode node : leafNodeList) {
//				deleteNode(node);
//			}
//			for (AppGanttChartNode node : parentNodeList) {
//				deleteNode(node);
//			}
//		}
	}
}
