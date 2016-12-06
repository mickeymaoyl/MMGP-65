package nc.ui.mmgp.base;

/**
 * 制造功能点界面状态接口，所有的生产制造节点的界面都应该统一使用此接口定义的界面状态， <br>
 * 如果业务节点有其他状态需要扩充，定义的状态值应该大于200， <br>
 * 这样才能够方便系统状态的扩充，避免状态扩充时不会与业务节点的特殊状态有冲突
 * 
 * @author wangweiu
 * @deprecated by wangweiu 不使用了，参考ui工厂2的方式设置状态
 */
public interface IUIState {
    /**
     * 界面初始化状态
     */
    int STATE_INIT = 0;

    /**
     * 列表状态
     */
    int STATE_LIST = 1;

    /**
     * 查询状态
     */
    int STATE_QUERY = 2;

    /**
     * 浏览状态
     */
    int STATE_CARD_BROWSE = 3;

    /**
     * 增加状态
     */
    int STATE_ADD = 4;

    /**
     * 首次增加状态
     */
    int STATE_ADD_FIRST = 5;

    /**
     * 其他增加状态
     */
    int STATE_ADD_ETC = 6;

    /**
     * 修改状态
     */
    int STATE_MODIFY = 7;

    /**
     * 其他修改状态
     */
    int STATE_MODIFY_ETC = 8;

    /**
     * 其他特殊状态--非列表与卡片界面的其他特殊界面
     */
    int STATE_SPECIAL = 9;

    /**
     * 异常状态
     */
    int STATE_ERROR = 99;
}
