package nc.bs.mmgp.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import nc.bs.logging.Logger;
import nc.bs.trade.business.HYSuperDMO;
import nc.itf.mmgp.bean.IMMBDMulUniqueCheckVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.trade.bdinfo.IBdinfoContext;
import nc.vo.trade.comcheckunique.TradBusiException;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.summarize.Hashlize;
import nc.vo.trade.summarize.IHashKey;
import nc.vo.trade.summarize.VOHashKeyAdapter;
import nc.vo.trade.summarize.VOHashPrimaryKeyAdapter;

/**
 * <b> ����UAPΨһ��У����룬ʹ֮��������Ҫ�� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * ��������:2011-6-20
 * 
 * @author Administrator
 * @deprecated ʹ��V6���·�ʽ���
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class MMBDUniqueCheck {
    private List<SuperVO> addedAndUpdatedVOs;

    private List<SuperVO> deletedVOs;

    /**
     * BillIsUnApprove ������ע�⡣
     */
    public MMBDUniqueCheck() {
        super();
    }

    /**
     * @return void
     */

    public void checkBDisUnique(HYBillVO billVO,
                                IMMBDMulUniqueCheckVO multiUniqueCheckVO) throws BusinessException {

        try {
            if (billVO == null) {
                return;
            }

            addedAndUpdatedVOs = new ArrayList<SuperVO>();
            deletedVOs = new ArrayList<SuperVO>();

            classfyVOs(getToBeCheckedVOs(billVO, multiUniqueCheckVO));

            // û�������ͱ༭��VO��û�п�У������ݣ�ֱ�ӷ���
            if (addedAndUpdatedVOs.size() == 0) {
                return;
            }

            // ȷ�������ĺ��޸���������û���ظ���
            verifyAddedAndUpdatedVOs(billVO, multiUniqueCheckVO);

            ArrayList<Object[]> valueArray = new ArrayList<Object[]>();
            // keyArray
            ArrayList<String[]> keyArray = new ArrayList<String[]>();
            // shownameArray
            ArrayList<String[]> shownameArray = new ArrayList<String[]>();
            //
            ArrayList<String[]> bdinfoKeyArray = new ArrayList<String[]>();

            for (int m = 0; m < addedAndUpdatedVOs.size(); m++) {

                SuperVO item = (SuperVO) addedAndUpdatedVOs.get(m);
                Vector<String> keyV = new Vector<String>();
                Vector<Object> valueV = new Vector<Object>();
                Vector<String> shownameV = new Vector<String>();
                Vector<String> bdinfoV = new Vector<String>();
                String[] uniqueFields = (String[]) multiUniqueCheckVO.getUniqueCheckFields();

                String[] nameArray = (String[]) multiUniqueCheckVO.getUniqueCheckFieldNames();

                String[] bdinfoArray = getBdinfoArray(multiUniqueCheckVO);

                if (!isAllUniqueFieldsIsNull(item, uniqueFields)) {
                    checkVOIsUnique(item, uniqueFields, nameArray, bdinfoArray, keyV, valueV, shownameV, bdinfoV);
                }
                if (keyV.size() > 0) {
                    keyArray.add((String[]) keyV.toArray(new String[keyV.size()]));
                    shownameArray.add((String[]) shownameV.toArray(new String[shownameV.size()]));
                    valueArray.add((Object[]) valueV.toArray(new Object[valueV.size()]));
                    bdinfoKeyArray.add((String[]) bdinfoV.toArray(new String[bdinfoV.size()]));

                }
            }

            if (keyArray.size() == 0) {
                return;
            }
            // ����ExceptionVO
            BusinessException exception = createException(billVO, keyArray, shownameArray, valueArray, bdinfoKeyArray);
            throw exception;
        } catch (BusinessException be) {
            throw (BusinessException) be;
        } catch (Exception e) {
            Logger.error(e);
            throw new BusinessException(e.getMessage());
        }
        // return;
    }

    /**
     * @param check
     * @param i
     * @return
     */
    private String[] getBdinfoArray(IMMBDMulUniqueCheckVO check) {
        String[] nameArray = check.getUniqueCheckFieldNames();
        String[] bdinfoKeys = new String[nameArray.length];
        for (int j = 0; j < bdinfoKeys.length; j++) {
            bdinfoKeys[j] = IBdinfoContext.BDINFOCONST_NOT_A_DOC;
        }
        return bdinfoKeys;

    }

    /**
     * @param item
     * @param uniqueFields
     * @return
     */
    private boolean isAllUniqueFieldsIsNull(SuperVO item,
                                            String[] uniqueFields) {
        for (int i = 0; i < uniqueFields.length; i++) {
            String fieldName = uniqueFields[i];
            if (item.getAttributeValue(fieldName) != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * ȷ���������޸ĵĵ�����û���ظ�
     * 
     * @param addedAndUpdatedVOs2
     * @param check
     */
	private void verifyAddedAndUpdatedVOs(HYBillVO billVO,
                                          IMMBDMulUniqueCheckVO check) throws BusinessException {
        ArrayList keys = new ArrayList();
        ArrayList names = new ArrayList();
        ArrayList values = new ArrayList();
        ArrayList bdinfos = new ArrayList();

        ArrayList tmpkeys = new ArrayList();
        ArrayList tmpnames = new ArrayList();
        ArrayList tmpvalues = new ArrayList();
        ArrayList tmpbdinfos = new ArrayList();

        String[] uniqueFields = check.getUniqueCheckFields();

        String[] nameArray = check.getUniqueCheckFieldNames();

        String[] bdinfoArray = getBdinfoArray(check);

        Set keyFieldValueSet = new HashSet();
        Map duplicatedVosMap = new HashMap();
        Iterator it = addedAndUpdatedVOs.iterator();
        IHashKey keyGetter = new VOHashKeyAdapter(uniqueFields);
        for (; it.hasNext();) {
            SuperVO vo = (SuperVO) it.next();
            String key = keyGetter.getKey(vo);
            if (!keyFieldValueSet.contains(key)) {
                keyFieldValueSet.add(key);
            } else {
                duplicatedVosMap.put(key, vo);
            }

        }
        SuperVO[] vos = (SuperVO[]) duplicatedVosMap.values().toArray(new SuperVO[duplicatedVosMap.values().size()]);
        for (int j = 0; j < vos.length; j++) {
            for (int k = 0; k < uniqueFields.length; k++) {
                tmpkeys.add(uniqueFields[k]);
                tmpnames.add(nameArray[k]);
                tmpvalues.add(vos[j].getAttributeValue(uniqueFields[k]));
                tmpbdinfos.add(bdinfoArray[k]);

            }

        }
        if (tmpkeys.size() > 0) {
            keys.add((String[]) tmpkeys.toArray(new String[tmpkeys.size()]));
            names.add((String[]) tmpnames.toArray(new String[tmpnames.size()]));
            values.add((Object[]) tmpvalues.toArray(new Object[tmpvalues.size()]));
            bdinfos.add((String[]) tmpbdinfos.toArray(new String[tmpbdinfos.size()]));
        }
        if (keys.size() > 0) {
            BusinessException exception = createException(billVO, keys, names, values, bdinfos);
            throw exception;
        }

    }

    /**
     * @param check
     * @param keys
     * @param names
     * @param values
     * @return
     */
    private BusinessException createException(HYBillVO billVO,
                                              ArrayList keys,
                                              ArrayList names,
                                              ArrayList values,
                                              ArrayList bdinfos) {
        TradBusiException exception = new TradBusiException();
        exception.setKeyArray(keys);
        exception.setShownameArray(names);
        exception.setValueArray(values);
        exception.setBdinfoArray(bdinfos);
        exception.setBody(billVO.getParentVO() == null);

        return new BusinessException(exception.getMessage());
    }

    /**
     * @param item
     * @param uniqueFields
     * @param nameArray
     * @param keyV
     * @param valueV
     * @param shownameV
     * @throws Exception
     */
    private void checkVOIsUnique(SuperVO item,
                                 String[] uniqueFields,
                                 String[] nameArray,
                                 String[] bdinfoArray,
                                 Vector<String> keyV,
                                 Vector<Object> valueV,
                                 Vector<String> shownameV,
                                 Vector<String> bdinfoV) throws Exception {
        SuperVO filterVo = null;

        List nullValueFields = new ArrayList();
        // ���������
        filterVo = createFilterVO(item, uniqueFields, nullValueFields);

        // �����ݿ��в�ѯ
        List l = queryByVO(filterVo, Boolean.TRUE, nullValueFields);
        // �����ݿ����Ľ����ȥ(���ϼ���)��ǰҪɾ��������
        List t = vosSubtractVos(l, deletedVOs);
        // Ȼ���ټ�ȥ�����ͱ༭������
        List r = vosSubtractVos(t, addedAndUpdatedVOs);

        SuperVO[] results = (SuperVO[]) r.toArray(new SuperVO[r.size()]);

        for (int h = 0; h < results.length; h++) {
            String resultKey = results[h].getPrimaryKey();

            // �ų����������˵��itemΥ����Ψһ��Լ��
            if (!isDoUniqueCheck(item, results[h])) {
                continue;
            }
            if (!resultKey.trim().equalsIgnoreCase(item.getPrimaryKey())) {
                for (int k = 0; k < nameArray.length; k++) {
                    keyV.add(uniqueFields[k]);
                    valueV.add(item.getAttributeValue(uniqueFields[k]));
                    shownameV.add(nameArray[k]);

                    bdinfoV.add(bdinfoArray[k]);

                }
                break;
            }
        }
    }

    /**
     * @param item
     *        :��ǰ�������������޸Ĺ�������
     * @param result
     *        �����ݿ��д��Ƚ����� return �����true��������ظ�У�飬���򣬲����룡
     */
    private boolean isDoUniqueCheck(SuperVO item,
                                    SuperVO result) {
        return true;
    }

    /**
     * ��vos1��vos2��VO���������ΪVO��ȱ�׼���������ϼ���
     * 
     * @param vos1
     * @param vos2
     * @return
     */
    private List vosSubtractVos(List vos1,
                                List vos2) {
        List result = new ArrayList();
        if (vos1 == null || vos1.size() == 0) {
            return result;
        }
        HashMap tmpMap =
                Hashlize.hashlizeObjects(
                    (SuperVO[]) vos1.toArray(new SuperVO[vos1.size()]),
                    new VOHashPrimaryKeyAdapter());
        for (Iterator iter = vos2.iterator(); iter.hasNext();) {
            SuperVO vo = (SuperVO) iter.next();
            if (tmpMap.containsKey(vo.getPrimaryKey())) {
                tmpMap.remove(vo.getPrimaryKey());
            }
        }
        // Iterator it = tmpMap.keySet().iterator();
        // for (; it.hasNext();) {
        // Object key = it.next();
        // ArrayList al = (ArrayList) tmpMap.get(key);
        // if (al != null && al.size() != 0) {
        // result.add(al.get(0));
        // }
        // }
        Set<Entry> entrySet = tmpMap.entrySet();
        for (Entry entry : entrySet) {
            ArrayList value = (ArrayList) entry.getValue();
            if (value != null && value.size() != 0) {
                result.add(value.get(0));
            }
        }
        return result;
    }

    /**
     * ������Ϊ��ѯ������VO,QueryObjectģʽ
     * 
     * @param item
     * @param uniqueFields
     * @param nullValueFields
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private SuperVO createFilterVO(SuperVO item,
                                   String[] uniqueFields,
                                   List nullValueFields) throws InstantiationException, IllegalAccessException {
        SuperVO filterVo;
        filterVo = (SuperVO) item.getClass().newInstance();

        if (uniqueFields == null) {
            return filterVo;
        }

        for (int j = 0; j < uniqueFields.length; j++) {
            Object value = item.getAttributeValue(uniqueFields[j]);
            if (value == null || "".equals(value)) {
                nullValueFields.add(uniqueFields[j]);
            } else {
                filterVo.setAttributeValue(uniqueFields[j], value);
            }
        }
        return filterVo;
    }

    /**
     * ��VO����items�е����ݽ��з��࣬�ֳ����顣һ��Ϊ�����͸ı��VO����һ��Ϊ��ɾ����VO
     * 
     * @param items
     */
    private void classfyVOs(SuperVO[] items) {
        if (items == null || items.length == 0) {
            return;
        }
        for (int i = 0; i < items.length; i++) {
            if (items[i].getStatus() == nc.vo.pub.VOStatus.DELETED) {
                if (items[i].getPrimaryKey() != null) {
                    deletedVOs.add(items[i]);
                }
            } else {
                addedAndUpdatedVOs.add(items[i]);
            }
        }
    }

    /**
     * @param billVO
     * @param check
     * @return
     */
    private SuperVO[] getToBeCheckedVOs(HYBillVO billVO,
                                        IMMBDMulUniqueCheckVO check) {
        SuperVO[] items;
        if (isCheckUniqueByHeadVO(billVO)) {
            if ((SuperVO) billVO.getParentVO() == null) {
                items = new SuperVO[0];
            } else {
                items = new SuperVO[]{(SuperVO) billVO.getParentVO() };
            }

        } else {
            items = (SuperVO[]) billVO.getChildrenVO();
            if (items == null || items.length == 0) {
                items = new SuperVO[0];
            }
        }
        return items;
    }

    /**
     * �ж��Ƿ���ݱ�ͷ���ж�Ψһ��
     * 
     * @param check
     * @return
     */
    private boolean isCheckUniqueByHeadVO(HYBillVO vo) {
        return vo.getParentVO() != null;
    }

    /**
     * ����VO�����趨�������������з���������VO���� �������ڣ�(2002-6-3)
     * 
     * @return SuperVO[]
     * @param vo
     *        SuperVO
     * @param isAnd
     *        boolean ����������ѯ�����Ի�������ѯ
     * @exception Exception
     *            �쳣˵����
     */
    public List queryByVO(SuperVO vo,
                          Boolean isAnd,
                          List nullFields) throws Exception {
        HYSuperDMO dmo = new HYSuperDMO();

        SuperVO[] vos = dmo.queryByVO(vo, isAnd);

        List vosWithoutLogicalDeleted = new ArrayList();
        // �޳�ɾ��״̬
        if (vos != null) {
            for (int i = 0; i < vos.length; i++) {
                if (vos[i].getAttributeValue("dr") != null) {
                    if (Integer.parseInt(vos[i].getAttributeValue("dr").toString()) > 0) {
                        continue;
                    }
                }
                vosWithoutLogicalDeleted.add(vos[i]);
            }
        }
        if (nullFields == null || nullFields.size() == 0) {
            return vosWithoutLogicalDeleted;
        }

        List tempV = new ArrayList();
        for (int i = 0; i < vosWithoutLogicalDeleted.size(); i++) {
            SuperVO tmpVO = (SuperVO) vosWithoutLogicalDeleted.get(i);
            if (isAllFileldNull(tmpVO, nullFields)) {
                tempV.add(tmpVO);
            }
        }

        return tempV;

    }

    /**
     * @param tmpVO
     * @param nullFields
     * @return
     */
    private boolean isAllFileldNull(SuperVO tmpVO,
                                    List nullFields) {
        for (int j = 0; j < nullFields.size(); j++) {
            Object value = tmpVO.getAttributeValue((String) nullFields.get(j));
            if (value != null && !"".equals(value)) {
                return false;
            }
        }
        return true;
    }
}
