package nc.impl.mmgp.uif2.template.grand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.impl.pubapp.pattern.data.table.UpdateTable;
import nc.impl.pubapp.pattern.data.table.UpdateTableTS;
import nc.mddb.constant.ElementConstant;
import nc.vo.mmgp.util.grand.MMGPGrandBusiEntityUtil;
import nc.vo.pub.IAttributeMeta;
import nc.vo.pub.IColumnMeta;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.ITableMeta;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.tool.VOTool;

/**
 * 主子孙更新.
 * 
 * @see VOUpdate.
 * @since 6.1
 * @version 2012-6-25 下午07:13:18
 * @author zhaoshb
 */
public class MMGPGrandVOUpdate<E extends ISuperVO> {

    /**
     * 将VO的所有属性（主键除外）更新到数据库中
     * 
     * @param vos
     *        要更新的VO
     * @return 保存到数据库后的最新状态的VO
     */
    public E[] update(E[] vos) {
        IVOMeta voMeta = vos[0].getMetaData();
        IAttributeMeta[] attributes = voMeta.getStatisticInfo().getPerisistentAttributes();
        List<IAttributeMeta> list = new ArrayList<IAttributeMeta>();
        for (IAttributeMeta attribute : attributes) {
            // 去掉对主键的更新
            if (voMeta.getPrimaryAttribute().equals(attribute)) {
                continue;
            }
            // 去掉对TS的更新（后续会自动处理TS）
            else if (attribute.getName().equals(ElementConstant.KEY_TS)) {
                continue;
            }
            list.add(attribute);
        }
        int size = list.size();
        attributes = new IAttributeMeta[size];
        attributes = list.toArray(attributes);
        return this.update(vos, attributes);
    }

    /**
     * 将新旧VO进行对比后，将改变值保存到数据库中。
     * 
     * @param vos
     *        新VO
     * @param originVOs
     *        旧VO
     * @return 保存到数据库后的最新状态的VO
     */
    public E[] update(E[] vos,
                      E[] originVOs) {
        IAttributeMeta[] attributes = this.getChangedAttribute(vos, originVOs);
        return this.update(vos, attributes);
    }

    /**
     * 将要更新的VO指定更新哪些属性到数据库中
     * 
     * @param vos
     *        要更新的VO
     * @param names
     *        要更新的属性名
     * @return 保存到数据库后的最新状态的VO
     */
    public E[] update(E[] vos,
                      String[] names) {
        IVOMeta voMeta = vos[0].getMetaData();
        IAttributeMeta[] attributes = this.geAttributeIndex(voMeta, names);
        return this.update(vos, attributes);
    }

    private IAttributeMeta[] geAttributeIndex(IVOMeta voMeta,
                                              String[] names) {
        int length = names.length;
        IAttributeMeta[] attributes = new IAttributeMeta[length];
        for (int i = 0; i < length; i++) {
            IAttributeMeta attribute = voMeta.getAttribute(names[i]);
            if (attribute == null) {
                ExceptionUtils.unSupported();
            } else if (!attribute.isPersistence()) {
                ExceptionUtils.unSupported();
            }
            attributes[i] = attribute;
        }
        return attributes;
    }

    private IAttributeMeta[] getChangedAttribute(E[] vos,
                                                 E[] originVOs) {
        int length = vos.length;
        Map<String, IAttributeMeta> diffrentIndex = new HashMap<String, IAttributeMeta>();
        IVOMeta voMeta = vos[0].getMetaData();
        VOTool tool = new VOTool();
        for (int i = 0; i < length; i++) {
            Set<String> set = tool.getDifferentField(vos[i], originVOs[i]);
            for (String name : set) {
                if (diffrentIndex.containsKey(name)) {
                    continue;
                }
                IAttributeMeta attribute = voMeta.getAttribute(name);
                diffrentIndex.put(name, attribute);
            }
        }
        List<IAttributeMeta> list = new ArrayList<IAttributeMeta>();
        Set<Entry<String, IAttributeMeta>> entrySet = diffrentIndex.entrySet();
        for (Entry<String, IAttributeMeta> entry : entrySet) {
            String name = entry.getKey();
            // 有时间戳不一致，一定是VO的时间戳没有传递过来，不要进行处理
            if (name.equals(ElementConstant.KEY_TS)) {
                continue;
            }
            IAttributeMeta attribute = entry.getValue();
            if (attribute.isPersistence()) {
                list.add(attribute);
            }
        }
        int size = list.size();
        IAttributeMeta[] attributes = new IAttributeMeta[size];
        attributes = list.toArray(attributes);
        return attributes;
    }

    private Map<String, List<IColumnMeta>> sort(IAttributeMeta[] attributes) {
        Map<String, List<IColumnMeta>> index = new HashMap<String, List<IColumnMeta>>();
        for (IAttributeMeta attribute : attributes) {
            IColumnMeta column = attribute.getColumn();
            ITableMeta table = column.getTable();
            List<IColumnMeta> list = null;
            if (index.containsKey(table.getName())) {
                list = index.get(table.getName());
            } else {
                list = new ArrayList<IColumnMeta>();
                index.put(table.getName(), list);
            }
            list.add(column);
        }
        return index;
    }

    private E[] update(E[] vos,
                       IAttributeMeta[] attributes) {
        IVOMeta voMeta = vos[0].getMetaData();
        // 过滤子表属性.
        IAttributeMeta[] afterFilterAttrs = this.filterChildAttrMeta(voMeta, attributes);
        ITableMeta mainTable = voMeta.getPrimaryAttribute().getColumn().getTable();
        Map<String, List<IColumnMeta>> attributeIndex = this.sort(afterFilterAttrs);
        boolean updateMainTable = false;
        UpdateTable bo = new UpdateTable();
        Set<Entry<String, List<IColumnMeta>>> entrySet = attributeIndex.entrySet();
        for (Entry<String, List<IColumnMeta>> entry : entrySet) {
            String key = entry.getKey();
            List<IColumnMeta> list = entry.getValue();
            int size = list.size();
            IColumnMeta[] columns = new IColumnMeta[size];
            columns = list.toArray(columns);
            bo.update(vos, columns);
            if (key.equals(mainTable.getName())) {
                updateMainTable = true;
            }
        }

        // 只是更新了附属表，则还必须更新主表的时间戳
        if (!updateMainTable) {
            UpdateTableTS tsBO = new UpdateTableTS();
            tsBO.update(vos, mainTable);
        }
        for (E vo : vos) {
            vo.setStatus(VOStatus.UNCHANGED);
        }
        return vos;
    }

    private IAttributeMeta[] filterChildAttrMeta(IVOMeta voMeta,
                                                 IAttributeMeta[] attributes) {
        Set<String> childAttrSet = MMGPGrandBusiEntityUtil.getInstance().queryChildAttrName(voMeta);
        List<IAttributeMeta> attrMetaList = new ArrayList<IAttributeMeta>();
        for (IAttributeMeta attrMeta : attributes) {
            String attrName = attrMeta.getName();
            if (childAttrSet.contains(attrName)) {
                continue;
            }
            attrMetaList.add(attrMeta);
        }
        return attrMetaList.toArray(new IAttributeMeta[0]);
    }

}
