package nc.ui.mmgp.flexgant.view.timelineobject;

import nc.ui.pubapp.gantt.model.AppGantContext;

import com.dlsc.flexgantt.model.gantt.IActivityObject;
/**
 * ����ͼ�Ҳ�ʱ����    ��Ҫ��ʾ������ɰٷֱ���ʱ�������ô���
 * @author tangxya
 *
 */
public class MMGPActiveTimelineObject extends MMGPTimelineObject implements IActivityObject{
    
    private double percentageComplete;

    public MMGPActiveTimelineObject(Object userObj, AppGantContext context){
        super(userObj,context);
    }

    @SuppressWarnings("unchecked")
    public void synchronizedFromUserObj() {
        super.synchronizedFromUserObj();
        
        if (this.getUserObject() != null && getValueAdapter() != null) {
            setPercentageComplete(getValueAdapter().getPercentageComplete(this.getUserObject()));
        }
         
    }
    
    @Override
    public double getPercentageComplete() {
        return percentageComplete;
    }

    @Override
    public boolean isPercentageChangeable() {
        return false;
    }

    @Override
    public void setPercentageComplete(double p) {
        this.percentageComplete = p;
    }

}
