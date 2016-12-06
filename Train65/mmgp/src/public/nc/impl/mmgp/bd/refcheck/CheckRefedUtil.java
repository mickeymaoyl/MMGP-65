package nc.impl.mmgp.bd.refcheck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.resource.NotSupportedException;

import nc.bs.businessevent.CheckEvent;
import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IEventType;
import nc.bs.businessevent.bd.BDCommonEvent;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.impl.pubapp.pattern.database.IDExQueryBuilder;
import nc.itf.bd.material.baseinfo.IMaterialBaseInfoQueryService;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IAttribute;
import nc.md.model.IBean;
import nc.md.model.MetaDataException;
import nc.pubitf.initgroup.InitGroupQuery;
import nc.vo.bd.material.MaterialVO;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.tool.VOTool;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

/**
 * 校验当前档案是否被业务单据引用工具类
 *
 * @since 6.0
 * @version 2012-5-24 上午11:03:57
 * @author chendb
 */
public class CheckRefedUtil {

    /**
     * 针对有多版本控制的档案校验
     *
     * @param event2
     */
    public static void chkMultiVersionRefed(IBusinessEvent event,
                                            List<FileRefedInfo> infoList,
                                            Set<String> editedSet,
                                            String checkOrgValue,
                                            String checkGroupValue,
                                            String checkPKValue) throws BusinessException {
        // key：billTable; value：list中一个String表示：一组where条件+"#"+错误提示信息
        // 注：每张表对应的where条件组中, 就第一个加错误信息，其他的不加
        Map<String, List<String>> sqlMap = new HashMap<String, List<String>>();

        if (infoList.size() > 0) {
            filterEditedSetWhenCreateVS(event, infoList, editedSet);
            fillSqlMapForBaseInfo(event, sqlMap, infoList, editedSet, checkOrgValue, checkPKValue);
            fillSqlMapForVersionInfo(event, sqlMap, infoList, editedSet, checkOrgValue, checkPKValue, checkGroupValue);
        }

        execSqlMap(event, sqlMap);
    }

    /**
     * 批改前监听校验
     *
     * @param event
     * @param edited已修改字段
     * @throws BusinessException
     * @throws NotSupportedException
     */
    public static void chkMulVsRefedForBatchUpdBef(IBusinessEvent event,
                                                   List<FileRefedInfo> infoList) throws BusinessException {
        CheckEvent checkevent = (CheckEvent) event;
        // 已注册的不能修改的字段
        Set<String> noEditSet = new HashSet<String>();
        // key:单据对应的类名称；value：当前单据主组织key名称+当前单据物料的key名称
        Map<String, List<String>> classOrgMap = new HashMap<String, List<String>>();
        setNoEditSet(noEditSet, infoList, classOrgMap);

        Object paramObj = checkevent.getParam();
        if (paramObj != null && paramObj instanceof String) {
            String param = (String) paramObj;
            if (!noEditSet.contains(param)) {
                return;
            }
        }

        Object[] vos = checkevent.getInputVOs();
        if (MMArrayUtil.isEmpty(vos)) {
            return;
        }
        Map<String, List<SuperVO>> marMap = new HashMap<String, List<SuperVO>>();
        String[] pk_materials = getPk_Materials(marMap, (SuperVO[]) vos);
        String[] pk_orgs = getOrgs4BatchUpd((SuperVO[]) vos);
        try {
            execSqlForBatchUpdBef(event, classOrgMap, pk_materials, pk_orgs, marMap);
        } catch (NotSupportedException e) {
            ExceptionUtils.wrappException(e);
        }
    }

    private static String[] getOrgs4BatchUpd(SuperVO[] marvos) {
        Set<String> orgset = new HashSet<String>();
        for (SuperVO marvo : marvos) {
            String pk_org = (String) marvo.getAttributeValue(MaterialVO.PK_ORG);
            if (!orgset.contains(pk_org)) {
                orgset.add(pk_org);
            }
        }
        return orgset.toArray(new String[orgset.size()]);
    }

    private static void execSqlForBatchUpdBef(IBusinessEvent event,
                                              Map<String, List<String>> classOrgMap,
                                              String[] pk_materials,
                                              String[] pk_orgs,
                                              Map<String, List<SuperVO>> marMap) throws NotSupportedException {
        Set<String> marNameSet = new HashSet<String>();
        for (Map.Entry<String, List<String>> entry : classOrgMap.entrySet()) {
            String classname = entry.getKey();
            String billtabel = getBillTable(classname);

            for (String orgToFiled : entry.getValue()) {
                String[] values = orgToFiled.split("#");
                String orgname = values[0];
                String marname = values[1];

                String inSQL = new IDExQueryBuilder("MMGPMA").buildAnotherSQL(marname, pk_materials);

                SqlBuilder sql = new SqlBuilder();
                sql.append(" select " + marname + " from " + billtabel);
                sql.append(" where ");
                sql.append(" dr=0 ");
                sql.append(" and ");
                sql.append(inSQL);
                /*sql.append(marname, pk_materials);*/
                if (!MMStringUtil.isEmpty(orgname) && !orgname.equals("null")) {
                    sql.append(" and ");
                    sql.append(orgname, pk_orgs);

                }
                DataAccessUtils utils = new DataAccessUtils();
                String[] results = utils.query(sql.toString()).toOneDimensionStringArray();
                if (MMArrayUtil.isEmpty(results)) {
                    continue;
                }
                for (String name : results) {
                    if (!marNameSet.contains(name)) {
                        marNameSet.add(name);
                    }
                }
            }
        }
        for (String result : marNameSet) {
            List<SuperVO> refedvos = marMap.get(result);
            if (refedvos == null) {
                continue;
            }

            String errMsg = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0061")/*@res "已被引用,不能修改 "*/;
            for (SuperVO refedvo : refedvos) {
                ((CheckEvent) event).addErrorMsg(refedvo, errMsg);
            }
        }
    }

    private static void execSqlMap(IBusinessEvent event,
                                   Map<String, List<String>> sqlMap) {
        if (sqlMap.size() > 0) {
            for (Map.Entry<String, List<String>> map : sqlMap.entrySet()) {
                SqlBuilder sql = new SqlBuilder();
                String billtable = map.getKey();
                sql.append(" select 1 from ");
                sql.append(billtable);
                sql.append(" where " + billtable + ".dr=0 and ");
                sql.append(" ( ");
                StringBuilder whereSql = new StringBuilder();
                String errMsg = "";
                for (int i = 0; i < map.getValue().size(); i++) {
                    String where = map.getValue().get(i);
                    if (i == 0) {
                        String[] tempwhere = where.split("#");
                        String tempErr = tempwhere[1];
                        where = tempwhere[0];
                        errMsg = tempErr.replace("&*", " ").replace("&&", " , ");
                    } else {
                        whereSql.append(" or ");
                    }
                    whereSql.append(" ( ");
                    whereSql.append(where);
                    whereSql.append(" ) ");
                }
                sql.append(whereSql.toString());
                sql.append(" ) ");

                DataAccessUtils utils = new DataAccessUtils();
                String[] results = utils.query(sql.toString()).toOneDimensionStringArray();
                if (!MMArrayUtil.isEmpty(results) && event instanceof BDCommonEvent) {
                    ExceptionUtils.wrappBusinessException(errMsg + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0062")/*@res "不可修改!"*/);
                }
            }
        }
    }

    private static void fillSqlMapForBaseInfo(IBusinessEvent event,
                                              Map<String, List<String>> sqlMap,
                                              List<FileRefedInfo> infoList,
                                              Set<String> editedSet,
                                              String checkOrgValue,
                                              String checkPKValue) throws BusinessException {
        // 按模块循环
        for (FileRefedInfo info : infoList) {
            if (!isModuleInstalled(info.getModulecode())) {
                continue;
            }
            List<String> displayList = getDisNoEditFileds(event.getSourceID(), info.getnoEditFileds(), editedSet);
            if (displayList.size() == 0) {
                continue;
            }

            // 按模块中的单据循环
            EntityBaseInfo[] billBaseInfos = info.getEntityBaseInfos();
            for (EntityBaseInfo billBaseInfo : billBaseInfos) {

                if (billBaseInfo instanceof EntityInfoForVersion) {
                    continue;
                }

                String billFieldName = billBaseInfo.getFieldName();
                String billOrgName = billBaseInfo.getOrgName();

                String billtabel = getBillTable(billBaseInfo.getVoclassName());

                SqlBuilder where = new SqlBuilder();
                where.append(billtabel + "." + billFieldName, checkPKValue);

                if (!MMStringUtil.isEmpty(billOrgName)) {
                    where.append(" and ");
                    where.append(billtabel + "." + billOrgName, checkOrgValue);
                }

                if (sqlMap.containsKey(billtabel)) {
                    List<String> value = sqlMap.get(billtabel);
                    value.add(where.toString());
                } else {
                    List<String> value = new ArrayList<String>();
                    String errorMsg = info.getErrInfo() + "&*" + displayList.toString();
                    where.append("#" + errorMsg);

                    value.add(where.toString());
                    sqlMap.put(billtabel, value);
                }
            }
        }
    }

    private static void fillSqlMapForVersionInfo(IBusinessEvent event,
                                                 Map<String, List<String>> sqlMap,
                                                 List<FileRefedInfo> infoForVersionList,
                                                 Set<String> editedSet,
                                                 String checkOrgValue,
                                                 String checkPKValue,
                                                 String checkGroupValue) throws BusinessException {
        MaterialVO[] marvos =
                NCLocator
                    .getInstance()
                    .lookup(IMaterialBaseInfoQueryService.class)
                    .queryDataByPks(new String[]{checkPKValue });
        if (marvos != null && marvos.length > 0) {
            String pk_source = "";
            for (MaterialVO marvo : marvos) {
                pk_source = marvo.getPk_source();
                break;
            }
            // 按模块循环
            for (FileRefedInfo infoForVersion : infoForVersionList) {
                if (!isModuleInstalled(infoForVersion.getModulecode())) {
                    continue;
                }
                List<String> vsdisplayList =
                        getDisNoEditFileds(event.getSourceID(), infoForVersion.getnoEditFileds(), editedSet);
                if (vsdisplayList.size() == 0) {
                    continue;
                }
                EntityBaseInfo[] billInfos = infoForVersion.getEntityBaseInfos();
                // 按模块中的单据循环
                for (EntityBaseInfo billInfo : billInfos) {

                    if (!(billInfo instanceof EntityInfoForVersion)) {
                        continue;
                    }

                    String billOrgName = billInfo.getOrgName();
                    String billSrcFieldName = ((EntityInfoForVersion) billInfo).getSrcFieldName();
                    String billtabel = getBillTable(billInfo.getVoclassName());

                    SqlBuilder where = new SqlBuilder();
                    where.append(billtabel + "." + billSrcFieldName, pk_source);
                    if (!MMStringUtil.isEmpty(billOrgName)) {
                        if (!MMStringUtil.isEmpty(checkGroupValue) && checkGroupValue.equals(checkOrgValue)) {
                            where.append(" and ");
                            where.append(billtabel + "." + "pk_group", checkGroupValue);
                        } else {
                            where.append(" and ");
                            where.append(billtabel + "." + billOrgName, checkOrgValue);
                        }
                    }

                    if (sqlMap.containsKey(billtabel)) {
                        List<String> value = sqlMap.get(billtabel);
                        // 补全错误信息,&&后补充多版本中注入的不可修改字段的信息
                        String firstwhere = value.get(0);
                        if (firstwhere.contains("&*") && firstwhere.split("&&").length == 0) {
                            firstwhere = firstwhere + "&&" + vsdisplayList.toString();
                            value.set(0, firstwhere);
                        }
                        value.add(where.toString());
                    } else {
                        List<String> value = new ArrayList<String>();
                        String errorMsg = infoForVersion.getErrInfo() + "&&" + vsdisplayList.toString();
                        where.append("#" + errorMsg);

                        value.add(where.toString());
                        sqlMap.put(billtabel, value);
                    }
                }
            }
        }
    }

    private static void filterEditedSetWhenCreateVS(IBusinessEvent event,
                                                    List<FileRefedInfo> infoList,
                                                    Set<String> editedSet) {
        // 创建新版本时，过滤editSet
        if (event.getEventType().equals(IEventType.TYPE_INSERT_AFTER)) {
            for (FileRefedInfo baseinfo : infoList) {
                for (String noedit : baseinfo.getnoEditFileds()) {
                    if (editedSet.contains(noedit)) {
                        editedSet.remove(noedit);
                    }
                }
            }
        }
    }

    public static String getBillTable(String voclassname) {
        IBean billbean;
        try {
            billbean = MDBaseQueryFacade.getInstance().getBeanByFullClassName(voclassname);
        } catch (MetaDataException e) {
            ExceptionUtils.wrappException(e);
            return null;
        }
        return billbean.getTable().getName();
    }

    public static List<String> getDisNoEditFileds(String metaId,
                                                  String[] noedits,
                                                  Set<String> editSet) {
        List<String> displayList = new ArrayList<String>();
        IBean bean = null;
        try {
            bean = MDBaseQueryFacade.getInstance().getBeanByID(metaId);
        } catch (MetaDataException e) {
            ExceptionUtils.wrappException(e);
            return new ArrayList<String>(0);
        }
        if (bean == null) {
            return new ArrayList<String>(0);
        }

        for (String noEdit : noedits) {
            if (editSet.contains(noEdit.toLowerCase())) {
                IAttribute attribute = bean.getAttributeByName(noEdit);
                String disPlayName = attribute.getDisplayName();
                displayList.add(disPlayName);
            }
        }
        return displayList;
    }

    private static String[] getPk_Materials(Map<String, List<SuperVO>> marMap,
                                            SuperVO[] marvos) {
        Set<String> set = new HashSet<String>();
        for (SuperVO vo : marvos) {
            String pk_material = (String) vo.getAttributeValue(MaterialVO.PK_MATERIAL);

            List<SuperVO> oneListMarVOs = marMap.get(pk_material);

            if (oneListMarVOs == null) {
                oneListMarVOs = new ArrayList<SuperVO>();
                marMap.put(pk_material, oneListMarVOs);
            }
            oneListMarVOs.add(vo);

            if (!set.contains(pk_material)) {
                set.add(pk_material);
            }
        }
        return set.toArray(new String[set.size()]);
    }

    public static boolean isModuleInstalled(String modulecode) throws BusinessException {
        return InitGroupQuery.isEnabled(InvocationInfoProxy.getInstance().getGroupId(), modulecode);
    }

    /**
     * 该档案是否被指定单据引用
     *
     * @param baseinfo
     * @return
     */
    public static boolean isRefedByBill(FileRefedInfo baseinfo,
                                        String checkOrgValue,
                                        String checkPKValue) {
        EntityBaseInfo[] billBaseInfos = baseinfo.getEntityBaseInfos();

        for (int i = 0; i < billBaseInfos.length; i++) {
            String billFieldName = billBaseInfos[i].getFieldName();
            String billOrgName = billBaseInfos[i].getOrgName();

            String billtabel = getBillTable(billBaseInfos[i].getVoclassName());

            SqlBuilder sql = new SqlBuilder();
            sql.append("select 1 from " + billtabel + " " + billtabel);
            sql.append(" where ");
            sql.append(billtabel + "." + billFieldName, checkPKValue);

            if (!MMStringUtil.isEmpty(billOrgName)) {
                sql.append(" and ");
                sql.append(billtabel + "." + billOrgName, checkOrgValue);
            }
            sql.append(" and ");
            sql.append(billtabel + ".dr", 0);
            DataAccessUtils utils = new DataAccessUtils();
            String[] results = utils.query(sql.toString()).toOneDimensionStringArray();
            // 被该单据引用
            if (!MMArrayUtil.isEmpty(results)) {
                return true;
            }
        }
        return false;
    }

    private static void setNoEditSet(Set<String> noEditSet,
                                     List<FileRefedInfo> baseinfoList,
                                     Map<String, List<String>> classOrgMap) throws BusinessException {
        for (FileRefedInfo baseinfo : baseinfoList) {
            // 判断该模块是否被安装
            if (!isModuleInstalled(baseinfo.getModulecode())) {
                continue;
            }
            for (String noedit : baseinfo.getnoEditFileds()) {
                noEditSet.add(noedit);
            }
            EntityBaseInfo[] entitys = baseinfo.getEntityBaseInfos();
            for (EntityBaseInfo entity : entitys) {
                String classname = entity.getVoclassName();
                List<String> oneClassOrgList = classOrgMap.get(classname);

                if (oneClassOrgList == null) {
                    oneClassOrgList = new ArrayList<String>();
                    classOrgMap.put(classname, oneClassOrgList);
                }

                String orgNameToFieldName = null;
                if (entity instanceof EntityInfoForVersion) {
                    orgNameToFieldName =
                            entity.getOrgName() + "#" + ((EntityInfoForVersion) entity).getSrcFieldName();
                } else {
                    orgNameToFieldName = entity.getOrgName() + "#" + entity.getFieldName();
                }

                oneClassOrgList.add(orgNameToFieldName);
            }
        }
    }

    /**
     * @param sourceID
     * @param getnoEditFileds
     * @param oldValue
     * @param newValue
     * @return
     */
    public static List<String> getDisNoEditFileds(String sourceID,
                                                  String[] noEditFileds,
                                                  SuperVO oldValue,
                                                  SuperVO newValue) {
        VOTool votool = new VOTool();
        Set<String> editedSet = votool.getDifferentField(oldValue, newValue);

        if (null == editedSet || editedSet.size() == 0) {
            return Collections.emptyList();
        }

        List<String> displayList = CheckRefedUtil.getDisNoEditFileds(sourceID, noEditFileds, editedSet);
        return displayList;
    }

    /**
     * @param refedBaseInfos
     * @param fileRefedInfos
     * @param fileRefedInfosForVersion
     */
    public static void classifyFileRefedInfo(List<FileRefedInfo> refedBaseInfos,
                                             List<FileRefedInfo> fileRefedInfos,
                                             List<FileRefedInfo> fileRefedInfosForVersion) {
        if (MMCollectionUtil.isEmpty(refedBaseInfos)) {
            return;
        }

        for (FileRefedInfo refedBaseInfo : refedBaseInfos) {
            EntityBaseInfo[] entityInfos = refedBaseInfo.getEntityBaseInfos();
            if (MMArrayUtil.isEmpty(entityInfos)) {
                fileRefedInfos.add(refedBaseInfo);
            }

            List<EntityBaseInfo> entityBaseInfos = new ArrayList<EntityBaseInfo>();
            List<EntityBaseInfo> entityBaseInfosForVersion = new ArrayList<EntityBaseInfo>();

            classifyFileRefedInfo(entityInfos, entityBaseInfos, entityBaseInfosForVersion);

            if (MMCollectionUtil.isNotEmpty(entityBaseInfos)) {
                FileRefedInfo newRefedInfo =
                        new FileRefedInfo(
                            refedBaseInfo.getModulecode(),
                            entityBaseInfos.toArray(new EntityBaseInfo[0]),
                            refedBaseInfo.getnoEditFileds(),
                            refedBaseInfo.getErrInfo());
                fileRefedInfos.add(newRefedInfo);
            }

            if (MMCollectionUtil.isNotEmpty(entityBaseInfosForVersion)) {
                FileRefedInfo newRefedInfo =
                        new FileRefedInfo(
                            refedBaseInfo.getModulecode(),
                            (EntityBaseInfo[]) MMArrayUtil.toArray(entityBaseInfosForVersion),
                            refedBaseInfo.getnoEditFileds(),
                            refedBaseInfo.getErrInfo());
                fileRefedInfosForVersion.add(newRefedInfo);
            }
        }
    }

    /**
     * @param entityInfos
     * @param entityBaseInfos
     * @param entityBaseInfosForVersion
     */
    private static void classifyFileRefedInfo(EntityBaseInfo[] entityInfos,
                                              List<EntityBaseInfo> entityBaseInfos,
                                              List<EntityBaseInfo> entityBaseInfosForVersion) {
        if (MMArrayUtil.isEmpty(entityInfos)) {
            return;
        }
        for (EntityBaseInfo entityInfo : entityInfos) {
            if (entityInfo instanceof EntityInfoForVersion) {
                entityBaseInfosForVersion.add(entityInfo);
            } else {
                entityBaseInfos.add(entityInfo);
            }
        }
    }
}