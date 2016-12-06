package nc.ui.mmgp.flexgant.listener;

import nc.ui.mmgp.flexgant.treetable.MMGPGantItem;
import nc.ui.pubapp.gantt.model.AppGanttChartNode;

/**
 * @Description: ���ȼ����ӿ�
    <p>
          ��ϸ��������
    </p>
 * @data:2014-5-29����9:40:04
 * @author: tangxya
 */
public interface ITreeTableModelDecimalListener extends java.util.EventListener{
	
	 /**
     * ����ֻ�ṩһ�����񾫶Ⱥ�ҵ�񾫶��������ͣ��������ṩ��������Ĭ��ʵ�֣�
     * ���ҵ����Ҫ�Լ���Ӿ��ȼ�����������þ�������ֵΪ(2~10)
     */


    /**
     * �õ������õ�ԴItemKey
     */
    public String getSource();

//    /**
//     * ��һ��item���еõ��侫��ֵ
//     */
//    public int getDecimalFromItem(int row, BillItem item);

    /**
     * ��һ����Ӧ��ֵ��node���õ��侫��ֵ
     */
    public int getDecimalFromSource(AppGanttChartNode node, Object okValue);
    
    
    //������������û���ã���������Ϊ������ҵ����Ŀ��ܼ̳У��ʼ�������֮

    /**
     *  �ж��Ƿ�ΪҪ���õ�Ŀ����
     *
     */
    public boolean isTarget(MMGPGantItem item);  
    
    
    /**
     * �õ���Ҫ���Ӽ�����Ŀ����
     */
    public String[] getTarget();


}
