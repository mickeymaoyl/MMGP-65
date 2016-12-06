package nc.itf.mmgp.linkquery;

import java.util.Map;

import nc.vo.mmgp.linkquery.LinkQueryBillNode;
import nc.vo.mmgp.linkquery.LinkQueryParamVO;
import nc.vo.mmgp.util.MMGPMapList;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * <b> ����׷�ݷ����� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since: ��������:2014-10-29
 * @author:liwsh
 */
public interface IMMGPLinkQueryService {

    /**
     * ���ݵ������ͺ͵��ݱ���id�ȣ���ѯ��Ӧ����Դ���ݱ��壨Ŀ�ĵ�������ָ����һ�����ͱ��壩<br\>
     * ���磬�ɹ���ⵥ->�ɹ�������->�ɹ�����->�빺��->��ҵ�� ������Ҫ���ݲɹ���ⵥ�ı���id���ҵ����ж�Ӧ����ҵ������Ϣ<br\>
     * ���ǾͿ��Դ����ɲɹ���ⵥ����id�Ͳɹ���ⵥ�������͹����paramvos����ҵ�ĵ���������Ϊ��destBillType<br\>
     * �������Ҫ���Ӧ�Ĳɹ���������һ�У�destBillType���ɲɹ������ĵ������ͼ��ɡ�
     * 
     * @param paramvos
     *        ���������뵥�ݱ���id��Ϣ
     * @param destBillType
     *        Ŀ�ĵ�������
     * @param vsrcbidkeyMap
     *        ��Դ���ݱ���key��Ϣ��key->�������� value->��Դ���ݱ���id key�� <br\>
     *        ����ÿһ�ŵ��ݵı����ֶ���Դ���ݱ���id������vsrcbid�������޷�����Ԫ����ȷ������ֶε�key.<br\>
     *        �����Ŀǰ�ĵ��ݻ��ݵ�Ŀ�ĵ����м��еĵ��ݱ�����ֶβ���vsrcbid����ͨ�����map���롣
     * @return key-> ���ݱ���id�� value->Ŀ�ĵ������Ͷ�Ӧ����Դ���ݱ���VO
     * @throws BusinessException
     *         ҵ���쳣
     */
    public Map<String, SuperVO> querySrcBodyVO(LinkQueryParamVO[] paramvos,
                                               String destBillType,
                                               Map<String, String> vsrcbidkeyMap) throws BusinessException;

    /**
     * ���ݵ������ͺͱ���ID��ѯָ���������͵����ε���<br/>
     * Ŀǰֻ��PMRP�õ��ýӿڣ�Ϊ�˷�ֹ����������ָֻ���˱��汾�õ������ε������͡�����û���õ��ĵ���������ʱ�����빹����������ͼ�BP��
     * 
     * @param paramvos
     *        ���������뵥�ݱ���id��Ϣ
     * @param destBillType
     *        Ŀ�ĵ�������
     * @param vsrcbidkeyMap
     *        ��Դ���ݱ���key��Ϣ��key->�������� value->��Դ���ݱ���id key�� <br\>
     *        ����ÿһ�ŵ��ݵı����ֶ���Դ���ݱ���id������vsrcbid�������޷�����Ԫ����ȷ������ֶε�key.<br\>
     *        �����Ŀǰ�ĵ��ݻ��ݵ�Ŀ�ĵ����м��еĵ��ݱ�����ֶβ���vsrcbid����ͨ�����map���롣
     * @return key-> ���ݱ���id,�������IDû����Ϊ��ͷID�� value->Ŀ�ĵ������Ͷ�Ӧ����ID�����û����Ϊ��ͷID
     * @throws BusinessException
     */
    public MMGPMapList<String, String> queryForwardBodyVOs(LinkQueryParamVO[] paramvos,
                                                           String destBillType,
                                                           Map<String, String> vsrcbidkeyMap)
            throws BusinessException;

    /**
     * ��ѯ�������е��ݣ����㷶Χ�ڵ�)
     * 
     * @param paramvos
     *        ���������뵥�ݱ���id��Ϣ
     * @param vsrcbidkeyMap
     *        ��Դ���ݱ���key��Ϣ��key->�������� value->��Դ���ݱ���id key�� <br\>
     *        ����ÿһ�ŵ��ݵı����ֶ���Դ���ݱ���id������vsrcbid�������޷�����Ԫ����ȷ������ֶε�key.<br\>
     *        �����Ŀǰ�ĵ��ݻ��ݵ�Ŀ�ĵ����м��еĵ��ݱ�����ֶβ���vsrcbid����ͨ�����map���롣
     * @return key-> ���ݱ���id,�������IDû����Ϊ��ͷID�� value->���ε�������node�ڵ�
     * @throws BusinessException
     */
    public Map<String, LinkQueryBillNode> queryForwardBodyVOs(LinkQueryParamVO[] paramvos,
                                                                      Map<String, String> vsrcbidkeyMap)
            throws BusinessException;

}
