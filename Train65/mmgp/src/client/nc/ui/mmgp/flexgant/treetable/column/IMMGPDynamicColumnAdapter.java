package nc.ui.mmgp.flexgant.treetable.column;

import java.util.List;

import nc.ui.pubapp.gantt.ui.treetable.IGantItem;

/**
 * @Description: TODO
    <p>
          ��ϸ��������
    </p>
 * @data:2014-5-20����7:14:30
 * @author: tangxya
 */
public interface IMMGPDynamicColumnAdapter {
	public List<IGantItem> getGantColumns(int fromindex);

}
