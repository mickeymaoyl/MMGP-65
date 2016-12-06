package nc.bs.mmgp.check;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import nc.bs.mmgp.dao.MMGPCmnDAO;
import nc.vo.mmgp.MMGPLangConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;

/**
 * <b> ʱ�����鹤���� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * ��������:2011-6-20
 * 
 * @author wangweiu
 * @deprecated ʹ��V6���·�ʽ���
 * @see {@link nc.vo.util.BDVersionValidationUtil#validateObject(Object...)}
 */
public class MMGPCheckTsUtil {

    /**
     * ���ʱ���.
     * 
     * @param checkVOs
     *        ����vo
     * @throws BusinessException
     */
    public static <T extends SuperVO> void checkTimeStamp(Class<T> clazz,
                                                          T... checkVOs) throws BusinessException {
        if (isVoEmpty(checkVOs)) {
            return;
        }

        // �µ��ݵ�ts
        Map<String, String> allTsInUI = new Hashtable<String, String>();
        // �޸�ǰԭʼ����ͳ��
        int iNowOriginalRowNum = 0;
        for (int i = 0; i < checkVOs.length; i++) {
            SuperVO vo = checkVOs[i];
            // ͳ�ƴ��뵥���з������е�����
            if (vo.getStatus() != VOStatus.NEW) {
                iNowOriginalRowNum++;
            } else {
                continue;
            }
            if (hasTs(vo)) {
                allTsInUI.put(vo.getPrimaryKey(), vo.getAttributeValue("ts").toString());
            }
        }

        // ------------------ û��ԭ�У���Ϊ���������ݡ����� ----------------------
        if (iNowOriginalRowNum == 0) {
            return;
        }
        // ��ѯ����ԭ���ݵ�ts
        MMGPCmnDAO dao = new MMGPCmnDAO();

        List<T> result = dao.retrieveByPKs(clazz, allTsInUI.keySet().toArray(new String[0]));

        SuperVO[] allBodys = new SuperVO[result.size()];
        int count = 0;
        for (SuperVO o : result) {
            allBodys[count++] = o;
        }
        if (allBodys != null && allBodys.length != iNowOriginalRowNum) {
            throw new BusinessException(MMGPLangConst.HINT_CHECK_TS);
        }
        compareTs(allTsInUI, allBodys);

    }

    private static <T extends SuperVO> boolean isVoEmpty(T... checkVOs) {
        return checkVOs == null || checkVOs.length == 0;
    }

    private static boolean hasTs(SuperVO vo) {
        return vo.getPrimaryKey() != null && vo.getAttributeValue("ts") != null;
    }

    private static void compareTs(Map<String, String> allTsInUI,
                                  SuperVO[] allBodys) throws BusinessException {
        String tsInDB;
        String tsInUI;
        // ��ԭ���ĵ���Ϊ��׼���Ƚ�ts������в�һ�µ�ts���״�
        for (int i = 0; i < allBodys.length; i++) {
            // ��Ҫ�ĳ�ʼ������Ϊû���µ�ts
            tsInUI = null;
            // ��ǰ���е�ts
            tsInDB = allBodys[i].getAttributeValue("ts").toString();
            // ��PKȡts
            if (allBodys[i].getPrimaryKey() != null && allTsInUI.containsKey(allBodys[i].getPrimaryKey())) {
                tsInUI = allTsInUI.get(allBodys[i].getPrimaryKey());
            }
            // ���hashtable���д�PK����ts��һ�£��״�
            if (tsInDB != null && tsInUI != null && !tsInDB.equals(tsInUI)) {
                throw new BusinessException(MMGPLangConst.HINT_CHECK_TS);
            }
        }
    }
}
