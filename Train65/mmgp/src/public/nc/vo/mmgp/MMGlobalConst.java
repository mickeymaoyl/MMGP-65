package nc.vo.mmgp;

import java.util.HashMap;
import java.util.Map;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ȫ�ֳ���
 * </p>
 * 
 * @since �������� May 10, 2013
 * @author wangweir
 */
public class MMGlobalConst {
    private static final String BILL_TYPE_PURBILLLIST = "YG21";

    private static final String BILL_TYPE_ECC1 = "ECC1";

    private static final String BILL_TYPE_PREORDER = "38";

    private static final String BILL_TYPE_BORROWOUT = "4H";

    /**
     * ������������
     */
    private static final String BILL_TYPE_STOREREQ = "422X";

    /**
     * ���뵥
     */
    private static final String BILL_TYPE_BORROWIN = "49";

    /**
     * ���۱��۵�
     */
    private static final String BILL_TYPE_SALEQUOTATION = "4310";

    /**
     * ���ۺ�ͬ
     */
    private static final String BILL_TYPE_CT = "Z3";

    /**
     * �ɹ�����
     */
    private static final String BILL_TYPE_POBALANCE = "27";

    /**
     * �������
     */
    private static final String BILL_TYPE_ASSIN = "YG45";

    /**
     * ת�ⵥ
     */
    public static final String BILL_TYPE_WHTRANS = "4K";

    public static final String BILL_TYPE_SALEORDER = "30";

    /**
     * 
     */
    public static final String UMES_FILESYS = "80";

    /** ��֯ */
    public static final String PK_ORG = "pk_org";

    /** ��֯�汾 */
    public static final String PK_ORG_V = "pk_org_v";

    /** ���� */
    public static final String PK_GROUP = "pk_group";

    /** �������� */
    public static final String VBILLTYPECODE = "vbilltypecode";

    /** ���ݺ� */
    public static final String VBILLCODE = "vbillcode";

    /** �������� */
    public static final String DBILLDATE = "dbilldate";

    /** ����״̬ */
    public static final String FSTATUSFLAG = "fstatusflag";

    /** �Ƶ��� */
    public static final String VBILLMAKER = "vbillmaker";

    /** �Ƶ�ʱ�� */
    public static final String DMAKEDATE = "dmakedate";

    /** ������ */
    public static final String CREATOR = "creator";

    /** ����ʱ�� */
    public static final String CREATIONTIME = "creationtime";

    /**
     * ������;���ڲ�ѯ������
     */
    public static final String BISAPPROVING = "bisapproving";

    /**
     * ��Դ���ݱ���id
     */
    public static final String VSRCBID = "vsrcbid";

    /**
     * ��Դ��������
     */
    public static final String VSRCTYPE = "vsrctype";

    /**
     * �����ӱ�����
     */
    public static final String BILLBID = "billbid";

    /**
     * Դͷ��������
     */
    public static final String VFIRSTTYPE = "vfirsttype";

    /**
     * Դͷ���ݱ���id
     */
    public static final String VFIRSTBID = "vfirstbid";

    /**
     * �������ͣ�Ӧ�յ�
     */
    public static final String BILL_TYPE_RECEIVABLE = "F0";

    /**
     * �������ͣ��빺��
     */
    public static final String BILL_TYPE_PRAY = "20";

    /**
     * �������ͣ��ɹ�����
     */
    public static final String BILL_TYPE_ORDER = "21";

    /**
     * �������ͣ�������
     */
    public static final String BILL_TYPE_ARRIVE = "23";

    /**
     * �������ͣ���Ŀ�ɹ��ƻ�
     */
    public static final String BILL_TYPE_PROCPLAN = "YG23";

    /** ��ҵ�������� */
    public static final String BILL_TYPE_TASK = "YG65";

    /**
     * ��������
     */
    public static final String BILL_TYPE_TRANSINREQ = "5A";

    /**
     * ��ҵ����
     */
    public static final String BILL_TYPE_TASKREQ = "55A8";

    /**
     * Ͷ�żƻ�
     */
    public static final String BILL_TYPE_PUTPLAN = "55C7";

    /**
     * ��������
     */
    public static final String BILL_TYPE_TO = "5X";

    /**
     * ����ԤͶ
     */
    public static final String BILL_TYPE_COMPONENT = "YG72";

    /**
     * ��������
     */
    public static final String BILL_TYPE_PRODUCE = "55C2";

    /**
     * �깤����
     */
    public static final String BILL_TYPE_WR = "55A4";

    /**
     * ���ϼƻ�
     */
    public static final String BILL_TYPE_PICKM = "55A3";

    /**
     * ѯ���۵�
     */
    public static final String BILL_TYPE_ASKBILL = "29";

    /**
     * ί�мӹ����
     */
    public static final String BILL_TYPE_SUBCONTIN = "47";

    /**
     * �ɹ���Ʊ
     */
    public static final String BILL_TYPE_INVOICE = "25";

    /**
     * �ɹ���ͬ
     */
    public static final String BILL_TYPE_CTPU = "Z2";

    /**
     * �۸�����
     */
    public static final String BILL_TYPE_PRICEAUDIT = "28";

    /**
     * ί�ⶩ��
     */
    public static final String BILL_TYPE_SCORDER = "61";

    /**
     * �տ
     */
    public static final String BILL_TYPE_GATHERINGBILL_F2 = "F2";

    /**
     * �տ
     */
    public static final String BILL_TYPE_GATHERINGBILL_D2 = "D2";

    /**
     * ����Ʒ��
     */
    public static final String BILL_TYPE_PRODIN = "46";

    /**
     * �ɹ���
     */
    public static final String BILL_TYPE_PURCHASEIN = "45";

    /**
     * �豸��Ƭ
     */
    public static final String BILL_TYPE_EQUIP = "4A00";

    /**
     * ������
     */
    public static final String BILL_TYPE_OTHREROUT = "4I";

    /**
     * �豸��
     */
    private static final String BILL_TYPE_EQUIPIN = "4401";

    /**
     * ����������
     */
    private static final String BILL_TYPE_PRODSCRBIN = "4X";

    /**
     * ������
     */
    private static final String BILL_TYPE_TRANSIN = "4E";

    /**
     * ������
     */
    private static final String BILL_TYPE_OTHERIN = "4A";

    /**
     * ������
     */
    private static final String BILL_TYPE_TRANSOUT = "4Y";

    /**
     * ��������
     */
    private static final String BILL_TYPE_OUTREQ = "4451";

    /**
     * �豸��
     */
    private static final String BILL_TYPE_EQUIPOUT = "4455";

    /**
     * ���۳�
     */
    public static final String BILL_TYPE_SALEOUT = "4C";

    /**
     * ���ϳ�
     */
    public static final String BILL_TYPE_MATROUT = "4D";

    /**
     * ѰԴ��Դ����IDĬ�ϲ���Map<��������,����ID�ֶ�>
     */
    public static final Map<String, String> TASK_TYPE_VSRCBID_MAP = new HashMap<String, String>();

    static {
        // �빺��
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_PRAY, "csourcebid");
        // �ɹ�����
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_ORDER, "csourcebid");
        // ������
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_ARRIVE, "csourcebid");
        // ί�ⶩ��
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_SCORDER, "csrcbid");
        // �۸�������
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_PRICEAUDIT, "csrcbid");
        // �ɹ���ͬ
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_CTPU, "csrcbid");
        // �ɹ���Ʊ
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_INVOICE, "csourcebid");
        // ί�мӹ����
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_SUBCONTIN, "csourcebillbid");
        // ѯ���۵�
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_ASKBILL, "csrcbid");
        // ���ϼƻ�
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_PICKM, "csourcebillrowid");
        // ��������
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_WR, "vbsrcrowid");
        // ��������
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_TO, "csrcbid");
        // ��ɢͶ�żƻ�
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_PUTPLAN, "vsrcrowid");
        // ��ҵ�걨
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_TASKREQ, "csrcrowid");
        // ��������
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_TRANSINREQ, "csrcbid");
        // �ɹ����
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_PURCHASEIN, "csourcebillbid");
        // ����Ʒ���
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_PRODIN, "csourcebillbid");
        // �տ
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_GATHERINGBILL_D2, "src_itemid");
        // �տ
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_GATHERINGBILL_F2, "src_itemid");
        // �豸��Ƭ
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_EQUIP, "pk_bill_b_src");
        // ������
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_OTHREROUT, "csourcebillbid");
        // ���ϳ���
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_MATROUT, "csourcebillbid");
        // ���۳���
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_SALEOUT, "csourcebillbid");
        // �豸����
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_EQUIPOUT, "csourcebillbid");
        // ��������
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_OUTREQ, "csourcebillbid");
        // ����������
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_TRANSOUT, "csourcebillbid");
        // ������
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_OTHERIN, "csourcebillbid");
        // ������
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_TRANSIN, "csourcebillbid");
        // ����������
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_PRODSCRBIN, "csourcebillbid");
        // �豸��
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_EQUIPIN, "csourcebillbid");
        // �ɹ�����
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_POBALANCE, "pk_stock_b");
        // ����
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_SALEORDER, "csrcbid");

        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_WHTRANS, "csourcebillbid");
        // ����������Ĭ��һ��
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_PRODUCE, "vsrcbid");
        // �������
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_ASSIN, "csourcebillbid");
        // ���ۺ�ͬ
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_CT, "csrcbid");
        // ���۱��۵�
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_SALEQUOTATION, "csrcbid");
        // ������������
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_STOREREQ, "csourcebid");
        // �����뵥
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_BORROWIN, "csourcebillbid");
        // �������
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_BORROWOUT, "csourcebillbid");

        // ���
        TASK_TYPE_VSRCBID_MAP.put("YG41", "vsrcbid");
        // ��������
        TASK_TYPE_VSRCBID_MAP.put("YG42", "vsrcbid");
        // ��Ŀ���ת�Ƶ�
        TASK_TYPE_VSRCBID_MAP.put("YG44", "vsrcbid");

        // �ɹ��嵥�����
        TASK_TYPE_VSRCBID_MAP.put("YG22", "vsrcbid");
        // ��Ŀ�ɹ��ƻ�
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_PROCPLAN, "vsrcbid");
        // ��Ŀ�ɹ��������
        TASK_TYPE_VSRCBID_MAP.put("YG24", "vsrcbid");

        // ����ƻ�
        TASK_TYPE_VSRCBID_MAP.put("YG31", "vsrcbid");
        // ���͵�
        TASK_TYPE_VSRCBID_MAP.put("YG32", "vsrcbid");
        // �����������
        TASK_TYPE_VSRCBID_MAP.put("YG33", "vsrcbid");
        // ���Ƽ��ӹ��ƻ�
        TASK_TYPE_VSRCBID_MAP.put("YG35", "vsrcbid");

        // ���
        TASK_TYPE_VSRCBID_MAP.put("YG61", "vsrcbid");
        // ��ҵ�����걨��
        TASK_TYPE_VSRCBID_MAP.put("YG63", "vsrcbid");
        // ��ҵ
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_TASK, "csrcbid");
        // WBS
        TASK_TYPE_VSRCBID_MAP.put("YG66", "csrcbid");
        // �ƻ�ģ��-WBS
        TASK_TYPE_VSRCBID_MAP.put("YG68", "vsrcbid");
        // �ƻ�ģ��-��ҵ
        TASK_TYPE_VSRCBID_MAP.put("YG69", "vsrcbid");

        // ��Ŀ�������ƻ�
        TASK_TYPE_VSRCBID_MAP.put("YG71", "vsrcbid");
        // ����ԤͶ
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_COMPONENT, "vsrcbid");

        // ��Ŀ�ƻ�����
        TASK_TYPE_VSRCBID_MAP.put("YG81", "vsrcbid");

    }
}
