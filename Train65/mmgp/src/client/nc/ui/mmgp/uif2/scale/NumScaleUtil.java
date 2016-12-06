package nc.ui.mmgp.uif2.scale;

import java.util.List;

import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMListUtil;
import nc.vo.pubapp.scale.BillScaleProcessor;
import nc.vo.pubapp.scale.PosEnum;
/**
 * MMGP精度处理工具类
 *
 */
public class NumScaleUtil {
    
    /**
     * 新增加工具类
     * 
     * @param scaleprocess
     * @param scaleBeans
     */
    public static void setScale(BillScaleProcessor scaleprocess,
                                MMGPScaleBean[] scaleBeans) {

        if (MMArrayUtil.isEmpty(scaleBeans)) {
            return;
        }
        for (MMGPScaleBean scaleBean : scaleBeans) {
            List<String> scaleKeys = scaleBean.getScalekeys();
            if (MMListUtil.isEmpty(scaleKeys)) {
                continue;
            }


            String scaletabcode = null;
            String reftabcode = null;

         // 由pos决定是否传入tabCode,如果为表头，则不传入tabCode
            if (!isPosHeadTail(scaleBean.getScalepos())) {
                scaletabcode = scaleBean.getScaletabcode();
            }


            if (!isPosHeadTail(scaleBean.getRefpos())) {
                reftabcode = scaleBean.getReftabcode();
            }


            /* Oct 14, 2013 wangweir 支持换算率精度配置 Begin */
            setScaleCtrlInfo(scaleprocess, scaleBean, scaleKeys, scaletabcode, reftabcode);
            /* Oct 14, 2013 wangweir End */
        }

        scaleprocess.process();

    }
    
    /**
     * @param scaleprocess
     * @param scaleBean
     * @param scaleKeys
     * @param scaletabcode
     * @param reftabcode
     */
    private static void setScaleCtrlInfo(BillScaleProcessor scaleprocess,
                                         MMGPScaleBean scaleBean,
                                         List<String> scaleKeys,
                                         String scaletabcode,
                                         String reftabcode) {
        switch (scaleBean.getType()) {
            case NumAssNum:
                scaleprocess.setNumCtlInfo(
                    scaleKeys.toArray(new String[0]),
                    scaleBean.getScalepos(),
                    scaletabcode,
                    scaleBean.getRefkey(),
                    scaleBean.getRefpos(),
                    reftabcode);
                break;
            case HSL:
                // 支持换算率
                scaleprocess.setHslCtlInfo(scaleKeys.toArray(new String[0]), scaleBean.getScalepos(), scaletabcode);
                break;
            case WEIGHT:
                scaleprocess
                    .setWeightCtlInfo(scaleKeys.toArray(new String[0]), scaleBean.getScalepos(), scaletabcode);
                break;
            case COSTPRICE_SCALE:
                scaleprocess.setCostPriceCtlInfo(
                    scaleKeys.toArray(new String[0]),
                    scaleBean.getScalepos(),
                    scaletabcode);
                break;
            case PS_PRICESCALE:
                scaleprocess.setPriceCtlInfo(scaleKeys.toArray(new String[0]), scaleBean.getScalepos(), scaletabcode,null,null,null);//maoyl
                break;
            case TAXRATE:
                scaleprocess.setTaxRateCtlInfo(
                    scaleKeys.toArray(new String[0]),
                    scaleBean.getScalepos(),
                    scaletabcode);
                break;
            case MONEY:
                scaleprocess.setMnyCtlInfo(
                    scaleKeys.toArray(new String[0]),
                    scaleBean.getScalepos(),
                    scaletabcode,
                    scaleBean.getRefkey(),
                    scaleBean.getRefpos(),
                    reftabcode);
                break;
            case GROUP_LOCAL_MNY:
                scaleprocess.setGroupLocMnyCtlInfo(
                    scaleKeys.toArray(new String[0]),
                    scaleBean.getScalepos(),
                    scaletabcode);
                break;
            case GLOBAL_LOCAL_MNY:
                scaleprocess.setGlobalLocMnyCtlInfo(
                    scaleKeys.toArray(new String[0]),
                    scaleBean.getScalepos(),
                    scaletabcode);
                break;
        }
    }

    
//    @SuppressWarnings("deprecation")
//    private static void setScale(BillScaleProcessor scaleprocess,
//                                MMGPNumScaleBean[] scaleBeans) {
//        setScale(scaleprocess, scaleBeans);
//    }

    private static boolean isPosHeadTail(PosEnum pos) {
        if (PosEnum.head.getCode() == pos.getCode()) {
            return true;
        }
        if (PosEnum.tail.getCode() == pos.getCode()) {
            return true;
        }

        return false;
    }

}
