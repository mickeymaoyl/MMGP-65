package nc.ui.mmgp.flexgant.view.timelineobject;

import nc.ui.pubapp.gantt.model.AppGantContext;

import com.dlsc.flexgantt.model.gantt.IActivityObject;
/**
 * 甘特图右侧时间线    需要显示类似完成百分比在时间线上用此类
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
