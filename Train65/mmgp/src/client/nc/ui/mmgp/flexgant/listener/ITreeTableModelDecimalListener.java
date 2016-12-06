package nc.ui.mmgp.flexgant.listener;

import nc.ui.mmgp.flexgant.treetable.MMGPGantItem;
import nc.ui.pubapp.gantt.model.AppGanttChartNode;

/**
 * @Description: 精度监听接口
    <p>
          详细功能描述
    </p>
 * @data:2014-5-29上午9:40:04
 * @author: tangxya
 */
public interface ITreeTableModelDecimalListener extends java.util.EventListener{
	
	 /**
     * 我们只提供一个财务精度和业务精度两种类型（因我们提供了两个的默认实现）
     * 如果业务组要自己添加精度监听便必须设置精度类型值为(2~10)
     */


    /**
     * 得到欲设置的源ItemKey
     */
    public String getSource();

//    /**
//     * 从一个item及行得到其精度值
//     */
//    public int getDecimalFromItem(int row, BillItem item);

    /**
     * 从一个对应的值及node来得到其精度值
     */
    public int getDecimalFromSource(AppGanttChartNode node, Object okValue);
    
    
    //下面两个方法没有用，但是现在为了其它业务组的可能继承，故继续保留之

    /**
     *  判断是否为要设置的目标列
     *
     */
    public boolean isTarget(MMGPGantItem item);  
    
    
    /**
     * 得到需要增加监听的目标列
     */
    public String[] getTarget();


}
