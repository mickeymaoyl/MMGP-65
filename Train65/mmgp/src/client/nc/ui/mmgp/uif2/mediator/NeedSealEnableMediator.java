package nc.ui.mmgp.uif2.mediator;

import nc.ui.mmgp.uif2.model.INeedSeal;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 支持启用/停用 的DataManager控制类。将实现了INeedSeal接口的DataManager注入即可。需要启用/停用功能的节点添加此Mediator
 * </p>
 * 
 * @since  创建日期:Apr 2, 2013
 * @author wangweir
 */
public class NeedSealEnableMediator {

    private INeedSeal needSeal;

    /**
     * @return the needSeal
     */
    public INeedSeal getNeedSeal() {
        return needSeal;
    }

    /**
     * @param needSeal
     *        the needSeal to set
     */
    public void setNeedSeal(INeedSeal needSeal) {
        this.needSeal = needSeal;
        this.needSeal.setNeedSeal(true);
    }

}
