package nc.bs.mmgp.common;

import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.vo.pub.BusinessException;

/**
 * 
 * <b> 公共后台任务类，为了集中控制 </b>
 * <p>
 *     详细描述功能
 * </p>
 * 创建日期:2011-7-29
 * @author wangweiu
 * @version 1.0
 * @version 1.1 适配nc v6
 */
public abstract class MMBackgroundWorkPlugin implements IBackgroundWorkPlugin {

    @SuppressWarnings("deprecation")
	public final PreAlertObject executeTask(BgWorkingContext bgwc) throws BusinessException {
    	String message = doExcute(bgwc);
    	PreAlertObject pao= new PreAlertObject();
    	pao.setReturnType(PreAlertReturnType.RETURNMESSAGE);
    	pao.setReturnObj(message);
        return pao;
    }
    
    protected abstract String doExcute(BgWorkingContext bgwc) throws BusinessException ;

}
