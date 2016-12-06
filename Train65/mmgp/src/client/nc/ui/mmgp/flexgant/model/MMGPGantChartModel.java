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
 * @Description: ���ڶ���ĸ���ͼģ��
 *               <p>
 *               ��ϸ��������
 *               </p>
 * @data:2014-5-19����5:07:47
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
        // �����������ֵ
        Map<String, MMGPGanttChartNode> keyMap = initTreeTableData(dataMap);
        // �����ұ�ͼ��ֵ
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
     * ��ʼ������ͼ�Ҳ�ʱ����
     * 
     * �������ý����׼ʱ�����߼���
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
                // �����þ���
                setBillItemDecimalByType(node, gantItem);
                // ת��ֵ
                attributeValue = gantItem.getConverter().convertToBillItem(gantItem.getDataType(), attributeValue);
                // tangxya end
                setColumnValue(node, attributeValue, item.getShowOrder());
                node.setValueEditable(item.getShowOrder(), item.isEdit());

            }

        }
        // ����Ԫ���ݹ����ʽ ��ʱ�Ȳ����� ��������node��ֵ��Ҫ����
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
        // ����Ԫ���ݹ����ʽ
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
        // ת����pk+name��ֵ���DefaultConstEnum
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
        // 2011-4-2 ���ͯΰ �Զ����ϵȡֵ����
        Map<Integer, IConstEnum[]> valuemap = new HashMap<Integer, IConstEnum[]>();
        // ����������ظ���������¼���е��к�
        Map<String, Map<String, Integer>> keyListMap = new HashMap<String, Map<String, Integer>>();

        // ������ȡֵ
        getRelationItemValues(itemkeys, gvs, valuemap, keyListMap, nodeMap);

        // �����ֵ
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
                        // ������ظ��������У����к����¶�λ
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

    // ������ȡֵ
    /**
     * @param itemkeys
     * @param erow
     * @param srow
     * @param gvs
     * @param valuemap
     *        key:(ԭΪ�кţ���Ϊ�����е�key
     * @param keyListMap
     *        :key:����(pk_org) value: Map<String, Integer> key:node ��Ӧ��pk��value:������ȡֵ��λ��
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
        for (int col = 0; col < billItems.length; col++) {// ��
            MMGPGantItem item = (MMGPGantItem) billItems[col];

            if (item.getDataType() == IBillItem.UFREF && item.getMetaDataProperty() != null) {
                ArrayList<IConstEnum> relationitem = getMetaDataRelationItems(item, false);
                if (relationitem != null) {
                    Map<String, Integer> rowMap = new HashMap<String, Integer>();// key:node��pkֵ��value:�ڼ���ֵ���п������ظ���pkֵ��������Ҫ��¼ȡ�ڼ���ֵ

                    Map<String, Integer> pk2RowMap = new HashMap<String, Integer>();// key��pk�е�ֵ
                                                                                    // ����Ҫ���ڼ�¼��������
                    List<String> pkList = new ArrayList<String>();
                    int dataIndex = 0;

                    Iterator<String> keys = nodeMap.keySet().iterator();
                    while (keys.hasNext()) {// ��
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
                            // ��¼�������������е�λ��
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

        // ���ITEM��ʾ����
        // ���ع������ʾ���Ƽ���
        if (!isediting) {
            String showattname = getRefItemShowAttributeName(item);

            if (showattname != null) {
                IConstEnum ic = new DefaultConstEnum(showattname, item.getKey());
                ics.add(ic);
            }
        }

        // ���ITEM�༭��������Ĭ��ֵ����
        // itemkey,����·��:AAAA=B.A,BBBB=B.B
        // �༭������
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

        // ���ITEM��������
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

    // itemcontainer ��ѭ��ȥȡ �����Ż�һ��
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
            // UFREF����
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
     * ����
     * @param parentNode ���ڵ�
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
     * ����
     * @param parentNode ���ڵ�
     * @param userobject VO
     * @param index ���
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
     * ���ö�Ӧ��node��gantItem�ľ���
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
     * ���Ӿ��ȼ���
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
     * �޸�PUBAPP ����BUG��
     * ��������״̬�£����г���
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
