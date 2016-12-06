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
 * <b> 时间戳检查工具类 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 创建日期:2011-6-20
 * 
 * @author wangweiu
 * @deprecated 使用V6的新方式检查
 * @see {@link nc.vo.util.BDVersionValidationUtil#validateObject(Object...)}
 */
public class MMGPCheckTsUtil {

    /**
     * 检查时间戳.
     * 
     * @param checkVOs
     *        检查的vo
     * @throws BusinessException
     */
    public static <T extends SuperVO> void checkTimeStamp(Class<T> clazz,
                                                          T... checkVOs) throws BusinessException {
        if (isVoEmpty(checkVOs)) {
            return;
        }

        // 新单据的ts
        Map<String, String> allTsInUI = new Hashtable<String, String>();
        // 修改前原始行数统计
        int iNowOriginalRowNum = 0;
        for (int i = 0; i < checkVOs.length; i++) {
            SuperVO vo = checkVOs[i];
            // 统计传入单据中非新增行的数量
            if (vo.getStatus() != VOStatus.NEW) {
                iNowOriginalRowNum++;
            } else {
                continue;
            }
            if (hasTs(vo)) {
                allTsInUI.put(vo.getPrimaryKey(), vo.getAttributeValue("ts").toString());
            }
        }

        // ------------------ 没有原行，认为是新增单据。返回 ----------------------
        if (iNowOriginalRowNum == 0) {
            return;
        }
        // 查询库中原单据的ts
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
        // 以原来的单据为基准，比较ts，如果有不一致的ts，抛错。
        for (int i = 0; i < allBodys.length; i++) {
            // 必要的初始化，认为没有新的ts
            tsInUI = null;
            // 当前表中的ts
            tsInDB = allBodys[i].getAttributeValue("ts").toString();
            // 按PK取ts
            if (allBodys[i].getPrimaryKey() != null && allTsInUI.containsKey(allBodys[i].getPrimaryKey())) {
                tsInUI = allTsInUI.get(allBodys[i].getPrimaryKey());
            }
            // 如果hashtable中有此PK，但ts不一致，抛错。
            if (tsInDB != null && tsInUI != null && !tsInDB.equals(tsInUI)) {
                throw new BusinessException(MMGPLangConst.HINT_CHECK_TS);
            }
        }
    }
}
