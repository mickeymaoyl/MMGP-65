package nc.bs.mmgp.common;

import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.vo.pub.BusinessException;

/**
 * 
 * <b> ������̨�����࣬Ϊ�˼��п��� </b>
 * <p>
 *     ��ϸ��������
 * </p>
 * ��������:2011-7-29
 * @author wangweiu
 * @version 1.0
 * @version 1.1 ����nc v6
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
