package nc.ui.mmgp.flexgant.view.timelineobject;

import nc.ui.pubapp.gantt.model.AppGantContext;
import nc.ui.pubapp.gantt.model.AppTimelineObject;
import nc.ui.pubapp.gantt.model.IAppTimelineObjectCreateStrategy;
/**
 * @author wangfan3
 * ʱ���ߴ�����
 * 
 * ��д ��������ΪMMGPTimelineObject
 * 
 *
 */
public class MMGPDefaultTimelineObjectCreator implements
        IAppTimelineObjectCreateStrategy {

    @Override
    public AppTimelineObject createAppTimelineObject(Object object,
            AppGantContext context) {
        if(object == null || context == null){
            return null;
        }
        return new MMGPTimelineObject(object,context);
    }

}
