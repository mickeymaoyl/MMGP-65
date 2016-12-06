package nc.ui.mmgp.flexgant.view.timelineobject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.dlsc.flexgantt.model.TimeSpan;
import com.dlsc.flexgantt.model.gantt.RelationshipType;
import com.dlsc.flexgantt.util.LabelType;

import nc.ui.pubapp.gantt.model.AppGantContext;
import nc.ui.pubapp.gantt.model.AppTimelineObject;
import nc.vo.mmgp.util.MMMapUtil;
/**
 * 甘特图普通的时间线 
 * @author tangxya
 *
 */
public class MMGPTimelineObject extends AppTimelineObject {

    private String layername;

    private Map<Object, RelationshipType> relatnTypeIndex;

    public MMGPTimelineObject(Object object,
                              AppGantContext context) {
        super(object, context);
    }

    public MMGPTimelineObject() {
        super();
    }

    public String getLayername() {
        return layername;
    }

    public void setLayername(String layername) {
        this.layername = layername;
    }

    @SuppressWarnings("unchecked")
    public void synchronizedFromUserObj() {
        if (this.getUserObject() != null && this.getValueAdapter() != null) {
            String name = this.getValueAdapter().getName(this.getUserObject());
            setProperty(PROPERTY_NAME, name);
            Date startTime2 = this.getValueAdapter().getStartTime(this.getUserObject());
            Long startTime = startTime2 == null ? null : startTime2.getTime();
            setProperty(PROPERTY_STARTTIME, startTime);
            Date endTime2 = this.getValueAdapter().getEndTime(this.getUserObject());
            Long endTime = endTime2 == null ? null : endTime2.getTime();
            String label = this.getValueAdapter().getLabelName(this.getUserObject());
            // label指tips下面显示百分比的刻度。
            // String label = String.valueOf(this.getValueAdapter().getPercentageComplete(this.getUserObject()));
            // 显示单据号
            String popupTitle = this.getValueAdapter().getPopupTitle(this.getUserObject());
            Object relation = this.getValueAdapter().getRelationType(this.getUserObject());
            if (relation instanceof RelationshipType) {
                RelationshipType relationShip = (RelationshipType) relation;
                if (relationShip != null) {
                    this.setRelationshipType(relationShip);
                }
            } else if (relation instanceof Map< ? , ? >) {
                Map<Object, RelationshipType> relatnTypeIndex = (Map<Object, RelationshipType>) relation;
                if(MMMapUtil.isNotEmpty(relatnTypeIndex)){
                    this.setRelatnTypeIndex(relatnTypeIndex);
                }
            }
            if (endTime == null || startTime == null || endTime.longValue() < startTime.longValue()) {
                return;
            }
            setProperty(PROPERTY_ENDTIME, endTime);
            this.setLabel(label, LabelType.NAME);
            this.setTimeSpan(new TimeSpan(startTime, endTime));
            this.setPopupTitleObject(popupTitle);
            this.setPopupObject(this, false);

        }
    }

    public void putRelatnType(Object pre,
                              RelationshipType relatnType) {
        if (relatnTypeIndex == null) {
            relatnTypeIndex = new HashMap<Object, RelationshipType>();
        }
        relatnTypeIndex.put(pre, relatnType);
    }

    public RelationshipType getRelatnType(Object pre) {
        if (relatnTypeIndex == null) {
            return null;
        }
        return relatnTypeIndex.get(pre);
    }

    public Map<Object, RelationshipType> getRelatnTypeIndex() {
        return relatnTypeIndex;
    }

    public void setRelatnTypeIndex(Map<Object, RelationshipType> relatnTypeIndex) {
        this.relatnTypeIndex = relatnTypeIndex;
    }
}
