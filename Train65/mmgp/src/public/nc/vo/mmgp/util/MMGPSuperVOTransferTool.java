package nc.vo.mmgp.util;

import java.util.Set;

import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.data.vo.tool.VOConcurrentTool;
import nc.mddb.constant.ElementConstant;
import nc.vo.pub.IAttributeMeta;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.log.TimeLog;
import nc.vo.pubapp.pattern.model.meta.entity.vo.PseudoColumnAttribute;
import nc.vo.pubapp.pattern.model.tool.VOTool;
import nc.vo.pubapp.pattern.pub.Constructor;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 *
 * @since 创建日期 Jul 15, 2013
 * @author wangweir
 */
public class MMGPSuperVOTransferTool<E extends SuperVO> {
    /**
     * 客户算传来的单据
     */
    private E[] clientVOs;

    /**
     * 数据库中存在的原始单据
     */
    private E[] originVOs;

    /**
     * 前后台单据传输工具构造函数
     *
     * @param bills
     *        单据实体
     */
    public MMGPSuperVOTransferTool(E[] bills) {
        // 为了防止网络中断导致单据重复增加，此处新增单据的主键可能已经设置
        if ((bills[0].getPrimaryKey() == null) || (bills[0].getStatus() == VOStatus.NEW)) {
            this.initInserted(bills);
        } else {
            this.initUpdateed(bills);
        }
    }

    /**
     * 与前台单据实体作比较，获取需要传递到前台的单据实体快照
     *
     * @param bills
     *        完整的单据实体
     * @return 需要传递到前台的单据实体快照
     */
    @SuppressWarnings({"unchecked" })
    public E[] getBillForToClient(E[] bills) {
        E[] newBills = (E[]) Constructor.construct(bills[0].getClass(), bills.length);
        for (int i = 0; i < bills.length; i++) {
            newBills[i] = this.contruct(this.clientVOs[i], bills[i]);
        }

        return newBills;
    }

    @SuppressWarnings("unchecked")
    protected E contruct(E clientVO,
                         E vo) {
        if (clientVO == null) {
            return null;
        }
        E newVO = (E) Constructor.construct(clientVO.getClass());
        VOTool tool = new VOTool();
        Set<String> set = tool.getDifferentFieldForDynamic(vo, clientVO);
        this.appendMandatoryKey(set, vo);
        for (String name : set) {
            Object value = vo.getAttributeValue(name);
            newVO.setAttributeValue(name, value);
        }
        String name = PseudoColumnAttribute.PSEUDOCOLUMN;
        Object value = vo.getAttributeValue(name);
        newVO.setAttributeValue(name, value);
        return newVO;
    }

    protected void appendMandatoryKey(Set<String> set,
                                      ISuperVO vo) {
        IAttributeMeta keyMeta = vo.getMetaData().getPrimaryAttribute();
        set.add(keyMeta.getName());
        set.add(ElementConstant.KEY_TS);
    }

    /**
     * 获取前台的完整单据实体
     *
     * @return 前台的完整单据实体
     */
    @SuppressWarnings("unchecked")
    public E[] getClientFullInfoBill() {
        // 克隆是为了方便返回前台时知道后台保存中又改变了什么字段的值
        int length = this.clientVOs.length;
        E[] bills = (E[]) Constructor.construct(this.clientVOs[0].getClass(), length);
        for (int i = 0; i < length; i++) {
            bills[i] = (E) this.clientVOs[i].clone();
        }
        return bills;
    }

    /**
     * 获取数据库中存放的完整原始单据实体
     *
     * @return 数据库中存放的完整原始单据实体
     */
    public E[] getOriginBills() {
        return this.originVOs;
    }

    @SuppressWarnings("unchecked")
    private void initInserted(E[] bills) {
        int size = bills.length;
        E[] vos = (E[]) Constructor.construct(bills[0].getClass(), size);
        for (int i = 0; i < size; i++) {
            vos[i] = (E) bills[i].clone();
        }
        this.originVOs = vos;
        this.clientVOs = vos;
    }

    @SuppressWarnings({"unchecked", "rawtypes" })
    private void initUpdateed(E[] bills) {
        VOConcurrentTool tool = new VOConcurrentTool();
        TimeLog.logStart();
        tool.lock(bills);
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0077")/*@res "锁定表头、表体主健"*/); /* -=notranslate=- */

        TimeLog.logStart();
        String[] ids = new String[bills.length];
        int length = bills.length;
        for (int i = 0; i < length; i++) {
            ids[i] = bills[i].getPrimaryKey();
        }
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0071")/*@res "获取单据主健"*/); /* -=notranslate=- */

        TimeLog.logStart();
        VOQuery query = new VOQuery(bills[0].getClass());
        this.originVOs = (E[]) query.query(ids);
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0072")/*@res "查询原始单据VO"*/); /* -=notranslate=- */

        TimeLog.logStart();
        length = this.originVOs.length;
        E[] vos = (E[]) Constructor.construct(bills[0].getClass(), length);
        for (int i = 0; i < length; i++) {
            vos[i] = (E) this.originVOs[i].clone();
        }
        VOTool combineClient = new VOTool();
        for (int i = 0; i < length; i++) {
            combineClient.combine(vos[i], bills[i]);
        }
        this.clientVOs = vos;
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0073")/*@res "前台单据VO补充完整"*/); /* -=notranslate=- */

        TimeLog.logStart();
        tool.checkTS(bills, this.originVOs);
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0074")/*@res "检查时间戳"*/); /* -=notranslate=- */
    }
}