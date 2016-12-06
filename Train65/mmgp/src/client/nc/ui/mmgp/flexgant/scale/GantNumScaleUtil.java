package nc.ui.mmgp.flexgant.scale;

import java.util.List;

import nc.ui.mmgp.uif2.scale.MMGPNumScaleBean;
import nc.ui.mmgp.uif2.scale.MMGPScaleBean;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMListUtil;

/**
 * @Description: TODO
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-5-30下午2:48:10
 * @author: tangxya
 */
public class GantNumScaleUtil {

    public static void setScale(MMGPGantScaleProcessor scaleprocess,
                                MMGPNumScaleBean[] scaleBeans) {

        if (MMArrayUtil.isEmpty(scaleBeans)) {
            return;
        }
        for (MMGPNumScaleBean scaleBean : scaleBeans) {
            List<String> numKeyList = scaleBean.getNumkeys();
            if (MMListUtil.isEmpty(numKeyList)) {
                continue;
            }

            String[] numKeys = numKeyList.toArray(new String[0]);

            switch (scaleBean.getType()) {
                case NumAssNum:
                    scaleprocess.setNumCtlInfo(numKeys, scaleBean.getUnitkey());
                    break;
                case HSL:
                    // 支持换算率
                    /* scaleprocess.setHslCtlInfo(numKeys); */
                    break;
                case WEIGHT:
                    /* scaleprocess.setWeightCtlInfo(numKeys); */
                    break;
               
            }
        }

        scaleprocess.process();
    }

    public static void setScale(MMGPGantScaleProcessor scaleprocess,
                                MMGPScaleBean[] scaleBeans) {

        if (MMArrayUtil.isEmpty(scaleBeans)) {
            return;
        }
        for (MMGPScaleBean scaleBean : scaleBeans) {
            List<String> numKeyList = scaleBean.getScalekeys();
            if (MMListUtil.isEmpty(numKeyList)) {
                continue;
            }

            String[] numKeys = numKeyList.toArray(new String[0]);

            switch (scaleBean.getType()) {
                case NumAssNum:
                    scaleprocess.setNumCtlInfo(numKeys, scaleBean.getRefkey());
                    break;
                case HSL:
                    // 支持换算率
                    /* scaleprocess.setHslCtlInfo(numKeys); */
                    break;
                case WEIGHT:
                    /* scaleprocess.setWeightCtlInfo(numKeys); */
                    break;
                case Param:
                	scaleprocess.setParamCtlInfo(numKeys, scaleBean.getRefkey());
                break;
                case Param_MeasDoc:
                	scaleprocess.setParamMeasdocCtlInfo(numKeys, scaleBean.getRefkey());
                break;
            }
        }

        scaleprocess.process();
    }

}
