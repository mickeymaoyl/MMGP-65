package nc.vo.mmgp.uif2;

import java.util.HashMap;
import java.util.Map;

import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

/**
 * һ�����ӵ���Ԫ���ݵĴ�����������֤Ԫ����ֻ�ᱻ����һ�Ρ���ʡ����ʱ����ڴ濪��
 * 
 * @since 6.0
 * @version 2008-7-24 ����04:30:42
 * @author
 */
public class MMGPBillMetaFactory {
    /**
     * һ�����ӵ���Ԫ���ݵĴ�������ʵ��
     */
    private static MMGPBillMetaFactory instance = new MMGPBillMetaFactory();

    /**
     * ����Ԫ����ȫ·�����͵���Ԫ���ݵ�ӳ��
     */
    private Map<String, IBillMeta> nameIndex = new HashMap<String, IBillMeta>();

    private MMGPBillMetaFactory() {
        // ȱʡ���췽��
    }

    /**
     * һ�����ӵ���Ԫ���ݵĴ�����������
     * 
     * @return һ�����ӵ���Ԫ���ݵĴ�������ʵ��
     */
    public static MMGPBillMetaFactory getInstance() {
        return MMGPBillMetaFactory.instance;
    }

    /**
     * ���ݵ���Ԫ����ȫ·������ȡ����Ԫ����
     * 
     * @param mainEntityName
     *        ����Ԫ����ȫ·����
     * @return ����Ԫ����
     */
    public IBillMeta getBillMeta(String mainEntityName) {
        IBillMeta billMeta = null;
        synchronized (MMGPBillMetaFactory.instance) {
            billMeta = this.nameIndex.get(mainEntityName);
            if (billMeta == null) {
                billMeta = new MMGPAggBillMeta(mainEntityName);
                this.nameIndex.put(mainEntityName, billMeta);
            }
        }
        return billMeta;
    }
}
